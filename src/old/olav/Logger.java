package old.olav;


import java.util.ArrayList;
import java.util.Collections;

import edu.wsu.KheperaSimulator.KSGripperStates;
import edu.wsu.KheperaSimulator.RobotController;

public class Logger extends RobotController {

  public final static int ROTATION_LEFT = -1;
  public final static int ROTATION_RIGHT = 1;

  public final static int DIRECTION_RIGHT = 0;
  public final static int DIRECTION_UP = 1;
  public final static int DIRECTION_LEFT = 2;
  public final static int DIRECTION_DOWN = 3;

  public final static int SENSOR_LEFT = 0;
  public final static int SENSOR_ANGLEL = 1;
  public final static int SENSOR_FRONTL = 2;
  public final static int SENSOR_FRONTR = 3;
  public final static int SENSOR_ANGLER = 4;
  public final static int SENSOR_RIGHT = 5;
  public final static int SENSOR_BACKR = 6;
  public final static int SENSOR_BACKL = 7;

  public Log log;

  ArrayList<Behavior> behaviors;
  int[] distanceSensors;
  int[] lightSensors;

  RobotState rstate;

  public Logger() {
    // setup
    distanceSensors = new int[8];
    lightSensors = new int[8];
    behaviors = new ArrayList<Behavior>();
    rstate = RobotState.getInstance();
    rstate.posX = 0;
    rstate.posY = 0;
    rstate.orientation = 0;
    log = new Log();

    // Temp methods for first assignment
    addBehaviors();
    Collections.sort(behaviors);
  }

  public void updateState() {
    float leftWheelPos = getLeftWheelPosition();
    float rightWheelPos = getRightWheelPosition();
    float LWDiff = leftWheelPos - rstate.lastLeftWheelPos;
    float RWDiff = rightWheelPos - rstate.lastRightWheelPos;

    if (LWDiff == 0 && RWDiff == 0)
      return;
    if (Math.signum(RWDiff) == Math.signum(LWDiff)) {
      rstate.posX += (LWDiff + RWDiff) / 6 * Math.cos(Math.toRadians(rstate.orientation));
      rstate.posY += (LWDiff + RWDiff) / 6 * Math.sin(Math.toRadians(rstate.orientation));
    } else if (Math.signum(RWDiff) != Math.signum(LWDiff)) {
      rstate.orientation += RWDiff / 3;
      if (rstate.orientation < 0)
        rstate.orientation += 360;
      else if (rstate.orientation >= 360)
        rstate.orientation -= 360;
    }
    // System.out.println("ORIENTATION: " + orientation);
    // System.out.println("X: " + posX);
    // System.out.println("Y: " + posY);

    rstate.lastLeftWheelPos = leftWheelPos;
    rstate.lastRightWheelPos = rightWheelPos;
  }

  @Override
  public void doWork() throws Exception {
    updateState();
    updateSensors(4);
    for (Behavior b : behaviors) {
      if (b.checkConditions()) {
        b.run();
        break;
      }
    }
  }

  @Override
  public void close() throws Exception {
    log.printMap();
    setFinished(true);
  }


  public void stop() {
    setMotorSpeeds(0, 0);
  }

  public void moveForward(int speed) {
    setMotorSpeeds(speed, speed);
  }

  public int getFacingDirection() {
    int facingDirection = (int) Math.rint(rstate.orientation / 90);
    return facingDirection;
  }

  public void rotate(int direction, int speed) {
    if (direction > 0) {
      setMotorSpeeds(speed, -speed); // Right turn
    } else {
      setMotorSpeeds(-speed, speed); // Left turn
    }
  }

  /***
   * 
   * @param angle - the angle you want the robot to be facing [0,359]
   * @param speed - the speed you want it to rotate at, [1, 5]. The bigger the speed, the lower the
   *        precision.
   * @return Returns true if the angle is yet obtained, returns false if it is not.
   */
  public boolean rotateTo(double angle, int speed) {
    int direction = 0;
    double diff = (this.rstate.orientation - angle);
    // System.out.println("ANGLE WANTED: " + angle);
    // System.out.println("ANGLE AT: " + this.orientation);
    // System.out.println("DIFF: " + diff +"\n");
    if (Math.abs(diff) < 1 * speed) {
      // System.out.println("YEEEEEEEE");
      return true;
    }

    // calc direction
    direction = ROTATION_RIGHT;
    rotate(direction, speed);
    return false;
  }

  // This method should be moved into the behaviours at a later point.
  private void updateSensors(int passes) {

    // THIS IS BAD, JUST WIPE VALUES INSTEAD OF REALLOC, CBA
    distanceSensors = new int[8];
    lightSensors = new int[8];

    for (int j = 0; j < passes; j++) {
      for (int i = 0; i < 8; i++) {
        distanceSensors[i] += getDistanceValue(i) / passes;
        lightSensors[i] += getLightValue(i) / passes;
      }
    }
  }

