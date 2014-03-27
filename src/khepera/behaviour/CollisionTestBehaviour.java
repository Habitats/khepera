package khepera.behaviour;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import khepera.state.Move;
import khepera.state.Turn;

public class CollisionTestBehaviour extends Behaviour{
	
	public CollisionTestBehaviour(int priority, SensorManager sensorManager, MovementManager movementManager) {
		super(priority, sensorManager, movementManager);
		setName("CollisionTest");
		addState(new Move(100, MovementManager.Direction.FORWARD, 0, 0));
	}

	@Override
	public boolean shouldRun() {
		return true;
	}
}
