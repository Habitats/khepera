package khepera.state;

import khepera.Logger;
import khepera.managers.MovementManager.Direction;
import khepera.managers.SensorManager;

/**
 * This state rotates the robot by 90 degrees in the direction fed into the constructor. 
 * It will detect that the robot is stuck if it gets stuck.
 * @author Olav
 *
 */
public class Turn extends State {

	int doneTransition;
	int turningBlocked;
	Direction direction;
	
	public Turn(Direction dir, int doneTransition, int turningBlockedTransition) {
		this.doneTransition = doneTransition;
		this.direction = dir;
		this.turningBlocked = turningBlockedTransition;
	}

  @Override
  public void doWork() {
	  
	  
	  //TODO: IMPLEMENT ROTATION BLOCK DETECTION!
	  
//	  if (sensorManager.getDistanceSensorReading(SensorManager.SENSOR_DIAGONAL_LEFT) > 1000
//			  || (sensorManager.getDistanceSensorReading(SensorManager.SENSOR_DIAGONAL_RIGHT) > 1000)) {
//		  Logger.getInstance().log("Turning was blocked");
//		  setTransitionFlag(turningBlocked);
//		  return;
//	  }
	  long startPos = sensorManager.getWheelPosition();
	  movementManager.rotate(90, direction);
	  if (startPos == sensorManager.getWheelPosition()) {
		  setTransitionFlag(turningBlocked);
		  return;
	  }
	  setTransitionFlag(doneTransition);
  }

  @Override
  public void resetState() {
  }
}