  // #########################TEMP METHODS#########################

  // so temp
  public void addBehaviors() {
    behaviors.add(new ExploreMaze(0, 50));
    // behaviors.add(new CollectBall(5, 50));
    // behaviors.add(new GoHome(10));
  }


  // #############################TEMP CLASSES#######################
  // This class should be moved into a seperate class file at a later point in time.
  private abstract class Behavior implements Comparable<Behavior> {
    protected int priority = 0;

    public Behavior(int priority) {
      this.priority = priority;
    }

    @Override
    public int compareTo(Behavior o) {
      return o.priority - priority;
    }

    public abstract void run();

    public abstract boolean checkConditions();
  }

  // ####################TEMP BEHAVIORS####################
  public class CollectBall extends Behavior {
    int state = 0;
    double initX = 0;
    double initY = 0;
    double angleToTarget;
    int detectThreshhold;

    public CollectBall(int priority, int detectThreshhold) {
      super(priority);
      this.detectThreshhold = detectThreshhold;
    }

    int sensor;

    @Override
    public void run() {
      switch (state) {
        case 0:
          sensor = -1;
          int max = -1;
          // find the angle!
          for (int i = 0; i < distanceSensors.length - 2; i++) {
            if (distanceSensors[i] >= max) {
              max = distanceSensors[i];
              sensor = i;
            }
          }
          int direction = ROTATION_RIGHT;
          if (sensor < 3)
            direction = ROTATION_LEFT;
          // Something has been spotted!
          stop();
          rotate(direction, 1);
          state++;
          break;
        case 1:
          // We now know approximately where somehting is, if sensor != -1,
          // if facing the object, start moving:
          int threshhold = 5;
          // System.out.println(Math.abs(distanceSensors[SENSOR_FRONTL] -
          // distanceSensors[SENSOR_FRONTR]));
          // System.out.println((distanceSensors[SENSOR_FRONTL] + distanceSensors[SENSOR_FRONTR]) /
          // 2);
          if ((distanceSensors[SENSOR_FRONTL] + distanceSensors[SENSOR_FRONTR]) / 2 > detectThreshhold * 9 / 10
              && Math.abs(distanceSensors[SENSOR_FRONTL] - distanceSensors[SENSOR_FRONTR]) < threshhold) {
            stop();
            state++;
            break;
          }

          // else rotate to face it:
          break;
        case 2:
          moveForward(1);
          if ((distanceSensors[SENSOR_FRONTL] + distanceSensors[SENSOR_FRONTR]) / 2 > 950) {
            stop();
            state++;
          }
          break;
        case 3:
          setGripperState(KSGripperStates.GRIP_OPEN);
          sleep(500);
          setArmState(KSGripperStates.ARM_DOWN);
          sleep(500);

          System.out.println(isObjectPresent());
          if (isObjectPresent()) {
            setGripperState(KSGripperStates.GRIP_CLOSED);
            sleep(500);
            System.out.println("GOT IT!");
          } else {
            System.out.println("There was nothing here......");
          }
          setArmState(KSGripperStates.ARM_UP);
          sleep(500);
          state++;
          break;
        default:
          break;
      }
    }

    @Override
    public boolean checkConditions() {
      // Look for objects
      if (state == 0) {
        boolean objectNear = false;
        for (int i = 0; i < distanceSensors.length - 2; i++) {
          if (distanceSensors[i] > detectThreshhold)
            objectNear = true;
        }
        if (objectNear)
          return true;
      }
      // is moving to object or picking up
      if (state > 0 && state < 4)
        return true;
      return false;
    }
  }

  private class GoHome extends Behavior {
    int state;
    double angleToOrigin;
    int ticks = 0;

    public GoHome(int priority) {
      super(priority);
      state = 0;
    }

    @Override
    public void run() {
      switch (state) {
        case 0:
          // We have picked up something!
          state++;
          break;
        case 1:
          // Calculate angle to origin
          angleToOrigin = (Math.toDegrees(Math.atan2(rstate.posY, rstate.posX)) + 540) % 360;
          state++;
        case 2:
          if (rotateTo(angleToOrigin, 1)) {
            ticks = 0;
            stop();
            state++;
          }
          break;
        case 3:
          moveForward(5);
          ticks++;
          // System.out.println(String.format("X: %.3f \tY: %.3f", posX, posY));
          // System.out.println("ORIENTATION: " + orientation + "\n");
          if (Math.abs(rstate.posX) < 5 && Math.abs(rstate.posY) < 5) {
            stop();
            state++;
          } else if (ticks > 50) {
            System.out.println("RECALIBRATING...");
            state = 1;
          }
          break;
        case 4:
          setArmState(KSGripperStates.ARM_DOWN);
          sleep(500);
          setGripperState(KSGripperStates.GRIP_OPEN);
          sleep(500);
          setArmState(KSGripperStates.ARM_UP);
          sleep(500);
          state++;
          break;
        default:
          break;
      }
    }

