package khepera.behaviour;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import khepera.state.PutDownObject;

/**
 * This behaviour will put a held object down if it is close to a light and facing an object (Hopefully a light)
 * @author Olav
 *
 */
public class PlaceOnLightBehaviour extends Behaviour{

	public PlaceOnLightBehaviour(int priority, SensorManager sensorManager,
			MovementManager movementManager) {
		super(priority, sensorManager, movementManager);
		
		setName("PlaceOnLight");
		addState(new PutDownObject(0));
	}

	@Override
	public boolean shouldRun() {
		if (!sensorManager.isObjectHeld()) {
			return false;
		}
		
		if (sensorManager.isLightInProximity(150) == -1) {
			return false;
		}
		
		boolean objectInFront = sensorManager.isObjectInProximity() == 2 || sensorManager.isObjectInProximity() == 3;
		if(!objectInFront) {
			return false;
		}
		
		return true;
	}
}
