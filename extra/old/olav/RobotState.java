package old.olav;

public class RobotState {

  private static RobotState instance = null;

  float posX;
  float posY;
  float orientation;
  float lastLeftWheelPos;
  float lastRightWheelPos;

  private RobotState() {
    lastLeftWheelPos = 0;
    lastRightWheelPos = 0;
  }

  public static RobotState getInstance() {
    if (instance == null)
      instance = new RobotState();
    return instance;
  }
}
