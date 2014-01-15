/**
 * Hugs the right wall, navigating the map
 * 
 * @author Patrick
 * 
 */
public class AWallHugger extends AbstractController {

	int started = 0;

	@Override
	public void doWork() throws Exception {
		if (started == 0) {
			forward();
			started = 1;
		}
		// avoidWall();
		hugRightWall();
	}

	protected boolean alignedWithRightWall() {
		double ratio = getAverageDistance(RIGHT) / (getAverageDistance(ANGLER) * 1.0);

		System.out.println(getAverageDistance(RIGHT) - getAverageDistance(ANGLER));
		System.out.println(ratio);

		if (getAverageDistance(RIGHT) < 200)
			return false;
		return ratio > 1.75 && ratio < 3.5;
	}

	private void hugRightWall() {
		if (closeToWall()) {
			if (alignedWithRightWall()) {
				setMotorSpeeds(SPEED_FORWARD, SPEED_FORWARD - 1);
			} else {
				System.out.println("Stopping");
				rotate(-1);
			}
		}
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

}