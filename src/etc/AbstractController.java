package etc;

import java.util.ArrayList;
import java.util.List;

import edu.wsu.KheperaSimulator.KSGripperStates;
import edu.wsu.KheperaSimulator.RobotController;
import etc.RobotEvent.RobotAction;
import gui.LevelPanel;
import gui.StatusPanel;

/**
 * Abstract controller with base functions and constants
 * 
 * @author Patrick
 * 
 */
public abstract class AbstractController extends RobotController {
	public enum RobotState {
		GOING_HOME, //
		LOOKING_FOR_BALL, //
		IDLE, //
	}

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

	protected final int SPEED_FORWARD = 5;
	protected final int SPEED_FORWARD_SLOW = 3;
	protected final int SPEED_ROTATE = 2;

	private int currentSpeed;

	protected RobotState state;

	protected double locationX = 0;
	protected double locationY = 0;

	protected StatusPanel statusPanel;

	protected LevelPanel levelPanel;

	protected Movement move;
	protected History history;

	@Override
	public void doWork() throws Exception {

		setStatus("Doing work: " + Long.toString(System.currentTimeMillis() / 1000), 0);

		setStatus("Speed: " + getSpeed(), 6);

		setStatus(String.format("Location: (%d, %d)", (int) locationX, (int) locationY), 7);

		setStatus("Direction radian: " + getDirectionInRadians(), 9);
		setStatus("Direction h or v: " + Boolean.toString(approximately(getDirectionInRadians() % (Math.PI / 2), 0., 0.1)), 8);

		setStatus(history.toString(), 11);
		setStatus("State: " + state.name(), 12);
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
		move = new Movement(this);
		history = new History();

		state = RobotState.LOOKING_FOR_BALL;
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

	protected Turn detectCorner() {
		Turn t = null;

		if (!goingVerticalOrHorizontal())
			return null;

		else if (detectLeftTurn() || detectRightTurn())
			t = Turn.CORNER;
		else if (!closeToWall() || !detectWall())
			t = Turn.STRAIGTH;
		else if (!detectLeftTurn() & !detectRightTurn() && detectWall())
			t = Turn.WALL;

		return t;
	}

	protected Turn evalCorner(Turn t) {
		// move a little forward in order to examine the corner and its surroundings
		forward(410);

		// no walls
		if (!detectWall() && !detectRight() && !detectLeft())
			t = Turn.FULL_CROSSING;
		// wall front
		else if (detectWall() && !detectRight() && !detectLeft())
			t = Turn.CROSSING;
		// wall front and right
		else if (detectWall() && detectRight() && !detectLeft())
			t = Turn.RIGHT_CORNER;
		// wall front and left
		else if (detectWall() && !detectRight() && detectLeft())
			t = Turn.LEFT_CORNER;
		// wall left
		else if (!detectWall() && !detectRight() && detectLeft())
			t = Turn.LEFT_TURN;
		// wall rigth
		else if (!detectWall() && detectRight() && !detectLeft())
			t = Turn.RIGHT_TURN;
		// walls everywhere!
		else if (detectWall() && detectRight() && detectLeft())
			t = Turn.DEADEND;
		statusPanel.setLabel("Detected: " + t.name(), 2);
		System.out.println("Detected: " + t.name());

		return t;
	}

	protected void setSpeed(int speed) {
		currentSpeed = speed;
	}

	protected boolean detectLeft() {
		return getAverageDistance(SENSOR_ANGLER) > 5//
				&& getAverageDistance(SENSOR_RIGHT) > 100;
	}

	protected boolean detectRight() {
		return getAverageDistance(SENSOR_ANGLEL) > 5 //
				&& getAverageDistance(SENSOR_LEFT) > 100;
	}

	protected boolean detectRightTurn() {
		return !detectWall() //
				&& getAverageDistance(SENSOR_ANGLER) < 5 //
				&& getAverageDistance(SENSOR_RIGHT) > 100;
	}

	protected boolean detectLeftTurn() {
		return !detectWall() //
				&& getAverageDistance(SENSOR_ANGLEL) < 5 //
				&& getAverageDistance(SENSOR_LEFT) > 100;
	}

	protected boolean detectWall() {
		return getAverageDistance(SENSOR_FRONTL) > 100 //
				&& getAverageDistance(SENSOR_FRONTR) > 100;
	}

	private int getSpeed() {
		return currentSpeed;
	}

	protected void rotate(long degrees) {
		rotate(degrees, true);
	}

	protected void rotate(long degrees, boolean LOG) {
		setStatus("Rotating!", 1);
		long start = getLeftWheelPosition();

		int d = 1;
		if (degrees < 0)
			d = -1;

		setMotorSpeeds(SPEED_ROTATE * d, -SPEED_ROTATE * d);
		while (Math.abs(getLeftWheelPosition() - start) / 3 < Math.abs(degrees)) {
			sleep(1);
		}
		// stop rotation
		stop();

		updateMap();

		setStatus(" ", 1);

		// do not log this event
		if (!LOG)
			return;
		RobotEvent e = new RobotEvent(state, RobotAction.ROTATE, degrees);
		history.addEvent(e);

	}

	protected void forward(long distance) {
		forward(distance, SPEED_FORWARD);
	}

	protected void forward(long distance, int speed) {
		setSpeed(speed);

		long start = getLeftWheelPosition();
		long end = start + distance;
		setMotorSpeeds(getSpeed(), getSpeed());
		while (Math.abs(getLeftWheelPosition()) < end && !crashing()) {
			// if (closeToWall())
			// break;
			sleep(1);
		}
		// stop motors until next forward-call
		stop();

		// update to actual traveled distance
		distance = getLeftWheelPosition() - start;

		// update the map and location
		updateLocation(distance);
		updateMap();

		// add event to history
		RobotEvent e = new RobotEvent(state, RobotAction.FORWARD, distance, speed);
		history.addEvent(e);
	}

	private boolean crashing() {
		boolean crash = getAverageDistance(SENSOR_FRONTL) > 1000 || getAverageDistance(SENSOR_FRONTR) > 1000;
		if (crash)
			System.out.println("Avoiding crash!");
		return crash;
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
