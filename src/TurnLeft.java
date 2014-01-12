

import edu.wsu.KheperaSimulator.RobotController;

/**
 * Turns 90 degrees to the left.
 * 
 * @author Christian Tellefsen 
 */
public class TurnLeft extends RobotController {
  public TurnLeft() {
  }
  public void close() throws java.lang.Exception {
    /**@todo Implement this edu.wsu.KepheraSimulator.Controller abstract method*/
  }
  int state = 0;
  long startCount = 0;

  /**
   * Turns a 90 degree left.
   *
   * Due to this function being called "only" earch 5 ms, it is in
   * actual use necessary to check that the robot has not turned too 
   * far. For simplicity, this is not done here. Look at TurnDegrees.java
   * for an example.   * 
   *
   * @throws Exception
   */
  public void doWork() throws java.lang.Exception {
    if(state == 0) {
      startCount = getLeftWheelPosition();
      state = 1;
    }

    if(state == 1) {
      if((startCount - getLeftWheelPosition()) < 270) {
        setMotorSpeeds(-5,5);
      } else {
        setMotorSpeeds(0,0);
        state = 2;
      }
    }

    if(state == 2) {
      // Nothing else to do
    }
  }

}
