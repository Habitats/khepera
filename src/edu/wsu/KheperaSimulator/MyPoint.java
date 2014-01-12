/**
 * @(#)MyPoint.java 1.1 2002/02/19
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
 * A generic class to represent a 2-dimensional point.
 *
 * @author  Steve Perretta
 */
public class MyPoint {
    /** */
    protected int x;

    /** */
    protected int y;

    /**
     * Allocate a new <code>MyPoint</code> object with specified coordinate parameters.
     */
    public MyPoint(int x, int y) {
      this.x = x;
      this.y = y;
    }
} // MyPoint
