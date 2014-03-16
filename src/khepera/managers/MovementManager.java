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
    int rest = (int) (steps % 20);
    for (int i = 0; i < tick; i++) {
      move(20, SPEED_FORWARD);
    }
    move(rest, SPEED_FORWARD);
  }

  private void move(long distance, int newSpeed) {
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
//    if (distance > 0) {
//      RobotEvent e = new RobotEvent(state, RobotAction.FORWARD, distance, speed);
//      Singleton.getInstance().getHistory().addEvent(e);
//    }
  }

  public void rotate(int degrees, Direction direction) {

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


  private boolean crashing() {
    boolean crash = getAverageDistance(SensorManager.SENSOR_FRONTL) > 1000 || getAverageDistance(SensorManager.SENSOR_FRONTR) > 1000;
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

  protected Coord getCurrentLocation() {
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

  protected double getDirectionInRadians() {
    return (getDirectionInDegrees() / 180.) * Math.PI;
  }

  protected int getDirectionInDegrees() {
    long diff = controller.getLeftWheelPosition() - controller.getRightWheelPosition();
    diff /= 2;
    int direction = (int) (diff % 1080) / 3;
    return direction;
  }


}
