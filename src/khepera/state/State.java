package khepera.state;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;

public abstract class State {
	
	protected MovementManager movementManager;
	protected SensorManager sensorManager;
	protected int nextTransition = 0;
	
	public void setManagers(MovementManager move, SensorManager sense) {
		this.movementManager = movementManager;
		this.sensorManager = sensorManager;
	}
	
	public int shouldTransition() {
		return nextTransition;
	}

  public abstract void doWork();

  public abstract void resetState();
}
