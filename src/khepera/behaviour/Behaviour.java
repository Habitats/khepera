package khepera.behaviour;

import java.util.ArrayList;

import khepera.state.State;

public abstract class Behaviour implements Comparable<Behaviour>{
	
	public int priority;
	private int currentState = 0;
	private ArrayList<State> states;
	
	public Behaviour(int priority) {
		this.priority = priority;
		states = new ArrayList<State>();
	}
	
	public void addState(State state) {
		states.add(state);
	}	
	
	public void doWork() {
		int transition = states.get(currentState).shouldTransition(); 
		
		if (transition != 0) {
			states.get(currentState).resetState();
			currentState += transition;
			return;
		}
		states.get(currentState).doWork();
	}

	@Override
	public int compareTo(Behaviour o) {
		return o.priority - priority;	
	}
	
	public abstract boolean shouldRun();
}