    @Override
    public boolean checkConditions() {
      if (state == 0 && getArmState() == KSGripperStates.ARM_UP && isObjectHeld())
        return true;
      if (state > 0 && state < 5)
        return true;
      return false;
    }

  }

  private class ExploreMaze extends Behavior {
    int state = 0;
    double lastX;
    double lastY;
    int direction = 0;
    double loggingInterval;
    boolean canGoLeft = true;
    boolean canGoRight = true;
    boolean canGoForward = true;
    double wantedOrientation = 0;
    long startCount;

    public ExploreMaze(int priority, double loggingInterval) {
      super(priority);
      lastX = -loggingInterval;
      lastY = -loggingInterval;
      this.loggingInterval = loggingInterval;
    }

    @Override
    public void run() {
      switch (state) {
        case 0:
          log.log(new LogEntry((int) (rstate.posX / loggingInterval),
              (int) (rstate.posY / loggingInterval), LogEntry.STATE_AIR));
          state++;
          break;
        case 1:
          // Logging state
          System.out.println("Logging");
          stop();
          int facingDirection = getFacingDirection();
          int dx = 0;
          int dy = 0;
          int frontX = 0,
          frontY = 0;
          canGoLeft = true;
          canGoRight = true;
          canGoForward = true;

          if (facingDirection == 0) {
            dy = 1;
            frontX = 1;
          }
          if (facingDirection == 1) {
            dx = -1;
            frontY = 1;
          }
          if (facingDirection == 2) {
            dy = -1;
            frontX = -1;
          }
          if (facingDirection == 3) {
            dx = 1;
            frontY = -1;
          }
          // Log left and right of the robot.
          int logState = LogEntry.STATE_AIR;
          if (distanceSensors[SENSOR_LEFT] > 300) {
            logState = LogEntry.STATE_SOMETHING;
            canGoLeft = false;
          }
          log.log(new LogEntry((int) ((rstate.posX + dx * loggingInterval) / loggingInterval),
              (int) ((rstate.posY + dy * loggingInterval) / loggingInterval), logState));

          logState = LogEntry.STATE_AIR;
          if (distanceSensors[SENSOR_RIGHT] > 300) {
            logState = LogEntry.STATE_SOMETHING;
            canGoRight = false;
          }
          log.log(new LogEntry((int) ((rstate.posX - dx * loggingInterval) / loggingInterval),
              (int) ((rstate.posY - dy * loggingInterval) / loggingInterval), logState));

          logState = LogEntry.STATE_AIR;
          if (distanceSensors[SENSOR_FRONTL] > 300) {
            logState = LogEntry.STATE_SOMETHING;
            canGoForward = true;
          }
          log.log(new LogEntry((int) ((rstate.posX + frontX * loggingInterval) / loggingInterval),
              (int) ((rstate.posY + frontY * loggingInterval) / loggingInterval), logState));

          lastX = rstate.posX;
          lastY = rstate.posY;
          state++;
          break;
        case 2:
          if (Math.abs(rstate.posX - lastX) > loggingInterval
              || Math.abs(rstate.posY - lastY) > loggingInterval) {
            stop();
            state = 1;
          }

          boolean mustTurn = false;
          if (distanceSensors[SENSOR_FRONTL] > 700) {
            mustTurn = true;
          }
          if (mustTurn) {
            stop();
            state++;
            break;
          }
          moveForward(3);
          break;
        case 3:
          stop();
          boolean canTurnLeft = true;
          boolean canTurnRight = true;

          if (distanceSensors[SENSOR_LEFT] > 700) {
            canTurnLeft = false;
          }
          if (distanceSensors[SENSOR_RIGHT] > 700) {
            canTurnRight = false;
          }

          if (canTurnRight && canTurnLeft) {
            if (Math.random() < 0.5) {
              direction = ROTATION_LEFT;
            } else {
              direction = ROTATION_RIGHT;
            }
          } else if (canTurnLeft) {
            direction = ROTATION_LEFT;
          } else if (canTurnRight) {
            direction = ROTATION_RIGHT;
          } else {
            direction = ROTATION_RIGHT;
          }
          state++;
          break;
        case 4:
          startCount = getLeftWheelPosition();
          state++;
          break;
        case 5:
          if ((Math.abs(startCount - getLeftWheelPosition())) < 3 * 90) {
            rotate(direction, 1);
          } else if ((startCount - getLeftWheelPosition()) > 3 * 90) {
            rotate(-direction, 1);
          } else {
            stop();
            sleep(500);
            log.printMap();
            state = 2;
          }
          break;
        default:
          break;
      // noop.
      }
    }

    @Override
    public boolean checkConditions() {
      return true;
    }
  }
}
