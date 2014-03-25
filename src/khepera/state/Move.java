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
		nextTransition = movementDoneTransition;
		shouldTransition = true;
		
		if (sensorManager.isWallInFront()){
			nextTransition = collisionTransition; 
			shouldTransition = true;
		}
	}

	@Override
	public void resetState()
	{
		nextTransition = 0;
	}
}
