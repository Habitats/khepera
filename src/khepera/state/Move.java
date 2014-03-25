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
		getMover().forward(distanceToMove);
		nextTransition = movementDoneTransition;
		
		if (sensorManager.isWallInFront()){
			System.err.println("WALL NIGA!");
			nextTransition = collisionTransition; 
		}
	}

	@Override
	public void resetState()
	{
		nextTransition = 0;
	}
}
