package old.pat.etc;

import old.pat.etc.AbstractController.RobotState;

public class RobotEvent {
  public enum RobotAction {
    FORWARD, //
    ROTATE, //
  }

  private Turn turn;
  private int direction;
  private long time;
  private RobotAction action;
  private int speed;
  private long distance;
  private long degrees;
  private final RobotState state;

  // forward
  public RobotEvent(RobotState state, RobotAction action, long distance, int speed) {
    time = System.currentTimeMillis();

    this.state = state;
    this.action = action;
    this.distance = distance;
    this.speed = speed;
  }

  //
  public RobotEvent(RobotState state, RobotAction action, long degrees) {
    time = System.currentTimeMillis();

    this.state = state;
    this.action = action;
    this.degrees = degrees;
  }

  public RobotAction getAction() {
    return action;
  }

  public int getDirection() {
    return direction;
  }

  public long getDistance() {
    return distance;
  }

  public int getSpeed() {
    return speed;
  }

  public Turn getTurn() {
    return turn;
  }

  public long getTime() {
    return time;
  }

  public RobotState getState() {
    return state;
  }

  public long getDegrees() {
    return degrees;
  }
}
