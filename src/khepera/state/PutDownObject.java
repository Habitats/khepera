package khepera.state;

/**
 * This state simply puts down anything in the gripper.
 * @author Olav
 *
 */
public class PutDownObject extends State {

	int done;
	
	public PutDownObject(int doneTransition) {
		this.done = doneTransition;
	} 

	@Override
	public void doWork() {
		movementManager.dropBall();
		setTransitionFlag(done);
	}

	@Override
	public void resetState() {
	}
}
