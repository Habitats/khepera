/**
 * @(#)CurrentRobotState.java 1.1 2001/07/27
 *
 * Copyright Wright State University. All Rights Reserved.
 *
 * This file is part of the WSU Khepera Simulator.
 *
 * This file may be distributed under the terms of the Q Public License
 * as defined by Trolltech AS of Norway and appearing in the file
 * WSU_Khepera_Sim_license.txt included in the packaging of this file.
 *
 * This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
 * WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 *
 * For information on the Q Public License see:
 * 		http://www.opensource.org/licenses/qtpl.php
 *
 * For information on the WSU Khepera Simulator see:
 * 		http://gozer.cs.wright.edu and follow the links.
 *
 * Contact robostaff@gozer.cs.wright.edu if any conditions of this licensing are
 * not clear to you.
 */

package edu.wsu.KheperaSimulator;

/**
 * The <code>CurrentRobotState</code> class represents the current state of the
 * robot at any time.  This class provides methods to set the current state of
 * the robot as well as to get the current state.
 *
 * @author  Steve Perretta
 */
public class CurrentRobotState {

  private Motor motorState;
  private int resistivity;
  private boolean objPresent;
  private int armState, gripperState;
  private boolean objHeld;

  protected Sensor[] sensors;
  protected RobotCoordinates currentPos;

  /**
   * Allocates a new <code>CurrentRobotState</code> object.  This object is
   * initialized and set to a default state.
   */
  public CurrentRobotState() {
    currentPos = new RobotCoordinates();
    motorState = new Motor();
    reInitialize();
  }

  /**
   * Reinitialize this <code>CurrentRobotState</code> object.  This will reset
   * the current state of the robot back to the default state.
   */
  protected void reInitialize() {
    currentPos.reInitialize();
    motorState.reInitialize();
    armState = KSGripperStates.ARM_UP;
    gripperState = KSGripperStates.GRIP_OPEN;
    objPresent = false;
    objHeld = false;
    resistivity = 0;
  }

	public void setMotorSpeeds( int left, int right )
	{
		motorState.setMotorSpeeds( left, right );
	}

	public void setLeftMotorSpeed( int speed )
	{
		motorState.setLeftMotorSpeed( speed );
	}

	public void setRightMotorSpeed( int speed )
	{
		motorState.setRightMotorSpeed( speed );
	}

	public long getLeftPosition()
	{
		return motorState.getLeftPosition();
	}
	
	public long getRightPosition()
	{
		return motorState.getRightPosition();
	}
	
	

  /**
   * Get the arm state of the robot.  The state is either <tt>ARM_UP</tt> or
   * <tt>ARM_DOWN</tt>.
   * @return the arm state.
   */
  protected int getArmState() { return armState; }

  /**
   * Set the arm state of the robot.  The The state is either <tt>ARM_UP</tt> or
   * <tt>ARM_DOWN</tt>.
   * @param arm the arm state
   */
  protected void setArmState(int arm) {
    if(arm == KSGripperStates.ARM_UP)
      armState = arm;
    else if(arm == KSGripperStates.ARM_DOWN)
      armState = arm;
  }

  /**
   * Get the gripper state of the robot.  The state is either <tt>GRIP_OPEN</tt>
   * or <tt>GRIP_CLOSED</tt>.
   * @return the gripper state
   */
  protected int getGripperState() { return gripperState; }

  /**
   * Set the gripper state of the robot.  The state is either <tt>GRIP_OPEN</tt>
   * or <tt>GRIP_CLOSED</tt>.
   * @param grip the gripper state
   */
  protected void setGripperState(int grip) {
    if(grip == KSGripperStates.GRIP_OPEN)
      gripperState = grip;
    else if(grip == KSGripperStates.GRIP_CLOSED)
      gripperState = grip;
  }

  /**
   * Provide the state of the robots <code>Motor</code> object.
   * @return the robots <code>Motor</code> object
   */
  protected Motor getMotorState() { return motorState; }

  /**
   * Set the sensor values of the robot.
   * @param s an array of sensor objects
   */
  protected void postSensorValues(Sensor[] s) { sensors = s; }

  /**
   * Provides the all of the <code>Sensor</code> objects of the robot.
   * @return an array of <code>Sensor</code> objects where each contains the
   * current sensor state.
   */
  protected Sensor[] getSensorValues() { return sensors; }

  /**
   * Currently provides no valid state information.
   */
  protected void postResistivity(int rVal) { resistivity = rVal; }

  /**
   * Currently provides no valid state information.
   */
  protected synchronized int getResistivity() { return resistivity; }

  /**
   * Set the state about wheter an object exists between the gripper arms
   * of the robot.
   * @param objPresent a <code>boolean</code> value that represents the object
   * present state
   */
  protected synchronized void postObjectPresent(boolean objPresent) {
    this.objPresent = objPresent;
  }

  /**
   * Provide state information about whether an object exists between the
   * gripper arms of the robot.
   * @return <tt>true</tt> if an object is present, <tt>false</tt> otherwise
   */
  protected synchronized boolean isObjectPresent() { return objPresent; }

  /**
   * Provide the robots object held state.
   * @return <tt>true</tt> if an object is held, <tt>false</tt> otherwise
   */
  protected boolean isObjectHeld() { return objHeld; }

  /**
   * Set the robots object held state.
   * @param oHeld a value that is <tt>true</tt> if an object is held,
   * <tt>false</tt> otherwise
   */
  protected void setObjectHeld(boolean oHeld) { objHeld = oHeld; }

  /**
   * Provide the current position and orientation of the robot.
   * @return a <code>RobotCoordinates</code> object with coordinate state
   * information
   */
  protected RobotCoordinates getRobotCoordinates() { return currentPos; }

  /**
   * Set the current position and orientation of the robot.
   * @param x the x-coordinate position of the robot
   * @param y the y-coordinate position of the robot
   * @param a the angle theta of directional orientation of the robot
   */
  protected void setRobotCoordinates(int x, int y, float a) {
    currentPos.setCoordinates(x, y, a, (float)x, (float)y);
  }
} // CurrentRobotState