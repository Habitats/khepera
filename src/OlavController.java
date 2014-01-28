

import java.util.ArrayList;
import java.util.Collections;

import javax.print.attribute.standard.Finishings;

import edu.wsu.KheperaSimulator.KSGripperStates;
import edu.wsu.KheperaSimulator.RobotController;
import edu.wsu.KheperaSimulator.RobotStatePanel;

public class OlavController extends RobotController {

	public final static int DIRECTION_LEFT = -1;
	public final static int DIRECTION_RIGHT = 1;

	public final static int SENSOR_LEFT = 0;
	public final static int SENSOR_ANGLEL = 1;
	public final static int SENSOR_FRONTL = 2;
	public final static int SENSOR_FRONTR = 3;
	public final static int SENSOR_ANGLER = 4;
	public final static int SENSOR_RIGHT = 5;
	public final static int SENSOR_BACKR = 6;
	public final static int SENSOR_BACKL = 7;

	ArrayList<Behavior> behaviors;
	int[] distanceSensors;
	int[] lightSensors;
	
	float posX;
	float posY;
	float orientation;
	float lastLeftWheelPos = 0;
	float lastRightWheelPos = 0;
	
	public OlavController() {
		//setup
		distanceSensors = new int[8];
		lightSensors = new int[8];
		behaviors = new ArrayList<Behavior>();
		posX = 0;
		posY = 0;
		orientation = 0;
		
		//Temp methods for first assignment
		addBehaviors();
		Collections.sort(behaviors);
	}

	public void updateState() {
		float leftWheelPos = getLeftWheelPosition();
		float rightWheelPos = getRightWheelPosition();
		float LWDiff = leftWheelPos - lastLeftWheelPos;
		float RWDiff = rightWheelPos - lastRightWheelPos;
		
		if(LWDiff == 0 && RWDiff == 0) return;
		if(Math.signum(RWDiff) == Math.signum(LWDiff)) {
			posX += (LWDiff + RWDiff) / 2 * Math.cos(Math.toRadians(orientation));
			posY += (LWDiff + RWDiff) / 2 * Math.sin(Math.toRadians(orientation));
		}
		else if (Math.signum(RWDiff) != Math.signum(LWDiff)) {
			orientation += RWDiff / 3;
			if (orientation < 0) orientation += 360;
			else if (orientation >= 360) orientation -= 360;
		}
//		System.out.println("ORIENTATION: " + orientation);
//		System.out.println("X: " + posX);
//		System.out.println("Y: " + posY);

		lastLeftWheelPos = leftWheelPos;
		lastRightWheelPos = rightWheelPos;
	}

	@Override
	public void doWork() throws Exception {
		updateState();
		updateSensors(4);
		for (Behavior b : behaviors) {
			if(b.checkConditions()) {
				b.run();
				break;
			}
		}
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		//CBA
	}
	
	
	public void stop() {
		setMotorSpeeds(0,0);
	}

	public void moveForward(int speed) {
		setMotorSpeeds(speed,speed);
	}

	public void rotate(int direction, int speed) {
		if(direction > 0) {
			setMotorSpeeds(speed, -speed); // Right turn
		} else {
			setMotorSpeeds(-speed, speed); // Left turn
		}
	}

	//This method should be moved into the behaviours at a later point.
	private void updateSensors(int passes) {
		
		//THIS IS BAD, JUST WIPE VALUES INSTEAD OF REALLOC, CBA
		distanceSensors = new int[8];
		lightSensors = new int[8];
		
		for (int j = 0; j < passes; j++) {
			for (int i = 0; i < 8; i++) {
				distanceSensors[i] += getDistanceValue(i)/passes;
				lightSensors[i] += getLightValue(i)/passes;
			}
		}
	}
	//#########################TEMP METHODS#########################

	//so temp
	public void addBehaviors() {
		behaviors.add(new WalkMaze(0));
		behaviors.add(new CollectBall(5, 50));
	}


	//#############################TEMP CLASSES#######################
	//This class should be moved into a seperate class file at a later point in time.
	private abstract class Behavior implements Comparable<Behavior> {
		protected int priority = 0;

