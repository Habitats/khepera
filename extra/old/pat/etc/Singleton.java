package old.pat.etc;

import old.pat.gui.Controller;

public class Singleton {
  private static Singleton instance;

  private Singleton() {
    history = new History();
    controller = new Controller();
  }

  synchronized public static Singleton getInstance() {
    if (instance == null)
      instance = new Singleton();
    return instance;
  }

  /**
   * Objects accessible through singleton below
   */

  private Controller controller;
  private History history;

  public History getHistory() {
    return history;
  }

  public Controller getController() {
    return controller;
  }
}
