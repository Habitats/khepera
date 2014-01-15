/**
 * @(#)RobotControllerDirector.java 1.1 2002/10/13
 *
 * Copyright Brian Potchik. All Rights Reserved.
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

/**
 * A <code>RobotControllerDirector</code> is a utility class used to load, manage, and unload controllers that extend <code>RobotController</code>.
 */
public class RobotControllerDirector {

	/**
	 * A hash of the loaded controllers by controller name.
	 */
	private HashMap controllers;

	/**
	 * The path of where controllers should reside with respect to the current working directory of the Khepera Simulator. The default location where
	 * controllers should be placed is in "./controllers/". For example if the simulator is running from the directory "c:\KheperaSimulator\", then the
	 * controller class files would be located in "c:\KheperaSimulator\controllers\".
	 */
	private final String controllerPathName = "./controllers/";

	/**
	 * A <code>String</code> array of all controllers available.
	 */
	private String[] controllerNames;

	/**
	 * A reference to the current robot state which is passed to each controller.
	 */
	private CurrentRobotState currentRobotState = null;

	private long controllerThreadWaitTime = 20;

	/**
	 * Initialize and create a new <code>RobotControllerDirector</code>. The availiable controllers will be discovered and made available.
	 * 
	 * @param currentRobotState
	 *            a reference that allows access to the robot accessor methods.
	 */
	public RobotControllerDirector(CurrentRobotState currentRobotState) {
		this.currentRobotState = currentRobotState;
		controllers = new HashMap();
		findControllers();
	}

	/**
	 * Find the available controllers that can be loaded and update the hashmap if needed.
	 */
	private void findControllers() {
		try {
			File directory = new File(controllerPathName);
			controllerNames = null;
			controllerNames = directory.list(new ClassFileFilter());
			for (int i = 0; i < controllerNames.length; i++) {
				controllerNames[i] = (new StringTokenizer(controllerNames[i], ".", false)).nextToken();
				if (!controllers.containsKey(controllerNames[i])) {
					controllers.put(controllerNames[i], null);
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Return a <code>String</code> array of the controllers available to the application.
	 * 
	 * @return the controller names
	 */
	protected String[] availableControllers() {
		findControllers();
		return controllerNames;
	}

	/**
	 * Start the controller and return <tt>true</tt> if the controller started successfully. Only one instance of a particular controller can be loaded at one
	 * time. If the controller is already loaded or it failed to load, this method will return false.
	 * 
	 * @param controllerName
	 *            the name of the controller to load
	 * @return <tt>true</tt> on success; <tt>false</tt> otherwise.
	 */
	protected boolean startController(String controllerName) {
		RobotController controller = (RobotController) controllers.get(controllerName);

		if (controller != null) {
			return false;
		}

		try {
			Class c = Class.forName(controllerName, true, new DirectoryClassLoader(controllerPathName));
			controller = (RobotController) c.newInstance();
			controllers.put(controllerName, controller);
			controller.initialize(controllerName, currentRobotState, controllerThreadWaitTime);
			controller.simStart();
			return true;
		}

		catch (java.lang.ClassNotFoundException ex) {
			JOptionPane.showMessageDialog(null, "The module class could not be found", "Class not Found", JOptionPane.ERROR_MESSAGE);
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	} // loadController

	/**
	 * Stop a running controller. The controller is not guarenteed to terminate immediatly. The controller is simply informed that it is time to finish. The
	 * controller will then terminate itself when a safe state is reached.
	 * 
	 * @param controllerName
	 *            the name of the controller to stop
	 */
	protected void stopController(String controllerName) {
		RobotController controller = (RobotController) controllers.get(controllerName);
		if (controller == null)
			return;
		controller.setFinished(true); // tell the controller to terminate gracefully
		controllers.put(controllerName, null);
	}

	/**
	 * Terminate all controllers.
	 */
	protected void stopAll() {
		for (int i = 0; i < controllerNames.length; i++) {
			stopController(controllerNames[i]);
		}
	}

	/**
	 * Provide a list of the running controllers.
	 * 
	 * @return a list of controllers that are currently running
	 */
	protected ArrayList runningControllers() {
		ArrayList list = new ArrayList();
		RobotController c = null;
		for (int i = 0; i < controllerNames.length; i++) {
			c = (RobotController) controllers.get(controllerNames[i]);
			if (c != null) {
				list.add(new String(controllerNames[i]));
			}
		}
		return list;
	}

} // RobotControllerDirector

/**
 * A <code>FilenameFilter</code> class used to filter a directory to only see class files.
 * 
 * Filter modified to exclude class files that contain the $ character
 */
class ClassFileFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		if (name.indexOf('$') > -1)
			return false;

		StringTokenizer tokenizer = new StringTokenizer(name, ".", false);
		String ext = null;

		while (tokenizer.hasMoreTokens()) {
			ext = tokenizer.nextToken();
		}

		if (ext.equals("class")) {
			return true;
		}

		return false;
	}
} // ClassFileFilter