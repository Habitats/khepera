package khepera.state;

import khepera.managers.MovementManager;


public class Turn extends State {

	int nextTransition = 0;
	int doneTransition;
	MovementManager.Direction direction;
	
	public Turn(MovementManager.Direction dir, int doneTransition) {
		this.doneTransition = doneTransition;
		this.direction = dir;
	}

  @Override
  public void doWork() {
	  //TODO: get access to movementmanager

  }

  @Override
  public void resetState() {
	  nextTransition = 0;
  }
}
