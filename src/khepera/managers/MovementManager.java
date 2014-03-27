package khepera.managers;

import edu.wsu.KheperaSimulator.KSGripperStates;
import khepera.AbstractController;
import khepera.Coord;
import khepera.Logger;

public class MovementManager {
  private final AbstractController controller;

  public static final int SPEED_FORWARD = 5;
  public static final int SPEED_FORWARD_SLOW = 3;
  public static final int SPEED_ROTATE = 2;

  public static final int GRIP_OPEN = KSGripperStates.GRIP_OPEN;
  public static final int GRIP_CLOSED = KSGripperStates.GRIP_CLOSED;
  public static final int ARM_DOWN = KSGripperStates.ARM_DOWN;
  public static final int ARM_UP = KSGripperStates.ARM_UP;

  private int currentSpeed;
  protected double locationX = 0;
  protected double locationY = 0;


  public enum RobotState {
    GOING_HOME, //
    LOOKING_FOR_BALL, //
    IDLE, //
  }

  public enum Direction {
    LEFT, RIGHT, RANDOM, FORWARD, BACKWARD;
  }

  public MovementManager(AbstractController abstractController) {
    this.controller = abstractController;
  }

  public void move(int steps, Direction direction) {

    int speed = 0;
    int tick = (int) (Math.floor(steps / 20.));
    int rest = steps % 20;
    int smallStep = 20;
    switch (direction) {
      case BACKWARD:
        speed = SPEED_FORWARD * -1;
        smallStep *= -1;
        rest *= -1;
        break;
      case FORWARD:
        speed = SPEED_FORWARD;
        break;
    }
    for (int i = 0; i < tick; i++) {
      move(smallStep, speed, direction);
    }
    move(rest, speed, direction);
  }

  private void move(long distance, int newSpeed, Direction direction) {
    currentSpeed = newSpeed;

    long start = controller.getLeftWheelPosition();
    long end = start + distance;
    controller.setMotorSpeeds(currentSpeed, currentSpeed);

    long lastWheelPos = start;
    long stuckCounter = 0;
    while (true) {
      if (controller.getLeftWheelPosition() < end && direction == Direction.BACKWARD) {
        break;
      } else if (controller.getLeftWheelPosition() > end && direction == Direction.FORWARD) {
        break;
      }
      if (lastWheelPos == controller.getLeftWheelPosition()) {
        if (++stuckCounter > 10)
          break;
      } else {
        stuckCounter = 0;
        lastWheelPos = controller.getLeftWheelPosition();
      }
      controller.sleep(1);
    }
    // stop motors until next forward-call
    stop();

    // update to actual traveled distance
    distance = controller.getLeftWheelPosition() - start;

    // update the map and location
    updateLocation(distance);
    updateLevelKnowledge();

  }

  public void rotate(int degrees, Direction direction) {
    rotate(degrees, direction, true);
  }

  public void rotate(int degrees, Direction direction, boolean enableCorrection) {

    Logger.getInstance().setStatus("Rotating: True");
    long start = controller.getLeftWheelPosition();

    int d = 0;
    switch (direction) {
      case LEFT:
        d = -1;
        break;
      case RIGHT:
        d = 1;
        break;
      case RANDOM:
        d = (Math.random() > 0.5) ? 1 : -1;
        break;
    }

    controller.setMotorSpeeds(SPEED_ROTATE * d, -SPEED_ROTATE * d);
    while (Math.abs(controller.getLeftWheelPosition() - start) / 3 < Math.abs(degrees)) {
      controller.sleep(1);
      updateLevelKnowledge();
    }
    // stop rotation
    stop();
    if (enableCorrection) {
      if (!goingVerticalOrHorizontal()) {
        correctDirection();
        Logger.getInstance().setStatus("Correcting direction!");
      }
    }

    Logger.getInstance().setStatus("Rotating: False");
  }

  private void stop() {
    controller.setMotorSpeeds(0, 0);
  }

  private void correctDirection() {
    double d = getDirectionInRadians() + 2 * Math.PI;
    Direction dir;
    if ((d % (Math.PI / 4)) > (Math.PI / 8))

      dir = Direction.RIGHT;
    else
      dir = Direction.LEFT;
    rotate(1, dir, true);
  }

  private boolean goingVerticalOrHorizontal() {
    return approximately(getDirectionInRadians() % (Math.PI / 2), 0., 0.005);
  }

  // a ~ b
  private boolean approximately(double a, double b, double delta) {
    return (a < (b + delta) && a > (b - delta));
  }

  // draw where the robot has been dirving
  private void updateRobotTail() {
    Logger.getInstance().updateRobotLocation(getCurrentLocation().getNormalized());
    Logger.getInstance().updateRobotDirection(getDirectionInRadians());
  }


  // update the robots knowledge of its surroundings
  private void updateLevelKnowledge() {
    updateRobotTail();
  }

  public Coord getCurrentLocation() {
    Coord location = new Coord((int) locationX, (int) locationY);
    return location;
  }

  private void updateLocation(long distance) {
    double r = getDirectionInRadians();
    double xNew = (int) (Math.cos(r) * distance + locationX);
    double yNew = (int) (Math.sin(r) * distance + locationY);

    setLocation(xNew, yNew);
  }

  private void setLocation(double xNew, double yNew) {
    locationX = xNew;
    locationY = yNew;
  }

  public double getDirectionInRadians() {
    return (getDirectionInDegrees() / 180.) * Math.PI;
  }

  public int getDirectionInDegrees() {
    long diff = controller.getLeftWheelPosition() - controller.getRightWheelPosition();
    diff /= 2;
    int direction = (int) (diff % 1080) / 3;
    return direction;
  }

  public void pickUpBall() {
    controller.setGripperState(GRIP_OPEN);
    controller.sleep(100);
    controller.setArmState(ARM_DOWN);
    controller.sleep(100);
    controller.setGripperState(GRIP_CLOSED);
    controller.sleep(100);
    controller.setArmState(ARM_UP);
    controller.sleep(100);
  }

  public void dropBall() {
    controller.setArmState(ARM_DOWN);
    controller.sleep(100);
    controller.setGripperState(GRIP_OPEN);
    controller.sleep(100);
    controller.setArmState(ARM_UP);
    controller.sleep(100);
    controller.setGripperState(GRIP_CLOSED);
    controller.sleep(100);
  }

}
