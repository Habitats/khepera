/**
 * @(#)AboutWindow.java 1.1 2002/02/12
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The <code>AboutWindow</code> class represents a dialog window that conveys information about the WSU Khepera Simulator.
 * 
 * @author Steve Perretta
 */
public class AboutWindow extends JDialog implements ActionListener {

	private JPanel mainPanel;
	private JButton closeButton;
	private Image background;
	private KSFrame parent;

	/**
	 * Allocates a new <code>AboutWindow</code> object. This window provides information about the WSU Khepera Simulator.
	 * 
	 * @param parent
	 *            a reference the main frame of the simulator
	 */
	public AboutWindow(KSFrame parent) {
		super(parent, "About this Program", true);
		this.parent = parent;
		initComponents();
		pack();
	}

	/**
	 * Initialize the graphical components used to display window.
	 */
	private void initComponents() {
		getContentPane().setLayout(new BorderLayout());
		background = Toolkit.getDefaultToolkit().getImage("images/about.gif");
		mainPanel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(background, 0, 0, this);
			}
		};
		mainPanel.setPreferredSize(new Dimension(300, 400));
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		getContentPane().add(mainPanel, "North");
		getContentPane().add(closeButton, "Center");
	}

	/**
	 * Dispose of this window.
	 * 
	 * @param e
	 * @see java.awt.event.ActionListener
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		setVisible(false);
		dispose();
		parent.repaint();
	}
} // AboutWindow