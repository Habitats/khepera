package khepera.managers;

import khepera.AbstractController;
import khepera.Coord;
import khepera.Logger;

public class MovementManager {
  private final AbstractController controller;

  public static final int SPEED_FORWARD = 5;
  public static final int SPEED_FORWARD_SLOW = 3;
  public static final int SPEED_ROTATE = 2;

  private int currentSpeed;
  protected double locationX = 0;
  protected double locationY = 0;


  public enum RobotState {
    GOING_HOME, //
    LOOKING_FOR_BALL, //
    IDLE, //
  }

  public enum Direction {
    LEFT, RIGHT;
  }

  public MovementManager(AbstractController abstractController) {
    this.controller = abstractController;
  }

  public void forward(int steps) {

    int tick = (int) (Math.floor(steps / 20.));
    int rest = steps % 20;
    for (int i = 0; i < tick; i++) {
      forward(20, SPEED_FORWARD);
    }
    forward(rest, SPEED_FORWARD);
  }

  private void forward(long distance, int newSpeed) {
    currentSpeed = newSpeed;

    long start = controller.getLeftWheelPosition();
    long end = start + distance;
    controller.setMotorSpeeds(currentSpeed, currentSpeed);
    while (Math.abs(controller.getLeftWheelPosition()) < end && !crashing()) {
      // if (closeToWall())
      // break;
      controller.sleep(1);
    }
    // stop motors until next forward-call
    stop();

    // update to actual traveled distance
    distance = controller.getLeftWheelPosition() - start;

    // update the map and location
    updateLocation(distance);
    updateLevelKnowledge();

    // // add event to history if the robot actually traveled
    // if (distance > 0) {
    // RobotEvent e = new RobotEvent(state, RobotAction.FORWARD, distance, speed);
    // Singleton.getInstance().getHistory().addEvent(e);
    // }
  }

  public void rotate(int degrees, Direction direction) {
    rotate(degrees, direction, true);
  }

  public void rotate(int degrees, Direction direction, boolean enableCorrection) {

    Logger.getInstance().setStatus("Rotating: True");
    long start = controller.getLeftWheelPosition();

    int d = 1;
    if (direction == Direction.LEFT)
      d = -1;

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

  private int getAverageDistance(int sensorID) {
    double accuracy = 5;
    double avg = 0;
    int v = 0;
    for (int i = 0; i < accuracy; i++) {
      v = controller.getDistanceValue(sensorID);
      avg += (v / accuracy);
      // sleep(1);
    }
    return (int) avg;
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
    rotate(1, dir,true);
  }

  private boolean goingVerticalOrHorizontal() {
    return approximately(getDirectionInRadians() % (Math.PI / 2), 0., 0.005);
  }

  // a ~ b
  private boolean approximately(double a, double b, double delta) {
    return (a < (b + delta) && a > (b - delta));
  }

  private boolean crashing() {
    boolean crash = getAverageDistance(SensorManager.SENSOR_FRONT_LEFT) > 1000 || getAverageDistance(SensorManager.SENSOR_FRONT_RIGHT) > 1000;
    if (crash)
      Logger.getInstance().log("Avoiding crash!");
    return crash;
  }

  // draw where the robot has been dirving
  private void updateRobotTail() {
    Logger.getInstance().updateRobotLocation(getCurrentLocation().getNormalized());
    Logger.getInstance().updateRobotDirection(getDirectionInRadians());
  }


  // update the robots knowledge of its surroundings
  private void updateLevelKnowledge() {
    // updateMap();
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


}
