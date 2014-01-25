import edu.wsu.KepheraSimulator.RobotController;

/**
 * Turns a number of degrees, both to the left and right.
 * 
 * Unlike the other controllers, this version makes sure it has turned
 * the exact number of degrees asked for.
 * 
 * This makes it a bit more complex.
 * 
 * The control structure with a state variable may not scale very well...
 */
public class TurnDegrees extends RobotController {

	public TurnDegrees() {
		super();
	}

	protected long theTurnDegree = -45;

	protected int state = 0;
	protected boolean slowSpeed = false; // check if we're compensating.
	protected long degreeTarget = 0;
	protected boolean firstRun = true;

	public void doWork() throws Exception {

		if(state == 0) {
		  degreeTarget = getLeftWheelPosition() + theTurnDegree*3;
		  state = 1;
		}

		if(state == 1) {
		
		// Sjekker om roboten har snudd langt nok.
			if(getLeftWheelPosition() == degreeTarget) {
				setMotorSpeeds(0,0);
				state = 2; // Good! Next step
			} else {
		
				// Checks if the robot have overturned and must adjust
				if (((getLeftWheelPosition() < degreeTarget) && (theTurnDegree < 0)) ||
						   (getLeftWheelPosition() > degreeTarget) && (theTurnDegree > 0)) {
						   	
					long offset = (getLeftWheelPosition() - degreeTarget);
				
					theTurnDegree = -offset*3;
					degreeTarget = getLeftWheelPosition() - offset;
					slowSpeed = true;
			
					// System.out.println("TurnDegrees: Wanted to turn to " + degreeTarget + " but turned to" + _ControllerRef.getLeftWheelPosition() + "("+offset+") COMPENSATING!");
			
				}
			
				int speed;
				if(slowSpeed)
					speed = 1;
				else
					speed = 5;
					
				// keep on turning
				if(theTurnDegree > 0) {
					setMotorSpeeds(speed, -speed); // Right turn
				} else {
					setMotorSpeeds(-speed, speed); // Left turn
				}
			  			  
			}
		}
	
		if(state == 2) {
		  // Nothing else to do.
		}				

	}

	/* (non-Javadoc)
	 * @see edu.wsu.KepheraSimulator.Controller#close()
	 */
	public void close() throws Exception {

	}

}
