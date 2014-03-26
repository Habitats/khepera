package khepera.managers;



import java.util.ArrayList;
import java.util.Random;

import khepera.AbstractController;
import khepera.Logger;


public class SensorManager implements Runnable{
	
	// User configured parameters
	private final int definedNearWall = 6; // an index from the discreteSensorIntervals array.
	// end
		
	public static final int SENSOR_LEFT = 0;
	public static final int SENSOR_DIAGONAL_LEFT = 1;
	public static final int SENSOR_FRONT_LEFT = 2;
	public static final int SENSOR_FRONT_RIGHT = 3;
	public static final int SENSOR_DIAGONAL_RIGHT = 4;
	public static final int SENSOR_RIGHT = 5;
	public static final int SENSOR_BACK_RIGHT = 6;
	public static final int SENSOR_BACK_LEFT = 7;
	
	private final AbstractController controller; 
	private volatile boolean running = true;
	
	
	private final SensorInterval[] discreteDistanceSensorIntervals;
	private int[] distanceValues  = new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}; // 1..8 = measurement values, 9-10 = distance guess, 
	private long[] wheelPositions = new long[]{-1,-1};

	public SensorManager( AbstractController controller ){
		this.controller = controller;
		
		this.discreteDistanceSensorIntervals = new SensorInterval[]{ 
				new SensorInterval(10,17.5,345,303), 
				new SensorInterval(17.5, 40, 302, 274), 
				new SensorInterval(40, 84, 273, 260),
				new SensorInterval(84, 150, 259, 217),
				new SensorInterval(150, 300, 216, 174),
				new SensorInterval(300, 540, 173, 130),
				new SensorInterval(540, 800, 129, 102),
				new SensorInterval(800, 2000, 101, 0),
			};
	}
	
	/**
	 * Stop the observer thread.
	 */
	public void close(){
		this.running = false;
	}

	/**
	 * @return True if there is a wall to the left of the robot
	 */
	public boolean isWallAtLeft(  ){ 
		// Measure the distance to any object according to the sensors.
		int 	distanceLeft = this.getDistanceSensorReading( SENSOR_LEFT ),
				distanceDiagonalLeft = this.getDistanceSensorReading( SENSOR_DIAGONAL_LEFT );
		
		// Check if there was a large object which obscured both the sideways and the diagonal sensor.
		return ( this.discreteDistanceSensorIntervals[ this.definedNearWall ].getLowestPossibleSensorReading() <= distanceLeft &&
				 this.discreteDistanceSensorIntervals[ this.definedNearWall-1 ].getLowestPossibleSensorReading() <= distanceDiagonalLeft ); 
	}
	
	/**
	 * @return True if there is a wall to the right of the robot.
	 */
	public boolean isWallAtRight(  ){
		// Measure the distance to any object according to the sensors.
		int 	distanceRight = this.getDistanceSensorReading( SENSOR_RIGHT ),
				distanceDiagonalRight = this.getDistanceSensorReading( SENSOR_DIAGONAL_RIGHT );
		
		// Check if there was a large object which obscured both the sideways and the diagonal sensor.
		return ( this.discreteDistanceSensorIntervals[ this.definedNearWall ].getLowestPossibleSensorReading() <= distanceRight &&
				 this.discreteDistanceSensorIntervals[ this.definedNearWall-1 ].getLowestPossibleSensorReading() <= distanceDiagonalRight );
	}
	
	/**
	 * @return True if there is a wall in front of the robot
	 */
	public boolean isWallInFront( ){
		// Measure the distance to any object according to the sensors.
		int 	distanceDiagonalLeft = this.getDistanceSensorReading( SENSOR_DIAGONAL_LEFT ),
				distanceFrontLeft = this.getDistanceSensorReading(	SENSOR_FRONT_LEFT ),
				distanceFrontRight = this.getDistanceSensorReading(	SENSOR_FRONT_RIGHT ),
				distanceDiagonalRight = this.getDistanceSensorReading(SENSOR_DIAGONAL_RIGHT );
		
		// Check if there was a large object which obscured both of the frontal sensor.
		boolean objectInFront = (this.discreteDistanceSensorIntervals[ this.definedNearWall ].getLowestPossibleSensorReading() <= distanceFrontLeft && 
				this.discreteDistanceSensorIntervals[ this.definedNearWall ].getLowestPossibleSensorReading() <= distanceFrontRight);

		// Check if there was a large object which obscured both of the diagonal sensor.
		boolean extendedFront = (this.discreteDistanceSensorIntervals[ this.definedNearWall-1 ].getLowestPossibleSensorReading() <= distanceDiagonalLeft  && 
				this.discreteDistanceSensorIntervals[ this.definedNearWall-1 ].getLowestPossibleSensorReading() <= distanceDiagonalRight );
		
//		System.err.println("Front: "+objectInFront+" # Extended: "+extendedFront);
		
		// A wall should occlude both of these parameters.
		return (objectInFront && extendedFront);
	}
	
	
	
	
	/**
	 * 
	 * @return An integer describing the sensor index related to a nearby object. If none, it returns -1.
	 */
	public int isObjectInProximity( ){
		// Check if there are walls surrounding us.
		boolean wallInFront = this.isWallInFront(),
				wallOnLeft = this.isWallAtLeft(),
				wallOnRight = this.isWallAtRight();
		
		// Measure the distance to any object for each sensor.
		int 	distanceLeft = this.getDistanceRange( SENSOR_LEFT )[1],
				distanceFrontLeft = this.getDistanceRange( SENSOR_FRONT_LEFT )[1],
				distanceFrontRight = this.getDistanceRange(SENSOR_FRONT_RIGHT )[1],
				distanceRight = this.getDistanceRange( SENSOR_RIGHT )[1];
		
		// Check if there was something there at all.
        boolean objectInFrontLeft = (distanceFrontLeft == 0),
                objectInFrontRight = (distanceFrontRight == 0),
                objectAtLeft = (distanceLeft == 0),
                objectAtRight = (distanceRight == 0);
        
		if( !wallInFront ){
			// If there is something in front of the robot, it isn't a wall.
			if( objectInFrontLeft ) return 2;
			if( objectInFrontRight ) return 3;
		}
		else if( !wallOnRight ){
			// If there is something to the right of the robot, it shouldn't be a wall.
		    if( objectAtRight ) return 5;
		}
		else if( !wallOnLeft ){
			// If there is something to the left of the robot, it shouldn't be a wall.
			if( objectAtLeft ) return 0;
		}
		
		return -1;
	}
	
	/**
	 * @return An integer describing the sensor index in the direction of the nearest object.
	 */
	public int getNearestObjectOrWall( ){
		/*
		 * Loop over the distance sensors and find the sensor which
		 * yields the minimum distance to an object. 
		 * 
		 * Solves equal distance occurrences by randomly deciding a direction.
		 */
		int shortestDistance = -1;
		ArrayList<Integer> sensorDirections = new ArrayList<Integer>(); // used to randomly solve situations with equal distance.
		
		for( int sensorIndex=0;sensorIndex<=7;sensorIndex++){
			int currentDistance = this.getDistanceRange(sensorIndex)[0];
			if( currentDistance < shortestDistance || shortestDistance==-1 ){
				shortestDistance = currentDistance; 
				sensorDirections.clear();
				sensorDirections.add( sensorIndex );
			}
			else if( currentDistance==shortestDistance ){
				sensorDirections.add( sensorIndex );
			}
		}
		
		
		if( sensorDirections.size()>1 )
			return sensorDirections.get( new Random().nextInt(sensorDirections.size()) ); // Resolve randomly.
		//else
		return sensorDirections.get(0);
		
	}
	
	
	/**
	 * 
	 * @return  An integer describing the sensor index related to a nearby light source. If none, it returns -1.
	 * @param - int minimumProximity, will ignore all light values lower than this.
	 */
	public int isLightInProximity(int minimumProximity){
	    boolean lightInFrontLeft = (this.getLightSensorReading( SENSOR_FRONT_LEFT ) <= minimumProximity),
	            lightInFrontRight = (this.getLightSensorReading( SENSOR_FRONT_RIGHT ) <= minimumProximity),
	            lightOnLeft = (this.getLightSensorReading( SENSOR_LEFT ) <= minimumProximity),
	            lightOnRight = (this.getLightSensorReading( SENSOR_RIGHT ) <= minimumProximity);
	    
	    if( lightInFrontLeft ) return 2;
	    if( lightInFrontRight) return 3;
	    if( lightOnLeft ) return 0;
	    if( lightOnRight ) return 5;
	    
	    return -1;
	}
	
	/**
	 * 
	 * @return An integer describing the sensor index in the direction of the nearest light source. If none, it returns -1.
	 */
	public int getNearestLightSource(){
	    
	    // Read the light sensors
	    int     lightSensorFrontLeft = this.getLightSensorReading( SENSOR_FRONT_LEFT ) - 50, // Minus 50 because of their noise model
                lightSensorFrontRight = this.getLightSensorReading( SENSOR_FRONT_RIGHT ) - 50,
                lightSensorDiagLeft = this.getLightSensorReading( SENSOR_DIAGONAL_LEFT ) - 50,
                lightSensoDiagRight = this.getLightSensorReading( SENSOR_DIAGONAL_RIGHT ) - 50,
                lightSensorLeft = this.getLightSensorReading( SENSOR_LEFT ) - 50,
                lightSensorRight = this.getLightSensorReading( SENSOR_RIGHT ) - 50;
	    
	    int[]  lightMeasurements = new int[]{ lightSensorLeft, lightSensorDiagLeft, lightSensorFrontLeft, lightSensorFrontRight, lightSensoDiagRight, lightSensorRight };
	    int    minValue = Integer.MAX_VALUE,
	           idSensor = -1;
	    
	    for( int i=0;i< lightMeasurements.length;i++ ){
	        // Loop through the light values and find the nearest
	        if( lightMeasurements[i]!=-1 && lightMeasurements[i]<minValue ){
	            minValue = lightMeasurements[i];
	            idSensor = i;
	        }
	    }
	    
	    // Return -1 if there wasnt detected any lights at all.
	    return (idSensor!=-1) ? idSensor: -1;
	}
	
	
	/**
	 * @param distanceValue Assign a distance interval to this sensor reading.
	 * @return Return the assigned interval or [-1,-1] if none.
	 */
	private int[] classifyDistance(int distanceValue){
 		for( int i=0; i<this.discreteDistanceSensorIntervals.length; i++ ){
			// Loop over the discrete distance sensor intervals
			if( this.discreteDistanceSensorIntervals[i].isInSensorInverval( distanceValue ) ){
				// If the measured distance may appear in this range interval relative to the wall.
				
				// returns [max distance, minimum distance] to an object.
				return this.discreteDistanceSensorIntervals[i].getDistanceInterval();
			}
		}
 		
 		return new int[]{-1,-1};
 	}
	
	/**
	 * @param sensorIndex The sensor to read distance measurements. 
	 * @return Integer array with [max, min] distance from the given sensor to an object.
	 */
 	public int[] getDistanceRange( int sensorIndex ){
 		// First we check if we have an improved guess, else we make a new observation.
 		
 		int[] measurementBuffer = this.distanceValues;
 		if( measurementBuffer[8]!=-1 ){
 			// The measurer thread has improved our guess
 			
 			return new int[]{measurementBuffer[8], measurementBuffer[9]};
 		}
 		else{
 			// Make a distance observation and classify the result
 			
 			int distanceValue = this.controller.getDistanceValue( sensorIndex );
 			return classifyDistance( distanceValue );
 		}
		
	}
 	
 	/**
 	 * 
 	 * @param sensorIndex The sensor to read the light value from.
 	 * @return A light value measurement.
 	 */
 	public int getLightSensorReading( int sensorIndex ){
 		int lightMeasurementResult = this.controller.getLightValue( sensorIndex );
 		
 		return lightMeasurementResult;
 	}
 	
 	public int getDistanceSensorReading( int sensorIndex ){
 	    return this.controller.getDistanceValue( sensorIndex );
 	}

	
 	
 	
	@Override
  public void run() {
		// This methods runs until the SensorManager.close() has been called.
		// It will continuously read sensor values, and estimate the distance between
		// the robot and a object.
		running = true;
		// Perform the observation.
		while( running ){
			this.updateLightObservations();
			this.updateDistanceObservations();
			
			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				Logger.getInstance().error("SensorManager.run() ERROR: during sleep between measurements.");
			}
		}
	}
	
	private void updateDistanceObservations(){
		int nSensors = 8; // number of distance sensors
		
		
		// Initialize data structure
		int[] distanceSensorBuffer = new int[ nSensors+2 ];
		long[] wheelPositionBuffer;
		
		
		// Observe the distances
		for (int i = 0; i < nSensors; i++) {
			distanceSensorBuffer[i] = this.controller.getDistanceValue(i);
		}
		// Observe the wheels
		wheelPositionBuffer = new long[]{
			this.controller.getLeftWheelPosition(),
			this.controller.getRightWheelPosition()
		};
		
		// Change in wheel positions
		long previousLeftWheelPosition = this.wheelPositions[0];
		long previousRightWheelPosition = this.wheelPositions[1];
		int deltaLeftWheel = (int) (wheelPositionBuffer[0]-previousLeftWheelPosition);
		int deltaRightWheel= (int) (wheelPositionBuffer[1]-previousRightWheelPosition);
		boolean isGoingStraight = (deltaLeftWheel == deltaRightWheel && deltaRightWheel>0);
		
		
		// Our current guess at the distance in front of the robot
		int[] bufferedDistanceInterval = new int[]{ this.distanceValues[8], this.distanceValues[9] };
		
		
		if( isGoingStraight ){
			// We might be able to tell something more accurate about distances.
			
//			String s = "";
//			for(int i=0;i<this.distanceValues.length;i++) s += " "+this.distanceValues[i];
//			System.err.println("STRAIGHT"+s);
			
			
			if ( bufferedDistanceInterval[0]==-1 ){
				// We have never recorded a distance measurement, and are thus not able to improve it.
				// We fill the buffer with our best estimate.
				
				int[] interval = this.classifyDistance( distanceSensorBuffer[ SENSOR_FRONT_LEFT ] );
				
				bufferedDistanceInterval[0] = interval[0];
				bufferedDistanceInterval[1] = interval[1];
			}
			else{
				// Improve our distance estimate
				
				int[] interval = this.classifyDistance( distanceSensorBuffer[ SENSOR_FRONT_LEFT ] );
				int currentIntervalMinDistance = interval[1];
				int previousIntervalMinDistance = this.classifyDistance( this.distanceValues[ SENSOR_FRONT_LEFT ] )[1];
				
//				System.err.println("DIFF: "+distanceValues[9]+" : "+previousIntervalMinDistance);
				
				if( currentIntervalMinDistance == previousIntervalMinDistance ){
					// We are in the same discrete interval relative to the previous reading.
					// We now know that the max distance to the object in front is one step less.
					
					bufferedDistanceInterval[0] = this.distanceValues[8]-1; // We are shrinking the possible position interval
					bufferedDistanceInterval[1] = this.distanceValues[9];
					
					if( this.distanceValues[8]==this.distanceValues[9] ){
						// We had a tight bound on our distance guess.
						
						bufferedDistanceInterval[1] -= 1;						
					}
					
					// The following should not happen. We should have moved on to the next interval instead.
//					assert this.distanceValues[9]>this.distanceValues[8] : "SensorManager.run() ERROR: the distance interval has become illegal.";
				}
				else if(currentIntervalMinDistance < previousIntervalMinDistance){
					// We have entered a new interval. Since we know this is a new interval, the distance is guaranteed to
					// be equal to the max distance in this interval.
					
					System.err.println("NEW INTERVAL");
					
					bufferedDistanceInterval[0] = interval[0];
					bufferedDistanceInterval[1] = interval[0];
				}
				
//				System.err.println("position: "+bufferedDistanceInterval[0]+", "+bufferedDistanceInterval[1]);
			}
			
		}
		else if( !isGoingStraight ){
			// Since we are turning, we are unaware what might be going on in front of us.
			
			// Set the buffered values to signify that we haven't got any distance information.
			bufferedDistanceInterval[0] = -1;
			bufferedDistanceInterval[1] = -1; 
			Logger.getInstance().setStatus("SensorManager.run() INFO: has found accurate distance to an object in front.",15);
		}
		
		distanceSensorBuffer[8] = bufferedDistanceInterval[0];
		distanceSensorBuffer[9] = bufferedDistanceInterval[1];
		
		// Update the new buffer with the new values.
		this.wheelPositions = wheelPositionBuffer;
		this.distanceValues = distanceSensorBuffer;
	}

	private void updateLightObservations(){
		int nSensors = 8; // number of distance sensors
		
		// Initialize data structure
		int[] lightSensorBuffer = new int[ nSensors+2 ];
		
		// Observe the distance
		for (int i = 0; i < nSensors; i++) {
			lightSensorBuffer[i]	= this.controller.getLightValue(i);
		}
		
		/*TODO IMPLEMENTATION*/
	}
	
	public boolean isObjectHeld(){
	    return controller.isObjectHeld();
	  }
	  public boolean isObjectPresent(){
	    return controller.isObjectPresent();
	  }
}
