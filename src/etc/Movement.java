package etc;

import etc.AbstractController.RobotState;

public class Movement {
	private AbstractController robot;

	public Movement(AbstractController robot) {
		this.robot = robot;
	}

	public void move(Turn t) {

		switch (t) {
		case LEFT_CORNER:
			leftCorner();
			break;
		case RIGHT_CORNER:
			rigthCorner();
			break;
		case RIGHT_TURN:
			rightTurn();
			break;
		case LEFT_TURN:
			leftTurn();
			break;
		case CROSSING:
			crossing();
			break;
		case FULL_CROSSING:
			fullCrossing();
			break;
		case DEADEND:
			deadEnd();
			break;
		case WALL:
			wall();
			break;
		case STRAIGTH:
			straight();
			break;
		}
	}

	private void straight() {
		robot.forward(20);
	}

	private void fullCrossing() {
		double rand = Math.random();
		if (rand > 0.33 && rand < 0.66)
			robot.right();
		else if (rand < 0.33)
			robot.left();
		else
			robot.forward(100);
		robot.forward(100);
	}

	private void crossing() {
		if (Math.random() > 0.5)
			robot.right();
		else
			robot.left();
		robot.forward(100);

	}

	private void wall() {
		if (Math.random() > 0.5)
			robot.right();
		else
			robot.left();
		// robot.forward(300);
	}

	private void rightTurn() {
		robot.stop();
		if (Math.random() > 0.5)
			robot.right();
		else
			robot.forward(100);
	}

	private void leftTurn() {
		robot.stop();

		if (Math.random() > 0.5)
			robot.left();
		else
			robot.forward(100);
	}

	private void rigthCorner() {
		robot.right();

	}

	private void leftCorner() {
		robot.left();

	}

	private void deadEnd() {
		robot.rotate(180);
		// robot.forward(300);
	}

	public void goHomeTheSameWayYouCame() {
		if (robot.history.backWardSize() == 0)
			robot.rotate(180, false);

		if (robot.history.forwardSize() > 0) {
			RobotEvent e = robot.history.getLastEvent();
			switch (e.getAction()) {
			case FORWARD:
				robot.forward(e.getDistance());
				break;
			case ROTATE:
				robot.rotate(e.getDegrees() * -1);
				break;
			default:
				break;

			}
		} else {
			robot.rotate(180, false);
			robot.state = RobotState.IDLE;
		}
	}
}
