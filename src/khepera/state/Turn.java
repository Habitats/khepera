package khepera.state;

import khepera.managers.MovementManager;


public class Turn extends State {

	int doneTransition;
	MovementManager.Direction direction;
	
	public Turn(MovementManager.Direction dir, int doneTransition) {
		this.doneTransition = doneTransition;
		this.direction = dir;
	}

  @Override
  public void doWork() {
	  movementManager.rotate(90, direction);
	  nextTransition = doneTransition;
	  System.out.println("Next transition is: " + nextTransition);
	  shouldTransition = true;
  }

  @Override
  public void resetState() {
	  System.out.println("Resetting turn");
	  nextTransition = 0;
  }
}
