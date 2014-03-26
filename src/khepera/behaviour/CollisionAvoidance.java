package khepera.behaviour;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import khepera.state.CollisionSense;
import khepera.state.Move;
import khepera.state.Turn;

public class CollisionAvoidance extends Behaviour{
	
	private final int threshold = 5000;
	private long lastSensedFree;
	boolean started, finished;
	
	public CollisionAvoidance(int priority, SensorManager sensorManager,
			MovementManager movementManager) {
		super(priority, sensorManager, movementManager);
		
		setName("CollisionAvoidance");
		
		lastSensedFree = System.currentTimeMillis();
		started = false;
		finished = false;
		
		addState(new Move(10, 1, 0));
		addState(new Turn(MovementManager.Direction.RANDOM, 0));
	}
	
	@Override
	public boolean shouldRun() {
		if (started && !finished) return true;
		
		boolean crashing = false;
		for (int i = 0; i < 5; i++) {
			if (sensorManager.getDistanceSensorReading(i) > 1000) {
				System.out.println("Crashing");
				crashing = true;
				break;
			}
		}
		if (!crashing) {
			System.out.println("NOT CRASHING");
			lastSensedFree = System.currentTimeMillis();
		}
		if (started && !crashing) {
			finished = true;
			return false;
		}
		if (lastSensedFree - System.currentTimeMillis() > threshold) {
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
