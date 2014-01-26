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

	public enum Turn {
		LEFT_TURN, RIGHT_TURN, CROSSING, WALL, LEFT_CORNER, RIGHT_CORNER, DEADEND, FULL_CROSSING;
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

	final int SPEED_FORWARD = 5;
	final int SPEED_FORWARD_SLOW = 3;
	final int SPEED_ROTATE = 2;

	private int currentSpeed;

	protected double locationX = 0;
	protected double locationY = 0;

	protected StatusPanel statusPanel;

	protected LevelPanel levelPanel;

	@Override
	public void doWork() throws Exception {

		setStatus("Doing work: " + Long.toString(System.currentTimeMillis() / 1000), 0);

		setStatus("Speed: " + getSpeed(), 6);

		setStatus(String.format("Location: (%d, %d)", (int) locationX, (int) locationY), 7);

		setStatus("Direction radian: " + getDirectionInRadians(), 9);
		setStatus("Direction h or v: " + Boolean.toString(approximately(getDirectionInRadians() % (Math.PI / 2), 0., 0.1)), 8);
		// setStatus("Direction degrees: " + getDirectionInDegrees(), 8);

	}

	private void updateMap() {
		levelPanel.draw(getLocation()[0], getLocation()[1]);
		levelPanel.direction(getDirectionInRadians());
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
			sleep(1);
		}
		updateMap();
		stop();
		state = S.IDLE;
		setStatus(" ", 1);
	}

	// a ~ b
	protected boolean approximately(double a, double b, double delta) {
		return (a < (b + delta) && a > (b - delta));
	}

	protected boolean goingVerticalOrHorizontal() {
		return approximately(getDirectionInRadians() % (Math.PI / 2), 0., 0.05);
	}

	protected void right() {
		System.out.println("Turning right...");
		rotate(90);
	}

	protected void left() {
		System.out.println("Turning left...");
		rotate(-90);
	}

	protected boolean closeToSomething() {
		return getMaxSensorValue() > 15;
	}

	protected boolean closeToWall() {
		if (getMaxSensorValue() > 800) {
			setStatus("Close to wall!", 3);
			return true;
		}
		setStatus("Far from wall!", 3);
		return false;
	}

	protected void correctDirection() {
		double d = getDirectionInRadians() + 2 * Math.PI;
		int x;
		if ((d % (Math.PI / 4)) > (Math.PI / 8))
			x = 1;
		else
			x = -1;
		rotate(x);
	}

	protected boolean detectTurn() {
		Turn t;
		if (!goingVerticalOrHorizontal())
			return false;

		if (detectLeftTurn() && detectRightTurn() && !detectWall())
			t = Turn.FULL_CROSSING;
		else if (detectLeftTurn() && detectRightTurn())
			t = Turn.CROSSING;
		else if (detectLeftCorner())
			t = Turn.LEFT_CORNER;
		else if (detectRightCorner())
			t = Turn.RIGHT_CORNER;
		else if (detectLeftTurn())
			t = Turn.LEFT_TURN;
		else if (detectRightTurn())
			t = Turn.RIGHT_TURN;
		else if (detectDeadend())
			t = Turn.DEADEND;
		else if (detectWall())
			t = Turn.WALL;
		else
			return false;

		statusPanel.setLabel("Detected: " + t.name(), 2);
		System.out.println("Detected: " + t.name());

		if (t == Turn.WALL) {
			if (Math.random() > 0.5)
				right();
			else
				left();
			forward(300);
		} else if (t == Turn.DEADEND) {
			rotate(180);
			forward(300);
		} else if (t == Turn.RIGHT_CORNER) {
			right();
			forward(300);
		} else if (t == Turn.LEFT_CORNER) {
			left();
			forward(300);
		} else {

			// move forward in order to turn
			stop();
			sleep(100);
			forward(150);
			if (detectWall()) {
				System.out.println("Wall!");
			} else {

				if (t == Turn.LEFT_TURN) {
					if (Math.random() > 0.5)
						left();
					// else
					// forward(100);
				} else if (t == Turn.RIGHT_TURN) {
					if (Math.random() > 0.5)
						right();
					// else forward(100);
				} else if (t == Turn.CROSSING) {
					if (Math.random() > 0.5)
						right();
					else
						left();
				} else if (t == Turn.FULL_CROSSING) {
					double rand = Math.random();
					if (rand > 0.33 && rand < 0.66)
						right();
					else if (rand < 0.33)
						left();
					else
						forward(100);
				}
			}
		}
		statusPanel.setLabel(" ", 1);
		setSpeed(SPEED_FORWARD);
		return true;
	}

	protected void setSpeed(int speed) {
		currentSpeed = speed;
	}

	protected boolean detectRightTurn() {
		return (getAverageDistance(SENSOR_ANGLER) < 5 && getAverageDistance(SENSOR_RIGHT) < 5);
	}

	protected boolean detectLeftTurn() {
		return (getAverageDistance(SENSOR_ANGLEL) < 5 && getAverageDistance(SENSOR_LEFT) < 5);
	}

	protected boolean detectDeadend() {
		return detectWall() && getAverageDistance(SENSOR_LEFT) > 100 && getAverageDistance(SENSOR_RIGHT) > 100;
	}

	protected boolean detectRightCorner() {
		return (detectWall() && getAverageDistance(SENSOR_LEFT) > 100 && getAverageDistance(SENSOR_RIGHT) < 15);
	}

	protected boolean detectLeftCorner() {
		return (detectWall() && getAverageDistance(SENSOR_RIGHT) > 100 && getAverageDistance(SENSOR_LEFT) < 15);
	}

	protected boolean detectWall() {
		return (getAverageDistance(SENSOR_FRONTL) > 800 && getAverageDistance(SENSOR_FRONTR) > 800);
	}

	private int getSpeed() {
		return currentSpeed;
	}

	protected void forward(long distance) {
		forward(distance, SPEED_FORWARD);
	}

	protected void forward(long distance, int speed) {
		setSpeed(speed);
		if (crashing())
			rotate((long) (Math.random() * 180));

		updateLocation(distance);
		long start = getLeftWheelPosition();
		long end = start + distance;
		setMotorSpeeds(getSpeed(), getSpeed());
		while (Math.abs(getLeftWheelPosition()) < end && !crashing()) {
			// if (closeToWall())
			// break;
			sleep(1);
		}
		updateMap();
		stop();
	}

	private boolean crashing() {

		// return getAverageDistance(SENSOR_FRONTL) > 1000 || getAverageDistance(SENSOR_RIGHT) > 1000;
		return false;
	}

	private void updateLocation(long d) {
		double r = getDirectionInRadians();
		double xNew = (int) (Math.cos(r) * d + locationX);
		double yNew = (int) (Math.sin(r) * d + locationY);

		setLocation(xNew, yNew);
	}

	private void setLocation(double xNew, double yNew) {
		locationX = xNew;
		locationY = yNew;
	}

	protected void stop() {
		setMotorSpeeds(0, 0);
	}

	private void setStatus(String s, int i) {
		statusPanel.setLabel(s, i);
	}

	protected int[] getLocation() {
		int[] location = { ((int) locationX / 15), ((int) locationY / 15) };
		return location;
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

	protected int getMaxSensorValue() {
		int max = 0;
		for (int i = 0; i < 8; i++) {
			int avg = getAverageDistance(i);
			if (avg > max)
				max = avg;
		}
		return max;
	}
}
