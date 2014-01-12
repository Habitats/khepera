/**
 * @(#)WorldPanel.java 1.1 2001/01/14
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

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.util.*;

/**
 * The <code>WorldPanel</code> class implements a custom JPanel for drawing
 * world objects and robot animations.
 * This panel also handles mouse events when placing or removing objects for
 * custom world configurations.
 * @author    Steve Perretta
 * @version   1.1 2003/7/29
 */
public class WorldPanel extends JPanel implements MouseListener, MouseMotionListener {
    protected Vector         worldObjects;
    protected Vector         lightObjects;
    protected Vertex         setRobot = null;
    private Vertex         updateRobot = null;
    private Vertex         dragObject = null;
    //private Vertex         heldObject = null;
    private int            heldObjectID = 0;
    private int            drawObj;   // type of object to draw
    private int            previousDrawObj;
    private int            buttonClicked;
    private int            idNum;     // unique id for each world object
    private float          theta;
    private boolean        running = false;
    private boolean        quickDraw = false;
    //private String         displayMode;
    private UpdateRobotImage robotImage = null;
    private Graphics2D     wg2;          // WorldPanel's context
    //private BufferedImage  worldImage;   // image drawn during runs
    //private Graphics2D     wig2;

    private BufferedImage  tempImage;
    private Graphics2D     tempg2;


    public static final int WALL  = 1;
    public static final int CAP   = 2;
    public static final int BALL  = 3;
    public static final int LIGHT = 4;

    public static final int ADD   = 5;
    public static final int REMOVE= 6;
    public static final int MOVE  = 7;
    public static final int NONE  = 8;

    public static final int ROBOT = 9;

    // NEW 7/29/2003 - sp
    private boolean worldChange = false;

    /**
     * Creates new <code>WorldPanel</code> object.
     */
    public WorldPanel() {
        super(true);  // 11/1
        //setOpaque(false);
        initialize();
    }

