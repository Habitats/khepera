/**
 * @(#)KSServerThread.java 1.1 2002/11/01
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
import java.util.Vector;

/**
 * A <code>KSServerWorker</code> provides the functionality to service a client.
 * This worker conforms to the protocol agreed to by this worker and the client.
 * This worker handles verification of the connection as well as streaming
 * the server data needed by the client for playback.
 *
 * @author  Brian Potchik
 * @version 1.1, 2002/10/30
 */
public class KSServerWorker extends Thread {

  /**
   * The socket this <code>KSServerThread</code> will use for communication
   * with the client.
   */
  private Socket serviceSocket = null;

  /**
   * The <code>ObjectOutputStream</code> for which this worker will send objects.
   */
  private ObjectOutputStream out = null;

  /**
   * The <code>ObjectInputStream</code> for which this worker will receive objects.
   */
  private ObjectInputStream in = null;

  /**
   * <code>String</code> objects that contain a status or protocol code which
   * will be sent or received.
   */
  private String inData, outData;

  /**
   * Internal indication to the current status of the connection.
   */
  private boolean connectionVerified = false;

  /**
   * Internal indication that tells this worker to transmit.
   */
  private boolean transmit = false;

  /**
   * Indicates if this worker should terminate the connection and quit,
   * releasing all resources.
   */
  private boolean terminate = false;

  /**
   * Indicates to this worker to transmit the controller name and world map.
   */
  private boolean reTransmit = true;

  /**
   * Indicates to this worker to transmit the world map.  For now this includes
   * static and moveable objects due to the current design of the simulator.
   */
  private boolean sendMap = false;

  /**
   * Amount of time in milliseconds to wait before transmitting the next set of
   * simulator data.
   */
  private long TX_INTERVAL = 100; // default is 100 ms

  /**
   * A reference to the local simulator.  This allows this worker access
   * to the simulator internals in order to transmit the required data.
   */
  private KSFrame frame;

  /**
   * The name of the controller currently running on the local simulator.
   */
  private String controllerName;

  /**
   * A reference to the local server in case this worker dies prematurely.
   * This allows the server to keep an updated pool of connected clients.
   */
  private KSServer server;

  /**
   * This workers unique ID.
   */
  private String workerID;

 /**
   * Constructor - Initializes a new <code>KSServerWorker</code>
   * @param frame a reference to the local simulator
   * @param socket a <code>Socket</code> object that is connected to a client.
   * @param workerID
   * @param TXStatus the initial transmit status of this worker
   * @param server a reference to the local server
   */
  public KSServerWorker(KSFrame frame, Socket socket, String workerID,
                        boolean TXStatus, KSServer server) {
    super("KSServerWorker");
    this.frame = frame;
    this.serviceSocket = socket;
    this.workerID = workerID;
    this.transmit = TXStatus;
    this.server = server;
    this.controllerName = server.controllerName;
  }

  /**
   * Tell this <code>KSServerWorker</code> to begin transmission immediatly.
   * @param controllerName the name of the currently running controller
   * @param sleepTime the time in milliseconds to wait before sending another
   * set of data
   */
  protected synchronized void startTransmission(String name, long interval) {
    if (connectionVerified) {
      controllerName = name;
      TX_INTERVAL = interval;
      transmit = true;
      reTransmit = true;
      this.notify();
    }
  }

  /**
   * Tell this <code>KSServerWorker</code> to stop transmitting.  The connection
   * will remain open and a call to startTransmission will begin transmission to
   * the client once again.
   */
  protected synchronized void stopTransmission() {
    transmit = false;
  }

  /**
   * Tell this <code>KSServerWorker</code> to terminate the connection with the
   * client.
   */
  protected synchronized void terminate() {
    terminate = true;
    transmit = false;
    this.notify();
  }

