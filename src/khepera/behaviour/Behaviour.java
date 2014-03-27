package khepera.behaviour;

import java.util.ArrayList;

import khepera.Logger;
import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import khepera.state.State;

public abstract class Behaviour implements Comparable<Behaviour>{
	
	private String name = "No Name";
	public int priority;
	protected int currentState = 0;
	private ArrayList<State> states;
	protected MovementManager movementManager;
	protected SensorManager sensorManager;
	
	public Behaviour(int priority, SensorManager sensorManager, MovementManager movementManager) {
		this.movementManager = movementManager;
		this.sensorManager = sensorManager;
		this.priority = priority;
		states = new ArrayList<State>();
	}
	
	public void addState(State state) {
		Logger.getInstance().log("Setting managers...");
		state.setManagers(movementManager, sensorManager);
		states.add(state);
	}	
	
	public void doWork() {
		if (states.get(currentState).shouldTransition()) {
			
			int transition = states.get(currentState).getTransition(); 
			states.get(currentState).initializeState();
			currentState = transition;
			return;
		}
		states.get(currentState).doWork();
	}

	@Override
	public int compareTo(Behaviour o) {
		return o.priority - priority;	
	}
	
	/**
	 * Resets the behaviour state machine.
	 */
	public void resetBehaviour() {
		currentState = 0;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public abstract boolean shouldRun();
}