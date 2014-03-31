package khepera.managers;

import edu.wsu.KheperaSimulator.KSGripperStates;
import khepera.AbstractController;
import khepera.Coord;
import khepera.Logger;

/**
 * Class to handle communication between the states and the actual robot controller. Everything that
 * deals with motor speeds should be handled in this class
 * 
 * @author Patrick
 * 
 */
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


  /**
   * Public enum used to represent some different states in the GUI (different state == different
   * color)
   * 
   * @author Patrick
   * 
   */
  public enum RobotState {
    GOING_HOME, //
    LOOKING_FOR_BALL, //
    IDLE, //
  }

  /**
   * Public enum used for roation and movement
   * 
   * @author Patrick
   * 
   */
  public enum Direction {
    LEFT, RIGHT, RANDOM, FORWARD, BACKWARD;
  }

  /**
   * Default constructor
   * 
   * @param abstractController - the robot controller
   */
  public MovementManager(AbstractController abstractController) {
    this.controller = abstractController;
  }

  /**
   * Move method to be called from states.
   * 
   * Moving is blocking.
   * 
   * @param steps - the distance to move
   * @param direction - the direction to move in
   */
  @SuppressWarnings("incomplete-switch")
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

  /**
   * Helper class for moving. Moving should be done in small steps to avoid crashing and taking up
   * the main thread for too long in one go. This method is the one that does the actual calls to
   * the robot controller.
   * 
   * Moving is blocking.
   * 
   * @param distance - the longest distance for /one/ move "tick"
   * @param newSpeed - the speed the robot should move in
   * @param direction - the direction to move in
   */
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

  /**
   * Rotation method. Since axis correction is used most of the time, a rotation method with axis
   * correction enabled by default i supplied
   * 
   * Rotation is blocking.
   * 
   * @param degrees - the degrees to rotate
   * @param direction - the direction to rotate
   */
  public void rotate(int degrees, Direction direction) {
    rotate(degrees, direction, true);
  }

  /**
   * Rotation method with axis correction as a parameter
   * 
   * Rotation is blocking.
   * 
   * @param degrees - the degrees to rotate
   * @param direction - the direction to rotate
   * @param enableCorrection - whether to enable axis correction. If axis correction is enabled, the
   *        robot will automatically rotate to the nearest axis. Ie., rotate(80) will rotate 90
   *        degrees, rotate(20) will not rotate at all.
   */
  @SuppressWarnings("incomplete-switch")
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
        axisCorrection();
        Logger.getInstance().setStatus("Correcting direction!");
      }
    }

    Logger.getInstance().setStatus("Rotating: False");
  }

  /**
   * Helper method to stop the robots movement entirely
   */
  private void stop() {
    controller.setMotorSpeeds(0, 0);
  }

  /**
   * Helper method for axis correction. See rotate.
   */
  private void axisCorrection() {
    double d = getDirectionInRadians() + 2 * Math.PI;
    Direction dir;
    if ((d % (Math.PI / 4)) > (Math.PI / 8))

      dir = Direction.RIGHT;
    else
      dir = Direction.LEFT;
    rotate(1, dir, true);
  }

  /**
   * Helper method for deciding whether the robot is following either the x or y axis
   * 
   * @return - is the robot following the axis?
   */
  private boolean goingVerticalOrHorizontal() {
    return approximately(getDirectionInRadians() % (Math.PI / 2), 0., 0.005);
  }

  /**
   * Approximation method. Is a ~ b?
   * 
   * @param a - first parammter
   * @param b - second parameter
   * @param delta - a delta value for the approximation
   * @return - yes or no
   */
  private boolean approximately(double a, double b, double delta) {
    return (a < (b + delta) && a > (b - delta));
  }

  /**
   * Method for updating the robot on the GUI map
   */
  private void updateLevelKnowledge() {
    Logger.getInstance().updateRobotLocation(getCurrentLocation().getNormalized());
    Logger.getInstance().updateRobotDirection(getDirectionInRadians());
  }

  /**
   * Get the current location of the robot
   * 
   * @return - a Coord instance with a x and y field
   */
  public Coord getCurrentLocation() {
    Coord location = new Coord((int) locationX, (int) locationY);
    return location;
  }

  /**
   * Method for updating the robots internal representation of the map. Used in conjunction with the
   * GUI.
   */
  private void updateLocation(long distance) {
    double r = getDirectionInRadians();
    double xNew = (int) (Math.cos(r) * distance + locationX);
    double yNew = (int) (Math.sin(r) * distance + locationY);

    setLocation(xNew, yNew);
  }

  /**
   * Set the new location in the robots internal map representation
   * 
   * @param xNew - x coordinate
   * @param yNew - y coordinate
   */
  private void setLocation(double xNew, double yNew) {
    locationX = xNew;
    locationY = yNew;
  }

  /**
   * @rethrn the robots facing direction in radians
   */
  public double getDirectionInRadians() {
    return (getDirectionInDegrees() / 180.) * Math.PI;
  }

  /**
   * @return the robots facing direction in degrees
   */
  public int getDirectionInDegrees() {
    long diff = controller.getLeftWheelPosition() - controller.getRightWheelPosition();
    diff /= 2;
    int direction = (int) (diff % 1080) / 3;
    return direction;
  }

  /**
   * Try and pick up a ball in front
   */
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

  /**
   * Try and drop a ball by lowering the arms and releasing the claws
   */
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
