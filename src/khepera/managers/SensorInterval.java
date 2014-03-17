package khepera.managers;

public class SensorInterval {
	
	// Denoting the max and min distance between an object
	// and our sensor in this measurement interval.   
	private int 	xFar,
					xClose;
	
	// Denoting the max and min sensor value that refere to the
	// same distance interval.
	private double 	sensorFar, 
					sensorNear;
	
	/**
	 * 
	 * @param sensorFar The futhermost (lowest) sensor value that belongs to this interval. 
	 * @param sensorNear The nearest (highest) sensor value that belongs to this interval.
	 * @param xFar The maximum distance a object may be at, within this sensor interval.
	 * @param xClose The minimum distance a object may be at, within this sensor interval.
	 */
	public SensorInterval(double sensorFar, double sensorNear, int xFar, int xClose){
		this.sensorFar = sensorFar;
		this.sensorNear = sensorNear;
		this.xFar = xFar;
		this.xClose = xClose;
	}
	
	/**
	 * @param sensorValue A value returned by a getDistanceValue() call
	 * @return Whether or not the robot is within this distance interval given the sensorValue. 
	 */
	public boolean isInSensorInverval( int sensorValue ){
		return (this.sensorNear>sensorValue && sensorValue>=this.sensorFar); 
	}
	
	/**
	 * 
	 * @return The [max, min] distance from an object within this interval.
	 */
	public int[] getDistanceInterval(){
		return new int[]{this.xFar, this.xClose};
	}
	
	/**
	 * 
	 * @return The lower bound of sensors readings which are within this interval.
	 */
	public int getLowestPossibleSensorReading(){
		return (int) this.sensorFar;
	}
	
	/**
	 * 
	 * @return The upper bound of sensors readings which are within this interval.
	 */
	public int getHighestPossibleSensorReading(){ 
		return (int) this.sensorNear; 
	}
}