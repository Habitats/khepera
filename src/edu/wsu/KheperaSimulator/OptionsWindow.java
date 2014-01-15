/**
 * @(#)OptionsWindow.java 1.1 2002/01/30
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
import javax.swing.event.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * An <code>OptionsWindow</code> class represents the options window of the
 * application.  The options window contains interactive graphical components
 * that allow the user to setup or change the default settings of the application.
 */
public class OptionsWindow extends JDialog {

    /* Initial Panels created */
    private JTabbedPane  sliderPane = new JTabbedPane();
    private JPanel distSliderPanel = new JPanel();
    private JPanel lightSliderPanel = new JPanel();
    private JPanel speedSliderPanel = new JPanel();
    private JPanel recordSliderPanel = new JPanel();
    private JPanel networkingSliderPanel = new JPanel();

    /* Sliders defined and constructed */
    private JSlider distSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 4, 2);
    private JSlider lightSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 20, 10);
    private JSlider speedSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 20, 10);

    /* Labels for tab screens */
    private JLabel recordSpeedInterval = new JLabel("Interval :  ");
    private JLabel rMiliSeconds = new JLabel(" ms");
    private JLabel networkingSpeedInterval = new JLabel("Netoworking Record Interval :  ");
    private JLabel nMiliSeconds = new JLabel(" ms");
    //private TextField nPort = new JTextField("5041", 8);
    //private JButton pSet = new JButton("Set");

    /* Combo boxes for setting Intervals */
    private JComboBox comboRecording = new JComboBox();
    private JComboBox comboNetworking = new JComboBox();
    private JComboBox comboPortSet = new JComboBox();

    private int distLevel, lightLevel, speedLevel;
    private KSFrame parent; // Defining KSFrame as the parent for scope

    /**
     * Allocate a new <code>OptionsWindow</code> component.
     * @param frame a reference to the parent frame
     * @param distance the distance level
     * @param light the light level
     * @param speed the speed level
     */
    public OptionsWindow(KSFrame frame, int distance, int light, int speed) {
        super(frame, "Simulator Options", true);
        parent = frame;
        distLevel = distance;
        lightLevel = light;
        speedLevel = speed;
        initComponents();
        pack();
    }

    /**
     * Initialize all components.
     */
    private void initComponents() {

	/** Set the dimension screen size */
        sliderPane.setPreferredSize(new Dimension(330, 150));

        /** Distance Slider */
	distSliderPanel.setLayout(new GridLayout(2,1,0,1));
        distSliderPanel.add(new JLabel("Distance Sensor Sensitivity",
                                        SwingConstants.CENTER));

	/** Various distance slider characteristics get set here */
	Hashtable labelTable = new Hashtable();
        labelTable.put( new Integer( 0 ), new JLabel("Min") );
        labelTable.put( new Integer( 4 ), new JLabel("Max") );
        labelTable.put( new Integer( 2 ), new JLabel("Normal") );

	distSlider.setLabelTable( labelTable );
        distSlider.setMinorTickSpacing(1);
        distSlider.setPaintLabels(true);
        distSlider.setSnapToTicks(true);
        distSlider.setPaintTicks(true);
        distSlider.setValue(distLevel);
        distSliderPanel.add(distSlider);

	/** ActionListener for the distance slider */
	distSlider.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                if (!distSlider.getValueIsAdjusting()) {
                   int sLevel = distSlider.getValue();
                   parent.distLevel = sLevel;
                   if(parent.running)
                       parent.drawManager.setDistLevel(sLevel);
                }
            }
        });


	/* Light Slider */
        lightSliderPanel.setLayout(new GridLayout(2,1,0,1));
        lightSliderPanel.add(new JLabel("Light Sensor Sensitivity",
                                         SwingConstants.CENTER));

	/* Various distance light characteristics get set here */
	labelTable = new Hashtable();
        labelTable.put( new Integer( 0 ), new JLabel("Min") );
        labelTable.put( new Integer( 20 ), new JLabel("Max") );
        labelTable.put( new Integer( 10 ), new JLabel("Normal") );

	lightSlider.setLabelTable( labelTable );
        lightSlider.setMinorTickSpacing(1);
        lightSlider.setPaintLabels(true);
        lightSlider.setSnapToTicks(true);
        lightSlider.setPaintTicks(true);
        lightSlider.setValue(lightLevel);
        lightSliderPanel.add(lightSlider);

	/** ActionListener for the Light slider */
        lightSlider.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                if (!lightSlider.getValueIsAdjusting()) {
                   int sLevel = lightSlider.getValue();
                   parent.lightLevel = sLevel;
                   if(parent.running)
                       parent.drawManager.setLightLevel(sLevel);
                }
            }
        });

	/* Speed Slider */

        speedSliderPanel.setLayout(new GridLayout(2,1,0,1));
        speedSliderPanel.add(new JLabel("Robot Speed Sensitivity",
                                         SwingConstants.CENTER));

	/** Various speed characteristics get set here */
	labelTable = new Hashtable();
        labelTable.put( new Integer( 0 ), new JLabel("Slow") );     // was 0
        labelTable.put( new Integer( 20 ), new JLabel("Fast") );    // was 20
        labelTable.put( new Integer( 10 ), new JLabel("Average") ); // was 10

	speedSlider.setLabelTable( labelTable );
        speedSlider.setMinorTickSpacing(1);      // was 1
        speedSlider.setPaintLabels(true);
        speedSlider.setSnapToTicks(true);
        speedSlider.setPaintTicks(true);
        speedSlider.setValue(speedLevel);
        speedSliderPanel.add(speedSlider);

	/** ActionListener for the Speed slider */
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                if (!speedSlider.getValueIsAdjusting()) {
                   int sLevel = speedSlider.getValue();
                   parent.speedLevel = sLevel;
                   if(parent.running)
                       parent.drawManager.setSpeedLevel(sLevel);
                }
            }
        });

	/* Recording Interval */
	recordSliderPanel.setLayout(new FlowLayout());

	ImageIcon newRecord = new ImageIcon("images/record.gif");


	recordSpeedInterval.setIcon(new ImageIcon(
                  Toolkit.getDefaultToolkit().getImage("images/record.gif")));

	recordSliderPanel.add(recordSpeedInterval);

	recordSliderPanel.add("Center",comboRecording);
	recordSliderPanel.add("Center",rMiliSeconds);

	// Adding Intervals to Recording interval for recording

	String rInterval[ ] = {"50","100","150","200","250","300","350","400"};   // item choice

	for(int i=0; i<rInterval.length ; ++i )  // rInterval.length gives the array size
     	    comboRecording.addItem(rInterval[i]);  // add the the Recordinginterval to combo box

	comboRecording.setSelectedItem("250");

	/** ActionListener for the Recorder slider */
	comboRecording.addActionListener( new ActionListener()  {
           @Override
		public void actionPerformed(ActionEvent e) {
            System.out.println(comboRecording.getSelectedItem());
	    parent.recordRate = Integer.parseInt((String)comboRecording.getSelectedItem());
           }
        });

	/* Networking Interval */
	networkingSliderPanel.setLayout(new FlowLayout());
	networkingSliderPanel.add(new JLabel("Server Transmition Rate:      ",
                                         SwingConstants.CENTER));

	networkingSliderPanel.add("Center", comboNetworking);
	networkingSliderPanel.add("Center",nMiliSeconds);

	comboNetworking.setBounds(20, 35, 40, 20); //combobox dimensions

	// Adding Intervals to networking interval

	String nInterval[ ] = {"50","100","150","200","250","300","350","400"};   // item choice

	for(int i=0; i<nInterval.length ; ++i )  // nInterval.length gives the array size
     	    comboNetworking.addItem(nInterval[i]);  // add the the networkinterval to combo box

	comboNetworking.setSelectedItem("100");

	/** ActionListener for the Networking slider */
	comboNetworking.addActionListener( new ActionListener()  {
           @Override
		public void actionPerformed(ActionEvent e) {
             System.out.println(comboNetworking.getSelectedItem());
	     parent.serverTXRate = Integer.parseInt((String)comboNetworking.getSelectedItem());
             comboNetworking.setSelectedItem(Integer.toString(parent.serverTXRate));
	 }
        });

	// Server default port rate
	networkingSliderPanel.add(new JLabel("Server Port Number:      ",
                                         SwingConstants.CENTER));

