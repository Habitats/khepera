/**
 * @(#)KSReader.java 1.1 2002/11/15
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
import javax.swing.JFileChooser;

/**
 * A <code>KSReader</code> provides the functionality to read a recorded simulation from file and play it back to the local simulator.
 */
public class KSReader implements Runnable {
	private String controllerName;
	private KSFrame frame;
	private int delayTime = 0;
	private ObjectInputStream in = null;
	private FileInputStream inFile = null;
	private String fileName = null;
	private File file = null;
	private boolean paused = false;
	private boolean isPlaying = false;
	private boolean fastForward = false;
	private boolean rewind = false;
	private boolean stop = false;
	private int origDelayTime = 0;
	private int readObjects = 0;
	private ArrayList allObjects = null;
	private Object currObject = null;
	private boolean haveRewound = false;
	private Thread thread;

	// NEW 7/29/2003 - sp
	private boolean tempChanges;
	private Vector worldObjects = null;
	private KSReadWriteState state = null;
	private KSReadWriteHeader header = null;
	private Vector lastState = null; // added 8/3/03 - sp -- needed for rewinds
	private int lastHeld = 0; // added 8/3/03 - sp -- needed for rewinds

	// END NEW

	/**
	 * Allocate a new <code>KSReader</code> object.
	 */
	public KSReader() {
	}

	/**
	 * Prompt the user for a simulation file to playback and start this <code>KSReader</code> on a separate thread.
	 * 
	 * @param frame
	 *            a reference to the simulator
	 */
	protected void initialize(KSFrame frame) {
		this.frame = frame;
		JFileChooser chooser = new JFileChooser(".");
		chooser.setDialogTitle("Load Recorded File");
		int status = chooser.showOpenDialog(this.frame);
		if (status == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
			try {
				inFile = new FileInputStream(file);
				in = new ObjectInputStream(inFile);
				allObjects = new ArrayList();
				currObject = new Object();
			} catch (Exception e) {
			}
			thread = new Thread(this, "KSRead");
			thread.start();
			StatusBar.setCenterStatus("Play Record Status: Playing Data");
		}
	} // initialize

	/**
	 * @see java.lang.Runnable
	 */
	@Override
	public void run() {
		try {
			while (!stop) {
				isPlaying = true;
				float[] data; // why???? - sp
				// NEW 8/2/2003 - sp -- ugly -- CHANGE!! (don't need new strings)
				header = (KSReadWriteHeader) in.readObject();
				String Date = header.date;
				String Controller = header.controller;
				String Delay = header.delay;
				String Control = Controller.substring(Controller.indexOf(':') + 1, Controller.length());
				this.delayTime = Integer.parseInt(Delay.substring(Delay.indexOf(':') + 1, Delay.length()));
				this.origDelayTime = this.delayTime;
				Vector v = header.objects; // ????
				frame.worldPanel.setWorldObjects(v);
				// END NEW

				/*
				 * The following is the original header reading routine ** String Date = (String)in.readObject(); String Controller = (String)in.readObject();
				 * String Delay = (String)in.readObject(); String Control = Controller.substring(Controller.indexOf(':')+1,Controller.length()); this.delayTime
				 * = Integer.parseInt(Delay.substring(Delay.indexOf(':')+1, Delay.length())); this.origDelayTime = this.delayTime; Vector v =
				 * (Vector)in.readObject(); frame.worldPanel.setWorldObjects(v);
				 */

				frame.drawManager.startPlayback();
				StatusBar.setRightStatus("Recorded Controller: " + Control);

				currObject = in.readObject();
				readObjects++;
				while (!(currObject.equals("EOF"))) {
					allObjects.add(currObject);
					readObjects++;
					currObject = in.readObject();
				}

				for (int i = 0; i < readObjects; i++) {
					if (stop)
						break;
					while (rewind) {
						/*
						 * Original -- commented out 8/2/2003 - sp data = new float[23]; // ??? data = (float[])allObjects.get(i);
						 */
						// NEW 8/2/2003 - sp
						state = (KSReadWriteState) allObjects.get(i);
						data = state.data;
						if (state.worldChange) {
							int temp = i - 1;
							boolean found = false;
							while (!found) {
								if (temp <= 0)
									break;
								KSReadWriteState tState = (KSReadWriteState) allObjects.get(temp);
								if (tState.worldChange) {
									lastState = tState.worldObjects;
									lastHeld = tState.heldID;
									found = true;
									break;
								}
								temp--;
							}
							if (found) {
								frame.worldPanel.setHeldId(lastHeld);
								frame.worldPanel.setWorldObjects(lastState);
							} else {
								frame.worldPanel.setHeldId(0);
								frame.worldPanel.setWorldObjects(v);
							}
						}
						// END NEW

						if (paused) {
							waitNow();
						}

						for (int j = 0; j < 8; j++) {
							frame.drawManager.rState.sensors[j].setDistValue((int) data[j]);
							frame.drawManager.rState.sensors[j].setLightValue((int) data[j + 8]);
						}
						frame.drawManager.rState.getRobotCoordinates().setCoordinates((int) data[16], (int) data[17], data[18], data[19], data[20]);
						frame.drawManager.rState.setGripperState((int) data[21]);
						frame.drawManager.rState.setArmState((int) data[22]);
						Thread.sleep(delayTime);
						i = i - 1;
						if (i == 0) {
							waitNow();
						}
					} // end rewind

					// data = (float[])allObjects.get(i); -- original
					// NEW 7/29/2003 - sp
					state = (KSReadWriteState) allObjects.get(i);
					data = state.data;
					if (state.worldChange) {
						frame.worldPanel.setHeldId(state.heldID);
						frame.worldPanel.setWorldObjects(state.worldObjects);
					}
					// END NEW

					if (paused) {
						waitNow();
					}

					for (int j = 0; j < 8; j++) {
						frame.drawManager.rState.sensors[j].setDistValue((int) data[j]);
						frame.drawManager.rState.sensors[j].setLightValue((int) data[j + 8]);
					}
					frame.drawManager.rState.getRobotCoordinates().setCoordinates((int) data[16], (int) data[17], data[18], data[19], data[20]);
					frame.drawManager.rState.setGripperState((int) data[21]);
					frame.drawManager.rState.setArmState((int) data[22]);
					Thread.sleep(delayTime);
				}
			}
		} catch (Exception b) {
		}
	}

