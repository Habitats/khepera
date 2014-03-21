package khepera.state;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;


public class Move extends State
{
	private int collisionTransition;
	private int movementDoneTransition;
	private int distanceToMove;
	
	public Move(int distanceToMove, int collisionTransition, int movementDoneTransition) {
		this.distanceToMove = distanceToMove;
		this.collisionTransition = collisionTransition;
		this.movementDoneTransition = movementDoneTransition;
	}
	
	@Override
	public void doWork()
	{
		movementManager.forward(distanceToMove);
	}

	@Override
	public void resetState()
	{
		//Nothing needed here
	}

}
