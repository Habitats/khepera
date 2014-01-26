package examples;

/**
 * @(#)Template.java 1.1 2002/11/20
 */
import edu.wsu.KheperaSimulator.RobotController;

/**
 * This class is a basic template that shows the required stucture for a controller. This Template controller can be loaded by the simulator and will cause the
 * robot to spin at its initial starting position.
 */
public class Template extends RobotController {

	/**
	 * Default Constructor
	 */
	public Template() {

		/**
		 * This sets the amount of time to wait before the next succeeding call to the method doWork. This time is specified in milliseconds. This method does
		 * not need to be explicitly called because the default wait time is set to 5 milliseconds.
		 */
		setWaitTime(5);
	}

	/**
	 * This method will be called after this controller has been loaded. When this method returns it is guaranteed to be called again after a specified amount
	 * of time has elapsed. The time can be specified with a call to setWaitTime(time) where time is the amount of time to wait in milliseconds.
	 */
	@Override
	public void doWork() throws Exception {

		/**
		 * This will cause the robot to spin in a circle same as this.setMotorSpeeds(3, -3) or super.setMotorSpeeds(3, -3)
		 */
		setMotorSpeeds(5, -5);
	} // doWork

	/**
	 * This method is guaranteed to be called when this controller is terminated.
	 */
	@Override
	public void close() throws Exception {
	} // close

} // Template