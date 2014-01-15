/**
 * @(#)RemoteControl.java 1.0 2004/06/13
 */

import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import edu.wsu.KheperaSimulator.RobotController;
import edu.wsu.KheperaSimulator.KSGripperStates;

/**
 * This class allows a robot in the simulator to be actively controlled via 
 * command line, simulating the command line control of the interface program.
 *
 * Author: 	Duane Bolick
 * Date: 	13 JUN 2004
 * Version:	1.0
 * 
 */
public class RemoteControl extends RobotController implements ActionListener {

	/**
	 * Default Constructor
	 */
	public RemoteControl() {
   		//Since this controller should be interactive, don't wait between
   		//doWork() calls.
    	setWaitTime(5);
    	
    	//Initialize GUI
    	frame = new JFrame();
    	panel = new JPanel();
    	out = new JTextArea(20,50);
        in = new JTextField(50);
        prompt = new JLabel(RemoteControl.PROMPT);
        
        //JTextArea - output window
        JScrollPane outScroll = new JScrollPane(out);
        outScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		outScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        out.setEditable(false);
        out.setLineWrap(true);
        
        //JTextField - input line
        in.addActionListener(this);
        in.requestFocus();
                      	
    	//Assemble console components and display console window
      	frame.getContentPane().add(panel);
       	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	panel.add(outScroll);
    	panel.add(prompt);
    	panel.add(in);
      	frame.setSize(600,200);
    	frame.setVisible(true);
    	    	
    	//Display opening message
    	out.setText("=====================================\n");
    	out.append("Khepera Remote Control Console Simulator\n");
    	out.append("=====================================\n");
    	out.append("Type help for a list of commands.");
    	
    }



	/**
	 * This method does nothing in this controller.
   	 */
  	public synchronized void doWork() throws Exception {
  		if (hasUserInput) {
  			//If the user has input something, parse the input.
			if(input != null)
	        	done = evalInput(input);
		
			//If the user wants to quit, instruct them to click the 'halt' button on
			//the main window.  (There's no graceful way to end the thread via this
			//controller)
			if (done)
				setFinished(true);
		}
		
		hasUserInput = false;		
	
  	}



        
    /**
     * This method cleans up the console window when the thread terminates.
     */
  	public void close() throws Exception {
  		frame.dispose();
  	} 




	/**
	 * This method handles user input.
	 */
	public synchronized void actionPerformed(ActionEvent e) {
					
		//Get the text from the input JTextField
		input = in.getText();
		
				
		hasUserInput = true;
		
		//Clear the input area, and redisplay the prompt
		in.setText("");
		in.requestFocus();
	}


	//==========================================================================
	//== Private Members =======================================================
	//==========================================================================
	
	//Current version number
	private static String CURRENT_VERSION = "v1.0";
	
	//Duration in ms for movement commands
	private static long DIST_SHORT = 100L;
    private static long DIST_MED   = 200L;
    private static long DIST_LONG  = 400L;
    
    //Required for movement timing
    private long startTime, endTime;
    
    //The prompt that appears in the JLabel of the console window.
    private static String PROMPT = "----Type commands below:----------";
    
    //GUI Members
    private JFrame frame;
    private JPanel panel;
    private JTextArea out;
    private JTextField in;
    private JLabel prompt;
    
    //User input
    private String input = null;
    
    //Status of remote control session
    private boolean done = false;
    
    //Status of user input
    private boolean hasUserInput = false;


