/**
 * @(#)HelpDialog.java 1.1 2002/01/11
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
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * A <code>HelpDialog</code> object provides a means to view the html documentation of the WSU Khepera Simulator within the application.
 * 
 * @author Steve Perretta
 */
public class HelpDialog extends JDialog implements ActionListener {
	private JPanel mainPanel;
	private JScrollPane scrollPane;
	private JEditorPane htmlWindow;
	private KSFrame parent;

	/**
	 * Allocates a new <code>HelpDialog</code> object and constructs the contents of this GUI component.
	 * 
	 * @param parent
	 *            a reference to the parent frame
	 */
	public HelpDialog(KSFrame parent) {
		super(parent, "Help Topics", true);
		this.parent = parent;
		initComponents();
		pack();
	}

	/**
	 * Construct a this GUI object's contents.
	 */
	private void initComponents() {
		htmlWindow = new JEditorPane();
		htmlWindow.setPreferredSize(new Dimension(500, 500));
		htmlWindow.setEditable(false);
		htmlWindow.setContentType("text/html");
		scrollPane = new JScrollPane(htmlWindow, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(500, 500));

		try {
			htmlWindow.setPage("file:./html-docs/index.html");
		} catch (IOException ioe) {
			System.err.println(ioe);
		}
		htmlWindow.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						htmlWindow.setPage(e.getURL()); // ?????
					} catch (IOException ioe) {
						System.err.println(ioe);
					}
				}
			}
		});

		JButton exitButton = new JButton("Close Help Window");
		exitButton.addActionListener(this);
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(exitButton, "North");
		mainPanel.add(scrollPane, "Center");
		getContentPane().add(mainPanel);
	}

	/**
	 * Handles events from the OK button. When OK is pressed the dialog becomes invisible, disposes of its self, and retruns.
	 * 
	 * @see java.awt.event.ActionListener
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		setVisible(false);
		dispose();
		parent.repaint();
	}
} // HelpDialog

