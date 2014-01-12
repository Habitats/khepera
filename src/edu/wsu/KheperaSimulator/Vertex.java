/**
 * @(#)Vertex.java 1.1 2001/06/11
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
import java.awt.geom.*;
import java.io.*;

/**
 *  This class defines a drawable object. The basic members and
 *  operations could be used to define class(es) for world objects.
 *  A more extensive definition could be used for a robot.
 * @author Steve Perretta
 */
public class Vertex implements Serializable {
    protected int    xPos;
    protected int    yPos;
    protected int    oldx;
    protected int    oldy;
    protected float  theta;
    private   Color  vertexColor;
    protected int    objType;
    protected int    id;
    private   Image  robotI;


    /**
     * Creates a new <code>Vertex</code> object.
     * @param type world object type
     * @param _id world object id number
     */
    public Vertex(int type, int _id) {  this(type, 250, 250, 0.0f, _id);  }

    /**
     * Creates a new <code>Vertex</code> object.
     * @param type world object type
     * @param x object's x coordinate
     * @param y object's y coordinate
     * @param _id world object id number
     */
    public Vertex(int type, int x, int y, int _id) { this(type, x, y, 0.0f, _id); }

    /**
     * Creates a new <code>Vertex</code> object.
     * @param type world object type
     * @param x object's x coordinate
     * @param y object's y coordinate
     * @param a object's angle (radians)
     * @param _id world object id number
     */
    public Vertex(int type, int x, int y, float a, int _id) {
        xPos = x;
        oldx = xPos;
        yPos = y;
        oldy = yPos;
        theta = a;
        objType = type;
        id = _id;
        if(objType == WorldPanel.WALL) vertexColor = Color.red;
        else if(objType == WorldPanel.BALL) vertexColor = Color.green;
        else if(objType == WorldPanel.CAP) vertexColor = Color.blue;
        else if(objType == WorldPanel.LIGHT) vertexColor = Color.orange;
        else vertexColor = Color.black;
        if(objType == WorldPanel.ROBOT)
            robotI = Toolkit.getDefaultToolkit().getImage("images/robot_au.gif");
    }

    /**
     * Set the objects color
     * @param c see java.awt.Color
     */
    public void setVertexColor(Color c) {
        vertexColor = c;
    }

    /**
     * Sets the object's coordinate and orientation values.
     * @param x object's x coordinate
     * @param y object's y coordinate
     * @param angle object's angle (radians)
     */
    public void setVertexCoordinates(int x, int y, float angle) {
        oldx = xPos;
        xPos = x;
        oldy = yPos;
        yPos = y;
        theta = angle;
    }

    /**
     * Access object's type
     * @return number associated with a world object type
     */
    public int getType() {
        return objType;
    }

    /**
     * Access object's id number
     * @return object's id number
     */
    public int getIdNum() {
        return id;
    }

    /**
     * Request that object draw itself in a given graphics context.
     * @param g see java.awt.Graphics
     */
    public void drawVertex(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int rcX, rcY;

        g2.setColor(vertexColor);
        switch(objType) {
            case WorldPanel.WALL: g2.rotate(theta, (double)xPos, (double)yPos);
                                  g2.fillRect(xPos,yPos,8,50);
                                  g2.setTransform(new AffineTransform());
                                  break;

            case WorldPanel.BALL: g2.fillArc(xPos,yPos,8,8,0,360);
                                  break;

            case WorldPanel.LIGHT:g2.fillArc(xPos,yPos,15,15,0,360);
                                  break;

            case WorldPanel.CAP:  g2.fillArc(xPos,yPos,8,8,0,360);
                                  break;
            case WorldPanel.ROBOT:rcX = xPos+10;
                                  rcY = yPos+10;
                                  g2.rotate(theta, (double)rcX, (double)rcY);
                                  g2.drawImage(robotI,xPos-3,yPos-3,null);
                                  g2.setTransform(new AffineTransform());
                                  /*
            case WorldPanel.ROBOT:rcX = xPos+10;
                                  rcY = yPos+10;
                                  g2.rotate(theta, (double)rcX, (double)rcY);
                                  g2.setColor(Color.green);
                                  g2.fillArc(xPos,yPos,20,20,0,360); // body
                                  g2.setColor(Color.black);
                                  g2.drawArc(xPos,yPos,20,20,0,360);
                                  g2.drawLine(xPos,yPos+10,xPos+20,yPos+10);//center
                                  g2.fillArc(xPos+15,yPos+8,5,5,0,360);  // nose
                                  g2.setTransform(new AffineTransform());
                                  //case WorldPanel.ROBOT: robot = new javax.swi
                                  //ng.ImageIcon("roboIcon.gif").getImage();
                                   */
        }
    }

}

