package khepera.managers;

import java.awt.Point;

public class SensorManager{
	
	RobotController controller; // 
	
	
	
	public SensorManager( RobotController controller ){
		this.controller = controller;
		
	}

	
	public int getSensorData( int sensorIndex ){
		/* Observation:
		 * Due to the noise implementation, we should only perform a single sensor measure
		 * and categorize the distance interval. (According to statistical analysis)
		 */
			
	}
	
	private Point calculateInterval( int s ){
		int[] sensorMeans = new int{15, 20, 70, 100, 200, 430, 675, 950, 1023};
		int[] 
		if( )
	}
}
