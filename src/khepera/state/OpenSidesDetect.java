package khepera.state;

public class OpenSidesDetect extends State{
	int[] transitions;
	
	public OpenSidesDetect(int openFrontTransition, int openRightTransition, int openLeftTransition, int openSidesTransition, int frontRightOpen, int frontLeftOpen, int allClosedTransition, int allOpenTransition) {
		transitions = new int[] {
				allOpenTransition, 
				openSidesTransition, 
				frontLeftOpen, 
				openLeftTransition, 
				frontRightOpen,
				openRightTransition,
				openFrontTransition,
				allClosedTransition
		};
	}
	
	@Override
	public void doWork() {
		int flag = 0;
		if (sensorManager.isWallInFront()) flag += 1;
		if (sensorManager.isWallAtRight()) flag += 2;
		if (sensorManager.isWallAtLeft()) flag += 4;
		setTransitionFlag(transitions[flag]);
	}

	@Override
	protected void resetState() {
	}

}
