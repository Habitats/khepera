package khepera.behaviour;

import java.util.ArrayList;

import khepera.Logger;
import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import khepera.state.State;

/**
 * Abstract class for the behaviours, all behaviours added must extend this and implement the functions.
 * @author Olav
 */
public abstract class Behaviour implements Comparable<Behaviour> {

  private String name = "No Name";
  public int priority;
  protected int currentState = 0;
  private ArrayList<State> states;
  protected MovementManager movementManager;
  protected SensorManager sensorManager;

  /**
   * Creates a new behaviour
   * 
   * @param priority - must be < 2'000'000'000
   * @param sensorManager
   * @param movementManager
   */
  public Behaviour(int priority, SensorManager sensorManager, MovementManager movementManager) {
    if (!(priority < 2000000000)) {
      throw new IllegalArgumentException();
    }
    this.movementManager = movementManager;
    this.sensorManager = sensorManager;
    this.priority = priority;
    states = new ArrayList<State>();
  }
  
  /***
   * Adds a state to the behaviour. It will be placed at the back of the list containing states.
   * @param state
   */
  public void addState(State state) {
    Logger.getInstance().log("Setting managers...");
    state.setManagers(movementManager, sensorManager);
    states.add(state);
  }

  /**
   *Function that is automatically called from the controller to make the robot do stuff.
   *It will select the highest priority behaviour that returns true from it's shouldRun() function. 
   */
  public void doWork() {
    if (states.get(currentState).shouldTransition()) {

      int transition = states.get(currentState).getTransition();
      states.get(currentState).initializeState();
      currentState = transition;
      return;
    }
    states.get(currentState).doWork();
  }

  @Override
  public int compareTo(Behaviour o) {
    return o.priority - priority;
  }

  /**
   * Resets the behaviour state machine to start from state 0.
   * This function is automatically called when the behaviour is changed by the controller.
   */
  public void resetBehaviour() {
    currentState = 0;
  }

  /**
   * Sets the name of the behaviour.
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  /**
   * This function should be implemented to return true if the behaviour model should be run.
   * @return
   */
  public abstract boolean shouldRun();
}
