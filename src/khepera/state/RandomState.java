package khepera.state;

public class RandomState extends State{
	
	int[] trans;
	
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
