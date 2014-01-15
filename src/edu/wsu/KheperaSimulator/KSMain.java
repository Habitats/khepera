/**
 * @(#)KSMain.java 1.1 2002/10/17
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

import javax.swing.UIManager;
import java.awt.*;

/**
 * Contains the main method. The application environment is setup and the simulator is constructed and show.
 * 
 * @author Brian Potchik
 * @version 1.1 2002/10/17
 */
public class KSMain {
	private boolean packFrame = true;
	private KSFrame frame = null;

	/**
	 * Allocates a new <code>KSMain</code> that will contruct the WSU Khepera Simulator and show it.
	 */
	public KSMain() {
		KSFrame frame = new KSFrame("WSU Khepera Robot Simulator v7.2");

		// Validate frames that have preset sizes
		// Pack frames that have useful preferred size info, from their layout
		if (packFrame) {
			frame.pack();
		} else {
			frame.validate();
		}

		// Center the application
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		frame.setVisible(true);
	} // KSMain

	/**
	 * Set the look and feel and create a new <code>KSMain</code>.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new KSMain();
	} // main
} // KSMain