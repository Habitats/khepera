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

  public void setStatus(String string) {
    setStatus(string, 12);
  }

  public void setStatus(String string, int i) {
    System.out.println("Status > " + string);
    controller.setStatus(string, i);
  }

  public void updateRobotDirection(double directionInRadians) {
    controller.direction(directionInRadians);
  }

  public void updateRobotLocation(Coord normalized) {
    controller.drawRobotTail(normalized, RobotState.LOOKING_FOR_BALL);
  }
}
