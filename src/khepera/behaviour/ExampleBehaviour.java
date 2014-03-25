package khepera.behaviour;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import khepera.state.Move;
import khepera.state.PickUpBall;
import khepera.state.Turn;

public class ExampleBehaviour extends Behaviour {
	
	
	/**
	 * Basic behavior that just moves until it has to turn. Not implemented correctly yet.
	 * @param priority
	 */
	public ExampleBehaviour(int priority, SensorManager sensorManager, MovementManager movementManager) {
		super(priority, sensorManager, movementManager);
		addState(new Move(50, 1, 0));
		addState(new PickUpBall(2, 2));
		addState(new Turn(MovementManager.Direction.LEFT, 0));
	}

	@Override
	public boolean shouldRun() {
		return true;
	}

}
