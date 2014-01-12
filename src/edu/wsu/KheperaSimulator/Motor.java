/**
 * @(#)Motor.java 1.1 2001/07/22
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
 * A <code>Motor</code> class represents a motor of the Khepera robot.
 *
 * @author  Steve Perretta
 */
public class Motor {
    protected int leftSpeed, rightSpeed;
    private long leftPosition, rightPosition;

    /**
     * Allocate a new <code>Motor</code> object.
     */
    public Motor() {
        reInitialize();
    }

    /**
     * Allocate a new <code>Motor</code> object with the specified defaults.
     * @param lSpeed left wheel speed
     * @param rSpeed right wheel speed
     * @param lPos left wheel position
     * @param rPos right wheel position
     */
    public Motor(int lSpeed, int rSpeed, long lPos, long rPos) {
        leftSpeed  = lSpeed;
        rightSpeed = rSpeed;
        leftPosition  = lPos;
        rightPosition = rPos;
    }

    /**
     * Initialize this motor to the default values.
     */
    protected void reInitialize() {
        leftSpeed  = 0;
        rightSpeed = 0;
        leftPosition  = 0;
        rightPosition = 0;
    }

    /**
     * Set the wheels speeds.
     * @param lSpeed the left wheel speed
     * @param rSpeed the right wheel speed
     */
    protected void setMotorSpeeds(int lSpeed, int rSpeed) {
        setLeftMotorSpeed(lSpeed);
        setRightMotorSpeed(rSpeed);
    }

    /**
     * Set the left wheel speed.
     * @param speed range from -10 to 10; 0 implies motor is off
     */
    protected void setLeftMotorSpeed(int speed) {
      if (speed < 10 && speed > -10) {
        leftSpeed = speed;
      }
    }

    /**
     * Set the right wheel speed.
     * @param speed range from -10 to 10; 0 implies motor is off
     */
    protected void setRightMotorSpeed(int speed) {
      if (speed < 10 && speed > -10) {
        rightSpeed = speed;
      }
    }

    /**
     * Set the current wheel positions.
     * @param lPos left position
     * @param rPos right position
     */
    protected void setMotorPositions(long lPos, long rPos) {
      leftPosition = lPos;
      rightPosition = rPos;
    }

    /**
     * Provide the left wheel position.
     * @return left position
     */
    protected long getLeftPosition() {
      return leftPosition;
    }

    /**
     * Provide the right wheel position.
     * @return right position
     */
    protected long getRightPosition() {
      return rightPosition;
    }

} // Motor