/**
 * @(#)WorldDrawManager.java 1.1 2002/10/19
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

import java.awt.event.*;
import java.util.*;

/**
 * A <code>WorldDrawManager</code> class provides the main thread to run the
 * simulator engine. This thread manages drawing routines, controller threads,
 * various book keeping and shared data.
 */
public class WorldDrawManager implements Runnable {

  private Thread thread = null;
  private Vector stuckObjects;
  private DrawableRobot robotUpdate = null;
  protected CurrentRobotState rState;
  private UpdateSensors readSensors = null;
  private UpdateRobotImage robotImage;
  protected int[][] wImage;
  private boolean active = false;
  private boolean holding = false;
  private Collision contact;
  private int distLevel, lightLevel, speedLevel;
  private int rx,ry;
  private javax.swing.Timer paintTimer;
  private ActionListener paintListener;
  private boolean streamFromClient = false;
  private KSFrame frame = null;

  // NEW 7/30/2003 - sp
  private boolean playbackState = false;

  /**
   * Allocate a new <code>WorldDrawManager</code>.
   * @param parent a reference to the parent container
   */
  public WorldDrawManager(KSFrame parent) {
    this.frame = parent;
    wImage = new int[500][500];
    rState = new CurrentRobotState();
    robotUpdate = new DrawableRobot(rState);
    robotImage = new UpdateRobotImage(rState);
    readSensors = new UpdateSensors(wImage, rState, frame.sLabels, frame.worldPanel);
    readSensors.setDistParam(distLevel);
    readSensors.setLightParam(lightLevel);
    frame.worldPanel.setRobotObject(robotImage);

    // Set-up world drawing routine
    ActionListener paintListener = new ActionListener (){
      @Override
	public void actionPerformed (ActionEvent e){
        int x = rState.currentPos.x;
        int y = rState.currentPos.y;
        //frame.worldPanel.repaint(x-40,y-40,x+40,y+40);  original
        // NEW 7/30/2003 - sp
        if(x < 50 || y < 50)  // fix drawing corruption
          frame.worldPanel.repaint(x-80,y-80,x+80,y+80);
        else
          frame.worldPanel.repaint(x-40,y-40,x+40,y+40); // original
        // END NEW
    }};
    paintTimer = new javax.swing.Timer(40, paintListener); // was 100
    paintTimer.setRepeats(true);
  } // WorldDrawManager

  /**
   * Access the value for the current sensor label display mode for GUI sensor
   * value updates. Legal display modes are "distance", "light", and "disable".
   * @return current display mode
   */
  protected String getDisplayMode() {
    return readSensors.getDisplayMode();
  }

  /**
   * Change the value for the current sensor label display mode for GUI sensor
   * value updates.
   * @param mode display mode ("distance", "light", or "disable")
   */
  protected void setDisplayMode(String mode) {
    readSensors.setDisplayMode(mode);
  }

  /**
   * Sets all values in the 500 x 500 world image matrix to zeros.
   */
  protected void clearWorldImage() {
    for(int i = 0; i < 500; i++)
      for(int j = 0; j < 500; j++)
        wImage[i][j] = 0;
  }

  /**
   * Sets values in the 500 x 500 world image matrix to appropriate values based
   * on the presence and position of user placed objects (and the static outer
   * walls). The matrix is simply another representation of the robot arena that
   * uses object id numbers at pixel locations where objects are present.
   */
  protected void createWorldImage() {
    int i,j,x,y;
    Vector wObj = frame.worldPanel.worldObjects;
    Vertex robObj = frame.worldPanel.setRobot;

    /* Init outer walls */
    // top section
    for(i = 0;i < 19;i++)
      for(j = 0;j < 500;j++)
        wImage[i][j] = 1;
    // bottom section
    for(i = 484;i < 500;i++)   // i = 480
      for(j = 0;j < 500;j++)
        wImage[i][j] = 1;
    // left section
    for(i = 18;i < 485;i++)   // i = 19
      for(j = 0;j < 19;j++)
        wImage[i][j] = 1;
    // right section
    for(i = 19;i < 480;i++)
      for(j = 482;j < 500;j++)
        wImage[i][j] = 1;
    //this algorithm with the objects can be used for the movable objects
    Enumeration e = wObj.elements();
    while(e.hasMoreElements()) {
      Vertex v = (Vertex)e.nextElement();
      x = v.xPos;
      y = v.yPos;

      if(v.objType == WorldPanel.WALL) {
        if(v.theta == 0.0f) {
          for(i = y;i < (y+50);i++) {
            for(j= x;j < (x+8);j++) {
              if(i < 0 || i > 499) break;
              if(j < 0 || j > 499) break;
              wImage[i][j] = 1;
            }
          }
        }
        // 90 degrees
        else {
          for(i = (y-8);i < y;i++) {
            for(j= x;j < (x+50);j++) {
              if(i < 0 || i > 499) break;
              if(j < 0 || j > 499) break;
              wImage[i][j] = 1;
            }
          }
        }
      }
      // change this to trim edges
      else if(v.objType == WorldPanel.LIGHT) {
        for(i = y;i < (y+16);i++) {
          for(j = x;j < (x+16);j++) {
            if(i < 0 || i > 499) break;
            if(j < 0 || j > 499) break;
            wImage[i][j] = 2;
          }
        }
      }

      // Moveable objects
      else if(v.objType == WorldPanel.CAP) {
        for(i = y;i < (y+9);i++) {
          for(j = x;j < (x+9);j++) {
            if(i < 0 || i > 499) break;
            if(j < 0 || j > 499) break;
            wImage[i][j] = v.id;
          }
        }
      }
      // Light
      else if(v.objType == WorldPanel.BALL) {
        for(i = y;i < (y+9);i++) {
          for(j = x;j < (x+9);j++) {
            if(i < 0 || i > 499) break;
            if(j < 0 || j > 499) break;
            wImage[i][j] = v.id;
          }
        }
      }
    }
    // robot placed with set button
    if(robObj != null) {
      rState.setRobotCoordinates(robObj.xPos-3,robObj.yPos-3,robObj.theta);
    }
  } // createWorldImage

