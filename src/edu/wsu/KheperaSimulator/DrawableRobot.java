/**
 * @(#)DrawableRobot.java 1.1 2001/07/31
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
 * A <code>DrawableRobot</code> class provides methods to calculate a new
 * position of the robot based on the current state of the robot
 */
public class DrawableRobot {

  private int xPos;
  private int yPos;
  private float dx;
  private float dy;
  private float sina, cosa;
  private float theta;
  private CurrentRobotState rState;
  private RobotCoordinates currentPos;
  private Motor motorState;
  private double speedLevel;
  private int turnLevel;

  /**
   * This value sets maximum speed that the robot can travel in the map.
   * This value must be >= 4.8.  The greater this value is the slower the
   * maximum speed of the robot.  For some odd reason this value cannot be
   * any lower than 4.8.  Go figure.
   */
  private double speedMax = 4.8; // found by trial/error

  /**
   * Allocate a new <code>DrawableRobot</code> object with the current robot
   * state.
   * @param rState the current robot state
   */
  public DrawableRobot(CurrentRobotState rState) {
    this.rState = rState;
    motorState = rState.getMotorState();
    currentPos = rState.getRobotCoordinates();
    reInitialize();
  }

  /**
   * Reinitialize this <code>DrawableRobot</code> object.  This will reset
   * the current state of the robot back to the default state.
   */
  protected void reInitialize() {
    speedLevel = 0;
    turnLevel  = 1050;
    initCoordinates();
  }

  /**
   * Set the speed level of the robot relative to the simulated environment.
   * @param sp speed level
   */
  protected void setSpeedParam(int sp) {
    int t = (21 - sp++); // translate the slider scale to be 21 to 1
    speedLevel = speedMax * t;
    if (speedLevel > speedMax) {
      speedLevel -= ((speedLevel - speedMax)/2);
    }
    turnLevel = (int)(0.21 * (speedLevel));
  }

  /**
   * Update the wheel positions of the robot.  Should not update if there is a
   * collision with immobile object.
   * @param lSpeed the new speed of the left wheel
   * @param rSpeed the new speed of the right wheel
   */
  private void updateWheelPositions(int lSpeed, int rSpeed) {
    long lp = motorState.getLeftPosition();
    long rp = motorState.getRightPosition();

    if(((lp + lSpeed) > Long.MAX_VALUE) ||
       ((lp + lSpeed) < Long.MIN_VALUE))
      lp = 0L;
    else
      lp += lSpeed;
    if(((rp + rSpeed) > Long.MAX_VALUE) ||
       ((rp + rSpeed) < Long.MIN_VALUE))
      rp = 0L;
    else
      rp += rSpeed;

    motorState.setMotorPositions(lp, rp);
  }

  /**
   * Initialize the coordinates of this robot to the current state of the
   * <code>RobotCoordinates</code> object.
   */
  private void initCoordinates() {
    xPos = currentPos.x;
    yPos = currentPos.y;
    dx = currentPos.dx;
    dy = currentPos.dy;
    theta = currentPos.alpha;
  }

  /**
    * Normalize value in radians.
    * @param theta the robot's angle in radians
    * @return normalized value of input parameter
    */
  private float normRad(float theta) {
    while(theta > Math.PI)  theta -= (float)(2*Math.PI);
    while(theta < -Math.PI) theta += (float)(2*Math.PI);
    return theta;
  }

  /**
   * Update the coordinates of the robot based on the current collision status
   * of the robot.
   * @param hit collision status of the robot
   */
  protected void updateCoordinates(int hit) {
    float direction;
    int dist, lSpeed, rSpeed;

    lSpeed = motorState.leftSpeed;
    rSpeed = motorState.rightSpeed;
    dist = lSpeed + rSpeed;
    if(hit != 0) {
      if(hit == 1) {
        if(lSpeed >= 0 || rSpeed >= 0)
          return;
      }
      else {
        if(lSpeed <= 0 || rSpeed <= 0)
          return;
      }
    }

    /* first update the angle and store angle (int) and theta (double) */
    direction = lSpeed - rSpeed;
    direction = (float)Math.toRadians(direction);
    //direction /= (950-(turnLevel*35));
    //direction /= (1050 + turnLevel);
    direction /= turnLevel;
    //direction *= dNoise;
    theta += direction;
    theta = normRad(theta);
    cosa = (float)Math.cos(theta);
    sina = (float)Math.sin(theta);
    // higher value = slower movement
    //dx += (lSpeed+ rSpeed) * cosa / (speedLevel);
    //dy += (lSpeed+ rSpeed) * sina / (speedLevel);
    dx += (lSpeed+ rSpeed) * cosa / (speedLevel);
    dy += (lSpeed+ rSpeed) * sina / (speedLevel);
    xPos = (int)dx;
    yPos = (int)dy;
    //System.err.println("In DR - x="+xPos+" y="+yPos);
    currentPos.setCoordinates(xPos, yPos, theta, dx, dy);
    updateWheelPositions(lSpeed, rSpeed);
  }
} // DrawableRobot