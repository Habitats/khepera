package khepera.state;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;

public abstract class State {

  public abstract int shouldTransition();

  public abstract void doWork();

  public abstract void resetState();
}
