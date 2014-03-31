package khepera.state;

import khepera.Logger;

/**
 * This state will detect what sides are not blocked by walls or objects.
 * @author Olav
 *
 */
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
		if (sensorManager.isWallInFront() || sensorManager.isObjectInProximity() == 2 || sensorManager.isObjectInProximity() == 3) {
			flag += 1;
			Logger.getInstance().log("WALL IN FRONT");
		}
		if (sensorManager.isWallAtRight()) {
			flag += 2;
			Logger.getInstance().log("WALL TO THE RIGHT");
		}
		if (sensorManager.isWallAtLeft()) {
			flag += 4;
			Logger.getInstance().log("WALL TO THE LEFT");
		}
		setTransitionFlag(transitions[flag]);
//		System.out.println("FLAG: " + flag);
	}

	@Override
	protected void resetState() {
	}

}
