package khepera.state;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;


public class Move extends State
{
	private int collisionTransition;
	private int movementDoneTransition;
	private int distanceToMove;
	private int startPosition;
	
	public Move(int distanceToMove, int collisionTransition, int movementDoneTransition) {
		this.distanceToMove = distanceToMove;
		this.collisionTransition = collisionTransition;
		this.movementDoneTransition = movementDoneTransition;
	}
	
	@Override
	public int shouldTransition()
	{
		//TODO: implement sensormanager fully.
		return 0;
	}

	@Override
	public void doWork()
	{
		//TODO: movement manager stuff needs static methods, or singleton access method.
	}

	@Override
	public void resetState()
	{
		// TODO Auto-generated method stub
		
	}

}
