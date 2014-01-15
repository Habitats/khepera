/**
 * @(#)KSServer.java 1.1 2002/10/30
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
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 * A <code>KSServer</code> provides the functionality to listen for and
 * service multiple clients.  This server object also provides methods to
 * manage the clients once they are being serviced, as well as controlling
 * the communication with the client.
 *
 * @author  Brian Potchik
 * @version 1.1, 2002/10/30
 */
public class KSServer implements Runnable{
  private ServerSocket serverSocket;
  private int port;
  private HashMap workers;
  private boolean transmitting = false;
  private boolean listening = false;
  protected static int DEFAULT_PORT = 5041; // default port
  protected static int DEFAULT_TX_RATE = 100; // default rate to transmit data in ms
  protected String controllerName;
  private KSFrame frame;

  /**
   * Allocate a new <code>KSServer</code> object that is not listening.
   */
  public KSServer() {
    workers = new HashMap();
  }

  /**
   * Initialize this server and start listening for clients on the
   * DEFAULT_PORT.  A call to this method will spawn the server on a
   * new <code>Thread</code>.
   */
  protected void initialize(KSFrame frame) {
    initialize(frame, DEFAULT_PORT);
  }

  /**
   * Initialize this server and start listening for clients on the specified
   * port.  A call to this method will spawn the server on a new
   * <code>Thread</code>.
   * @param frame reference to the parent frame
   * @param port desired port for which this server will listen for clients
   */
  protected void initialize(KSFrame frame, int port) {
    this.frame = frame;
    this.port = port;
    new Thread(this, "KSServer").start();
  }

  /**
   * Return the status of this server.
   * @return return <tt>true</tt> if this server is currently listening for
   * for clients; <tt>false</tt> otherwise.
   */
  protected boolean isListening() {
    return listening;
  }

  /**
   * Provide the port that this server will use to service clients.
   * @return the server service port
   */
  protected int getServicePort() {
    return port;
  }

  /**
   * Provide a list of clients that the server is servicing.
   * @return an array of worker ID's that are servicing clients
   */
  protected String[] listWorkerIDs() {
      Object[] keys = workers.keySet().toArray();
      String[] sKeys = new String[keys.length];
      for (int i = 0; i < keys.length; i++) {
          sKeys[i] = keys[i].toString();
      }
      return sKeys;
  }

  /**
   * Terminate communication with a client.
   * @param workerID the ID of the worker  to terminate
   */
  protected void dropClient(String workerID) {
    ((KSServerWorker)workers.get(workerID)).terminate();
    removeResource(workerID);
  }

  /**
   * Terminate communication with all clients.
   */
  protected void dropAllClients() {
    Object[] clientWorkers = workers.keySet().toArray();
    for (int i = 0; i < clientWorkers.length; i++) {
      ((KSServerWorker)workers.get(clientWorkers[i])).terminate();
      removeResource((String)clientWorkers[i]);
    }
  }

  /**
   * Remove a worker from the current pool of workers.
   * @param workerID the worker ID of the worker to remove
   */
  protected void removeResource(String workerID) {
    workers.remove(workerID);
  }

  /**
   * Begin transmission of simulation data with a client.
   * @param controllerName the client name to start transmission
   * @param interval the time in milliseconds at which the service thread
   * will wait until transmission of the next batch of simulator data.
   */
  protected void startTransmission(String controllerName, long interval) {
    this.controllerName = controllerName;
    transmitting = true;
    Object[] clientWorkers = workers.keySet().toArray();
    for (int i = 0; i < clientWorkers.length; i++) {
      ((KSServerWorker)workers.get(clientWorkers[i])).startTransmission(controllerName, interval);
    }
  }

  /**
   * Stop transmission of simulator data to all clients.  The connections to
   * the clients will remain alive.  A subsequent call to
   * <code>startTransmission</code> will begin transmission of simulator data
   * to the clients once again.
   */
  protected void stopTransmission() {
    transmitting = false;
    Object[] clientWorkers = workers.keySet().toArray();
    for (int i = 0; i < clientWorkers.length; i++) {
      ((KSServerWorker)workers.get(clientWorkers[i])).stopTransmission();
    }
  }

  /**
   * Stop this server from listening for clients.  Any active client connections
   * will remain active.
   */
  protected void kill() {
    try {
      setListening(false);
      serverSocket.close();
    } catch (Exception e) { /*System.out.println(e);/* what can you do */ }
  } // kill

  /**
   * @see java.lang.Runnable
   */
  @Override
public void run() {
    try {
      serverSocket = new ServerSocket(port);
      setListening(true);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, e.toString(), "Server Exception", JOptionPane.ERROR_MESSAGE);
      setListening(false);
      return;
    }
    try {
      while (listening) {
        createWorker(serverSocket.accept());
      }
    } catch (Exception e) {
      setListening(false);
    }
  } // listen

  /**
   * Set the listening status of this server and display the status in the
   * status bar of the application.
   * @param listening <tt>true</tt> if this server is listening
   */
  private void setListening(boolean listening) {
    this.listening = listening;
    if (listening) {
      StatusBar.setLeftStatus("Server listening on port: " + port);
    }
    else {
      StatusBar.setLeftStatus("Client/Server Status: Disabled");
    }
  }

  /**
   * Spawn a new <code>KSServerWorker</code> to handle the communication with
   * a client.
   * @param socket the <code>Socket</code> to which the client is connected.
   */
  private void createWorker(Socket socket) {
    String workerID = socket.getInetAddress().getHostName() + socket.getPort();
    KSServerWorker worker = new KSServerWorker(frame, socket, workerID, transmitting, this);
    workers.put(workerID, worker);
    worker.start();
  } // createWorker
} // KSServer