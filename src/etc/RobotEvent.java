package etc;

import etc.AbstractController.RobotState;
import etc.RobotEvent.RobotAction;

public class RobotEvent {
	public enum RobotAction {
		FORWARD, //
		ROTATE, //
	}

	private Turn turn;
	private int direction;
	private long time;
	private RobotAction action;
	private int speed;
	private long distance;
	private long degrees;
	private final RobotState state;

	// rotation
	// public RobotEvent(RobotAction action, Turn t, double rads, int direction) {
	// time = System.currentTimeMillis();
	//
	// this.action = action;
	// this.turn = t;
	// this.direction = direction;
	// }

	// forward
	public RobotEvent(RobotState state, RobotAction action, long distance, int speed) {
		this.state = state;
		time = System.currentTimeMillis();

		this.action = action;
		this.distance = distance;
		this.speed = speed;
	}

	public RobotEvent(RobotState state, RobotAction action, long degrees) {
		this.state = state;
		this.action = action;
		this.degrees = degrees;
	}

	public RobotAction getAction() {
		return action;
	}

	public int getDirection() {
		return direction;
	}

	public long getDistance() {
		return distance;
	}

	public int getSpeed() {
		return speed;
	}

	public Turn getTurn() {
		return turn;
	}

	public long getTime() {
		return time;
	}

	public RobotState getState() {
		return state;
	}
	public long getDegrees() {
		return degrees;
	}
}
