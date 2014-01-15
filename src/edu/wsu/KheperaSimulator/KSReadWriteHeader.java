/**
 * @(#)KSReadWriteHeader.java 1.1 2003/07/29
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
 * The <code>KSReadWriteHeader</code> class is used by the <code>KSWriter</code> and <code>KSReader</code> classes when serializing and deserializing header
 * information associated with recorded files.
 * 
 * @author Steve Perretta
 * @version 1.1 2003/08/02
 */
public class KSReadWriteHeader implements Serializable {
	/** Date and time of recording. */
	protected String date;
	/** Name of the currently active controller. */
	protected String controller;
	/** Time delay information message. */
	protected String delay;
	/** All world objects that make up the statring state of the arena. */
	protected Vector objects;

	/**
	 * Allocate a new <code>KSReadWriteHeader</code> object with all fields initialized to null.
	 */
	public KSReadWriteHeader() {
		date = null;
		controller = null;
		delay = null;
		objects = null;
	}
}
