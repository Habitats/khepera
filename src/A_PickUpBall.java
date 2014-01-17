

public class A_PickUpBall extends AbstractController {

	boolean started = false;
	boolean done = false;

	@Override
	public void doWork() throws Exception {
		if (done)
			return;
		findBall();
	}

	private void pickUpBall() {
		setGripperState(GRIP_OPEN);
		sleep(500);
		setArmState(ARM_DOWN);
		sleep(500);
		setGripperState(GRIP_CLOSED);
		sleep(500);
		setArmState(ARM_UP);
		sleep(500);
	}

	private void findBall() {
		if (!closeToWall())
			forward();
		else {
			System.out.println("found something!");
			stop();
			sleep(500);
			pickUpBall();

			returnToBase();
		}
	}

	private void returnToBase() {

		// return to base
		long distanceTraveled = getLeftWheelPosition();
		rotate(180);
		forward(distanceTraveled);
		rotate(180);
		done = true;
	}

	@Override
	public void close() throws Exception {
	}

}
