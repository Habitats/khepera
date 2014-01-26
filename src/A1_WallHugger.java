import etc.AbstractController;
import etc.Turn;

/**
 * Hugs the right wall, navigating the map
 * 
 * @author Patrick
 * 
 */
public class A1_WallHugger extends AbstractController {

	int started = 0;

	public A1_WallHugger() {
		super();
		setSpeed(SPEED_FORWARD);
	}

	@Override
	public void doWork() throws Exception {
		super.doWork();

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
	}

	@Override
	public void close() throws Exception {
	}
}