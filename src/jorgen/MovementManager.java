import java.util.Random;


import edu.wsu.KheperaSimulator.KSGripperStates;



public class MovementManager {

	private enum MovementState{
		TURN, STRAIGHT, SEEK_WALL, STALE, AT_WALL, LOST_WALL, TOO_CLOSE, BACKTRACK, SURVEY, LOWER_GRIP, RAISE_ARM, CLOSE_GRIP
	}

	private final static int SENSOR_LEFT = 0;			// do not touch
	private final static int SENSOR_RIGHT = 5;			//
	private final static int TURN_RIGHT = 90;			//
	private final static int TURN_LEFT = -90;			//
	private final static int TURN_AROUND = 180;			//
	
	public final static int TOO_CLOSE = 1000; 			// do not touch
	public final static int TOO_FAR = 600;				//
	public final static int PERFECT_DISTANCE = 800;	//
	
	private final static int TICKS = 3; // do not touch
	private final static int SPEED = 9; // max value: 9
	

	LogManager logManager;
	SensorManager sensorManager;
	Controller controller;

	private MovementState state = MovementState.STALE; // keeps track of the current state
	private long turnTarget = -1; // The aim for turning
	private int followSensor = -1; // which wall/sensor to follow

	private static MovementManager instance = null;
	
	protected MovementManager(Controller rc){	
		this.sensorManager = new SensorManager(rc);
		this.sensorManager.start(); // start thread
		this.logManager = new LogManager();
		this.controller = rc;
	}
	
	public static MovementManager getInstance( Controller rc ) {
	      if(instance == null) {
	         instance = new MovementManager(rc);
	      }
	      return instance;
	   }

	public void close() {
		this.sensorManager.signalStop();
	}
	
	public MovementState getState() {
		return this.state;
	}

	
	/*
	 *  Movement
	 */
	public void setMotors(int left, int right){
		this.controller.setMotorSpeeds(left, right);
	}
	
	public void setMotors(int left, int right, boolean doLogging){
		if( doLogging ){
			if( left==right){
				if( left!=0 )
					this.logManager.foreward();
				else if( left==1 )
					this.logManager.forewardslow();
				else if( left==-1)
					this.logManager.backwardslow();			
			}
		}
		
		this.setMotors(left, right);
	}
	
	public boolean turn(int degrees){
		if( this.state != MovementState.TURN ){
			this.setMotors(0, 0);
			this.state = MovementState.TURN;
			this.turnTarget = this.controller.getLeftWheelPosition() + degrees*MovementManager.TICKS;
		}
		else {
			int remaining_ticks = (int) (this.turnTarget - this.controller.getLeftWheelPosition());
			
			int speed = Math.min(MovementManager.SPEED, Math.abs(remaining_ticks));
			if( Math.abs(remaining_ticks) < 50 ){
				speed = 1;
			}
			
			if( remaining_ticks>0 ){
				this.setMotors(speed, -speed, true);
			}
			else if( remaining_ticks<0 ){
				this.setMotors(-speed, speed, true);
			}
			else if( remaining_ticks==0 ){
				// remaining_ticks==0
				this.setMotors(0,0);
				return true;
			}
		}
		return false;
	}

	public boolean turn(int degrees, boolean doLogging){
		if( doLogging && this.state != MovementState.TURN){
			switch( degrees ){
				case TURN_RIGHT:
					this.logManager.right();
					break;
				case TURN_LEFT:
					this.logManager.left();
					break;
				case TURN_AROUND:
					this.logManager.turnaround();
					break;
			}				
		}
		return this.turn(degrees);
	}
	
	private boolean moveToWall( boolean doLogging ) {
		switch( this.state ){
			case STALE:
				this.state = MovementState.STRAIGHT;
				break;
			case STRAIGHT:
				if( this.sensorManager.getProximityFront() <= MovementManager.PERFECT_DISTANCE ){
					if( this.sensorManager.getProximityFront() > MovementManager.PERFECT_DISTANCE-200){
						// slow down for accuracy
						this.setMotors( 1, 1, doLogging);
					}
					else
						this.setMotors( MovementManager.SPEED, MovementManager.SPEED, doLogging);
					
					if( doLogging ){
						// only occurs while already holding a ball
						
						int[] checkSensors = (this.followSensor==-1) ? new int[]{0,5} : new int[]{this.followSensor};
						for( int i : checkSensors){
							int sensor = this.sensorManager.checkBall( i );
							if( sensor!=-1 ){
								// the sensors reporting a nearby ball
								this.setMotors(0, 0);
								this.state = MovementState.STALE; // required for consistency
								this.controller.checkBall( sensor );
							}
						}
					}
				}
				else{
					this.state = MovementState.AT_WALL;
					this.setMotors(0, 0);					
				}
				break;
			case AT_WALL:
				return true;
		}
		return false;
	}
	
	
	
