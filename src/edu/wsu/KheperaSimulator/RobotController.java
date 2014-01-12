/*
 *		RobotController.java
 *
 *		Created by Steve Perretta, Modified by Duane Bolick on 08 JAN 2005
 *
 */		

package edu.wsu.KheperaSimulator;


public abstract class RobotController implements Controller
{

	private long waitTime = 5;
	private boolean finished = false;
	private boolean running = false;
	private Thread thread = null;
	private String name = null;

	private CurrentRobotState state = null;
	private Sensor[] sensors = null;

	
	public RobotController()
	{
	}

	
	protected void initialize(String _name, CurrentRobotState _state, long timeout)
	{
		name = _name;         
		state = _state;
		sensors = state.getSensorValues();
		waitTime = timeout;
	} 
	
	protected void simStart()
	{
		thread = new Thread( this, name );
		thread.start();
	}

	protected void setWaitTime(long waitTime)
	{
		this.waitTime = waitTime;
	}

	protected void setFinished(boolean finished)
	{
		this.finished = finished;
	}

	protected boolean isRunning()
	{
		return running;
	}


	public void run() 
	{
		running = true;

		try
		{
			while (!finished)
			{
				doWork();
				sleep(waitTime);
			}

			close();
		} 

		catch (Exception e)
		{
			System.out.println("\nThere was a problem with the controller.");
			System.out.println("Here is the controller thread's stack at");
			System.out.println("the time of the error:\n\n");

			System.out.println("=== Stack Trace ======================\n");
			e.printStackTrace();
			System.out.println("\n=== End of the stack trace ===========");

			System.out.println("\nSession ended\n");
			System.exit(0);
		}

		running = false;
	} 
  


	public void sleep(long timeout)
	{
		try 
		{
			thread.sleep(timeout);
		}
	
		catch (Exception e) 
		{
		}
	}
  
  
	// =========================================================================
	// ===                          	========================================
	// ===		Mutator Methods			========================================
	// ===                          	========================================
	// =========================================================================
    

	public void setMotorSpeeds(int left, int right)
	{
		state.setMotorSpeeds(left, right);
	}


	public void setLeftMotorSpeed(int speed)
	{
		state.setLeftMotorSpeed(speed);
	}


	public void setRightMotorSpeed(int speed)
	{
		state.setRightMotorSpeed(speed);
	}

	
	public void setArmState(int armState)
	{
		state.setArmState(armState);
	}


	public void setGripperState(int gripState)
	{
		state.setGripperState(gripState);
	}



	
	// =========================================================================
	// ===                          	========================================
	// ===		Accessor Methods		========================================
	// ===                          	========================================
	// =========================================================================
    
		
	public int getArmState()
	{
		return state.getArmState();
	}

	
	public int getGripperState()
	{
		return state.getGripperState();
	}

		
	public boolean isObjectPresent()
	{
		return state.isObjectPresent();
	}

	
	public boolean isObjectHeld()
	{
		return state.isObjectHeld();
	}

	
	public int getLightValue(int sensorID)
	{
		return sensors[sensorID].getLightValue();
	}

	
	public int getDistanceValue(int sensorID)
	{
		return sensors[sensorID].getDistValue();
	}

	
	public int getResistivity()
	{
		return state.getResistivity();
	}

	
	public long getRightWheelPosition()
	{
		return state.getRightPosition();
	}

	
	public long getLeftWheelPosition()
	{
		return state.getLeftPosition();
	}


} // RobotController