	/**
	 * Accepts command input to the robot, parses the input, and calls the
	 * specified execution method.  
	 *
	 * <br><br>
	 * Valid input strings are as follows.  If a verbose command has a shortcut,
	 * it is listed in parentheses following the long command.
	 *
	 * <ul><b>Interface commands</b>
	 * <li>help - displays a list of valid commands</li>
	 * <li>quit (q) - exits the simulated console</li>
	 * </ul>
	 *
	 * <ul><b>Movement commands</b>. 
	 * <br>May be followed by a flag (&lt;short&gt; <-s>, &lt;med&gt;, <-m>, &lt;long&gt;, <-l>).
	 * <li>forward (f) - moves robot forward</li>
	 * <li>back (b) - moves robot backward</li>
	 * <li>right (r) - turns robot right</li>
	 * <li>left (l) - turns robot left</li>
	 * </ul>
	 *
	 * <ul><b>Gripper actions</b>.  
	 * <br><i>Must</i> be followed by a flag (&lt;open&gt;, <-o>, &lt;close&gt;, <-c>).
	 * <li>grip (g)</li>
	 * </ul>
	 *
	 * <ul><b>Arm actions</b>.  
	 * <br><i>Must</i> be followed by a flag (&lt;up&gt;, <-u>, &lt;down&gt;, <-d>).
	 * <li>arm (a)</li>
	 * </ul>
	 * 
	 * @param input	The user's command line input.
	 *
	 * @return boolean	True if the user inputs the quit command.
	 *
	 */
    private synchronized boolean evalInput(String input) {
    	
    	//Make sure the user has input anything at all.
    	if ( !(input.length() > 0) )
    		return false;
    	    	
    	    	
    	    	
    	// Convert input to all lower case.
    	input.toLowerCase();
    	    	
    	    	
    	    	
    	//Split the input into command and flag parts
    	String[] token = input.split("\\s");
    	String command = token[0];
    	char flag = ' ';
               	
    	//If the user entered a flag, grab the first character of the flag.
    	if ( token.length > 1 ) {
    		if ( token[1].indexOf('-') == 0 )
    			flag = token[1].charAt(1);
    		else
    			flag = token[1].charAt(0);
    	}
    	
    	
    	
    	//See if the flag is a valid duration flag
    	boolean validDuration = false;
    	if ( flag == 's' || flag == 'm' || flag == 'l' )
    		validDuration = true;
    
    	if(command.equals("ver")) {
    		out.append("\nCurrent Version: ");
    		out.append(RemoteControl.CURRENT_VERSION);
    		out.setCaretPosition(out.getDocument().getLength());
    		return false;
    	}	
    
    
        // help, added winter 2003 by Tom Mills, modified 12 JUN 2004 by Duane Bolick
		if(command.equals("help")) {
			out.append("\n\nThe following commands are supported by the Remote Control Application:");
			out.append("\nhelp	brings up this menu");
			out.append("\nquit	exits the program");
			out.append("\nver	displays the controller version currently running");
		
			out.append("\n\n== Verbose Commands ===================");
			out.append("\nforward		moves the robot forward");
			out.append("\nback		moves the robot backwards");
			out.append("\nleft		turns the robot left");
			out.append("\nright		turns the robot right");
			out.append("\nYou may specify a duration of short, med, or long for each of the above movements.");
			out.append("\n\narm up	moves the arm up");
			out.append("\narm down	moves the arm down");
			out.append("\ngrip close	closes the gripper");
			out.append("\ngrip open	opens the gripper");
			
			out.append("\n\n== Terse Commands =====================");
			out.append("\nf	causes the robot to move forward");
			out.append("\nb	causes the robot to move backward");
			out.append("\nl	causes the robot to turn left");
			out.append("\nr	causes the robot to turn right");
			out.append("\nThe above movement commands take the following options:");
			out.append("\n-s (short 500ms) -m (medium 1000ms) -l (long 3000ms)");
			out.append("\nDefaults to -m.");
			out.append("\n\na	causes the arm to move -u (up) or -d (down)");
			out.append("\ng	causes the gripper to -o (open) or -c (close)");
            out.setCaretPosition(out.getDocument().getLength());
            return false;
		}// end help
		
				
		//Parse remaining input
		switch(command.charAt(0)) {
			
			
			//quit
			case 'q':
				return true;
						
						
							
			//gripper command
			case 'g':
				
				switch(flag) {
					case 'o':
						setGripperState(KSGripperStates.GRIP_OPEN);
						break;
						
					case 'c':
						setGripperState(KSGripperStates.GRIP_CLOSED);
						break;
					
					default:
						out.append("\n\n>You must specify a valid gripper action.\n Valid actions: <open> <close> <-o> <-c>.");
						out.setCaretPosition(out.getDocument().getLength());
				}
							
				break;
					
					
					
			//arm command
			case 'a':
				
				switch(flag) {
					case 'u':
						setArmState(KSGripperStates.ARM_UP);
						break;
						
					case 'd':
						setArmState(KSGripperStates.ARM_DOWN);
						break;
						
					default:
						out.append("\n\n>You must specify an arm action.\n Valid actions: <up> <down> <-u> <-d>.");
						out.setCaretPosition(out.getDocument().getLength());
				}
												
				break;
					

			//forward
			case 'f':
			
				if ( validDuration )
					setForward(flag);
				else
					setForward('m');
									
				break;


					
			//back
			case 'b':
			
				if ( validDuration )
					setBackward(flag);
				else
					setBackward('m');
					
				break;
			
			
					
			//left turn
			case 'l':
				
				if ( validDuration )
					setLeftTurn(flag);
				else
					setLeftTurn('m');
						
				break;
				
					
			//right turn
			case 'r':
				
				if ( validDuration )
					setRightTurn(flag);
				else
					setRightTurn('m');
						
				break;
					
				
					
			//unrecognized command
			default:
				out.append("\n\n>Unrecognized command.  Type help for a list of commands.");
				out.setCaretPosition(out.getDocument().getLength());
		
		
						
		}// end switch
				
		
		return false;
		
		
	}//end evalInput
	
	
	// Motors
    private void setForward(char duration) {
        setMotorSpeeds(4,4);
        
        if(duration == 's')
	       	sleep(DIST_SHORT);
          
        else if(duration == 'm')
	    	sleep(DIST_MED);
            
        else
	       	sleep(DIST_LONG);
        
       
       setMotorSpeeds(0,0);
    }

    private void setBackward(char duration) {
       setMotorSpeeds(-4,-4);
        
        if(duration == 's')
	       	sleep(DIST_SHORT);
          
        else if(duration == 'm')
	    	sleep(DIST_MED);
            
        else
	       	sleep(DIST_LONG);
	
       setMotorSpeeds(0,0);
    }

    private void setLeftTurn(char duration) {
        setMotorSpeeds(-4,4);
        
        if(duration == 's')
	       	sleep(DIST_SHORT);
          
        else if(duration == 'm')
	    	sleep(DIST_MED);
            
        else
	       	sleep(DIST_LONG);
	
       setMotorSpeeds(0,0);
    }

    private void setRightTurn(char duration) {
       setMotorSpeeds(4,-4);
       
       if(duration == 's')
	       	sleep(DIST_SHORT);
          
        else if(duration == 'm')
	    	sleep(DIST_MED);
            
        else
	       	sleep(DIST_LONG);
	
       setMotorSpeeds(0,0);
    }



	
	
	
} // end RemoteControl