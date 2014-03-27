package khepera.behaviour;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import khepera.state.CollisionSense;
import khepera.state.FindNearestLight;
import khepera.state.Move;
import khepera.state.Turn;

/**
 * Behaivour that moves to any light it detects with its sensors.
 * @author Olav
 *
 */
public class MoveToLightBehaviour extends Behaviour{
	
	public MoveToLightBehaviour(int priority, SensorManager sensorManager,
			MovementManager movementManager) {
		super(priority, sensorManager, movementManager);
		setName("MoveToLightBehavior");
		
		addState(new FindNearestLight(400, 3, 3, 1, 2, 3, 3));
		addState(new Turn(MovementManager.Direction.LEFT, 3));
		addState(new Turn(MovementManager.Direction.RIGHT, 3));
		addState(new Move(100, MovementManager.Direction.FORWARD, 1, 0));
	}
	
	int ticker = 0;
	
	@Override
	public boolean shouldRun() {
		int nearestLight = sensorManager.isLightInProximity(400);
		boolean holding = sensorManager.isObjectHeld();
		if (ticker++ == 0) {
			System.out.println("Nearest: " + nearestLight);
			System.out.println("Crrent state: " + currentState);
		}
		ticker %= 50;
//		System.out.println("Holding:" + holding);
		return (nearestLight > -1 && holding);
	}

}
