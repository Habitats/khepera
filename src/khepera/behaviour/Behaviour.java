package khepera.behaviour;

import java.util.ArrayList;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import khepera.state.State;

public abstract class Behaviour implements Comparable<Behaviour>{
	
	public int priority;
	private int currentState = 0;
	private ArrayList<State> states;
	private MovementManager movementManager;
	protected SensorManager sensorManager;
	
	public Behaviour(int priority, SensorManager sensorManager, MovementManager movementManager) {
		this.movementManager = movementManager;
		this.sensorManager = sensorManager;
		this.priority = priority;
		states = new ArrayList<State>();
	}
	
	public void addState(State state) {
		System.out.println("Setting managers...");
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
	public void resetBehavior() {
		currentState = 0;
	}
	
	public abstract boolean shouldRun();
}