package khepera.gui;


import java.util.ArrayList;

import khepera.Coord;
import khepera.managers.MovementManager.RobotState;


/**
 * Class to act as the GUI controller. Initialization of the GUI happens here. The "brain" of the
 * model view controller architectural approach
 * 
 * @author Patrick
 * 
 */
public class GuiController {
  private RobotFrame robotFrame;
  private ArrayList<StatusView> statusViews;
  private StatusModel statusModel;

  /**
   * Default constructor. Initialization of the GUI happens here.
   */
  public GuiController() {
    statusViews = new ArrayList<StatusView>();
    statusModel = new StatusModel();
    robotFrame = new RobotFrame(this);
  }

  /**
   * Method for drawing the robot
   * 
   * @param coordinate - which coordinate to draw on
   * @param state - which state the robot is currently in
   */
  public void drawRobotTail(Coord coordinate, RobotState state) {
    robotFrame.drawRobotTail(coordinate, state);
  }

  /**
   * Set the direction of the robot. Used in conjunction with the graphical arrow that follows the
   * robots rotation
   * 
   * @param direction - the direction in radians
   */
  public void direction(double direction) {
    robotFrame.direction(direction);
  }

  /**
   * Method for drawing miscallaneous objects
   * @param coord - where to draw 
   */
  public void drawSomething(Coord coord) {
    robotFrame.drawSomething(coord);
  }

  /**
   * Set the status in the model, and update the view with this new information.
   * This is a simplified way to use MVC.
   * Status messages is whatever goes through the Logger.
   * @param statusMessage - the status message
   * @param fieldNumber - an integer for deciding status field number
   */
  public void setStatus(String statusMessage, int fieldNumber) {
    statusModel.setStatus(statusMessage, fieldNumber);
    for (StatusView view : statusViews) {
      view.setStatus(statusModel.getStatus(fieldNumber), fieldNumber);
    }
  }

  /**
   * Method for adding additional status views to the controller
   * @param statusView - the view to be added
   */
  public void addStatusView(StatusView statusView) {
    statusViews.add(statusView);
  }
}
