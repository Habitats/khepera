/**
 * @(#)KSReadWriteState.java 1.1 2003/07/29
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

import java.io.*;
import java.util.*;

/**
 * The <code>KSReadWriteState</code> class is used by the <code>KSWriter</code>
 * and <code>KSReader</code> classes when serializing and deserializing robot and
 * world state information associated with recorded files.
 *
 * @author    Steve Perretta
 * @version   1.1 2003/07/29
 */
public class KSReadWriteState implements Serializable {
  /** Information regarding the robot's state. Includes sensor values, position,
   * orientation, gripper/arm states, motor speeds....
   */
  protected float[] data;

  /** Set to <tt>true</tt> if something other than the robot changed in the
   * arena, <tt>false</tt> otherwise.
   */
  protected boolean worldChange;

  /** If <code>worldChange</code> is <tt>true</tt>, then this field will refer
   * to the collection of world objects at the particular time step when the
   * change occured.
   */
  protected Vector worldObjects;

  /** The id number of any object held in the gripper, 0 if nothing is held. */
  protected int heldID;

  /**
    * Allocate a new <code>KSReadWriteState</code> object, setting fields to
    * default values.
    */
  public KSReadWriteState() {
    data = null;
    worldChange = false;
    worldObjects = null;
    heldID = 0;
  }

}
