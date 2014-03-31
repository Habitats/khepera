package khepera.state;

public class ObjectDetect extends State{
	
	
	int none;
	int front;
	int left;
	int right;
	
	public ObjectDetect(int noneTransition, int leftTransition, int rightTransition, int frontTransition) {
		this.none = noneTransition;
		this.front = frontTransition;
		this.left = leftTransition;
		this.right = rightTransition;
	}

	@Override
	public void doWork() {
		int transition = none;
		int detected = sensorManager.isObjectInProximity();
		if (detected == 2 || detected == 3) {
			transition = front;
		}
		else if (detected == 5) {
			transition = right;
		}
		else if (detected == 0) {
			transition = left;
		}
		setTransitionFlag(transition);
	}

	@Override
	protected void resetState() {
		// TODO Auto-generated method stub
		
	}

}