  /**
   * Resets appropriate data before starting the <code>WorldDrawManager</code>
   * thread.
   */
  protected void reInitialize() {
    rState.reInitialize();
    readSensors.reInitialize();
  }

  /**
   * Starts routine used when user plays a previously recorded file.
   */
  protected void startPlayback() {
    frame.worldPanel.setRunState(true);
    // NEW 7/30/2003 - sp
    playbackState = true;
    paintTimer.start();
  }

  /**
   * Stops playing a previously recorded file.
   */
  protected void stopPlayback() {
    paintTimer.stop();
    frame.worldPanel.setRunState(false);
    // NEW 7/30/2003 - sp
    playbackState = false;
    // frame.worldPanel.clearObjects(); // why?? -sp
    // uncommented 7/28/2003 - sp - fixes object detection on 2nd+ runs
    //frame.worldPanel.clearObjects(); 8/6/03 -sp - fixed  above and commented out again
    clearWorldImage();
    reInitialize();
  }

  /**
   * Starts the main simulation thread, which runs the robot based on the currently
   * loaded controller and generated sensor values.
   */
  protected void startSimulator() {
    active = true;
    // NEW 7/30/2003 - sp attempting to fix obj prob w/ loaded maps
    readSensors.reInitialize();
    readSensors.setDistParam(distLevel);
    readSensors.setLightParam(lightLevel);

    stuckObjects = readSensors.getStuckObjects(); // ?????
    contact = new Collision(rState, wImage, stuckObjects); // object creation bad

    frame.armPanel.setCurrentState(rState);
    frame.gripPanel.setCurrentState(rState);
    frame.objPresPanel.setCurrentState(rState);
    createWorldImage(); // create world image and get robots position
    robotUpdate.reInitialize(); // set robots position
    robotUpdate.setSpeedParam(speedLevel);
    startPlayback();

    // create new thread and start sim
    thread = new Thread(this, "WorldDrawManager");
    thread.setPriority(thread.getPriority() - 2);
    thread.start();
  } // startRobot

  /**
   * Terminates the main simulation thread, halting the controller execution.
   */
  protected void stopSimulator() {
    active = false;
    stopPlayback();
  } // stopRobot


  /**
   * Sets the varaiable parameter associated with distance sensor sensitivity
   * to the value passed.
   * @param level new distance sensor sensitivity level
   */
  protected void setDistLevel(int level) {
    distLevel = level;
    if(readSensors != null)
      readSensors.setDistParam(distLevel);
  }

  /**
   * Sets the varaiable parameter associated with light sensor sensitivity
   * to the value passed.
   * @param level new light sensor sensitivity level
   */
  protected void setLightLevel(int level) {
    lightLevel = level;
    if(readSensors != null)
      readSensors.setLightParam(lightLevel);
  }

  /**
   * Sets the varaiable parameter associated with calculating motor speeds
   * to the value passed.
   * @param level new speed adjustment level
   */
  protected void setSpeedLevel(int level) {
    speedLevel = level;
    if(robotUpdate != null)
      robotUpdate.setSpeedParam(speedLevel);
  }

  /**
   * Sets distance, light and speed adjustment parameters to those passed.
   * @param dl new distance sensor sensitivity level
   * @param ll new light sensor sensitivity level
   * @param sl new speed adjustment level
   */
  protected void setAllLevels(int dl, int ll, int sl) {
    distLevel = dl;
    lightLevel = ll;
    speedLevel = sl;
  }

  /**
   * This method does not appear to be used any more. Probably should be removed.
   */
  private void getCurrentRobotClip() {
    rx = rState.currentPos.x - 10;
    ry = rState.currentPos.y - 10;
  }

  /**
   * @see java.lang.Runnable
   */
  @Override
public void run() {
    int hit   = 0;
    while (active) {
      if( !active ) break;
      hit = contact.testCollision(); // hmmm still works when this is gone
      if( !active ) break;
      robotUpdate.updateCoordinates(hit);
      if( !active ) break;
      readSensors.processSensors();
      frame.armPanel.updateImage();
      frame.gripPanel.updateImage();
      frame.objPresPanel.updateImage();

      // essential for effeciency
      try {
        Thread.sleep(5);
        } catch (Exception e) {}
    }
  } // run
} // WorldDrawManager