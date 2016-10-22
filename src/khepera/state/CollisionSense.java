package khepera.state;


 
/**
 * * This state checks if there is an object currently close (>Threshold) on the front or sides of the robot.  
 * @author Olav
 *
 */
public class CollisionSense extends State{
	
	int noCollision;
	int collision;
	int threshold;
	
	public CollisionSense(int collisionThreshold, int noCollisionTransition, int collisionTransiton) {
		this.threshold = collisionThreshold; 
		this.noCollision = noCollisionTransition;
		this.collision = collisionTransiton;
	}
	
	@Override
	public void doWork() {
		boolean crashing = false;
		
		for (int i = 0; i < 5; i++) {
			if (sensorManager.getDistanceSensorReading(i) > threshold) {
				crashing = true;
				break;
			}
		}
		if (crashing) {
			setTransitionFlag(collision);
		} else {
			setTransitionFlag(noCollision);
		}
	}

	@Override
	protected void resetState() {
	}

}
