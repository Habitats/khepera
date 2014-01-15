/**
 * @(#)UpdateBufferedWorld.java 1.1 2001/09/15
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

import java.awt.*;
import java.awt.geom.*;

/**
 * An <code>UpdateRobotImage</code> class provides the functionality to update the robots image.
 */
public class UpdateRobotImage {
	private CurrentRobotState rState;
	private RobotCoordinates rCoords;
	private Image robotAU, robotAD, robotADGO, robotADGC;

	/**
	 * Allocates a new <code>UpdateRobotImage</code> to provide the ablility to update the robots image.
	 * 
	 * @param rState
	 *            the current robot state
	 */
	public UpdateRobotImage(CurrentRobotState rState) {
		robotAU = Toolkit.getDefaultToolkit().getImage("images/robot_au.gif");
		robotAD = Toolkit.getDefaultToolkit().getImage("images/robot_ad.gif");
		robotADGO = Toolkit.getDefaultToolkit().getImage("images/robot_ad-go.gif");
		robotADGC = Toolkit.getDefaultToolkit().getImage("images/robot_ad-gc.gif");
		this.rState = rState;
		this.rCoords = rState.getRobotCoordinates();
	}

	/**
	 * Update the robot image based on the current status of the robot.
	 * 
	 * @param g2
	 *            graphic context to draw in
	 * @see java.awt.Graphics2D
	 */
	public void updateRobotImage(Graphics2D g2) {
		g2.rotate(rCoords.alpha, rCoords.dx + 13.0, rCoords.dy + 13.0);
		g2.drawImage(robotAU, rCoords.x, rCoords.y, null);
		if (rState.getArmState() == KSGripperStates.ARM_DOWN) {
			g2.drawImage(robotAD, rCoords.x, rCoords.y, null);
			if (rState.getGripperState() == KSGripperStates.GRIP_CLOSED) {
				g2.drawImage(robotADGC, rCoords.x, rCoords.y, null);
			} else if (rState.getGripperState() == KSGripperStates.GRIP_OPEN) {
				g2.drawImage(robotADGO, rCoords.x, rCoords.y, null);
			}
		}
		g2.setTransform(new AffineTransform());
	} // updateRobotImage
} // UpdateBufferedWorld