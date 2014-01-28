package etc;

import edu.wsu.KheperaSimulator.KSGripperStates;

public class Balls {

	public final int GRIP_OPEN = KSGripperStates.GRIP_OPEN;
	public final int GRIP_CLOSED = KSGripperStates.GRIP_CLOSED;
	public final int ARM_DOWN = KSGripperStates.ARM_DOWN;
	public final int ARM_UP = KSGripperStates.ARM_UP;

	private AbstractController robot;

	public Balls(AbstractController robot) {
		this.robot = robot;
	}

	public void pickUpBall() {
		robot.setGripperState(GRIP_OPEN);
		robot.sleep(500);
		robot.setArmState(ARM_DOWN);
		robot.sleep(500);
		robot.setGripperState(GRIP_CLOSED);
		robot.sleep(500);
		robot.setArmState(ARM_UP);
		robot.sleep(500);
	}

	public boolean detectBalls() {
		return false;
	}
}
