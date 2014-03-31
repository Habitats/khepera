package khepera.gui;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import khepera.Coord;
import khepera.managers.MovementManager.RobotState;

/**
 * Class to represent the actual frame of the GUI
 * 
 * @author Patrick
 * 
 */
public class RobotFrame extends JFrame {

  private StatusView statusView;

  private LevelPanel levelPanel;

  /**
   * Constructor that initializes the frame
   * 
   * @param controller - the gui controller needs to be passed here in order to set the views
   */
  public RobotFrame(GuiController controller) {
    setName("Robot Management");
    levelPanel = new LevelPanel();
    statusView = new StatusView();

    controller.addStatusView(statusView);

    setLayout(new GridBagLayout());

    add(levelPanel, new GBC(0, 0));
    add(statusView, new GBC(1, 0));

    buildFrame(this);
  }

  /**
   * Helper method for building the frame
   * 
   * @param frame - the frame to be built
   */
  private void buildFrame(JFrame frame) {
    frame.getContentPane().setBackground(Color.black);

    frame.setTitle("Status Panel");
    frame.pack();

    frame.setLocation(0, 0);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    frame.setVisible(true);
  }

  /**
   * Set the direction the robot is facing
   * 
   * @param direction - the direction in radians
   */
  public void direction(double direction) {
    levelPanel.direction(direction);
  }

  /**
   * Method to draw the robots path
   * 
   * @param coordinate - coordinate to draw
   * @param state - the robots internal state
   */
  public void drawRobotTail(Coord coordinate, RobotState state) {
    Color tailColor = null;
    if (state == RobotState.GOING_HOME)
      tailColor = Color.orange;
    else if (state == RobotState.LOOKING_FOR_BALL)
      tailColor = Color.red;
    else if (state == RobotState.IDLE)
      tailColor = Color.blue;
    coordinate.setColor(tailColor);

    levelPanel.addTail(coordinate);
  }

  /**
   * Method for drawing "something". This could be anything witha color as long as you supply a
   * coordinate.
   * 
   * @param coordinate
   */
  public void drawSomething(Coord coordinate) {
    coordinate.setColor(Color.green);
    levelPanel.addSomething(coordinate);
  }

  public StatusView getStatusPanel() {
    return statusView;
  }

  public LevelPanel getLevelPanel() {
    return levelPanel;
  }
}
