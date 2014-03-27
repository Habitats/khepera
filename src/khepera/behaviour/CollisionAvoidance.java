package khepera.behaviour;

import khepera.Logger;
import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import khepera.state.Move;
import khepera.state.Turn;

public class CollisionAvoidance extends Behaviour{
	
	private final int timeThreshold = 5000;
	private long lastSensedFree;
	boolean started, finished;
	
	public CollisionAvoidance(int priority, SensorManager sensorManager,
			MovementManager movementManager) {
		super(priority, sensorManager, movementManager);
		
		setName("CollisionAvoidance");
		
		lastSensedFree = System.currentTimeMillis();
		started = false;
		finished = false;
		
		addState(new Move(10, MovementManager.Direction.BACKWARD, 1, 0));
		addState(new Turn(MovementManager.Direction.RANDOM, 0));
	}
	
	@Override
	public boolean shouldRun() {
		if (started && !finished) return true;
		
		boolean crashing = false;
		for (int i = 0; i < 5; i++) {
			if (sensorManager.getDistanceSensorReading(i) > 1000) {
				System.out.println("CRASHING");
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
			finished = true;
			return false;
		}
		if (lastSensedFree - System.currentTimeMillis() > timeThreshold) {
			started = true;
		}
		return started;
	}

	@Override
	public void resetBehaviour() {
		currentState = 0;
		started = false;
		finished = false;
	}
}
