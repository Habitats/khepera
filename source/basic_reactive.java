/**
 * @(#)Template.java 1.1 2002/11/20
 */

import edu.wsu.KheperaSimulator.RobotController;
import edu.wsu.KheperaSimulator.KSGripperStates;

/**
 * This class is a basic avoider that is attracted to lights. Basically, it 
   makes the until it sees something.  If it's a close solid object, it turns
   away.  If it's a "distant" light, it steers toward it slightly.  Some interesting
   behaviors can happen here, including orbiting lights if they're set right.
*/

public class basic_reactive extends RobotController{

  /**
   * Default Constructor
   */
  public basic_reactive() {

    /**
     * This controller "polls" (I.E. manually checks) all of its sensors and then
	 * makes an action choice based on what it sees.  The wait time is the amount of time
	 * that elapses between every cycle of check/execute.  It is measured in miliseconds and
	 * is set by changing the number in the setWaitTime(number) command below.  Currently it is
	 * set to 5... which means a sensor check/action decision is done every 5 miliseconds.  You 
	 * can change this if you want... but if you make it too long... the robot could crash into
	 * something while the controller is waiting to do its next sensor check.  Remember, unless you
	 * stop them... the motors will keep on doing what you tell them.
     */
    setWaitTime(5);
  }

  /**
   * This method (I.E. list of instructions) will be called after this controller has been started 
   * and then again every so often until you stop the controller.  Specifically, it will be called
   * once every how ever many miliseconds you specified in the call to setWaitTime() (you read the
   * last comment, right [grin]).  
   *  
   * Basically you'll note that it checks each distance sensor from 0 to 5 in turn.  If the returned value
   * is above a certain threshold, it tells one motor to spin "forward" and one "backward"... this 
   * would cause the robot to spin in place until it is told otherwise.  Sensors 0 through 5 are the
   * forward looking sensors to the front of the robot.  You might want to experiment or check
   * manuals to find out which number corresponds to which physical sensor.  Further, you can 
   * conclude that sensors 6 and 7  are the "backward looking" sensors.  From the code, you might
   * also conclude that the closer you get to a solid object, the bigger the number you get
   * from the sensor (you can see that from the greater than sign usage).  
   *
   * Note that if nothing is seen in any of the distance sensor beams... then you end up 
   * checking for lights to see if you should go toward them.  Examine the code and
   * compare it to the methods for avoiding solid objects.
   *
   * If the robot sees neither solid objects nor lights... it just goes straight forward.
   * One can actually come up with some interesting behaviors by selectively changing the
   * motor speeds and sensor thresholds.  You might want to experiment....
   */

  public void doWork() throws Exception {

    	if (getDistanceValue(2) > 50) setMotorSpeeds(2, -5); else
	if (getDistanceValue(3) > 50) setMotorSpeeds(-5, 2); else
	if (getDistanceValue(1) > 50) setMotorSpeeds(5, -5); else
	if (getDistanceValue(4) > 50) setMotorSpeeds(-5, 5); else
	if (getDistanceValue(0) > 50) setMotorSpeeds(5, -5); else
	if (getDistanceValue(5) > 50) setMotorSpeeds(-5, 5); else
	
    if (getLightValue(0)    < 200) setMotorSpeeds( 1, 5); else
    if (getLightValue(1)    < 200) setMotorSpeeds( 1, 5); else
    if (getLightValue(2)    < 200) setMotorSpeeds( 1, 5); else
    if (getLightValue(3)    < 200) setMotorSpeeds( 5, 1); else
    if (getLightValue(4)    < 200) setMotorSpeeds( 5, 1); else
    if (getLightValue(5)    < 200) setMotorSpeeds( 5, 1); else
	
    setMotorSpeeds(5, 5);


  } // doWork

  /**
   * This method is guaranteed to be called when this controller is terminated.  You can 
	 basically ignore this unless there's some special clean-up you want to do when a 
	 controller is finished running
   */
  public void close() throws Exception {
  } // close

} // Template
