package khepera.behaviour;

import khepera.managers.MovementManager;
import khepera.managers.MovementManager.Direction;
import khepera.managers.SensorManager;
import khepera.state.Move;
import khepera.state.PickUpObject;
import khepera.state.Turn;

/**
 * This behaviour will just pick up an object if it detects that one is straight in front of it.
 * @author Olav
 *
 */
public class CollectBehaviour extends Behaviour{

	boolean trying = false;

	public CollectBehaviour(int priority, SensorManager sensorManager,
			MovementManager movementManager) {
		super(priority, sensorManager, movementManager);
		setName("CollectBehaviour");

		addState(new PickUpObject(1, 1));
		addState(new Turn(Direction.RANDOM, 2, 2));
		addState(new Move(30, Direction.BACKWARD, 1, 1));
	}

	@Override
	public boolean shouldRun() {

	//Returns if something is already held in the gripper
	boolean holding = sensorManager.isObjectHeld();
	if (holding) return false;

	//Ignore objects if there is a light nearby
	if (sensorManager.isLightInProximity(150) > -1) return false;
	
	if(trying) {
//		if (currentState == 2) {
//			trying = false;
//		}
		trying = false;
//		return true;
		return true;
	}
	//Checks if there is something in front
	trying = sensorManager.isObjectInProximity() == 2 || sensorManager.isObjectInProximity() == 3;

	boolean isWall = sensorManager.isWallInFront();

	return trying && !isWall;
}
}