	/**
	 * A wait call is needed in a syncronized block in order to wait as the owner of this objects monitor. This allows other threads access to the public
	 * control methods of this <code>KSServerWorker</code> object.
	 */
	private synchronized void waitNow() {
		try {
			wait();
		} catch (InterruptedException e) { /* when are these thrown? look it up, its cool */
		}
	}

	/**
	 * Stop the playback simulation.
	 */
	protected void stop() {
		this.stop = true;
		isPlaying = false;
		// Thread.
	}

	/**
	 * Pause the playback simulation.
	 * 
	 * @param p
	 *            <tt>true</tt> to pause the current playback, <tt>false</tt> otherwise
	 */
	protected synchronized void setPause(boolean p) {
		paused = p;
		if (!paused) {
			this.notify();
		}
	}

	/**
	 * Provide the current pause status.
	 * 
	 * @return <tt>true</tt> if paused, <tt>false</tt> otherwise
	 */
	protected boolean isPaused() {
		return paused;
	}

	/**
	 * Provide the current play status.
	 * 
	 * @return <tt>true</tt> if playing, <tt>false</tt> otherwise
	 */
	protected boolean isPlaying() {
		return isPlaying;
	}

	/**
	 * Provide the current fast forward status.
	 * 
	 * @return <tt>true</tt> if fast forward, <tt>false</tt> otherwise
	 */
	protected boolean isFastForward() {
		return fastForward;
	}

	/**
	 * Fast forward the current playback.
	 */
	protected void fastForward() {
		delayTime = delayTime / 2;
		fastForward = true;
	}

	/**
	 * Reset the delay time to the default delay time and turn fast forward off.
	 */
	protected void resetDelayTime() {
		delayTime = origDelayTime;
		fastForward = false;
	}

	/**
	 * Set the rewind status.
	 * 
	 * @param re
	 *            <tt>true</tt> to rewind the current playback, <tt>false</tt> otherwise
	 */
	protected synchronized void setRewind(boolean re) {
		rewind = re;
		if (!rewind) {
			this.notify();
		}
	}

	/**
	 * Provide the current rewind status.
	 * 
	 * @return <tt>true</tt> if fast forward, <tt>false</tt> otherwise
	 */
	protected boolean isRewind() {
		return rewind;
	}
} // KSReader