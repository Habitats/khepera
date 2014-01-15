/**
 * @(#)KSWriter.java 1.1 2002/11/19
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

import java.io.*;
import java.util.*;

/**
 * A <code>KSWriter</code> class provides the functionality to plug into the
 * local simulator and record the current running controller to a playable file.
 */
public class KSWriter implements Runnable {
  private KSFrame nFrame;
  private FileOutputStream writer = null;
  private ObjectOutputStream out = null;
  private String controller;
  private long delayTime = 200;
  private boolean notWritten = true;
  private boolean record = false;
  private boolean ready = false;
  private int month;
  private int day;
  private int year;
  private String current;
  private Thread thread;
  private int writtenObjects = 0;

  /* New 7/29/2003 - sp
   * -- these variables have been introduced to allow the recording of
   * dynamic changes in the environment.
   */
  private boolean worldChange = false;
  private KSReadWriteState state = null;
  private KSReadWriteHeader header = null;
  // end new

  /**
   * Allocate a new <code>KSWriter</code> object.
   */
  public KSWriter() {
  }

  /**
   * Initialize this <code>KSWriter</code> object to write at the specified
   * speed.
   * @param frame the local simulator
   * @param the delay between frames of data written to file
   */
  protected void initialize(KSFrame frame, int delay) {
    nFrame = frame;
    delayTime = delay;
    record = true;
    ready = true;

  }

  /**
   * Provide the writer state.
   * @return <tt>true</tt> if this writer is ready, <tt>false</tt> otherwise
   */
  protected boolean isReady() {
    return ready;
  }

  /**
   * Start recording data from the local simulator to file.
   */
  protected void startRecording() {
    String name = null;
    Calendar nDate = Calendar.getInstance();
    day = nDate.get(Calendar.DATE);
    month = nDate.get(Calendar.MONTH);
    year = nDate.get(Calendar.YEAR);
    int hour = nDate.get(Calendar.HOUR);
    int min = nDate.get(Calendar.MINUTE);
    int sec = nDate.get(Calendar.SECOND);
    month++;
    ArrayList control = nFrame.rcd.runningControllers();
    current = (String)control.get(0);
    name = current + " " + month + "-" + day + "-" + year + " " + hour + "-" + min + "-" + sec + ".txt";
    //StatusBar.setRightStatus("Writing: " + Name);
    try{
      writer = new FileOutputStream(name);
      } catch (Exception e){}
      try{
        out = new ObjectOutputStream(writer);
        } catch (Exception A){}
        try {

          } catch (Exception badWrite){}
          thread = new Thread(this, "KSWriter");
          thread.start();
  }

  protected void setRecord(boolean Rec) {
    record = Rec;
  }

  /**
   * Return the current recording status of this writer object.
   * @return <tt>true</tt> if recording is on, <tt>false</tt> otherwise
   */
  protected boolean isRecording() {
    return record;
  }

  /**
   * @see java.lang.Runnable
   */
  @Override
public void run() {
    while(record) {
      if(notWritten) {
        try{
          // NEW 8/2/2003 - sp
          header = new KSReadWriteHeader();
          header.date = "## " + month + "-" + day + "-" + year;
          header.controller = "## The current Controller is :" + current;
          header.delay = "## Current Delay is :" + delayTime;
          header.objects = nFrame.worldPanel.worldObjects;
          Vector temp2 = header.objects;
          out.writeObject(header);
          out.reset();

          /* The following is the original header writing routine **
          String date = "## " + month + "-" + day + "-" + year;
          String controller = "## The current Controller is :" + current;
          String writeDelay = "## Current Delay is :" + delayTime;
          out.writeObject(date);
          out.writeObject(controller);
          out.writeObject(writeDelay);
          out.writeObject(nFrame.worldPanel.worldObjects);
          */



          out.flush();
          notWritten = false;
          } catch (Exception FILEIO){}
      }
      float[] data = new float[23];
      for (int i = 0; i < 8; i++) {
        data[i] = nFrame.drawManager.rState.sensors[i].getDistValue();
        data[i+8] = nFrame.drawManager.rState.sensors[i].getLightValue();
      }
      data[16] = nFrame.drawManager.rState.getRobotCoordinates().x;
      data[17] = nFrame.drawManager.rState.getRobotCoordinates().y;
      data[18] = nFrame.drawManager.rState.getRobotCoordinates().alpha;
      data[19] = nFrame.drawManager.rState.getRobotCoordinates().dx;
      data[20] = nFrame.drawManager.rState.getRobotCoordinates().dy;
      data[21] = nFrame.drawManager.rState.getGripperState();
      data[22] = nFrame.drawManager.rState.getArmState();

      // NEW 7/29/2003 - SP
      worldChange = nFrame.worldPanel.getWorldChange();
      nFrame.worldPanel.setWorldChange(false);
      state = new KSReadWriteState();
      state.data = data;
      state.worldChange = worldChange;
      if(worldChange) {
        state.worldObjects = nFrame.worldPanel.worldObjects;
        state.heldID = nFrame.worldPanel.getHeldObjectID();
        try {
          out.reset();
        }
        catch(IOException ioe) {}
      }
      // END NEW

      try{
        // NEW 7/29/2003 - SP
        out.writeObject(state);
        // END NEW
        writtenObjects++;
        out.flush();
        Thread.sleep(delayTime);
        } catch (Exception a){ System.out.println("error 1");}
    }
    try{
      //System.out.println("About to write eof\n");
      out.writeObject("EOF");
      out.flush();
      } catch (Exception e){}
  }

} // KSWriter