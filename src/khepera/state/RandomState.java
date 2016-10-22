package khepera.state;

public class RandomState extends State{
	
	int[] trans;
	/**
	 * This state will call for a transition with a random integer from the possibleStateTransitions list. 
	 * @param possibleStateTransitions
	 */
	public RandomState(int[] possibleStateTransitions) {
		trans = possibleStateTransitions;
	}  
	
	@Override
	public void doWork() {
		int index = (int) (Math.random() * trans.length);
		setTransitionFlag(trans[index]);
	}

	@Override
	protected void resetState() {
	}
}
