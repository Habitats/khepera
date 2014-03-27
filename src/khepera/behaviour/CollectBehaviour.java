package khepera.behaviour;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import khepera.state.PickUpObject;

public class CollectBehaviour extends Behaviour{

	boolean trying = false;
	
	public CollectBehaviour(int priority, SensorManager sensorManager,
			MovementManager movementManager) {
		super(priority, sensorManager, movementManager);
		
		addState(new PickUpObject(0, 0));
		setName("CollectBehaviour");
	}
	
	@Override
	public boolean shouldRun() {

		//Returns if something is already held in the gripper
		boolean holding = sensorManager.isObjectHeld();
		if (holding) return false;
		
		//Ignore objects if there is a light nearby
		if (sensorManager.isLightInProximity(100) > -1) return false;
		
		if(trying) {
			System.out.println("Failed to pick shit up");
			trying = false;
			return false;
		}
		//Checks if there is something in front
		trying = sensorManager.isObjectInProximity() == 2 || sensorManager.isObjectInProximity() == 3;
		
		return trying;
	}
}
