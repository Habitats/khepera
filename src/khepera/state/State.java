package khepera.state;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;

public abstract class State {
	
	protected MovementManager movementManager;
	protected SensorManager sensorManager;
	protected int nextTransition = 0;
	
	public void setManagers(MovementManager move, SensorManager sense) {
		System.out.println("Managers set");
		this.movementManager = move;
		this.sensorManager = sense;
	}
	
	public int shouldTransition() {
		return nextTransition;
	}

	protected MovementManager getMover() {
		return this.movementManager;
	}
  public abstract void doWork();

  public abstract void resetState();
}
