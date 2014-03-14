package khepera;


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

  private Logger() {};

  public synchronized static Logger getInstance() {
    if (instance == null)
      instance = new Logger();
    return instance;
  }

  public void log(String logMessage) {
    System.out.println(logMessage);
  }

  public void error(String errorMessage) {
    System.err.println(errorMessage);

  }
}