		public Behavior(int priority) {
			this.priority = priority;
		}

		@Override
		public int compareTo(Behavior o) {
			return o.priority - priority;
		}

		public abstract void run();
		public abstract boolean checkConditions();
	}

	//####################TEMP BEHAVIORS####################
	public class CollectBall extends Behavior {
		int state = 0;
		double initX = 0;
		double initY = 0;
		double angleToTarget;
		int detectThreshhold;
		
		public CollectBall(int priority, int detectThreshhold) {
			super(priority);
			this.detectThreshhold = detectThreshhold;
			// TODO Auto-generated constructor stub
		}

		int sensor;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			switch(state) {
			case 0:
				sensor = -1;
				//Something has been spotted!
				stop();
				state++;
			break;
			case 1:
				int max = -1;
				//find the angle!
				for (int i = 0; i < distanceSensors.length - 2; i++) {
					if (distanceSensors[i] >= max) {
						max = distanceSensors[i];
						sensor = i;
					}
				}
				//We now know approximately where somehting is, if sensor != -1,
				//if facing the object, start moving:
				int threshhold = 25;
				System.out.println(Math.abs(distanceSensors[SENSOR_FRONTL] - distanceSensors[SENSOR_FRONTR]));
					if(distanceSensors[SENSOR_FRONTL] > detectThreshhold*2/3 && 
							Math.abs(distanceSensors[SENSOR_FRONTL] - distanceSensors[SENSOR_FRONTR]) < threshhold) {
						stop();
						state++;
						break;
					}
				
				//else rotate to face it:
				int direction = DIRECTION_LEFT;
				if (sensor < 3) {
					direction = DIRECTION_LEFT;
				} else {direction = DIRECTION_RIGHT;}
				rotate(direction, 1);
			break;
			case 2:
				moveForward(1);
				if ((distanceSensors[SENSOR_FRONTL] + distanceSensors[SENSOR_FRONTR]) / 2 > 1000) {
					stop();
					state++;
				}
			break;
			case 3:
				setGripperState(KSGripperStates.GRIP_OPEN);
				setArmState(KSGripperStates.ARM_DOWN);
				System.out.println(isObjectPresent());
				if (isObjectPresent()) {
					setGripperState(KSGripperStates.GRIP_CLOSED);
					System.out.println("GOT IT!");
				} else {System.out.println("There was nothing here......");}
				state++;
			break;
			case 4:
			break;
			default: break;
			}
		}

		@Override
		public boolean checkConditions() {
			//Look for objects
			if (state == 0) {
				boolean objectNear = false;
				for (int i = 0; i < distanceSensors.length - 2; i++) {
					if (distanceSensors[i] > detectThreshhold) objectNear = true; 
				}
				if (objectNear) return true;
			}
			//is moving to object or picking up
			if (state > 0 && state < 5) return true;
			return false;
		}
		
	}
	
	private class GoHome extends Behavior{

		public GoHome(int priority) {
			super(priority);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean checkConditions() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
	private class WalkMaze extends Behavior {

		public WalkMaze(int priority){
			super(priority);
		}

		int state = 0;
		long startCounter = 0;
		int direction = 0;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			switch(state) {
			case 0:
				if((getDistanceValue(SENSOR_FRONTL) < 700) && (getDistanceValue(SENSOR_FRONTR) < 700)) {
					moveForward(5);
				} else {
					if(getDistanceValue(SENSOR_RIGHT) > getDistanceValue(SENSOR_LEFT)) {
						direction = DIRECTION_LEFT;
					} else { direction = DIRECTION_RIGHT;
					}
					stop();
					state++;
				}
				break;

			case 1:
				startCounter = getLeftWheelPosition();
				state++;
				break;

			case 2:
				if((Math.abs(startCounter - getLeftWheelPosition())) < 3*90) {
					rotate(direction, 3);
				} 
				else if((Math.abs(startCounter - getLeftWheelPosition())) > 3*90) {
					rotate(-direction, 1);
				} else {
					stop();
					state = 0;
				}
				break;
			default:
				// noop.
			}
		}

		@Override
		public boolean checkConditions() {
			return true;
		}

	}

}
