package khepera.state;

import khepera.managers.SensorManager;

/**
 * Finds any lights close to the robots front or sides, and has transitions to treat the acoordingly.
 * @author Olav
 *
 */
public class FindNearestLight extends State{
	
	int inFront,
	onLeft,
	onRight,
	diagonalLeft,
	diagonalRight,
	noLight;
	
	int minimumProximity;
	
	public FindNearestLight(int minimumProximity, int noLightTransition, int inFrontTransition, int onLeftTransition, int onRightTransition, int diagonalLeft, int diagonalRight) {
		this.inFront = inFrontTransition;
		this.onLeft = onLeftTransition;
		this.onRight = onRightTransition;
		this.noLight = noLightTransition;
		this.diagonalLeft = diagonalLeft;
		this.diagonalRight = diagonalRight;
		this.minimumProximity = minimumProximity;
	}
	
	@Override
	public void doWork() {
		
		int transition = -1;
		if (sensorManager.isLightInProximity(minimumProximity) < 0) setTransitionFlag(noLight);
		
		int nearestLight = sensorManager.getNearestLightSource();
		switch (nearestLight) {
			case SensorManager.SENSOR_FRONT_LEFT:
			case SensorManager.SENSOR_FRONT_RIGHT:
				transition = inFront;
				break;
			case SensorManager.SENSOR_LEFT:
				transition = onLeft;
				break;
			case SensorManager.SENSOR_RIGHT:
				transition = onRight;
				break;
			case SensorManager.SENSOR_DIAGONAL_LEFT:
				transition = diagonalLeft;
				break;
			case SensorManager.SENSOR_DIAGONAL_RIGHT:
				transition = diagonalRight;
				break;
			default:
				break;
		}
		System.out.println("NEAREST LIGHT FOUND AT: " + transition);
		setTransitionFlag(transition);
	}

	@Override
	protected void resetState() {
	}

}
