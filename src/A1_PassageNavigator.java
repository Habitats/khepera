import etc.AbstractController;
import etc.Turn;

/**
 * Hugs the wall -- great for navigating "passage", not so great for anything else...
 * 
 * @author Patrick
 * 
 */
public class A1_PassageNavigator extends AbstractController {

	int started = 0;

	public A1_PassageNavigator() {
		super();
		setSpeed(SPEED_FORWARD);
	}

	@Override
	protected void findBall() {
		if (!goingVerticalOrHorizontal()) {
			correctDirection();
		}

		Turn t;
		if ((t = detectCorner()) != null) {
			if (t == Turn.CORNER)
				t = evalCorner(t);
			else if (t == Turn.WALL)
				t = evalWall(t);
			move.move(t);
		}

		// if (System.currentTimeMillis() - time > 20000 && history.backWardSize() == 0)
		// state = RobotState.GOING_HOME;
		// else if (history.forwardSize() == 0)
		state = RobotState.LOOKING_FOR_BALL;
	}

	@Override
	protected void goHome() {
		move.goHomeTheSameWayYouCame();
	}

	protected Turn evalCorner(Turn t) {
		// move a little forward in order to examine the corner and its surroundings, 410 is a little more than one robot length, giving it some space
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
		setStatus("Detected: " + t.name(), 2);
		System.out.println("Detected: " + t.name());

		return t;
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

	protected Turn evalWall(Turn t) {
		if (detectWall() && detectRight() && !detectLeft())
			t = Turn.RIGHT_CORNER;
		// wall front and left
		else if (detectWall() && !detectRight() && detectLeft())
			t = Turn.LEFT_CORNER;
		else if (detectWall() && detectRight() && detectLeft())
			t = Turn.DEADEND;

		setStatus("Detected: " + t.name(), 2);
		System.out.println("Detected: " + t.name());

		return t;

	}
}