	private void checkWallDistance( int sensor ) {
		int[] distances = this.sensorManager.getDistanceSensorData();
		
		if(distances[ sensor ]<TOO_FAR){
			this.state = MovementState.LOST_WALL;
		}
		else if(distances[ sensor ]>TOO_CLOSE){
			this.state = MovementState.TOO_CLOSE;				
		}
	}
	
	
	/*
	 * Behavior
	 */
	public boolean gotoWall( boolean doLogging ) throws InterruptedException {
		if( this.state==MovementState.STALE ){
			int sensor = -1;
			if( this.followSensor==-1 )
				sensor = this.sensorManager.getClosestWall().x;
			else
				sensor = this.followSensor;
			
			switch( sensor ){
				case SENSOR_LEFT: 
					this.turn( TURN_LEFT, doLogging );
					break;
				case SENSOR_RIGHT:
					this.turn( TURN_RIGHT, doLogging );
					break;
				default:
					this.state = MovementState.STRAIGHT;
					break;
			}
			
			
		}
		else if( this.state==MovementState.TURN ){
			if( this.turn( -1 ) ){
				this.state = MovementState.STRAIGHT;
			}
		}
		else if( this.state==MovementState.STRAIGHT ){
			if( this.moveToWall( doLogging) ){
				this.state = MovementState.AT_WALL;
			}
		}
		else if( this.state==MovementState.AT_WALL ){
			int[] distances = this.sensorManager.getDistanceSensorData();
			int variation = (distances[2] - distances[3]);
			
			int degrees = variation/50;
			if( Math.abs(degrees)>=2 )
				this.turn( -1*degrees, false );
			else{
				this.state = MovementState.STALE;
				return true;
			}
		}
		
		return false;	
	}
	
	public boolean fromWall( boolean doLogging ) throws InterruptedException{
		if( this.state == MovementState.STALE ){
			if( this.followSensor==SENSOR_RIGHT )
				this.turn( TURN_RIGHT, false );
			else if( this.followSensor==SENSOR_LEFT )
				this.turn( TURN_LEFT, false );
		}
		else if( this.state == MovementState.AT_WALL ){
			if( this.gotoWall( false ) ){
				this.state = MovementState.STRAIGHT;//reverse
			}
		}
		else if( this.state==MovementState.TURN ){
			if( this.turn( -1 ) ){
				this.state = MovementState.AT_WALL;
			}
		}
		else if( this.state == MovementState.STRAIGHT ){
			int distance = this.sensorManager.getProximityFront();
			
			if( distance > MovementManager.PERFECT_DISTANCE )
				this.setMotors(-1, -1, false);
			else{
				this.setMotors(0, 0);
				
				this.state = MovementState.STALE;
				return true;
			}
		}
		
		
		return false;
	}
	
