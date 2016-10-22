package khepera.behaviour;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import khepera.managers.MovementManager.Direction;
import khepera.state.FindNearestLight;
import khepera.state.Move;
import khepera.state.Turn;

/**
 * Behaivour that moves to any light it detects with its sensors. It will stop once it is standing by the light source and is facing it.
 * @author Olav
 *
 */
public class MoveToLightBehaviour extends Behaviour{
	
	public MoveToLightBehaviour(int priority, SensorManager sensorManager,
			MovementManager movementManager) {
		super(priority, sensorManager, movementManager);
		setName("MoveToLightBehavior");
		
		addState(new FindNearestLight(100, 3, 3, 1, 2, 3, 3));
		addState(new Turn(MovementManager.Direction.LEFT, 3, 3));
		addState(new Turn(MovementManager.Direction.RIGHT, 3, 3));
		addState(new Move(100, MovementManager.Direction.FORWARD, 4, 0));
		addState(new Turn(Direction.RANDOM, 0, 3));
	}
	
	int ticker = 0;
	
	@Override
	public boolean shouldRun() {
		int nearestLight = sensorManager.isLightInProximity(400);
		boolean holding = sensorManager.isObjectHeld();
//		if (ticker++ == 0) {
//			System.out.println("Nearest: " + nearestLight);
//			System.out.println("Crrent state: " + currentState);
//		}
		ticker %= 50;
//		System.out.println("Holding:" + holding);
		return (nearestLight > -1 && holding);
	}

}
