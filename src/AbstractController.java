import java.util.Set;
import java.util.TreeSet;

import edu.wsu.KheperaSimulator.KSGripperStates;
import edu.wsu.KheperaSimulator.RobotController;

/**
 * Abstract controller with base functions and constants
 * 
 * @author Patrick
 * 
 */
public abstract class AbstractController extends RobotController {
	public enum S {
		ROTATING, //
		DRIVING, //
		IDLE, //
		BUSY, //
		INIT, //
	}

	protected S state = S.INIT;

	public final int SENSOR_LEFT = 0;
	public final int SENSOR_ANGLEL = 1;
	public final int SENSOR_FRONTL = 2;
	public final int SENSOR_FRONTR = 3;
	public final int SENSOR_ANGLER = 4;
	public final int SENSOR_RIGHT = 5;
	public final int SENSOR_BACKR = 6;
	public final int SENSOR_BACKL = 7;

	public final int GRIP_OPEN = KSGripperStates.GRIP_OPEN;
	public final int GRIP_CLOSED = KSGripperStates.GRIP_CLOSED;
	public final int ARM_DOWN = KSGripperStates.ARM_DOWN;
	public final int ARM_UP = KSGripperStates.ARM_UP;

	final int SPEED_FORWARD = 3;
	final int SPEED_ROTATE = 5;

	@Override
	abstract public void doWork() throws Exception;

	@Override
	abstract public void close() throws Exception;

	protected int getAverageDistance(int sensorID) {
		double accuracy = 5;
		double avg = 0;
		int v = 0;
		for (int i = 0; i < accuracy; i++) {
			v = getDistanceValue(sensorID);
			// System.out.println(getDistanceValue(sensorID));
			avg += (v / accuracy);
			// sleep(1);
		}
		return (int) avg;
	}

	protected void rotate(long degrees) {
		state = S.ROTATING;
		long start = getLeftWheelPosition();
		int d = 1;
		if (degrees < 0)
			d = -1;
//		System.out.println("Rotating: " + Long.toString(degrees));
		setMotorSpeeds(SPEED_ROTATE * d, -SPEED_ROTATE * d);
		while (Math.abs(getLeftWheelPosition() - start) / 3 < Math.abs(degrees)) {
			// System.out.println(Math.abs(getLeftWheelPosition() - start) / 3);
			sleep(1);
		}
		stop();
		state = S.IDLE;
	}

	protected boolean alignedWithRightWall() {
		double ratio = getAverageDistance(SENSOR_RIGHT) / (getAverageDistance(SENSOR_ANGLER) * 1.0);

		if (getAverageDistance(SENSOR_RIGHT) < 100)
			return false;
		return ratio > 1.75 && ratio < 3.0;
		// int r = getAverageDistance(SENSOR_RIGHT);
		// int ra = getAverageDistance(SENSOR_ANGLER);
		// System.out.println("Right:  " + Integer.toString(r));
		// System.out.println("RightA: " + Integer.toString(ra));
		//
		// if ((940 < r && r < 950) && (430 < ra && ra < 450))
		// return true;
		// return false;
	}

	protected void right() {
		System.out.println("Turning right...");
		rotate(90);
	}

	protected void left() {
		System.out.println("Turning left...");
		rotate(-90);
	}

	protected boolean closeToWall() {
		for (int i = 0; i < 8; i++) {
			if (getAverageDistance(i) > 500)
				return true;
		}
		return false;
	}

//	protected boolean closeToRigthWall() {
//		if (getAverageDistance(SENSOR_RIGHT) > 600 && getAverageDistance(SENSOR_ANGLER) > 100)
//			return true;
//		return false;
//	}

	protected boolean detectCornerRight() {
		if (getAverageDistance(SENSOR_ANGLER) < 15 && getAverageDistance(SENSOR_RIGHT) > 200) {
			System.out.println("Detected corner: right");
			return true;
		}
		return false;
	}

	protected boolean detectCornerLeft() {
		if (getAverageDistance(SENSOR_ANGLEL) < 15 && getAverageDistance(SENSOR_LEFT) > 200) {
			System.out.println("Detected corner: left");
			return true;
		}
		return false;
	}

	protected void forward() {
		setMotorSpeeds(SPEED_FORWARD, SPEED_FORWARD);
	}

	protected void forward(long distance) {
		long start = getLeftWheelPosition();
		long end = start + distance;
		System.out.println(distance);
		forward();
		while (Math.abs(getLeftWheelPosition()) < end) {
			// if (closeToWall())
			// break;
			sleep(1);
		}
		stop();
	}

	protected void stop() {
		setMotorSpeeds(0, 0);
	}

}
