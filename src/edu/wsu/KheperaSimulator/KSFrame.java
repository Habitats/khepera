/**
 * @(#)KSFrame.java 1.1 2001/01/19
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
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

/**
 * A <code>KSFrame</code> is the WSU Khepera Simulator's main GUI component. It is the container for all other components of the simulator except the robot
 * controller.
 */
public class KSFrame extends JFrame {

	/* Variable Declarations */
	private JPanel contentPane;
	private JSplitPane splitPane;
	private JScrollPane worldScroll;
	private JPanel westPanel = new JPanel();
	private JPanel nEastPanel = new JPanel();
	private JPanel sEastPanel = new JPanel();
	private JSplitPane eastSplitPane;

	/** West Panel */
	private JPanel controllerButtonPanel = new JPanel();
	private JPanel worldButtonPanel = new JPanel();
	protected WorldPanel worldPanel = new WorldPanel();

	/** East Panel */
	private JPanel recordPlayBackPanel = new JPanel();
	private JPanel sensorDisplayPanel = new JPanel();
	private ObjectDialog mapObjectsPanel = new ObjectDialog(worldPanel);

	/** Main MenuBar */
	private JMenuBar jMenuBar = new JMenuBar();
	private JMenu jMenuFile = new JMenu("File");
	private JMenu jMenuActions = new JMenu("Actions");
	private JMenu jMenuTools = new JMenu("Tools");
	private JMenu jMenuHelp = new JMenu("Help");

	/* File */
	private JMenuItem jMItemOpenMap = new JMenuItem("Open Map");
	private JMenuItem jMItemSaveMap = new JMenuItem("Save Map");
	private JMenuItem jMItemExit = new JMenuItem("Exit");

	/* Actions */
	private JMenuItem jMItemRun = new JMenuItem("Run Controller");
	private JMenuItem jMItemHalt = new JMenuItem("Halt Controller");
	private JMenu jMenuRecorder = new JMenu("Recorder");
	private JMenu jMenuServer = new JMenu("Server");
	private JMenu jMenuClient = new JMenu("Client");

	/* Recorder */
	private JMenuItem jMItemRecord = new JMenuItem("Record");
	private JMenuItem jMItemPlay = new JMenuItem("Play");
	private JMenuItem jMItemStop = new JMenuItem("Stop");
	private JMenuItem jMItemPause = new JMenuItem("Pause");
	private JMenuItem jMItemFastForward = new JMenuItem("Fast Forward");
	private JMenuItem jMItemRewind = new JMenuItem("Rewind");

	/* Client/Server */
	private JMenuItem jMItemStartServer = new JMenuItem("Start Server");
	private JMenuItem jMItemStopServer = new JMenuItem("Stop Server");
	private JMenuItem jMItemDropClient = new JMenuItem("Drop Client");
	private JMenuItem jMItemStartClient = new JMenuItem("Start Client");
	private JMenuItem jMItemStopClient = new JMenuItem("Stop Client");

	/** Tools Menu Pulldown */
	private JMenuItem jMItemOptions = new JMenuItem("Options...");

	/** Help menu pulldown */
	private JMenuItem jMItemHelp = new JMenuItem("Help Topics");
	private JMenuItem jMItemAbout = new JMenuItem("About WSU Khepera Simulator");

	/** Control buttons, Button Controls for RobotControls and gifs for buttons */
	private ImageIcon saveIcon = new ImageIcon("images/save.gif");
	private ImageIcon newIcon = new ImageIcon("images/load.gif");
	private ImageIcon haltIcon = new ImageIcon("images/halt.gif");
	private ImageIcon addIcon = new ImageIcon("images/plus.gif");
	private ImageIcon removeIcon = new ImageIcon("images/remove.gif");
	private ImageIcon openIcon = new ImageIcon("images/open.gif");
	private ImageIcon clearIcon = new ImageIcon("images/clear.gif");
	private ImageIcon loadcIcon = new ImageIcon("images/loadc.gif");
	private ImageIcon rotateIcon = new ImageIcon("images/rotate.gif");
	private JButton buttonCRun = new JButton("Run   ", loadcIcon);
	private JButton buttonCHalt = new JButton("Halt   ", haltIcon);
	private JButton buttonCRotate = new JButton("Rotate   ", rotateIcon);
	private JToggleButton buttonCSet = new JToggleButton("Set   ", newIcon);