	public boolean followAlongWall() throws InterruptedException{
		if( this.state==MovementState.STALE ){
			if( this.followSensor==-1 ){
				int sensor = this.sensorManager.getClosestWall().x;

				switch( sensor ){
					case 2: // straight
					case 3: // straight
						this.followSensor = new Random().nextInt(2)*5; // choose left or right
						break;
					default: // sides
						this.followSensor = sensor;
						break;
				}
			}
			
			if( this.followSensor==SENSOR_RIGHT )
				this.turn( TURN_LEFT, true ); 
			else
				this.turn( TURN_RIGHT, true );
		}
		else if( this.state==MovementState.TURN ){
			if( this.turn( -1 ) ){
				this.state = MovementState.STRAIGHT;
			}
		}
		else if( this.state==MovementState.STRAIGHT ){
			if( this.moveToWall( true ) ){				
				this.state = MovementState.AT_WALL;
			}
			
			this.checkWallDistance( this.followSensor );
				
		}
		else if( this.state==MovementState.AT_WALL ){
			if( this.followSensor==SENSOR_RIGHT ){
				this.turn(TURN_LEFT, true);
			} 
			else {
				this.turn(TURN_RIGHT, true);
			}
		}
		else if( this.state==MovementState.LOST_WALL ){
			// check if we have entered a cross section 
			// or if we've moved out from the wall
			this.setMotors(0, 0);
			this.state = MovementState.STALE; // required for consistency
			
			this.controller.lostWall();
		}
		else if( this.state==MovementState.TOO_CLOSE ){
			this.setMotors(0, 0);
			this.state = MovementState.STALE; // required for consistency
			
			this.controller.tooClose();
		}
		
		
		return false;
	}
	
	
	public boolean goBack( boolean toHome ){
		if( this.state==MovementState.STALE ){
			this.state = MovementState.BACKTRACK;
		}
		else if( this.state==MovementState.TURN ){
			if( this.turn( -1 ) )
				this.state = MovementState.BACKTRACK;
		}
		else if( this.state==MovementState.BACKTRACK ){
			Moves move = this.logManager.stepBack( toHome );
			
			switch( move ){
				case LEFT:
					this.turn( TURN_LEFT, false );
					break;
				case RIGHT:
					this.turn( TURN_RIGHT, false );
					break;
				case FORWARD:
					this.setMotors( MovementManager.SPEED, MovementManager.SPEED, false );
					if( this.followSensor != -1)
						this.checkWallDistance( this.followSensor );
					break;
				case FORWARDSLOW:
					this.setMotors( 1,1, false );
					break;
				case BACKWARDSLOW:
					this.setMotors(-1, -1, false);
					break;
				case TURN_AROUND:
					this.turn( MovementManager.TURN_AROUND, false );
					break;
				case END:
					this.state = MovementState.STALE;
					this.setMotors( 0, 0);
					return true;
			}
		}
		else if( this.state==MovementState.LOST_WALL ){
			// check if we have entered a cross section 
			// or if we've moved out from the wall
			this.setMotors(0, 0);
			this.state = MovementState.STALE; // required for consistency
			
			this.controller.lostWall();
		}
		else if( this.state==MovementState.TOO_CLOSE ){
			this.setMotors(0, 0);
			this.state = MovementState.STALE; // required for consistency
			
			this.controller.tooClose();
		}
		
		return false;
	}
	
	public boolean surveyBall(int sensor) {
		// possible ball detected by [sensor]
		if( this.state == MovementState.STALE){
			this.logManager.setRevertPoint();
			if( sensor==SENSOR_RIGHT )
				this.turn( TURN_RIGHT, true );
			else if( sensor==SENSOR_LEFT )
				this.turn( TURN_LEFT, true );
			else
				this.state = MovementState.SURVEY;
			
		}
		else if( this.state==MovementState.TURN ){
			if( this.turn( -1 ) ){
				this.state = MovementState.SURVEY;
			}
		}
		else if( this.state==MovementState.SURVEY ){
			int[] distances = this.sensorManager.getDistanceSensorData();
			
			boolean possible = true;
			for( int i : new int[]{1,4} )
				if( distances[i]>40 ){
					possible = false;
				}
			
			if( possible )
				this.state = MovementState.LOWER_GRIP;
			else{
				if( sensor==0 || sensor==5)
					this.controller.backtrack();
				else
					this.logManager.clearToPoint();
				this.setMotors(0, 0);
				this.state = MovementState.STALE;
				this.controller.followWall();
			}
				
		}
		else if( this.state==MovementState.LOWER_GRIP ){
			if( this.controller.getArmState()!=KSGripperStates.ARM_DOWN )
				this.controller.setArmState( KSGripperStates.ARM_DOWN );
			else
				this.state = MovementState.STRAIGHT;
		}
		else if( this.state==MovementState.STRAIGHT ){
			if( !this.controller.isObjectPresent() )
				this.setMotors(MovementManager.SPEED, MovementManager.SPEED, true);
			else{
				this.setMotors(MovementManager.SPEED, MovementManager.SPEED, true);
				this.state = MovementState.CLOSE_GRIP;
			}
		}
		else if( this.state==MovementState.CLOSE_GRIP ){
			if( this.controller.getGripperState()!=KSGripperStates.GRIP_CLOSED )
				this.controller.setGripperState( KSGripperStates.GRIP_CLOSED );
			else
				this.state = MovementState.RAISE_ARM;
			
		}
		else if( this.state==MovementState.RAISE_ARM ){
			if( this.controller.getArmState()!=KSGripperStates.ARM_UP )
				this.controller.setArmState( KSGripperStates.ARM_UP );
			else{
				this.setMotors(0, 0);
				this.state = MovementState.STALE;
				return true;
			}
		}
		
		return false;
	}

	
}