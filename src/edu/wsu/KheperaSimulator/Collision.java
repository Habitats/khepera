/**
 * @(#)Collision.java 1.1 2001/07/12
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

import java.util.*;

/**
 * The <code>Collision</code> class provides methods to check for a collision
 * between the robot and an object in the simulated environment.  A collision
 * can occur on the front of the robot or on the rear.  This class provides a
 * way to determine which type of collision occurred.
 *
 * @author  Steve Perretta
 */
public class Collision {

  private Vector stuckObjects;
  private CurrentRobotState rState;
  private int[][] wImage;
  private Sensor[] sensors;
  private RobotCoordinates currentPos;
  private MyPoint front;
  private int[] objectIDs;

  /**
   * Signifies a status of no collision.
   */
  protected static int NO_HIT    = 0;

  /**
   * Signifies a collision between the front of the robot and some object.
   */
  protected static int FRONT_HIT = 1;

  /**
   * Signifies a collision between the rear of the robot and some object.
   */
  protected static int BACK_HIT  = 2;

  /**
   * Allocates a new <code>Collision</code> object that will detect collisions
   * in the current simulator environment.
   * @param rs a reference to the current state of the robot
   * @param wi the simulator environment matrix
   * @param stuck objects that exist in the simulator environment
   */
  public Collision(CurrentRobotState rs, int[][] wi, Vector stuck) {
    stuckObjects = stuck;
    rState = rs;
    wImage = wi;
    objectIDs = new int[10];
    sensors = new Sensor[8];
    initSensors();
    currentPos  = rState.getRobotCoordinates();
    front = new MyPoint(9, 0); // was 12, 0
  }

  /**
   * Initialize the <code>Sensor</code> objects of the robot.
   */
  private void initSensors() {
    sensors[0] = new Sensor(3.5f, 9.0f, (float)(Math.PI/2.0));
    sensors[1] = new Sensor(6.5f, 7.5f, (float)(Math.PI/4.0));
    sensors[2] = new Sensor(9.0f, 4.0f, 0.0f); // y was 4.0
    sensors[3] = new Sensor(9.0f, -4.0f, 0.0f); // y was -4.0
    sensors[4] = new Sensor(6.5f, -7.5f, (float)(-Math.PI/4.0));
    sensors[5] = new Sensor(3.5f, -9.0f, (float)(-Math.PI/2.0));
    sensors[6] = new Sensor(-9.5f, -5.5f, (float)Math.PI); //....
    sensors[7] = new Sensor(-9.5f, 5.5f, (float)Math.PI); //....
  }

  /**
   * Test for a collision between the current state of the robot and the
   * simulator environment.
   * @return an integer signifying a collision if one occurred and which type
   */
  protected int testCollision() {
    int i, j, xc, yc;
    float ra, rx, ry, sina, cosa;
    int len = 0;
    boolean testObjects = false;
    int grip, arm;

    rx = currentPos.dx + 13.0f;
    ry = currentPos.dy + 13.0f;
    ra = currentPos.alpha;
    sina = (float)Math.sin(ra);
    cosa = (float)Math.cos(ra);
    xc = (int)(rx + front.y*sina + front.x*cosa);
    yc = (int)(ry - front.y*cosa + front.x*sina);

    arm = rState.getArmState();
    grip = rState.getGripperState();

    if(!stuckObjects.isEmpty()) {
      testObjects = true;
      len = stuckObjects.size();
      objectIDs = new int[len];
      Enumeration e = stuckObjects.elements();
      i = 0;
      while(e.hasMoreElements()) {
        Vertex v = (Vertex)e.nextElement();
        objectIDs[i] = v.id;
        i++;
      }
    }

    if(wImage[yc][xc] == 1 || wImage[yc][xc] == 2)
      return FRONT_HIT;
    if(testObjects) {
      int tmp = wImage[yc][xc];
      for(i = 0;i < len;i++) {
        if(tmp == objectIDs[i]) {
          return FRONT_HIT;
        }
      }
    }
    if(arm == KSGripperStates.ARM_DOWN) {
      if(grip == KSGripperStates.GRIP_OPEN) {
        int armTip1X = (int)(rx + 10.0f*sina + 20.0f*cosa); // 24
        int armTip1Y = (int)(ry - 10.0f*cosa + 20.0f*sina); // "
        if(wImage[armTip1Y][armTip1X] == 1 ||
           wImage[armTip1Y][armTip1X] == 2)
          return FRONT_HIT;
        if(testObjects) {
          int tmp = wImage[armTip1Y][armTip1X];
          for(i = 0;i < len;i++) {
            if(tmp == objectIDs[i])
              return FRONT_HIT;
          }
        }
        int armTip2X = (int)(rx + -10.0f*sina + 20.0f*cosa); // 24
        int armTip2Y = (int)(ry - -10.0f*cosa + 20.0f*sina); // "
        if(wImage[armTip2Y][armTip2X] == 1 ||
           wImage[armTip2Y][armTip2X] == 2)
          return FRONT_HIT;
        if(testObjects) {
          int tmp = wImage[armTip2Y][armTip2X];
          for(i = 0;i < len;i++) {
            if(tmp == objectIDs[i])
              return FRONT_HIT;
          }
        }
      }
      else  {// grip closed
        int armTip1X = (int)(rx + 0.0f*sina + 20.0f*cosa); // 24
        int armTip1Y = (int)(ry - 0.0f*cosa + 20.0f*sina); // "
        if(wImage[armTip1Y][armTip1X] == 1 ||
           wImage[armTip1Y][armTip1X] == 2)
          return FRONT_HIT;
        if(testObjects) {
          int tmp = wImage[armTip1Y][armTip1X];
          for(i = 0;i < len;i++) {
            if(tmp == objectIDs[i])
              return FRONT_HIT;
          }
        }
      }
    }
    else {
      for(i = 1;i < 5;i++) {
        xc = (int)(rx + sensors[i].y*sina + sensors[i].x*cosa);
        yc = (int)(ry - sensors[i].y*cosa + sensors[i].x*sina);
        if(wImage[yc][xc] == 1 || wImage[yc][xc] == 2)
          return FRONT_HIT;
        if(testObjects) {
          int tmp = wImage[yc][xc];
          for(int k = 0;k < len;k++) {
            if(tmp == objectIDs[k])
              return FRONT_HIT;
          }
        }
      }
    }
    for(i = 6;i < 8;i++) {
      xc = (int)(rx + sensors[i].y*sina + sensors[i].x*cosa);
      yc = (int)(ry - sensors[i].y*cosa + sensors[i].x*sina);
      if(wImage[yc][xc] == 1 || wImage[yc][xc] == 2)
        return BACK_HIT;
      if(testObjects) {
        int tmp = wImage[yc][xc];
        for(int k = 0;k < len;k++) {
          if(tmp == objectIDs[k])
            return BACK_HIT;
        }
      }
    }
    return NO_HIT;
  } // testCollision
} // Collision