  /**
   * @see java.lang.Runnable
   */
  public void run() {
    try {
      initializeStreams();
      verifyConnection();
      while (!terminate) {
        while (transmit) {
          transmit();
        }
        if (terminate) { break; }
        waitNow();
      }

      // terminate connection
      out.writeObject("EOT");
      out.flush();

      // wait for client to quit
      inData = (String)in.readObject();
      if (!inData.startsWith("QUIT")) {
        throw new ProtocolException(serviceSocket.getInetAddress().getHostName());
      }

      // close server channel
      close();
    } catch (Exception e) { server.removeResource(workerID); }
  } // run

  /**
   * A wait call is needed in a syncronized block in order to wait as the
   * owner of this objects monitor.  This allows other threads access to the
   * public control methods of this <code>KSServerWorker</code> object.
   */
  private synchronized void waitNow() {
    try {
      wait();
    } catch (InterruptedException e) { /* when are these thrown? look it up, its cool */ }
  }

  /**
   * Transmit one set of simulator data to the client, then wait
   * <tt>sleepTime</tt>.
   */
  private synchronized void transmit() throws Exception {

    if (reTransmit) {
      // send controller name
      outData = "320 Controller Name";
      out.writeObject(outData);
      out.flush();
      out.writeObject(this.controllerName);
      out.flush();
    }

    if (reTransmit || sendMap) {
      // send world objects
      outData = "360 World Objects";
      out.writeObject(outData);
      out.flush();
      // NEW 8/3/03 - sp
      out.writeObject(frame.worldPanel.worldObjects);
      out.flush();
      out.reset();
      out.writeInt(frame.worldPanel.getHeldObjectID());
      sendMap = false;
      // END NEW
      out.flush();
    }

    // send sensor data and robot coordinates
    outData = "400 Sensors and Coordinates";
    out.writeObject(outData);
    out.flush();

    float[] data = new float[23]; // you must create a new object each time, read api for ObjectOutputStream
    // write sensors to a vector
    for (int i = 0; i < 8; i++) {
      data[i] = frame.drawManager.rState.sensors[i].getDistValue();
      data[i+8] = frame.drawManager.rState.sensors[i].getLightValue();
    }
    data[16] = frame.drawManager.rState.getRobotCoordinates().x;
    data[17] = frame.drawManager.rState.getRobotCoordinates().y;
    data[18] = frame.drawManager.rState.getRobotCoordinates().alpha;
    data[19] = frame.drawManager.rState.getRobotCoordinates().dx;
    data[20] = frame.drawManager.rState.getRobotCoordinates().dy;
    data[21] = frame.drawManager.rState.getGripperState();
    data[22] = frame.drawManager.rState.getArmState();
    out.writeObject(data);
    out.flush();

    // NEW 8/3/03 - sp -- need to determine if a change in the world
    //  config. occured, and if so, retransmit on next iteration
    if(frame.worldPanel.getWorldChange()) {
      frame.worldPanel.setWorldChange(false);
      sendMap = true;
    }
    // END NEW

    // finally the specified time before the next transmission
    reTransmit = false;
    wait(TX_INTERVAL);
  }

  /**
   * Verify the connection with the client by conforming to the request/response
   * protocol.
   */
  private void verifyConnection() throws Exception {

    // tell the client I am ready
    outData = "220 " + " Khepera Simulator Server Ready";
    out.writeObject(outData);
    out.flush();

    // get response from client
    inData = (String)in.readObject();
    if (!inData.startsWith("HELO")) {
      throw new ProtocolException(serviceSocket.getInetAddress().getHostName());
    }

    // respond - OK
    outData = "250 " + serviceSocket.getLocalAddress().getHostName() + " OK";
    out.writeObject(outData);
    out.flush();

    connectionVerified = true;
  } // verifyConnection

  /**
   * Initializes the streams that this <code>KSServerWorker</code> will use to
   * communicate with the client.
   */
  private void initializeStreams() throws Exception {
    out = new ObjectOutputStream(serviceSocket.getOutputStream());
    out.flush();
    in = new ObjectInputStream(serviceSocket.getInputStream());
  }

  /**
   * Close the socket and the streams associated with this
   * <code>KSServerWorker</code> objects socket.
   */
  private void close() throws Exception {
      out.close();
      in.close();
      serviceSocket.close();
  } // close
} // KSServerThread