//	networkingSliderPanel.add(nPort);
//	networkingSliderPanel.add(pSet);

	networkingSliderPanel.add(comboPortSet);
	String pInterval[ ] = {"5040","5041","5042","5043","5044","5045","5046","5047"};   // item choice

	for(int i=0; i<pInterval.length ; ++i )  // nInterval.length gives the array size
     	    comboPortSet.addItem(pInterval[i]);  // add the the networkinterval to combo box
	comboPortSet.setSelectedItem("5041");

	/** ActionListener for the set Port slider */
	comboNetworking.addActionListener( new ActionListener()  {
           @Override
		public void actionPerformed(ActionEvent e) {
             System.out.println(comboPortSet.getSelectedItem());
	     parent.defaultPort = Integer.parseInt((String)comboNetworking.getSelectedItem());
             comboNetworking.setSelectedItem(Integer.toString(parent.defaultPort));
	 }
        });

	/* Slider tab Panels and text get added */
        sliderPane.addTab("Distance", distSliderPanel);
        sliderPane.addTab("Light", lightSliderPanel);
        sliderPane.addTab("Speed", speedSliderPanel);
	sliderPane.addTab("Record", recordSliderPanel);
	sliderPane.addTab("Network", networkingSliderPanel);

	/* Slider Panel gets added to the main */
        getContentPane().add(sliderPane);
    }
} // OptionsWindow