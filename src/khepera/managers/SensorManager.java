package khepera.managers;

import java.awt.Point;

import edu.wsu.KheperaSimulator.RobotController;

public class SensorManager{
  public static final int SENSOR_LEFT = 0;
  public static final int SENSOR_ANGLEL = 1;
  public static final int SENSOR_FRONTL = 2;
  public static final int SENSOR_FRONTR = 3;
  public static final int SENSOR_ANGLER = 4;
  public static final int SENSOR_RIGHT = 5;
  public static final int SENSOR_BACKR = 6;
  public static final int SENSOR_BACKL = 7;
	
	// User configurated parameters
	int definedNearWall = 6; // a index of the discreteSensorIntervals[].
	// end
	
	
	private RobotController controller; 
	private SensorInterval[] discreteSensorIntervals = new SensorInterval[]{ 
			new SensorInterval(10,17.5,345,303), 
			new SensorInterval(17.5, 40, 302, 274), 
			new SensorInterval(40, 84, 273, 260),
			new SensorInterval(84, 150, 259, 217),
			new SensorInterval(150, 300, 216, 174),
			new SensorInterval(300, 540, 173, 130),
			new SensorInterval(540, 800, 129, 102),
			new SensorInterval(800, 2000, 101, 0),
		};
	
	
	public SensorManager( RobotController controller ){
		this.controller = controller;
		
	}
	
	private class SensorInterval {
		private int xFar, xClose;
		private double sensorFar, sensorNear;
		
		public SensorInterval(double sensorFar, double sensorNear, int xFar, int xClose){
			this.sensorFar = sensorFar;
			this.sensorNear = sensorNear;
			this.xFar = xFar;
			this.xClose = xClose;
		}
		public boolean inSensorInverval( int sensorValue ){ return (this.sensorNear>sensorValue && sensorValue>=this.sensorFar); }
		public int[] getDistanceInterval(){ return new int[]{this.xFar, this.xClose}; }
		public int getLowerBound(){ return (int) this.sensorFar; }
		public int getUpperBound(){ return (int) this.sensorNear; }
	}
	

	/**
	 * @return True if there is a wall to the left of the robot
	 */
	public boolean isWallAtLeft(  ){ 
		int 	sensorLeft = this.getDistanceRange(0)[0],
				sensorDiagonalLeft = this.getDistanceRange(1)[0];
		return ( this.discreteSensorIntervals[ this.definedNearWall ].getLowerBound() <= sensorLeft &&
				 this.discreteSensorIntervals[ this.definedNearWall-1 ].getLowerBound() <= sensorDiagonalLeft ); 
	}
	
	/**
	 * @return True if there is a wall to the right of the robot.
	 */
	public boolean isWallAtRight(  ){
		int 	sensorRight = this.getDistanceRange(5)[0],
				sensorDiagonalRight = this.getDistanceRange(4)[0];
		return ( this.discreteSensorIntervals[ this.definedNearWall ].getLowerBound() <= sensorRight &&
				 this.discreteSensorIntervals[ this.definedNearWall-1 ].getLowerBound() <= sensorDiagonalRight );
	}
	
	/**
	 * @return True if there is a wall in front of the robot
	 */
	public boolean isWallInFront( ){
		int 	diagonalSensorLeft = this.getDistanceRange(1)[0],
				frontSensorLeft = this.getDistanceRange(2)[0],
				frontSensorRight = this.getDistanceRange(3)[0],
				diagonalSensorRight = this.getDistanceRange(4)[0];
		
		boolean objectInFront = (this.discreteSensorIntervals[ this.definedNearWall ].getLowerBound() <= frontSensorLeft && 
				this.discreteSensorIntervals[ this.definedNearWall ].getLowerBound() <= frontSensorRight);
		boolean extendedFront = (this.discreteSensorIntervals[ this.definedNearWall-1 ].getLowerBound() <= diagonalSensorLeft  && 
				this.discreteSensorIntervals[ this.definedNearWall-1 ].getLowerBound() <= diagonalSensorRight );
		
		return (objectInFront && extendedFront);
	}
	
	/**
	 * 
	 * @return An integer describing the sensor index related to a nearby object. If none, it returns -1.
	 */
	public int isObjectInProximity( ){
		boolean wallInFront = this.isWallInFront(),
				wallOnLeft = this.isWallAtLeft(),
				wallOnRight = this.isWallAtRight();
		
		if( !wallInFront ){
			// If there is something in front of the robot, it isn't a wall.
			
			int 	sensorLeft = this.getDistanceRange(0)[0],
					frontSensorLeft = this.getDistanceRange(2)[0],
					frontSensorRight = this.getDistanceRange(3)[0],
					sensorRight = this.getDistanceRange(5)[0];
			
			boolean objectInFrontLeft = (this.discreteSensorIntervals[ this.definedNearWall ].getLowerBound() <= frontSensorLeft),
					objectInFrontRight = (this.discreteSensorIntervals[ this.definedNearWall ].getLowerBound() <= frontSensorRight),
					objectAtLeft = false,
					objectAtRight = false;
			
			if( !wallOnLeft ){
				// If there is something to the left of the ball, is won't be a wall.
				objectAtLeft = (this.discreteSensorIntervals[ this.definedNearWall ].getLowerBound() <= sensorLeft);
			}
			if( !wallOnRight ){
				// If there is something to the right of the ball, is won't be a wall.
				objectAtRight = (this.discreteSensorIntervals[ this.definedNearWall ].getLowerBound() <= sensorRight);
			}
			
			if( objectAtLeft ) return 0;
			if( objectInFrontLeft ) return 2;
			if( objectInFrontRight ) return 3;
			if( objectAtRight ) return 5;
		}
		
		return -1;
	}
	
	
	public int getNearestObjectOrWall( ){
		// TODO
	  return (Integer) null;
	}
	
	
	/**
	 * 
	 * @param sensorIndex The sensor to read distance measurements. 
	 * @return Integer array with [max, min] distance from the given sensor to an object.
	 */
 	public int[] getDistanceRange( int sensorIndex ){
		// Observation:
		// Due to the noise implementation, we should only perform a single sensor measure
		// and categorize the distance interval. (According to statistical analysis)
		int distanceValue = this.controller.getDistanceValue( sensorIndex );
		
		for( int i=0; i<this.discreteSensorIntervals.length; i++ ){
			// Loop over the discrete distance sensor intervals
			if( this.discreteSensorIntervals[i].inSensorInverval( distanceValue ) ){
				// If the measured distance may appear in this range interval relative to the wall
				
				// returns [max distance, minimum distance] to an object
				return this.discreteSensorIntervals[i].getDistanceInterval();
			}
		}
    return null;
	}
	
}