	/** World buttons, Button controls for the map and gifs for buttons */
	private JButton buttonWLoad = new JButton("Load   ", openIcon);
	private JButton buttonWSave = new JButton("Save   ", saveIcon);
	private JToggleButton buttonWAdd = new JToggleButton("Add   ", addIcon);
	private JToggleButton buttonWRem = new JToggleButton("Remove   ", removeIcon);
	private JButton buttonWClear = new JButton("Clear   ", clearIcon);

	/** ImageIcons & Button controls for Playback recorder */
	private ImageIcon newPlay = new ImageIcon("images/play.gif");
	private ImageIcon newRecord = new ImageIcon("images/record.gif");
	private ImageIcon newPause = new ImageIcon("images/pause.gif");
	private ImageIcon newForward = new ImageIcon("images/forward.gif");
	private ImageIcon newRewind = new ImageIcon("images/reverse.gif");
	private ImageIcon newStop = new ImageIcon("images/stop.gif");

	private JButton buttonPlay = new JButton(newPlay);
	private JButton buttonStop = new JButton(newStop);
	private JButton buttonRecord = new JButton(newRecord);
	private JButton buttonPause = new JButton(newPause);
	private JButton buttonForward = new JButton(newForward);
	private JButton buttonRewind = new JButton(newRewind);

	/** Radio Buttons for Sensor Display */
	private JRadioButton rbLight = new JRadioButton("Light");
	private JRadioButton rbDistance = new JRadioButton("Distance");
	private JRadioButton rbNothing = new JRadioButton("Disable", true);
	private ButtonGroup radioBGroup = new ButtonGroup();

	/** More panels for the robot */
	private JPanel selectRobotPanel = new JPanel();
	private JPanel sensorButtonPanel = new JPanel();
	private JPanel robotDisplayPanel = new JPanel();

	/** Sensors initialized for JLabel Sensor Display */
	private JLabel s1 = new JLabel("0");
	private JLabel s2 = new JLabel("0");
	private JLabel s3 = new JLabel("0");
	private JLabel s4 = new JLabel("0");
	private JLabel s5 = new JLabel("0");
	private JLabel s6 = new JLabel("0");
	private JLabel s7 = new JLabel("0");
	private JLabel s8 = new JLabel("0");
	protected JLabel[] sLabels = new JLabel[8];

	private JPanel robotStatePanel = new JPanel();
	private JPanel selectSensorPanel = new JPanel();
	private JLabel robotImageLabel = new JLabel();
	private JPanel stateImagePanel = new JPanel();

	protected RobotStatePanel armPanel = new RobotStatePanel("arm");
	protected RobotStatePanel gripPanel = new RobotStatePanel("grip");
	protected RobotStatePanel objPresPanel = new RobotStatePanel("obj");

	/** JPanels for Robot states */
	private JPanel armContainer = new JPanel();
	private JPanel gripContainer = new JPanel();
	private JPanel objContainer = new JPanel();
	private JPanel armLabelPanel = new JPanel();
	private JPanel gripLabelPanel = new JPanel();
	private JPanel objLabelPanel = new JPanel();

	/** I/O Text Area */
	private OptionsWindow optionDialog;
	private AboutWindow aboutWindow;
	private JPanel taPanel = new JPanel();
	private JTextArea textArea = new JTextArea(5, 5);
	private JPanel tfPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane(textArea);
	private JButton buttonTAClear = new JButton("Clear Output Window");

	/** Action listeners for GUI declared */
	private RadioEListener rbObjectListener = new RadioEListener();
	private MyObjectListener objectListener = new MyObjectListener();
	private MySaveFileListener saveFileListener = new MySaveFileListener();
	private MyLoadFileListener loadFileListener = new MyLoadFileListener();
	private MyRunListener runListener = new MyRunListener();
	private MyHaltListener haltListener = new MyHaltListener();
	private MyLoadRecorderListener loadRecordListener = new MyLoadRecorderListener();
	private MyStopRecorderListener StopRecordListener = new MyStopRecorderListener();
	private MyPlayRecorderListener PlayRecordListener = new MyPlayRecorderListener();
	private MyPauseRecorderListener PauseRecordListener = new MyPauseRecorderListener();
	private MyFastForwardRecorderListener FastForwardListener = new MyFastForwardRecorderListener();
	private MyRewindRecorderListener RewindListener = new MyRewindRecorderListener();

	// non-GUI objects
	private KSFrame parent;
	protected boolean running = false;

