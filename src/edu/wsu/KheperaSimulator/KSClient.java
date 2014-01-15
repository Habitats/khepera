/**
 * @(#)KSClient.java 1.1 2002/10/30
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
import java.net.*;
import java.util.*;
import javax.swing.JOptionPane;

/**
 * A <code>KSClient</code> provides the functionality to connect to a server
 * simulator and playback the currently running controller on that server.  It
 * is important to note that the client simulator is not running a controller of
 * its own, rather displaying what the server controller is doing.
 *
 * @author  Brian Potchik
 * @version 1.1, 2002/10/30
 */
public class KSClient implements Runnable{

  /**
   * The socket this <code>KSClient</code> will use for communication
   * with the server.
   */
  private Socket clientSocket = null;

  /**
   * The <code>ObjectOutputStream</code> for which this client will send objects.
   */
  private ObjectOutputStream out = null;

  /**
   * The <code>ObjectInputStream</code> for which this worker will receive objects.
   */
  private ObjectInputStream in = null;

  /**
   * The host that this client is connected.
   */
  private String host;

  /**
   * The port that the server is listening.
   */
  private int port;

  /**
   * Indicates that this client is connected and currently may or may not be
   * sending or receiving data.
   */
  private boolean isStarted = false;

  /**
   * A reference to the local simulator.  This allows this client access
   * to the simulator internals in order to set and playback a simulation.
   */
  private KSFrame frame;

  /**
   * The name of the controller currently running on the remote simulator.
   */
  private String controllerName;

  /**
   * Allocate a new, not-connected <code>KSClient</code>.
   */
  public KSClient() {}

  /**
   * Initializes, connects this <code>KSClient</code>, and starts it on a
   * separate thread.
   * @param frame a reference to the local simulator
   * @param host the host name of a server
   * @param port the port number that the server is listening
   */
  protected void initialize(KSFrame frame, String host, int port) {
    this.frame = frame;
    this.host = host;
    this.port = port;
    new Thread(this, "KSClient").start();
  } // constructor

  /**
   * Indicates that this client is connected and currently may or may not be
   * sending or receiving data.
   * @return <tt>true</tt> if this client is started and connected to a server.
   */
  protected boolean isStarted() {
    return isStarted;
  }

  /**
   * Terminate communication with the server.  There is no graceful way to tell
   * the server this client is done.  However the server is robust enough to
   * recover and release any resources.
   */
  protected void kill() {
    frame.drawManager.stopPlayback(); // tell local simulator to stop playback
    close();
  }

  /**
   * @see java.lang.Runnable
   */
  @Override
public void run() {
    try {
      // set up connection
      clientSocket = new Socket(host, port);
      out = new ObjectOutputStream(clientSocket.getOutputStream());
      out.flush();
      in = new ObjectInputStream(clientSocket.getInputStream());
      setStarted(true);
    } catch (UnknownHostException e) {
      JOptionPane.showMessageDialog(null,
                                    "Unknown Host: " + host,
                                    "Unknown Host",
                                    JOptionPane.ERROR_MESSAGE);
      close();
      return;
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null,
                                    "IOException: " + host,
                                    "IOException",
                                    JOptionPane.ERROR_MESSAGE);
      close();
      return;
    }

    try {
      String inData, outData;
      float[] data;

      // verify the connection
      // get server status
      inData = (String)in.readObject();
      if (!inData.startsWith("220")) {
        throw new ProtocolException(host);
      }

      // respond
      outData = "HELO " + clientSocket.getLocalAddress().getHostName();
      out.writeObject(outData);
      out.flush();

      // get server response
      inData = (String)in.readObject();
      if (!inData.startsWith("250")) {
        throw new ProtocolException(host);
      }
      // connection verified

      // start to recieve headers
      while (!(inData = (String)in.readObject()).equals("EOT")) {
        if (inData.startsWith("320")) {
          controllerName = (String)in.readObject();
          StatusBar.setRightStatus("Controller: " + controllerName);
        }

        else if (inData.startsWith("360")) {
          frame.worldPanel.setWorldObjects((Vector)in.readObject());
          // NEW 8/3/03 - sp
          frame.worldPanel.setHeldId(in.readInt());
          // END NEW
          frame.drawManager.startPlayback(); // prepare local sim for playback
        }

        else if (inData.startsWith("400")) {
          data = (float[])in.readObject();
          for (int i = 0; i < 8; i++ ) {
            frame.drawManager.rState.sensors[i].setDistValue((int)data[i]);
            frame.drawManager.rState.sensors[i].setLightValue((int)data[i+8]);
          }
          frame.drawManager.rState.getRobotCoordinates().setCoordinates((int)data[16],
                                                                        (int)data[17],
                                                                        data[18],
                                                                        data[19],
                                                                        data[20]);
          frame.drawManager.rState.setGripperState((int)data[21]);
          frame.drawManager.rState.setArmState((int)data[22]);
        }
      } // while

      // received EOT, time to quit
      out.writeObject("QUIT");
      out.flush();

      // kill this client
      kill();
    } // try

    catch (Exception e) {
      kill();
    }
  } // run

  /**
   * Set the status of this client and update the visual status on the status
   * bar.
   * @param isStarted indicate if this client is started or not.
   */
  private void setStarted(boolean isStarted) {
    this.isStarted = isStarted;
    if (isStarted) {
      StatusBar.setLeftStatus("Client connected to: " +
                              clientSocket.getInetAddress().getHostName());
    }
    else {
      StatusBar.setLeftStatus("Client/Server Status: Disabled");
      StatusBar.setRightStatus("Idle");
    }
  } // setStarted

  /**
   * Close the socket and the streams associated with this <code>KSClient</code>
   * objects socket.
   */
  private void close() {
    try {
      setStarted(false);
      out.close();
      in.close();
      clientSocket.close();
      } catch (Exception e) { /* and do what */ }
  } // close
} // KSClient