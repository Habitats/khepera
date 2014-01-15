import edu.wsu.KheperaSimulator.KSGripperStates;
import edu.wsu.KheperaSimulator.RobotController;

/**
 * Abstract controller with base functions and constants
 * 
 * @author Patrick
 * 
 */
public abstract class AbstractController extends RobotController {
	public final int LEFT = 0;
	public final int ANGLEL = 1;
	public final int FRONTL = 2;
	public final int FRONTR = 3;
	public final int ANGLER = 4;
	public final int RIGHT = 5;
	public final int BACKR = 6;
	public final int BACKL = 7;

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
		double accuracy = 3.;
		int avg = 0;
		for (int i = 0; i < accuracy; i++) {
			avg += (int) (getDistanceValue(sensorID) / accuracy);
		}
		return avg;
	}

	protected void rotate(long degrees) {
		long start = getLeftWheelPosition();
		int d = 1;
		if (degrees < 0)
			d = -1;
		System.out.println("Rotating: " + Long.toString(degrees));
		setMotorSpeeds(SPEED_ROTATE * d, -SPEED_ROTATE * d);
		while (Math.abs(getLeftWheelPosition() - start) / 3 < Math.abs(degrees)) {
			System.out.println(Math.abs(getLeftWheelPosition() - start) / 3);
			sleep(1);
		}
		stop();
	}

	protected boolean closeToWall() {
		for (int i = 0; i < 8; i++) {
			if (getAverageDistance(i) > 800)
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
			sleep(10);
		}
		stop();
	}

	protected void stop() {
		setMotorSpeeds(0, 0);
	}

}
