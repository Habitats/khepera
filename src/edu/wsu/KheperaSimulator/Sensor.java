/**
 * @(#)Sensor.java 1.1 2001/06/18
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
 * A <code>Sensor</code> class represents one infared sensor of the Khepera
 * robot.
 *
 * @author  Steve Perretta
 */
public class Sensor implements java.io.Serializable {
  protected float x;
  protected float y;
  protected float theta;
  private int lightValue;
  private int distValue;

  /**
   * Allocate a new <code>Sensor</code> object.
   * @param x the x position
   * @param y the y position
   * @param a the directional location
   */
  public Sensor(float x, float y, float a) {
    this.x = x;
    this.y = y;
    theta = a;
    lightValue = 500;
    distValue  = 0;
  }

  /**
   * Reset this sensor back to the default sensor values.
   */
  protected void reset() {
    // Should not reset coordinates -- see UpdateSensor class
    //this.x = 0;
    //this.y = 0;
    //this.theta = 0;
    lightValue = 500;
    distValue  = 0;
  }

  /**
   * get the distance value.
   * @return the distance value
   */
  protected int getDistValue() {
    return distValue;
  }

  /**
   * Set the distance value.
   * @param d the distance value
   */
  protected void setDistValue(int d) {
    distValue = d;
  }

  /**
   * Get the light value.
   * @return the light value
   */
  protected int getLightValue() {
    return lightValue;
  }

  /**
   * Set the light value.
   * @param l the light value
   */
  protected void setLightValue(int l) {
    lightValue = l;
  }
} // Sensor