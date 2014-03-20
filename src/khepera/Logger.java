package khepera;

import khepera.gui.GuiController;
import khepera.managers.MovementManager.RobotState;


/**
 * Logger class, implemented as a Singleton, to centralize logging
 * 
 * Sysouts mid code is bad!
 * 
 * @author Patrick
 * 
 */
public class Logger {
  private static Logger instance;
  private GuiController controller;

  private Logger() {
    controller = new GuiController();
  };

  public synchronized static Logger getInstance() {
    if (instance == null)
      instance = new Logger();
    return instance;
  }

  public void log(String logMessage) {
    System.out.println("Log > " + logMessage);
  }

  public void error(String errorMessage) {
    System.err.println("Error > " + errorMessage);

  }

  /**
   * Display a status message in the default status field - USE WITH CAUTION as it may override
   * other messages
   * 
   * @param statusMessage
   */
  public void setStatus(String statusMessage) {
    setStatus(statusMessage, 12);
  }

  /**
   * Set a status, useful for parameters that update regularly Currently displaying status in
   * console and GUI
   * 
   * @param statusMessage
   * @param statusLine - make sure this parameter doesn't conflic with existing messages
   */
  public void setStatus(String statusMessage, int statusLine) {
    System.out.println("Status > " + statusMessage);
    controller.setStatus(statusMessage, statusLine);
  }

  public void updateRobotDirection(double directionInRadians) {
    controller.direction(directionInRadians);
  }

  public void updateRobotLocation(Coord normalized) {
    controller.drawRobotTail(normalized, RobotState.LOOKING_FOR_BALL);
  }
}