	protected WorldDrawManager drawManager = null;
	protected RobotControllerDirector rcd;

	private KSServer server = new KSServer();
	private KSClient client = new KSClient();
	private KSReader reader = new KSReader();
	private KSWriter writer = new KSWriter();

	private String sensorDisplayMode = "disable";
	protected int distLevel = 2;
	protected int lightLevel = 10;
	protected int speedLevel = 10;
	protected int recordRate = 250;
	protected int serverTXRate = KSServer.DEFAULT_TX_RATE;
	protected int defaultPort = KSServer.DEFAULT_PORT;

	/**
	 * Allocate a new <code>KSFrame</code>.
	 */
	public KSFrame(String title) {
		super(title);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		initComponents();
		drawManager = new WorldDrawManager(this);
		drawManager.setDisplayMode(sensorDisplayMode);
		drawManager.setAllLevels(distLevel, lightLevel, speedLevel);
		rcd = new RobotControllerDirector(drawManager.rState);
	}

	/**
	 * Initialize and construct all the the child components. Also create all the action listeners.
	 */
	private void initComponents() {
		parent = this; // KSFrame reference

		// Main Window Icon
		setIconImage(Toolkit.getDefaultToolkit().getImage("images/logo-icon.gif"));

		// get the contentpane as a JPanel and set the layout
		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BorderLayout());

		sLabels[0] = s1;
		sLabels[1] = s2;
		sLabels[2] = s3;
		sLabels[3] = s4;
		sLabels[4] = s5;
		sLabels[5] = s6;
		sLabels[6] = s7;
		sLabels[7] = s8;

