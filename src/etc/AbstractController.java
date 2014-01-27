package etc;

import java.util.ArrayList;
import java.util.List;

import edu.wsu.KheperaSimulator.RobotController;
import etc.RobotEvent.RobotAction;
import gui.RobotFrame;

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

	protected final int SPEED_FORWARD = 5;
	protected final int SPEED_FORWARD_SLOW = 3;
	protected final int SPEED_ROTATE = 2;

	private int currentSpeed;

	protected RobotState state;

	protected double locationX = 0;
	protected double locationY = 0;

	protected Movement move;
	protected History history;
	protected Balls balls;
	private RobotFrame robotFrame;
	private List<Coord> map;
	private long startTime;

	public AbstractController() {
		startTime = System.currentTimeMillis();
		robotFrame = new RobotFrame();

		move = new Movement(this);
		balls = new Balls(this);

		history = new History();
		map = new ArrayList<Coord>();

		state = RobotState.LOOKING_FOR_BALL;
	}

	@Override
	public void doWork() throws Exception {

		setStatus("Time passed: " + Double.toString((System.currentTimeMillis() - startTime) / 1000.), 0);

		setStatus("Speed: " + getSpeed(), 6);

		setStatus(String.format("Location: (%d, %d)", (int) locationX, (int) locationY), 7);

		setStatus("Direction radian: " + getDirectionInRadians(), 9);
		setStatus("Direction h or v: " + Boolean.toString(approximately(getDirectionInRadians() % (Math.PI / 2), 0., 0.1)), 8);

		setStatus(history.toString(), 11);
		setStatus("State: " + state.name(), 12);

		switch (state) {
		case LOOKING_FOR_BALL:
			findBall();
			break;
		case GOING_HOME:
			goHome();
			break;
		case IDLE:
			break;
		}
	}

	// children need to implement their own way of finding the actual ball, as wall as getting back home
	abstract protected void goHome();

	abstract protected void findBall();

	private void updateMap() {
		map.add(getCurrentLocation());
		robotFrame.draw(getCurrentLocation().getNormalized(), state);
		robotFrame.direction(getDirectionInRadians());
	}

	@Override
	public void close() throws Exception {
		// levelPanel.
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
		// System.out.println("Turning right...");
		rotate(90);
	}

	protected void left() {
		// System.out.println("Turning left...");
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

	protected void setSpeed(int speed) {
		currentSpeed = speed;
	}

	private int getSpeed() {
		return currentSpeed;
	}

	// default to logging the event
	protected void rotate(long degrees) {
		rotate(degrees, true);
	}

	protected void rotate(long degrees, boolean LOG) {
		setStatus("Rotating: True", 1);
		long start = getLeftWheelPosition();

		int d = 1;
		if (degrees < 0)
			d = -1;

		setMotorSpeeds(SPEED_ROTATE * d, -SPEED_ROTATE * d);
		while (Math.abs(getLeftWheelPosition() - start) / 3 < Math.abs(degrees)) {
			sleep(1);
			updateMap();
		}
		// stop rotation
		stop();

		updateMap();

		setStatus("Rotating: False", 1);

		// do not log this event
		if (!LOG)
			return;
		RobotEvent e = new RobotEvent(state, RobotAction.ROTATE, degrees);
		history.addEvent(e);

	}

	protected void forward(long distance) {
		int tick = (int) (Math.floor(distance / 20.));
		int rest = (int) (distance % 20);
		for (int i = 0; i < tick; i++) {
			forward(20, SPEED_FORWARD);
		}
		forward(rest, SPEED_FORWARD);
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

	protected void setStatus(String s, int i) {
		robotFrame.setStatus(s, i);
	}

	protected Coord getCurrentLocation() {
		Coord location = new Coord((int) locationX, (int) locationY);
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
