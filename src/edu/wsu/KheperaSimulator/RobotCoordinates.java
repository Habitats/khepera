/**
 * @(#)RobotCoordinates.java 1.1 2001/09/09
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
 * The <code>RobotCoordinates</code> class represents the current coordinates
 * and direction of the robot. The information stored in this class is used for
 * drawing the robot and generating sensor data, and should not be used by the
 * controller.
 *
 * @author  Steve Perretta
 */
public class RobotCoordinates implements java.io.Serializable{
  protected int x;
  protected int y;
  protected float alpha, dx, dy;

  /**
   * Allocate a new <code>RobotCoordinates</code> object and initialize it
   * with default values.
   */
  public RobotCoordinates() {
    reInitialize();
  }

  /**
   * Allocate a new <code>RobotCoordinates</code> object and initialize it
   * with default values.
   * @param x initial x-coordinate of the robot
   * @param y initial y-coordinate of the robot
   * @param a initial direction of the robot
   * @param dx floating point representation of x
   * @param dy floating point representation of y
   */
  public RobotCoordinates(int x, int y, float a, float dx, float dy) {
    this.x = x;
    this.y = y;
    this.alpha = a;
    this.dx = dx;
    this.dy = dy;
  }

  /**
   * Reset the robots position back to the default values.
   */
  protected void reInitialize() {
    x = 250;
    y = 250;
    alpha = 0.0f;
    dx = (float)x;
    dy = (float)y;
  }

  /**
   * Set the current robot coordinates.
   * @param x initial x-coordinate of the robot
   * @param y initial y-coordinate of the robot
   * @param a initial direction of the robot
   * @param dx floating point representation of x
   * @param dy floating point representation of y
   */
  protected synchronized void setCoordinates(int x, int y, float a, float dx,
                                            float dy) {
    this.x = x;
    this.y = y;
    this.alpha = a;
    this.dx = dx;
    this.dy = dy;
  }
} // RobotCoordinates