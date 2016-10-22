package khepera.state;

import khepera.Logger;
import khepera.managers.MovementManager;
import khepera.managers.SensorManager;

public abstract class State {
	
	protected MovementManager movementManager;
	protected SensorManager sensorManager;
	private int nextTransition = 0;
	private boolean shouldTransition = false;
	
	/**
	 * Method invoked automagically, it sets the movement and sensor managers for the state.
	 * @param move - MovementManager instance used by the controller.
	 * @param sense - SensorManager instance used by the controller.
	 */
	public void setManagers(MovementManager move, SensorManager sense) {
		Logger.getInstance().log("Managers set");
		this.movementManager = move;
		this.sensorManager = sense;
	}
	
	/**
	 * Returns true if the state is done and a transition value has been set.
	 * @return shouldTransition
	 */
	public boolean shouldTransition() {
		return shouldTransition;
	}
	
	/**
	 * Return nextTransition, this variable should be set by the user to return the index of the next state in the behavior module.
	 * @return nextTransition
	 */
	public int getTransition() {
		return nextTransition;
	}
	
	/**
	 * Call this method with the index of the state you want to run on the next doWork() iteration. If this method is not called, the state will not be changed, and will be run again.
	 * @param nextState
	 */
	protected void setTransitionFlag(int nextState) {
		this.nextTransition = nextState; 
		this.shouldTransition = true;
	}
	
	/**
	 * This method should invoke methods from the managers to make the robot do stuff, and it should also set transition flags (shouldTransition and nextTransition) when necessary.
	 */
  public abstract void doWork();
  
  /**
   * This function is called automatically on a transition away from this state. It also calls the resetState() function, which should reset all user created variables.
   * It also resets the shouldTransition variable to false.
   */
  public void initializeState() {
	  this.shouldTransition = false;
	  resetState();
  }
  
  /**
   * Can be used by the user to wipe any data in the state model that needs wiping. Else leave it empty.
   */
  protected abstract void resetState();
}
