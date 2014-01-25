/**
 * Hugs the right wall, navigating the map
 * 
 * @author Patrick
 * 
 */
public class A1_WallHugger extends AbstractController {

	public enum Turn {
		LEFT, RIGHT, CROSSING;
	}

	int started = 0;

	public A1_WallHugger() {
		super();
	}

	@Override
	public void doWork() throws Exception {
		super.doWork();
		if (closeToWall()) {
			hugRightWall();
		} else {
			forward();
		}

	}

	private boolean detectTurn() {
		Turn t;
		if (detectCornerLeft() && detectCornerRight())
			t = Turn.CROSSING;
		else if (detectCornerLeft())
			t = Turn.LEFT;
		else if (detectCornerRight())
			t = Turn.RIGHT;
		else
			return false;
		statusPanel.setLabel("Turning: " + t.name(), 1);

		// move forward in order to turn
		stop();
		sleep(500);
		forward(430);

		if (t == Turn.LEFT)
			left();
		else if (t == Turn.RIGHT)
			right();
		else if (t == Turn.CROSSING) {
			if (Math.random() > 0.5)
				right();
			else
				left();
		}
		forward(300);
		statusPanel.setLabel(" ", 1);
		return true;
	}

	private void hugRightWall() {
		if (closeToWall()) {
			if (alignedWithRightWall()) {
				if (!detectTurn())
					setMotorSpeeds(SPEED_FORWARD, SPEED_FORWARD - 1);
			} else {
				// System.out.println("Aligning");
				rotate(-1);
			}
		}
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

}