/**
 * @(#)WallFollower.java 1.1 2002/10/15
 */

import edu.wsu.KheperaSimulator.RobotController;
import edu.wsu.KheperaSimulator.KSGripperStates;


/**
 * This controller find simply finds a wall and then follows the wall
 * indefinately
 */
public class WallFollower extends RobotController {

  private int distThreshold = 600;
  boolean runOnce = false;
  int max = 0;
  int closeIndex = -1;
  int closeIndex2 = -1;
  int tempVal;

  /**
   * Default Constructor
   */
  public WallFollower() {
    setWaitTime(5); // set the wait time until doWork() is invoked again
  }

  /**
   * When an object implementing the interface <code>Controller</code> is used
   * to create a thread, the starting thread causes the objects
   * <code>doWork</code> method to be called.  This method is called repeatedly
   * and may perform any task.  The time needed for <code>doWork</code> to
   * complete is not significant with respect to the next invocation of this
   * method.
   * @throws java.lang.Exception
   */
  public void doWork() throws java.lang.Exception {
    if (!runOnce) {
      findWall();
      setMotorSpeeds(3, 3);
      runOnce = true;
    }
    testProximity();
  } // doWork

  /**
   * Indicates that the application has finished using the controller, and that
   * any resources being used may be released. The starting thread invokes the
   * object's <code>close</code> method only when <code>doWork</code> has
   * returned and is not scheuled to be called again.
   * @throws java.lang.Exception
   */
  public void close() throws java.lang.Exception {
    // no resources used
  }

  private void findWall() throws Exception {
    setMotorSpeeds(3,3);
    while(getDistanceValue(2) < 700) {
      sleep(100);
    }
    setMotorSpeeds(2,-2);
    while(getDistanceValue(0) < 700) {
      sleep(100);
    }
    setMotorSpeeds(0,0);
  }

  private void testProximity() {
    max = 0;
    closeIndex = -1;
    closeIndex2 = -1;
    for (int i = 0; i < 5; i++) {
      tempVal = getDistanceValue(i);
      if(tempVal > distThreshold) {
        if(tempVal > max) {
          max = tempVal;
          closeIndex2 = closeIndex;
          closeIndex = i;
        }
      }
    }
    if(closeIndex == 0) {
      if(closeIndex2 != 2 && closeIndex2 != 3)
        setMotorSpeeds(3, -1);
          else
            setMotorSpeeds(3, -5);
          return;
    }
    if(closeIndex == 1) {
      if(closeIndex2 != 2 && closeIndex2 != 3)
        setMotorSpeeds(3, 1);
      else
              setMotorSpeeds(3, -5);
      return;
    }
    if(closeIndex == 2 || closeIndex == 3) {
      setMotorSpeeds(3, -5);
      return;
    }
    if(closeIndex == 4 || closeIndex == 5) {
      if(closeIndex2 == 2 ||  closeIndex2 == 3)
        setMotorSpeeds(3, -5);
      else
        setMotorSpeeds(-1, 3);
      return;
    }
    setMotorSpeeds(1, 3);
  } // testProximity
} // WallFollower