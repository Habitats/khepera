package khepera.state;

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
		//Call moving this babe forward
		movementManager.forward(distanceToMove);
		setTransitionFlag(movementDoneTransition);
		
		if (sensorManager.isWallInFront()){
			setTransitionFlag(collisionTransition);
		}
	}

	@Override
	public void resetState()
	{
	}
}
