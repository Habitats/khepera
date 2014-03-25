package khepera.behaviour;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import khepera.state.PickUpBall;

public class CollectBehaviour extends Behaviour{

	public CollectBehaviour(int priority, SensorManager sensorManager,
			MovementManager movementManager) {
		super(priority, sensorManager, movementManager);
		addState(new PickUpBall(0, 0));
	}

	@Override
	public boolean shouldRun() {
		boolean l = sensorManager.isObjectInProximity() == 2 || sensorManager.isObjectInProximity() == 3;
		return (l); 
	}

}
