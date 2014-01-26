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
		detectTurn();
		forward(20);
	}

	@Override
	public void close() throws Exception {
	}
}