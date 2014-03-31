package khepera.behaviour;

import khepera.managers.MovementManager;
import khepera.managers.MovementManager.Direction;
import khepera.managers.SensorManager;
import khepera.state.Move;
import khepera.state.OpenSidesDetect;
import khepera.state.RandomState;
import khepera.state.Turn;

public class ExploreBehaviour extends Behaviour{

	public ExploreBehaviour(int priority, SensorManager sensorManager,
			MovementManager movementManager) {
		super(priority, sensorManager, movementManager);
		setName("ExploreBehaviour");
		
		addState(new OpenSidesDetect(4, 2, 1, 7, 2, 1, 7, 5));
		addState(new Turn(Direction.LEFT, 4, 4));
		addState(new Turn(Direction.RIGHT, 4, 4));
		addState(new Turn(Direction.RANDOM, 4, 4));
		addState(new Move(50, Direction.FORWARD, 7, 6));
		addState(new Move(100, Direction.FORWARD, 7, 6));
		addState(new RandomState(new int[]{1, 2, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5 ,5, 5, 5, 5, 5, 5, 5, 5, 5, 5}));
		addState(new Move(30, Direction.BACKWARD, 0, 3));
	}

	@Override
	public boolean shouldRun() {
		return true;
	}
	
}
