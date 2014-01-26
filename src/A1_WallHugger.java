import etc.AbstractController;
import etc.RobotEvent;
import etc.Turn;

/**
 * Hugs the right wall, navigating the map
 * 
 * @author Patrick
 * 
 */
public class A1_WallHugger extends AbstractController {

	int started = 0;
	private long time;

	public A1_WallHugger() {
		super();
		setSpeed(SPEED_FORWARD);

		time = System.currentTimeMillis();
	}

	@Override
	public void doWork() throws Exception {
		super.doWork();
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

	private void findBall() {
		if (!goingVerticalOrHorizontal()) {
			correctDirection();
		}

		// if (closeToWall()) {
		// }
		Turn t;
		if ((t = detectCorner()) != null) {
			if (t == Turn.CORNER)
				t = evalCorner(t);
			move.move(t);
		}

		if (System.currentTimeMillis() - time > 10000 && history.backWardSize() == 0)
			state = RobotState.GOING_HOME;
		else if (history.forwardSize() == 0)
			state = RobotState.LOOKING_FOR_BALL;
	}

	private void goHome() {
		if (history.backWardSize() == 0)
			rotate(180, false);

		if (history.forwardSize() > 0) {
			RobotEvent e = history.getLastEvent();
			switch (e.getAction()) {
			case FORWARD:
				forward(e.getDistance());
				break;
			case ROTATE:
				rotate(e.getDegrees() * -1);
				break;
			default:
				break;

			}
		} else {
			rotate(180, false);
			state = RobotState.IDLE;
		}
	}

	@Override
	public void close() throws Exception {
	}
}