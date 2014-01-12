/**
 * @(#)ObjectDialog.java 1.1 2001/07/21
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
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * An <code>ObjectDialog</code> class provides the GUI components and
 * functionality to create maps with a variety of objects such as walls, balls,
 * caps, and lights.
 *
 * @author  Steve Perretta
 */
public class ObjectDialog extends JPanel {
    private ButtonGroup  objectBGroup;
    private JRadioButton rbWall;
    private JRadioButton rbLight;
    private JRadioButton rbBall;
    private JRadioButton rbCap;

    private JButton      rotateButton;
    private ButtonGroup  rotateBGroup;
    private JRadioButton rbCWise;
    private JRadioButton rbCCWise;

    private JPanel      rbPanel;
    private JPanel      displayPanel;
    private ObjectPanel objectPanel;
    private JPanel      rotatePanel;
    private JPanel      buttonPanel;

    private WorldPanel  worldPanel;

    private RadioListener rbObjectListener;

    /**
     * Allocate a new <code>ObjectDialog</code> object with access to the local
     * simulator.
     * @param wPanel the simulator <code>WorldPanel</code>
     */
    public ObjectDialog(WorldPanel wPanel) {
        super();
        initComponents (wPanel);
    }

    /**
     * Initialize and build this GUI component.
     * @param wPanel the simulator <code>WorldPanel</code>
     */
    private void initComponents(WorldPanel wPanel) {
        rbWall       = new JRadioButton("Wall", true);
        rbLight      = new JRadioButton("Light");
        rbBall       = new JRadioButton("Ball");
        rbCap        = new JRadioButton("Cap");
        objectBGroup = new ButtonGroup();

        rbPanel      = new JPanel();
        displayPanel = new JPanel();
        objectPanel  = new ObjectPanel();
        rotatePanel  = new JPanel();
        buttonPanel  = new JPanel();

	ImageIcon rotateIcon = new ImageIcon("images/rotate.gif");
        rotateButton = new JButton("Rotate   ", rotateIcon);
	rotateButton.setBorder(BorderFactory.createRaisedBevelBorder());

	rotateBGroup = new ButtonGroup();
        rbCWise      = new JRadioButton("CWise", true);
        rbCCWise     = new JRadioButton("CCWise");

        rbObjectListener = new RadioListener();
        worldPanel   = wPanel;

        this.setLayout(new BorderLayout());

        /* Radio Buttons: set Action Commands */
        rbWall.setActionCommand("wall");
        rbLight.setActionCommand("light");
        rbBall.setActionCommand("ball");
        rbCap.setActionCommand("cap");
        rbCWise.setActionCommand("cw");
        rbCCWise.setActionCommand("ccw");

        /* RadioButtons: register RadioListener for type change */
        rbWall.addActionListener(rbObjectListener);
        rbLight.addActionListener(rbObjectListener);
        rbCap.addActionListener(rbObjectListener);
        rbBall.addActionListener(rbObjectListener);

        rbCWise.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               if (rbCWise.isSelected())
                   objectPanel.setRotateDirection(ObjectPanel.CW);
           }
        });

        rbCCWise.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               if (rbCCWise.isSelected())
                   objectPanel.setRotateDirection(ObjectPanel.CCW);
           }
        });

        // regular button -> rotates object
        rotateButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               objectPanel.rotateObject();
               worldPanel.setRotationAngle(objectPanel.getRotationAngle());
           }
        });


        /* Radio Buttons: add to ButtonGroup */
        objectBGroup.add(rbWall);
        objectBGroup.add(rbLight);
        objectBGroup.add(rbBall);
        objectBGroup.add(rbCap);

        /* Radio Buttons: add to Panels */
        buttonPanel.setLayout(new BorderLayout());
        this.add(buttonPanel, "West");
        rbPanel.add(rbWall);
        rbPanel.add(rbLight);
        rbPanel.add(rbBall);
        rbPanel.add(rbCap);
        rbPanel.add(rotateButton);
        buttonPanel.add(rbPanel, "North");


        rotatePanel.add(rotateButton);
        buttonPanel.add(rotatePanel, "South");

        objectPanel.setBackground(Color.white);
        objectPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        objectPanel.setMinimumSize(objectPanel.getPreferredSize());
        add(objectPanel, "East");
    }

     /** Listens to the radio buttons. */
    class RadioListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();

            if (action.equals("wall")) {
                objectPanel.setObjectType(ObjectPanel.WALL);
                worldPanel.setObjectType(WorldPanel.WALL);
            }
            else if (action.equals("light")) {
                objectPanel.setObjectType(ObjectPanel.LIGHT);
                worldPanel.setObjectType(WorldPanel.LIGHT);
            }
            else if (action.equals("cap")) {
                objectPanel.setObjectType(ObjectPanel.CAP);
                worldPanel.setObjectType(WorldPanel.CAP);
            }
            else {
                objectPanel.setObjectType(ObjectPanel.BALL);
                worldPanel.setObjectType(WorldPanel.BALL);
            }
       }
    }
}