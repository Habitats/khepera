package khepera.behaviour;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import khepera.state.PickUpBall;

public class CollectBehaviour extends Behaviour{

	public CollectBehaviour(int priority, SensorManager sensorManager,
			MovementManager movementManager) {
		super(priority, sensorManager, movementManager);
		addState(new PickUpBall(0, 0));
		setName("CollectBehaviour");
	}

	@Override
	public boolean shouldRun() {
		//Returns if something is already held in the gripper
		boolean holding = sensorManager.isObjectHeld();
		if (holding) return false;
		
		//Checks if there is something in front
		return sensorManager.isObjectInProximity() == 2 || sensorManager.isObjectInProximity() == 3;
	}

}
