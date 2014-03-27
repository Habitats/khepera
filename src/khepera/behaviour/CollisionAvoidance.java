package khepera.behaviour;

import khepera.Logger;
import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import khepera.state.Move;
import khepera.state.Turn;

public class CollisionAvoidance extends Behaviour{
	
	private final int timeThreshold = 2000;
	private long lastSensedFree;
	boolean started, finished;
	
	public CollisionAvoidance(int priority, SensorManager sensorManager,
			MovementManager movementManager) {
		super(priority, sensorManager, movementManager);
		
		setName("CollisionAvoidance");
		
		lastSensedFree = System.currentTimeMillis();
		started = false;
		finished = false;
		
		addState(new Turn(MovementManager.Direction.RANDOM, 1));
		addState(new Move(10, MovementManager.Direction.BACKWARD, 0, 0));
	}
	
	@Override
	public void doWork() {
		System.out.println("RUNNING AVOIDANCE");
		super.doWork();
	}
	
	@Override
	public boolean shouldRun() {
		if (started && !finished) return true;
		
		boolean crashing = false;
		for (int i = 0; i < 5; i++) {
			if (sensorManager.getDistanceSensorReading(i) > 1010) {
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
		if (System.currentTimeMillis() - lastSensedFree > timeThreshold) {
			started = true;
			System.out.println("STARTING AVOIDANCE");
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
