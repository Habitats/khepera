/**
 * @(#)Controller.java 1.1 2002/10/12
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
 * The <code>Controller</code> interface should be implemented by any class
 * whose instances are intended to be executed by a thread and perform
 * real-time manipulation of an object.
 *
 * @author    Brian Potchik
 * @version   1.0 10/12/02
 */
public interface Controller extends Runnable{

  /**
   * When an object implementing the interface <code>Controller</code> is used
   * to create a thread, the starting thread causes the objects
   * <code>doWork</code> method to be called.  This method is called repeatedly
   * and may perform any task.  The time needed for <code>doWork</code> to
   * complete is not significant with respect to the next invocation of this
   * method.
   * @throws java.lang.Exception
   */
  public abstract void doWork() throws java.lang.Exception;

  /**
   * Indicates that the application has finished using the controller, and that
   * any resources being used may be released. The starting thread invokes the
   * object's <code>close</code> method only when <code>doWork</code> has
   * returned and is not scheuled to be called again.
   * @throws java.lang.Exception
   */
  public abstract void close() throws java.lang.Exception;
} // Controller