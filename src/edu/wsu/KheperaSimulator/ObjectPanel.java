/**
 * @(#)ObjectPanel.java 1.1 2001/06/19
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

/**
 *  The ObjectPanel defines a panel used to display the set of objects
 *  that can be added to the world. Like the original simulator, these
 *  objects (ie., ball, cap, wall, light) can be rotated prior to
 *  placement.
 *
 * @author  Steve Perretta
 */
public class ObjectPanel extends JPanel {
    private int    angle;
    private float  theta;
    private int    rotDirection;
    private int    objectType;

    public static final int WALL  = 1;
    public static final int CAP   = 2;
    public static final int LIGHT = 3;
    public static final int BALL  = 4;
    public static final int CW  = 5;
    public static final int CCW = 6;

    /**
     * Allocate a new <code>ObjectPanel</code>.
     */
    public ObjectPanel() {
        super();
        initialize();
    }

    /**
     * @see javax.swing.JComponent
     * @see java.awt.Graphics
     */
    public void paint(Graphics g) {
	super.paint(g); // do the default paint routine first
	//super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.rotate(theta, 30.0, 30.0);
        switch (objectType) {
            case 1: g2.setColor(Color.red);
                    g2.fillRect(26, 5, 8, 50);
                    break;
            case 2: g2.setColor(Color.blue);
                    g2.fillArc(22,22,16,16,0,360);
                    g2.setColor(Color.lightGray);
                    g2.fillArc(25,25,10,10,0,360);
                    break;
            case 3: g2.setColor(Color.orange);
                    g2.drawLine(14,14, 46,46);
                    g2.drawLine(14,46, 46,14);
                    g2.drawLine(30,14, 30,46);
                    g2.drawLine(14,30, 46,30);
                    g2.fillArc(22,22,16,16,0,360);
                    g2.setColor(Color.yellow);
                    g2.fillArc(25,25,10,10,0,360);
                    break;
            case 4: g2.setColor(new Color(42,186,52));
                    g2.fillArc(22,22,16,16,0,360);
                    g2.setColor(new Color(63,219,32));
                    g2.fillArc(22,22,13,13,0,360);
                    g2.setColor(Color.green);
                    g2.fillArc(23,23,9,9,0,360);
                    break;
        }
    }

    /**
     * Initialize this <code>ObjectPanel</code>.
     */
    private void initialize() {
        this.setPreferredSize(new Dimension(60,60));
        angle = 0;
        theta = (float)Math.toRadians((double)angle);
        rotDirection = this.CW;
        objectType = this.WALL;
    }

    /**
     * Set the current object type.
     * @param type the current type of object selected.
     */
    protected void setObjectType(int type) {
        objectType = type;
        repaint();
    }

    /**
     * Set the rotation direction.  This value can be either 'CW' for
     * clockwise or 'CCW' for counter-clockwise.
     * @param dir the direction
     */
    protected void setRotateDirection(int dir) {
        rotDirection = dir;
    }

    /**
     * Get the current rotation angle.  This angle is used to rotate objects.
     * @return the rotation angle
     */
    protected float getRotationAngle() {
        return theta;
    }

    /**
     * Rotate the selected object.
     */
    protected void rotateObject() {
        if (angle == 0)
          angle = 270;
        else
          angle = 0;
        theta = (float)Math.toRadians((double)angle);
        repaint();
    }
} // ObjectPanel