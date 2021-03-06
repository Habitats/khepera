package khepera;

import java.util.ArrayList;
import java.util.Collections;

import khepera.behaviour.Behaviour;
import khepera.behaviour.CollisionRecovery;
import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import edu.wsu.KheperaSimulator.RobotController;

/**
 * Abstract controller class that is to be extended by any specific controller implementation. This
 * class extends the basic functionality of the default khepera controller to suit and manage our
 * own achitectual properties.
 * 
 * @author Olav, Patrick 
 * 
 */
public abstract class AbstractController extends RobotController {
  protected SensorManager sensorManager;
  protected MovementManager movementManager;
  private long startTime;
  private Behaviour lastRunBehaviour = null;
  private ArrayList<Behaviour> behaviours;
  private boolean initialized = false;

  public AbstractController() {
    startTime = System.currentTimeMillis();
    sensorManager = new SensorManager(this);
    movementManager = new MovementManager(this);
    behaviours = new ArrayList<Behaviour>();
    lastRunBehaviour = new CollisionRecovery(1999999999, sensorManager, movementManager);
    addBehaviour(lastRunBehaviour);

    addBehaviours();

    // Start the sensor manager
    Thread t = new Thread(sensorManager);
    t.setName("SensorManager thread");
    t.start();
    initialized = true;
  }

  @Override
  public void doWork() throws Exception {
    if (!initialized)
      return;
    updateStatus();
    runBehaviour();
  }

  /**
   * This method should use the addBehaviour() function to add all wanted behaviors
   */
  protected abstract void addBehaviours();

  /**
   * Adds a behaviour to the controller. The priority of the behaviour should not exceed
   * 2'000'000'000.
   * 
   * @param b - behaviour
   */
  protected void addBehaviour(Behaviour b) {
    behaviours.add(b);
    Collections.sort(behaviours);
  }

  /**
   * This method is invoked on every doWork() iteration. It will select the proper behaviour to be run, according to priority and shouldRun() functions.
   */
  private void runBehaviour() {
    if (behaviours.size() == 0) {
      Logger.getInstance().error("No behaviours added to the controller...");
      return;
    }

    for (Behaviour b : behaviours) {
      if (b.shouldRun()) {
        if (b != lastRunBehaviour) {
          Logger.getInstance().log("Changing to new behaviour: " + b.getName());
          Logger.getInstance().setStatus("Behavior: " + b.getName(), 13);
          lastRunBehaviour.resetBehaviour();
          lastRunBehaviour = b;
        }
        b.doWork();
        break;
      }
    }
  }

  @Override
  public void close() throws Exception {
    this.sensorManager.close(); // Necessary for stopping the SensorManager.run thread.
  }

  /**
   * Log messages to be updated on every iteration of doWork()
   */
  private void updateStatus() {
    Logger.getInstance().setStatus("Time passed: " + Double.toString((System.currentTimeMillis() - startTime) / 1000.), 0);
    // Logger.getInstance().setStatus("Speed: " + movementManager.getSpeed(), 6);
    Logger.getInstance().setStatus(String.format("Location: (%d, %d)", movementManager.getCurrentLocation().x, movementManager.getCurrentLocation().y), 7);
    Logger.getInstance().setStatus("Direction radian: " + movementManager.getDirectionInRadians(), 9);
    Logger.getInstance().setStatus("Direction degrees: " + movementManager.getDirectionInDegrees(), 8);
    // Logger.getInstance().setStatus("Direction h or v: " +
    // Boolean.toString(approximately(movementManager.getDirectionInRadians() % (Math.PI / 2), 0.,
    // 0.1)), 8);
  }
}
