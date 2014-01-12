/**
 * @(#)KSDefinitions.java 1.1 2003/7/28
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
 * Contains static definitions for gripper/arm states. Importing this file
 * into a controller provides the user with named constants used in other
 * code that manipulates the robot arm and gripper.
 *
 * @author    Steve Perretta
 * @version   1.1 2003/7/28
 */
public class KSGripperStates {

  /**  */
  public static final int ARM_UP = 20;
  /**  */
  public static final int ARM_DOWN = 21;
  /**  */
  public static final int GRIP_OPEN = 22;
  /**  */
  public static final int GRIP_CLOSED = 23;

}
