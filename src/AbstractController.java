import edu.wsu.KheperaSimulator.KSGripperStates;
import edu.wsu.KheperaSimulator.RobotController;
import gui.LevelPanel;
import gui.StatusPanel;

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

	final int SPEED_FORWARD = 7;
	final int SPEED_FORWARD_SLOW = 3;
	final int SPEED_ROTATE = 5;

	protected StatusPanel statusPanel;

	protected LevelPanel levelPanel;

	@Override
	public void doWork() throws Exception {
		setStatus("Doing work: " + Long.toString(System.currentTimeMillis() / 1000), 0);
		levelPanel.draw((int) getLeftWheelPosition(), (int) getLeftWheelPosition());
		levelPanel.direction(getDirectionInRadians());

		setStatus("Left Wheel: " + Long.toString(getLeftWheelPosition() % 1080) + " " + Long.toString(getLeftWheelPosition()), 6);
		setStatus("Right Wheel: " + Long.toString(getRightWheelPosition() % 1080) + " " + Long.toString(getRightWheelPosition()), 7);

		setStatus("Direction: " + getDirectionInRadians(), 9);
		setStatus("Direction: " + getDirectionInDegrees(), 8);
	}

	@Override
	abstract public void close() throws Exception;

	public AbstractController() {
		statusPanel = new StatusPanel();
		levelPanel = new LevelPanel();
	}

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
		setStatus("Rotating!", 1);
		long start = getLeftWheelPosition();
		int d = 1;
		if (degrees < 0)
			d = -1;
		// System.out.println("Rotating: " + Long.toString(degrees));
		setMotorSpeeds(SPEED_ROTATE * d, -SPEED_ROTATE * d);
		while (Math.abs(getLeftWheelPosition() - start) / 3 < Math.abs(degrees)) {
			// System.out.println(Math.abs(getLeftWheelPosition() - start) / 3);
			sleep(1);
		}
		stop();
		state = S.IDLE;
		setStatus(" ", 1);
	}

	protected boolean alignedWithRightWall() {
		double ratio = getAverageDistance(SENSOR_RIGHT) / (getAverageDistance(SENSOR_ANGLER) * 1.0);

		if (getAverageDistance(SENSOR_RIGHT) < 100) {
			return false;
		}
		if (ratio > 1.75 && ratio < 3.0) {
			setStatus("Aligned!", 2);
			return true;
		} else {
			setStatus("Not aligned!", 2);
			return false;
		}
		// int r = getAverageDistance(SENSOR_RIGHT); int ra = getAverageDistance(SENSOR_ANGLER);
		//
		// double c = 1 / Math.cos(Math.PI / 4) * ra;
		//
		// System.out.println("Right:  " + Integer.toString(r));
		// System.out.println("RightA: " + Integer.toString(ra));
		// System.out.println("RightC: " + Double.toString(c));
		//
		// if (r > 900 && c > 1023 && c < 1200) {
		// setStatus("Aligned!", 2);
		// return true;
		// }
		// setStatus("Not aligned!", 2);
		// return false;
	}

	protected void right() {
		rotate(90);
	}

	protected void left() {
		rotate(-90);
	}

	protected boolean closeToWall() {
		for (int i = 0; i < 8; i++) {
			if (getAverageDistance(i) > 800) {
				setMotorSpeeds(2, 2);
				setStatus("Close to wall!", 3);
				return true;
			}
		}
		setStatus("Far from wall!", 3);
		return false;
	}

	// protected boolean closeToRigthWall() {
	// if (getAverageDistance(SENSOR_RIGHT) > 600 && getAverageDistance(SENSOR_ANGLER) > 100)
	// return true;
	// return false;
	// }

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

	private void setStatus(String s, int i) {
		statusPanel.setLabel(s, i);
	}

	protected void getLocation() {

	}

	protected int getDirectionInDegrees() {
		long diff = getLeftWheelPosition() - getRightWheelPosition();
		diff /= 2;
		int direction = (int) (diff % 1080) / 3;
		return direction;
	}

	protected double getDirectionInRadians() {
		return (getDirectionInDegrees() / 180.) * Math.PI;

	}
}
