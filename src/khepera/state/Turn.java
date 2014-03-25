package khepera.state;

import khepera.managers.MovementManager.Direction;


public class Turn extends State {

	int doneTransition;
	Direction direction;
	
	public Turn(Direction dir, int doneTransition) {
		this.doneTransition = doneTransition;
		this.direction = dir;
	}

  @Override
  public void doWork() {
	  movementManager.rotate(90, direction);
	  setTransitionFlag(doneTransition);
  }

  @Override
  public void resetState() {
  }
}
