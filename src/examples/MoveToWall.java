

/**
 * Moves forward until it reaches a wall. Turns to the left, and repeats.
 * 
 * In an empty map, the robot will follow the wall, and never collide.
 * 
 * There are some obvious problems with this, worth noticing. - The robot only uses the two forward sensors, it may very well hit an edge just barely touching
 * its course, as it never checks ANGLE-LEFT or ANGLE-RIGHT. - This controller is really simple.
 * 
 */

public class GoToWall extends RobotController {
	public GoToWall() {
	}

	public void close() throws java.lang.Exception {
		/** @todo Implement this edu.wsu.KepheraSimulator.Controller abstract method */
	}

	// Liste over sensor-indekser:
	public final static int LEFT = 0;
	public final static int ANGLEL = 1;
	public final static int FRONTL = 2;
	public final static int FRONTR = 3;
	public final static int ANGLER = 4;
	public final static int RIGHT = 5;
	public final static int BACKR = 6;
	public final static int BACKL = 7;

	int state = 0;
	long startCounter;

	public void doWork() throws java.lang.Exception {
		switch (state) {
		case 0:
			if ((getDistanceValue(FRONTL) < 700) && (getDistanceValue(FRONTR) < 700)) {
				setMotorSpeeds(5, 5);
			} else {
				setMotorSpeeds(0, 0);
				state++;
			}
			break;

		case 1:
			startCounter = getLeftWheelPosition();
			state++;
			break;

		case 2:
			if ((startCounter - getLeftWheelPosition()) < 3 * 90) {
				setMotorSpeeds(-5, 5);
			} else {
				setMotorSpeeds(0, 0);
				state = 0;
			}
			break;

		default:
			// noop.
		}
	}

}
