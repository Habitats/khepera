import edu.wsu.KepheraSimulator.*;
/**
 * Shows a simple way to make the robot move a certain distance by using
 * the wheel counters.
 *
 * @author Christian Tellefsen
 * @version 1.0
 */

public class Move extends RobotController {
  public Move() {
  }

  public void close() throws java.lang.Exception {
    // To be used of cleanup is required on exit
    }

    int state = 0;
    long startCount = 0;

    /**
     * Moves the robot forward 250 units
     *
     * Due to this function being called "only" earch 5 ms, it is in
     * actual use necessary to check that the robot has not moved too 
     * far. For simplicity, this is not done here. Look at TurnDegrees.java
     * for an example.
     *
     * @throws Exception
     */
    public void doWork() throws java.lang.Exception {
      if(state == 0) {
        startCount = getLeftWheelPosition();
        state = 1;
      }

      if(state == 1) {
        if((startCount - getLeftWheelPosition()) < 250) {
          setMotorSpeeds(5,5);
        } else {
          setMotorSpeeds(0,0);
          state = 2;
        }
      }

      if(state == 2) {
        // Finished
      }
    }

}