		// Initialize all action listeners
		jMItemOpenMap.addActionListener(loadFileListener);
		jMItemSaveMap.addActionListener(saveFileListener);
		jMItemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jMExitPerformed();
			}
		});
		jMItemRun.addActionListener(runListener);
		jMItemHalt.addActionListener(haltListener);
		jMItemPlay.addActionListener(PlayRecordListener);
		jMItemStop.addActionListener(StopRecordListener);
		jMItemRecord.addActionListener(loadRecordListener);
		jMItemFastForward.addActionListener(FastForwardListener);
		jMItemRewind.addActionListener(RewindListener);
		jMItemPause.addActionListener(PauseRecordListener);
		jMItemOptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optionDialog = new OptionsWindow(parent, distLevel, lightLevel, speedLevel);
				optionDialog.setLocationRelativeTo(parent);
				optionDialog.pack();
				optionDialog.setVisible(true);
			}
		});

		// Action Listener's and Control Alt-Masks for Help Menu pulldown
		jMItemHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HelpDialog help = new HelpDialog(parent);
				help.setLocationRelativeTo(parent);
				help.setVisible(true);
			}
		});
		jMItemAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aboutWindow = new AboutWindow(parent);
				aboutWindow.setLocationRelativeTo(parent);
				aboutWindow.setVisible(true);
			}
		});

		/**
		 * ActionListener for Start Server Menu Pulldown. If there is not a server running then initialize the server port.
		 */
		jMItemStartServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!server.isListening()) {
					server.initialize(parent, parent.defaultPort);
				}
			}
		});

		/**
		 * ActionListener for Stop Server Menu Pulldown. If there is a server running then drop all the clients kill the server.
		 */
		jMItemStopServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				server.dropAllClients();
				server.kill();
			}
		});

		/**
		 * ActionListener for Stop Server Menu Pulldown. If there is a server running then drop all the clients kill the server.
		 */
		jMItemDropClient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String name = (String) JOptionPane.showInputDialog(parent, "Select a Client to drop.", "Drop a Clinet", JOptionPane.QUESTION_MESSAGE, null,
							server.listWorkerIDs(), null);
					if (name != null) {
						server.dropClient(name);
					}
				} catch (Exception ex) {
					parent.showExceptionDialog(ex);
				}
			}
		});

		/**
		 * ActionListener for Start Client Menu Pulldown. Pull up a JOptionPanel to connect to a server port and Initialize the client.
		 */
		jMItemStartClient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!client.isStarted()) {
					try {
						String s = JOptionPane.showInputDialog(parent, "Server and " + " Port in the form of <HOST>:<PORT>. \nDefault server port is 5041.",
								"Connect to Server", JOptionPane.QUESTION_MESSAGE);
						StringTokenizer st = new StringTokenizer(s, ":", false);
						client.initialize(parent, st.nextToken(), Integer.parseInt(st.nextToken()));
					} catch (Exception ex) {
						parent.showExceptionDialog(ex);
					}
				}
			}
		});

		/**
		 * ActionListener for Stop Client Menu Pulldown. If a client is started then kill the client
		 */
		jMItemStopClient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (client.isStarted()) {
					client.kill();
				}
			}
		});

		// Button Clear Method
		buttonWClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!running) {
					worldPanel.clearObjects();
					drawManager.clearWorldImage();
				} else {
					parent.showFunctionDisabled();
				}
			}
		});

		// Button Rotate Method
		buttonCRotate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!running) {
					worldPanel.rotateRobot();
				} else {
					parent.showFunctionDisabled();
				}
			}
		});

		// Button Clear Action
		buttonTAClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setText(null);
			}
		});

		// ActionListeners for Buttons
		buttonCRun.addActionListener(runListener);
		buttonCHalt.addActionListener(haltListener);
		buttonWLoad.addActionListener(loadFileListener);
		buttonWSave.addActionListener(saveFileListener);
		buttonWAdd.addActionListener(objectListener);
		buttonWRem.addActionListener(objectListener);
		buttonCSet.addActionListener(objectListener);

		// ActionListeners for Recorder Playback Buttons
		buttonPlay.addActionListener(PlayRecordListener);
		buttonStop.addActionListener(StopRecordListener);
		buttonRecord.addActionListener(loadRecordListener);
		buttonPause.addActionListener(PauseRecordListener);
		buttonRewind.addActionListener(RewindListener);
		buttonForward.addActionListener(FastForwardListener);

		// Add actionlisteners
		rbLight.addActionListener(rbObjectListener);
		rbDistance.addActionListener(rbObjectListener);
		rbNothing.addActionListener(rbObjectListener);

		// Construct menubar
		jMenuFile.add(jMItemOpenMap);
		jMenuFile.addSeparator();
		jMenuFile.add(jMItemSaveMap);
		jMenuFile.addSeparator();
		jMenuFile.add(jMItemExit);

		jMenuActions.add(jMItemRun);
		jMenuActions.add(jMItemHalt);
		jMenuActions.addSeparator();

		jMenuRecorder.add(jMItemPlay);
		jMenuRecorder.add(jMItemStop);
		jMenuRecorder.add(jMItemRecord);
		jMenuRecorder.add(jMItemRewind);
		jMenuRecorder.add(jMItemFastForward);
		jMenuRecorder.add(jMItemPause);
		jMenuActions.add(jMenuRecorder);
		jMenuActions.addSeparator();

		jMenuServer.add(jMItemStartServer);
		jMenuServer.add(jMItemStopServer);
		jMenuServer.addSeparator();
		jMenuServer.add(jMItemDropClient);
		jMenuClient.add(jMItemStartClient);
		jMenuClient.add(jMItemStopClient);
		jMenuActions.add(jMenuServer);
		jMenuActions.add(jMenuClient);

		jMenuTools.add(jMItemOptions);

		jMenuHelp.add(jMItemHelp);
		jMenuHelp.addSeparator();
		jMenuHelp.add(jMItemAbout);

		// Add items to menu bar
		jMenuBar.add(jMenuFile);
		jMenuBar.add(jMenuActions);
		jMenuBar.add(jMenuTools);
		jMenuBar.add(jMenuHelp);
		setJMenuBar(jMenuBar);

		// Set up Key Mneumonics
		jMenuFile.setMnemonic(KeyEvent.VK_F);
		jMItemOpenMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		jMItemSaveMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		jMItemExit.setMnemonic(KeyEvent.VK_X);

		jMenuActions.setMnemonic(KeyEvent.VK_A);
		jMItemRun.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		jMItemHalt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));

		jMenuTools.setMnemonic(KeyEvent.VK_T);
		jMenuHelp.setMnemonic(KeyEvent.VK_H);

		jMItemPlay.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		jMItemStop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		jMItemRecord.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		jMItemStartServer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		jMItemStopServer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK));

		/* Controller Button Panel */
		controllerButtonPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		controllerButtonPanel.add(buttonCRun);
		controllerButtonPanel.add(buttonCHalt);
		controllerButtonPanel.add(buttonCSet);
		controllerButtonPanel.add(buttonCRotate);

		/* Set Border style to raised buttons */
		buttonCRun.setBorder(BorderFactory.createRaisedBevelBorder());
		buttonCHalt.setBorder(BorderFactory.createRaisedBevelBorder());
		buttonCSet.setBorder(BorderFactory.createRaisedBevelBorder());
		buttonCRotate.setBorder(BorderFactory.createRaisedBevelBorder());

		/* Add tooltips for Controller Main Buttons */
		buttonCRun.setToolTipText("Start Simulation");
		buttonCHalt.setToolTipText("Stop Simulation");
		buttonCSet.setToolTipText("Set Robot Position");
		buttonCRotate.setToolTipText("Rotate Robot");

		/* World View Window */
		worldPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		/* World Button Panel =============================================== */
		worldButtonPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		worldButtonPanel.add(buttonWLoad);
		worldButtonPanel.add(buttonWSave);
		worldButtonPanel.add(buttonWAdd);
		worldButtonPanel.add(buttonWRem);
		worldButtonPanel.add(buttonWClear);

		/** Setup Button Style */
		buttonWLoad.setBorder(BorderFactory.createRaisedBevelBorder());
		buttonWSave.setBorder(BorderFactory.createRaisedBevelBorder());
		buttonWAdd.setBorder(BorderFactory.createRaisedBevelBorder());
		buttonWRem.setBorder(BorderFactory.createRaisedBevelBorder());
		buttonWClear.setBorder(BorderFactory.createRaisedBevelBorder());

		/** Add tool tips for map world */
		buttonWLoad.setToolTipText("Load Map");
		buttonWSave.setToolTipText("Save Map");
		buttonWAdd.setToolTipText("Add Object");
		buttonWRem.setToolTipText("Remove Object");
		buttonWClear.setToolTipText("Clear Map");

		/* Set Border style to raised buttons */
		buttonPlay.setBorder(BorderFactory.createRaisedBevelBorder());
		buttonRecord.setBorder(BorderFactory.createRaisedBevelBorder());
		buttonPause.setBorder(BorderFactory.createRaisedBevelBorder());
		buttonRewind.setBorder(BorderFactory.createRaisedBevelBorder());
		buttonForward.setBorder(BorderFactory.createRaisedBevelBorder());
		buttonStop.setBorder(BorderFactory.createRaisedBevelBorder());

		/* Add tooltips for Recorder Playback */
		buttonPlay.setToolTipText("Play Simulation");
		buttonRecord.setToolTipText("Record New Simulation");
		buttonPause.setToolTipText("Pause");
		buttonRewind.setToolTipText("Rewind");
		buttonForward.setToolTipText("Fast Forward");
		buttonStop.setToolTipText("Stop");

		/* Radio ButtionRobot Sensor Mode */
		rbLight.setActionCommand("displayLight");
		rbDistance.setActionCommand("displayDistance");
		rbNothing.setActionCommand("displayNothing");
		radioBGroup.add(rbLight);
		radioBGroup.add(rbDistance);
		radioBGroup.add(rbNothing);

		robotDisplayPanel.setLayout(null);
		robotDisplayPanel.setPreferredSize(new Dimension(200, 200));
		robotDisplayPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		robotDisplayPanel.setBackground(Color.white);
		robotImageLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/khep_trans_top.gif")));
		robotImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		robotImageLabel.setBounds(80, 35, 115, 115);
		robotDisplayPanel.add(robotImageLabel);

		int xOffset = 20;
		int yOffset = 5;

		s3.setHorizontalAlignment(SwingConstants.CENTER);
		s3.setBounds(72 + xOffset, 5 + yOffset, 45, 18);
		robotDisplayPanel.add(s3);

		s4.setHorizontalAlignment(SwingConstants.CENTER);
		s4.setBounds(117 + xOffset, 5 + yOffset, 45, 18);
		robotDisplayPanel.add(s4);

		s2.setHorizontalAlignment(SwingConstants.CENTER);
		s2.setBounds(15 + xOffset, 30 + yOffset, 45, 18);
		robotDisplayPanel.add(s2);

		s5.setHorizontalAlignment(SwingConstants.CENTER);
		s5.setBounds(175 + xOffset, 30 + yOffset, 45, 18);
		robotDisplayPanel.add(s5);

		s1.setHorizontalAlignment(SwingConstants.CENTER);
		s1.setBounds(10 + xOffset, 60 + yOffset, 45, 18);
		robotDisplayPanel.add(s1);

		s6.setHorizontalAlignment(SwingConstants.CENTER);
		s6.setBounds(180 + xOffset, 60 + yOffset, 45, 18);
		robotDisplayPanel.add(s6);

		s8.setHorizontalAlignment(SwingConstants.CENTER);
		s8.setBounds(45 + xOffset, 140 + yOffset, 45, 18);
		robotDisplayPanel.add(s8);

		s7.setHorizontalAlignment(SwingConstants.CENTER);
		s7.setBounds(145 + xOffset, 140 + yOffset, 45, 18);
		robotDisplayPanel.add(s7);

		textArea.append("WSU Khepera Robot Simulator v7.2\n\n");
		int pos = textArea.getText().length();
		textArea.setCaretPosition(pos);
		rbDistance.setBackground(Color.white);
		rbLight.setBackground(Color.white);
		rbNothing.setBackground(Color.white);

		/* Sensor Selection Panel */
		selectSensorPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		selectSensorPanel.setBackground(Color.white);
		selectSensorPanel.add(rbDistance);
		selectSensorPanel.add(rbLight);
		selectSensorPanel.add(rbNothing);

		/* Robot State Panel */
		robotStatePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 35, 0));
		robotStatePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		robotStatePanel.setBackground(Color.white);
		robotStatePanel.add(armPanel);
		robotStatePanel.add(gripPanel);
		robotStatePanel.add(objPresPanel);

		/* Text Display Panel */
		taPanel.setLayout(new BorderLayout());
		taPanel.add(scrollPane, BorderLayout.CENTER);
		taPanel.add(buttonTAClear, BorderLayout.SOUTH);

		/* Record/Playback */
		recordPlayBackPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		recordPlayBackPanel.add(buttonPlay);
		recordPlayBackPanel.add(buttonRecord);
		recordPlayBackPanel.add(buttonPause);
		recordPlayBackPanel.add(buttonRewind);
		recordPlayBackPanel.add(buttonForward);
		recordPlayBackPanel.add(buttonStop);

		/* Control Parameters Display Panel */
		mapObjectsPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		sEastPanel.setLayout(new BorderLayout());
		sEastPanel.add(taPanel, BorderLayout.CENTER);
		sEastPanel.add(mapObjectsPanel, BorderLayout.SOUTH);

		/* Sensor Display Panel */
		sensorDisplayPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		sensorDisplayPanel.setLayout(new BorderLayout());
		sensorDisplayPanel.add(selectSensorPanel, BorderLayout.NORTH);
		sensorDisplayPanel.add(robotDisplayPanel, BorderLayout.CENTER);
		sensorDisplayPanel.add(robotStatePanel, BorderLayout.SOUTH);

		/* Add to the West Panel */
		westPanel.setLayout(new BorderLayout());
		westPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		westPanel.add(controllerButtonPanel, BorderLayout.NORTH);
		worldPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		worldScroll = new JScrollPane(worldPanel);
		westPanel.add(worldScroll, BorderLayout.CENTER);
		westPanel.add(worldButtonPanel, BorderLayout.SOUTH);

		/* Add to the East Panel */
		nEastPanel.setLayout(new BorderLayout());
		nEastPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		nEastPanel.add(recordPlayBackPanel, BorderLayout.NORTH);
		nEastPanel.add(sensorDisplayPanel, BorderLayout.CENTER);

		/* Finally add to contentPane */
		eastSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, nEastPanel, sEastPanel);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, westPanel, eastSplitPane);
		contentPane.add(splitPane, BorderLayout.CENTER);
		contentPane.add(new StatusBar(), BorderLayout.SOUTH);

		/* Set initial status bar values */
		StatusBar.setLeftStatus("Client/Server Status: Disabled");
		StatusBar.setCenterStatus("Play/Record Status: Disabled");
		StatusBar.setRightStatus("Idle");
	} // initComponents

	/**
	 * ActionListener for Load Map button and pulldown If there is a controller running then flag and error, else load a map setworldObjects and repaint those
	 * objects.
	 */
	class MyLoadFileListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!running) {
				JFileChooser chooser = new JFileChooser("./maps");
				chooser.setDialogTitle("Load Map File");
				int status = chooser.showOpenDialog(parent);
				if (status == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					FileInputStream inFile = null;
					ObjectInputStream in = null;
					Vector wObjects = null;
					try {
						inFile = new FileInputStream(file);
						in = new ObjectInputStream(inFile);
						wObjects = (Vector) in.readObject();
					} catch (FileNotFoundException fe) {
						System.err.println("FileIStream - file not found");
					} catch (IOException oe) {
						System.err.println("OIStream - readObject");
					} catch (ClassNotFoundException nfe) {
						System.err.println("OIStream - Class !found");
					}
					worldPanel.setWorldObjects(wObjects);
					repaint();
				} else if (status == JFileChooser.ERROR_OPTION)
					System.err.println("FileChooser Error");
			} else {
				parent.showFunctionDisabled();
			}
		} // actionPerformed
	} // MyLoadFileListener

	/**
	 * ActionListener for Save Map button and pulldown If there is a controller running then flag and error, else save a map world file and pull up the
	 * appropriate dialog for save.
	 */
	class MySaveFileListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!running) {
				JFileChooser chooser = new JFileChooser("./maps");
				chooser.setDialogTitle("Save World File");
				int status = chooser.showSaveDialog(parent);
				if (status == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					FileOutputStream fileOut = null;
					ObjectOutputStream out = null;
					Vector wObjects = worldPanel.getWorldObjects();
					try {
						fileOut = new FileOutputStream(file);
						out = new ObjectOutputStream(fileOut);
						out.writeObject(wObjects);
					} catch (FileNotFoundException fe) {
						System.err.println("FileOStream - file not found");
					} catch (IOException oe) {
						System.err.println("OOStream - writeObject or");
						System.err.println("ObjectOStream - error");
					}
					repaint();
				} else if (status == JFileChooser.ERROR_OPTION)
					System.err.println("FileChooser Error");
			} else {
				parent.showFunctionDisabled();
			}
		} // actionPerformed
	} // MySaveFileListener

	/**
	 * ActionListener for Run Controller button and pulldown If there is not a controller running then bring up a JOptionPane to select a controller to run.
	 * Also is called for server: if there is not a server currently running then start the server transmission.
	 */
	class MyRunListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!running) {
				String name = (String) JOptionPane.showInputDialog(parent, "Select a Controller to Run", "Select", JOptionPane.QUESTION_MESSAGE, null,
						rcd.availableControllers(), null);
				if (name != null) {
					drawManager.startSimulator();
					if (rcd.startController(name)) {
						StatusBar.setRightStatus("Controller: " + name);
						running = true;
						server.startTransmission(name, serverTXRate);
						if (writer.isReady()) {
							writer.startRecording();
						}
					} // controller started
					else {
						drawManager.stopSimulator();
						parent.showExceptionDialog(new Exception("Controller could not start"));
					}
				}
			} else {
				parent.showFunctionDisabled();
			}
		} // actionPerformed
	} // MyRunListener

	/**
	 * ActionListener for Halt Controller button and pulldown If there is a controller running then stop the robot and the server.
	 */
	class MyHaltListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (running) {
				running = false;
				rcd.stopAll();
				drawManager.stopSimulator();
				drawManager.reInitialize();
				StatusBar.setRightStatus("Idle");
				server.stopTransmission();
				if (writer.isRecording()) {
					writer.setRecord(false);
				}
			}
		}
	} // MyHaltListener

	/**
	 * ActionListener for Buttons remove set and add Set Buttons to not selected, else if selected then set is selected and set the appropriate event.
	 */
	class MyObjectListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (running) {
				buttonWRem.setSelected(false);
				buttonCSet.setSelected(false);
				buttonWAdd.setSelected(false);
				parent.showFunctionDisabled();
			} else {
				if (buttonWAdd.isSelected()) {
					buttonWRem.setSelected(false);
					buttonCSet.setSelected(false);
					worldPanel.setEventType(WorldPanel.ADD);
				} else if (buttonWRem.isSelected()) {
					buttonWAdd.setSelected(false);
					buttonCSet.setSelected(false);
					worldPanel.setEventType(WorldPanel.REMOVE);
				} else if (buttonCSet.isSelected()) {
					buttonWAdd.setSelected(false);
					buttonWRem.setSelected(false);
					worldPanel.setEventType(WorldPanel.ROBOT);
				} else {
					buttonWAdd.setSelected(false);
					buttonWRem.setSelected(false);
					buttonCSet.setSelected(false);
					worldPanel.setEventType(WorldPanel.NONE);
				}
			}
		}
	} // MyObjectListener

	/**
	 * Loads Recorder File Listener Sets the playRecord mode to Records and passes this to the World Draw Manager The world draw manager then selects the
	 * appropriate method. The statusBar then gets updated with recording data as the status.
	 */
	class MyLoadRecorderListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (running) {
				if (!(writer.isReady())) {
					writer.initialize(parent, recordRate);
				}
				writer.startRecording();
				StatusBar.setCenterStatus("Play Record Status: Recording Data");
			} else {
				StatusBar.setCenterStatus("Play Record Status: Recording Data");
				writer.initialize(parent, recordRate);
			}
		}
	} // MyLoadRecorderListener

	/**
	 * Stops Recorder File Listener Sets the playRecord mode to Stop and passes this to the World Draw Manager The world draw manager then selects the
	 * appropriate method. The statusBar then gets updated with Stopped data as the status.
	 */
	class MyStopRecorderListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (writer.isRecording()) {
				writer.setRecord(false);
				StatusBar.setCenterStatus("Play Record Status: Stopped");
			} else if (reader.isPlaying()) {
				drawManager.stopPlayback();
				drawManager.reInitialize();
				reader.stop();
				StatusBar.setCenterStatus("Play Record Status: Stopped");
				reader = new KSReader();
			}
		}
	} // MyStopRecorderListener

	/**
	 * Play Recoder File Listener Sets the playRecord mode to Play and passes this to the World Draw Manager The world draw manager then selects the appropriate
	 * method. The statusBar then gets updated with Playing data as the status.
	 */
	class MyPlayRecorderListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (reader.isPaused()) {
				reader.setPause(false);
			} else if (reader.isFastForward()) {
				reader.resetDelayTime();
			} else if (reader.isRewind()) {
				reader.setRewind(false);
			} else {
				if (!reader.isPlaying()) {
					reader.initialize(parent);
				}
			}
		}
	} // MyPlayRecorderListener

	/**
	 * Pause Recoder File Listener Sets the playRecord mode to Pause and passes this to the World Draw Manager The world draw manager then selects the
	 * appropriate method. The statusBar then gets updated with Pausing Record as the status.
	 */
	class MyPauseRecorderListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (reader.isPlaying()) {
				reader.setPause(true);
				StatusBar.setCenterStatus("Play Record Status: Paused");
			}
		}
	} // MyPauseRecorderListener

	/**
	 * Fast Forward Recoder File Listener Sets the playRecord mode to Fast Forward and passes this to the World Draw Manager The world draw manager then selects
	 * the appropriate method. The statusBar then gets updated with Fast Forward as the status.
	 */
	class MyFastForwardRecorderListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (reader.isPlaying()) {
				reader.fastForward();
				StatusBar.setCenterStatus("Play Record Status: Fast Forward");
			}
		}
	} // MyFastForwardRecorderListener

	/**
	 * Rewind Recoder File Listener Sets the playRecord mode to Rewind and passes this to the World Draw Manager The world draw manager then selects the
	 * appropriate method. The statusBar then gets updated with Rewind as the status.
	 */
	class MyRewindRecorderListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (reader.isPlaying()) {
				reader.setRewind(true);
				StatusBar.setCenterStatus("Play Record Status: Rewinding");
			}
		}
	} // MyRewindRecorderListener

	/**
	 * RadioListener for Robot display modes Get the appropriate action and update the appropriate display mode.
	 */
	class RadioEListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String action = e.getActionCommand();
			if (action.equals("displayLight"))
				sensorDisplayMode = "light";
			else if (action.equals("displayDistance"))
				sensorDisplayMode = "distance";
			else
				sensorDisplayMode = "disable";
			if (drawManager != null)
				drawManager.setDisplayMode(sensorDisplayMode);
		}
	} // RadioEListener

	/**
	 * Display dialog status that exception has occurred.
	 */
	private void showExceptionDialog(Exception e) {
		JOptionPane.showMessageDialog(this, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	} // showExceptionDialog

	/**
	 * Display dialog status that a particular function is currently disabled.
	 */
	private void showFunctionDisabled() {
		JOptionPane.showMessageDialog(this, "This function is disabled while a controller is running.", "Function Disabled", JOptionPane.INFORMATION_MESSAGE);
	} // showFunctionDisabled

	/**
	 * Exit the simulator.
	 */
	private void jMExitPerformed() {
		if (running) {
			rcd.stopAll();
			drawManager.stopSimulator();
			server.dropAllClients();
			server.kill();
		}
		System.exit(0);
	} // jMExitPerformed

	/**
	 * Overridden so we can exit when window is closed.
	 */
	@Override
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			jMExitPerformed();
		}
	} // processWindowEvent
} // KSFrame