    /**
     * Initializes class fields.
     */
    protected void initialize() {
	worldObjects = new Vector();
        lightObjects = new Vector();
        drawObj = this.WALL;
        previousDrawObj = this.WALL;
        buttonClicked = this.NONE;
        theta = 0.0f;
        idNum = 5;
        setPreferredSize(new Dimension(500, 500));
        tempImage = new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
        tempg2 = (Graphics2D)tempImage.getGraphics();

        // add listeners
	addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Creates an image of the current world configuration.
     */
    private void createWorldImage() {
        tempg2.setColor(Color.white);
        tempg2.fillRect(0,0,500,500);
        tempg2.setColor(Color.red);
        tempg2.fillRect(10, 10, 8, 480);  // left wall
        tempg2.fillRect(480, 10, 8, 480); // right wall
        tempg2.fillRect(10, 10, 478, 8);  // top wall
        tempg2.fillRect(10, 482, 478, 8); // bottom wall

	Enumeration e = worldObjects.elements();
	while(e.hasMoreElements()) {
	    Vertex v = (Vertex)e.nextElement();
            if(v.objType == WALL || v.objType == LIGHT)
	        v.drawVertex(tempg2);
	}

    }

    /**
     * @see javax.swing.JComponent
     * @see java.awt.Graphics
     */
    public void paint(Graphics g) {
	super.paint(g);  // do the default paint routine first
        //Graphics2D g2 = (Graphics2D)g;
        wg2 = (Graphics2D)g;

        if(!running) {
            tempg2.setColor(Color.white);
            tempg2.fillRect(0,0,500,500);
            tempg2.setColor(Color.red);
            tempg2.fillRect(10, 10, 8, 480);  // left wall
            tempg2.fillRect(480, 10, 8, 480); // right wall
            tempg2.fillRect(10, 10, 478, 8);  // top wall
            tempg2.fillRect(10, 482, 478, 8); // bottom wall

	    Enumeration e = worldObjects.elements();
	    while(e.hasMoreElements()) {
	        Vertex v = (Vertex)e.nextElement();
                if(heldObjectID != v.id)
	            v.drawVertex(tempg2);
	    }
            if(!running && setRobot != null)
                setRobot.drawVertex(tempg2);
            if((dragObject != null) && (buttonClicked == this.ADD ||
                                    buttonClicked == this.ROBOT))
                dragObject.drawVertex(tempg2);
            wg2.drawImage(tempImage,0,0,this);
        }
        else {
        //if(running)
            wg2.drawImage(tempImage,0,0,this);
            Enumeration e = worldObjects.elements();
	    while(e.hasMoreElements()) {
	        Vertex v = (Vertex)e.nextElement();
                if((v.objType == CAP || v.objType == BALL) && heldObjectID != v.id) {
                  v.drawVertex(g);
                }
	    }
            robotImage.updateRobotImage(wg2);
        }
        validate();
    }

    /**
     * @see javax.swing.JComponent
     * @see java.awt.Graphics
     */
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * Change the current robot image object.
     * @param _rUpdate robot image object
     */
    protected void setRobotObject(UpdateRobotImage _rUpdate) {
        robotImage = _rUpdate;
    }

    /**
     * Change the state of the simulation (running or not running).
     * @param state true if robot controller is running, else false
     */
    protected void setRunState(boolean state) {
        running = state;
        if(running) {
            setRobot = null;
            createWorldImage();
        }
        repaint();
    }

    /**
     * Removes all objects from the world.
     */
    protected void clearObjects() {
        //worldObjects = new Vector(); // was
        worldObjects.clear();          // now 7/28/2003 - sp
        lightObjects.clear();	// added 15 OCT 2004 dsb
        repaint();
    }

    /**
    * Normalize value in radians.
    * @param theta of robot's angle in radians
    * @return normalized value of input parameter
    */
    private float normRad(float theta) {
        while(theta > Math.PI)  theta -= (float)(2*Math.PI);
        while(theta < -Math.PI) theta += (float)(2*Math.PI);
        return theta;
    }

    /**
     * Rotates robot by 45 degrees in a clockwise direction.
     */
    protected void rotateRobot() {
        if(setRobot != null) {
            setRobot.theta = normRad(setRobot.theta + (float)(Math.PI/4));
            repaint();
        }
    }

    /**
     * Access <code>Vector</code> containing all current world objects.
     * @return collection of world objects (<code>Vertex</code> objects)
     */
    protected Vector getWorldObjects() {
        return worldObjects;
    }

    // this method should only be called from RoboMain when
    // a world is loaded from a file
    /**
     * Set the Vector containing world objects to the reference passed.
     */
    protected void setWorldObjects(Vector wo) {
        worldObjects = wo;
        //worldObjects.clear();
        // re-set idNum
        int size = worldObjects.size();
        if (size == 0) return;
          Vertex last = (Vertex)worldObjects.get(size-1);
          idNum = last.id+2;
          repaint();
        //writeToBuffer();
    }

    // if heldID == 0, then no object held
    /**
     * Sets the <code>heldObjectID</code> field to the id number of any object
     * held in the gripper.
     * @param heldID id number of a gripped object, or 0 (no object held)
     */
    protected void setHeldId(int heldID) {
        heldObjectID = heldID;
    }


    /**
     * Set what should happen when the mouse is clicked in this panel.
     * Types of "events" are add, remove and move objects. None indicates
     * that no world editing buttons are selected.
     * @param button id number of recently clicked button (id numbers correspond
     * to the named constants ROBOT, ADD, NONE)
     */
    protected void setEventType(int button) {
        if((buttonClicked == this.ROBOT && button == this.ADD) ||
        (buttonClicked == this.NONE && button == this.ADD)) {
            if(drawObj == this.ROBOT)
                drawObj = previousDrawObj;
        }
        buttonClicked = button;
        if(button == this.ROBOT) {
            previousDrawObj = drawObj;
            drawObj = this.ROBOT;
        }
    }

    /**
     * Set the type of object currently selected in the objectPanel.
     * The default object is a BRICK.
     * @param object corresonds to constants WALL, LIGHT, BALL, CAP, ROBOT
     */
    protected void setObjectType(int object) {  // was setObjectType()
        drawObj = object;
    }

    /**
     * Sets angle of an object.
     * @param angle angle in radians
     */
    protected void setRotationAngle(float angle) {
        theta = angle;
    }

    // need to 1) delete from worldNodes collection based on id#
    //         2) incorporate outer walls - store as vertex ?
    /**
     * Deletes any object in the world panel located at the coordinates passed.
     * @param mouseX x coordinate of a mouse click
     * @param mouseY y coordinate of a mouse click
     */
    private void deleteObject(int mouseX, int mouseY) {
        Enumeration e = worldObjects.elements();
        int objX, objY;

	while(e.hasMoreElements()) {
	    Vertex v = (Vertex)e.nextElement();
            objX = v.xPos;
            objY = v.yPos;

            if(drawObj == v.objType) {
                // walls == 8 X 50
                if(drawObj == this.WALL) {
                    if(v.theta == 0.0f) {    // note: change rotation to 90 or 0
                        if(mouseX >= objX && mouseX <= (objX + 7) &&
                           mouseY >= objY && mouseY <= (objY +49)) {
                               worldObjects.removeElement(v);  // should test ret. val.
                               return;
                        }
                    }
                    else {  // v.theta == 270.0
                        if(mouseX >= objX && mouseX <= (objX + 49) &&
                           mouseY <= objY && mouseY >= (objY - 7)) {
                               worldObjects.removeElement(v);
                               return;
                        }
                    }
                } // end wall
                // balls, caps: 15 X 15
                if(drawObj == this.CAP || drawObj == this.BALL) {
                    if(mouseX >= objX && mouseX <= (objX + 10) &&
                       mouseY >= objY && mouseY <= (objY + 10)) {
                           worldObjects.removeElement(v);
                           return;
                    }
                }
                if(drawObj == this.LIGHT) {
                    if(mouseX >= objX && mouseX <= (objX + 15) &&
                       mouseY >= objY && mouseY <= (objY + 15)) {
                           worldObjects.removeElement(v);
                           lightObjects.removeElement(v);
                           return;
                    }
                }
            }
	}  // end while
        repaint();
    }

    /**
     * This method is no longer used, and should be removed.
     * @param p
     * @return
     */
    private String findQuad(Point p) {
        // WorldPanel = 500 X 500
        if(p.x <= 250) {
            if(p.y <= 250)
                return "nw";
            else
                return "sw";
        }
        // for p.x > 250
        else {
            if(p.y <= 250)
                return "ne";
            else
                return "se";
        }
    }

    /**
     * Performs the appropriate action when the mouse is clicked in this panel.
     * @param e
     * @see java.awt.event.MouseListener
     */
    public void mouseClicked(MouseEvent e){
	int x = e.getX();
  	int y = e.getY();

        switch(buttonClicked) {
            case ADD    : Vertex objectNode = new Vertex(drawObj,x,y,theta,idNum);
  	                  worldObjects.add(objectNode);
                          if(drawObj == this.LIGHT)
                              lightObjects.add(objectNode);
                          idNum++;
                          repaint();
                          //writeToBuffer();
                          break;
            case REMOVE : deleteObject(x,y);
                          break;
            case ROBOT  : setRobot = new Vertex(this.ROBOT,x,y,0.0f,-1);
                          repaint();
                          //writeToBuffer();
                          break;
        }
    }

    /**
     * @see java.awt.event.MouseListener
     */
    public void mouseEntered(MouseEvent e){ }

    /**
     * Stop painting any object being dragged around.
     * @see java.awt.event.MouseListener
     */
    public void mouseExited(MouseEvent e){
        dragObject = null;
        repaint();
    }

    /**
     * @see java.awt.event.MouseListener
     */
    public void mousePressed(MouseEvent e){ }

    /**
     * @see java.awt.event.MouseListener
     */
    public void mouseReleased(MouseEvent e){ }

    /**
     * @see java.awt.event.MouseMotionListener
     */
    public void mouseDragged(MouseEvent e) { }

    /**
     * Draw any object being dragged by the mouse at the cursor coordinates.
     * @see java.awt.event.MouseMotionListener
     */
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
  	int y = e.getY();
        //
        if(buttonClicked == this.ADD || buttonClicked == this.ROBOT) {
            if(dragObject == null || dragObject.objType != drawObj) {
                if(drawObj == this.ROBOT)
                    dragObject = new Vertex(drawObj,x,y,0.0f,-1);
                else
                    dragObject = new Vertex(drawObj,x,y,theta,-1);
            }
            else {
                if(drawObj == this.ROBOT)
                    dragObject.setVertexCoordinates(x,y,0.0f);
                else
                    dragObject.setVertexCoordinates(x,y,theta);
            }
            repaint();
            //writeToBuffer();
        }
    }

    // NEW 7/29/2003 - sp
    /**
     * Notifies this class that a coordinate change in one or more objects has
     * occured. This method should be replaced by an event generating/listening
     * scheme.
     * @param change true if a change has occured, false otherwise
     */
    protected void setWorldChange(boolean change) {
      //if(change) System.out.println("change");
      worldChange = change;
    }
    // NEW 7/29/2003 - sp
    /**
     * Access status of any recent changes to object positions.
     * @return true if a change has occured, false otherwise
     */
    protected boolean getWorldChange() {
      return worldChange;
    }

    // NEW 8/2/2003 - sp
    /**
     * Access the current id number for any object in the gripper (held).
     * @return id number of gripped object, 0 if no object is held
     */
    protected int getHeldObjectID() {
      return heldObjectID;
    }

}