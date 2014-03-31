package khepera.behaviour;

import khepera.Logger;
import khepera.managers.MovementManager;
import khepera.managers.MovementManager.Direction;
import khepera.managers.SensorManager;
import khepera.state.Move;
import khepera.state.OpenSidesDetect;
import khepera.state.Turn;

public class CollisionRecovery extends Behaviour{
	
	private final int timeThreshold = 1000;
	private long lastSensedFree;
	boolean started;
	
	public CollisionRecovery(int priority, SensorManager sensorManager,
			MovementManager movementManager) {
		super(priority, sensorManager, movementManager);
		
		setName("CollisionRecovery");
		this.priority = 2000000000;
		
		lastSensedFree = System.currentTimeMillis();
		started = false;
		
		addState(new Move(40, MovementManager.Direction.BACKWARD, 1, 1));
		addState(new OpenSidesDetect(5, 3, 2, 0, 3, 5, 0, 4));
		addState(new Turn(Direction.LEFT, 1, 0));
		addState(new Turn(Direction.RIGHT, 1, 0));
		addState(new Turn(MovementManager.Direction.RANDOM, 1, 0));
		addState(new Move(30, MovementManager.Direction.FORWARD, 1, 1));
	}
	
	@Override
	public boolean shouldRun() {
		
		boolean crashing = false;
		for (int i = 0; i <= 5; i++) {
			if (sensorManager.getDistanceSensorReading(i) > 1020) {
				Logger.getInstance().setStatus("CRASHING",11);
				crashing = true;
				break;
			}
		}
		
		if (!crashing) {
			Logger.getInstance().setStatus("NOT CRASHING",11);
			lastSensedFree = System.currentTimeMillis();
		}
		
		if (started && !crashing) {
//			if (currentState == 0 || currentState == 1) {
//				currentState = 1;
//				return true;
//			}
			
			currentState = 0;
			started = false;
			return false;
		}
		
		
		if (started) return true;
		
		if (System.currentTimeMillis() - lastSensedFree > timeThreshold) {
			started = true;
			System.out.println("STARTING AVOIDANCE");
		}
		return started;
	}
}
