package khepera.managers;

import java.awt.Point;

import edu.wsu.KheperaSimulator.RobotController;

public class SensorManager{
	
	RobotController controller; // 
	
	/* Discretizing the sensor data */
	private class SensorInterval {
		int xFar, xClose;
		double sensorFar, sensorNear;
		public SensorInterval(double sensorFar, double sensorNear, int xFar, int xClose){
			this.sensorFar = sensorFar;
			this.sensorNear = sensorNear;
			this.xFar = xFar;
			this.xClose = xClose;
		}
		public boolean inSensorInverval( int sensorValue ){ return (this.sensorNear<sensorValue && sensorValue<=this.sensorFar); }
		public int[] getDistanceInterval(){ return new int[]{this.xFar, this.xClose}; }
	}
	SensorInterval[] discreteSensorIntervals = new SensorInterval[]{ 
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

	
	public int[] getDistanceRange( int sensorIndex ){
		/* Observation:
		 * Due to the noise implementation, we should only perform a single sensor measure
		 * and categorize the distance interval. (According to statistical analysis)
		 */
		int distanceValue = this.controller.getDistanceValue( sensorIndex );
		
		for( int i=0; i<this.discreteSensorIntervals.length; i++ ){
			// Loop over the discrete distance sensor intervals
			if( this.discreteSensorIntervals[i].inSensorInverval( distanceValue ) ){
				// If the measured distance may appear in this range interval relative to the wall
				return this.discreteSensorIntervals[i].getDistanceInterval();
			}
		}
	}
	
}
