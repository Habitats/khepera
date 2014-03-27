package khepera;

import java.util.ArrayList;
import java.util.Collections;

import khepera.behaviour.Behaviour;
import khepera.behaviour.CollisionRecovery;
import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import edu.wsu.KheperaSimulator.RobotController;

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
    lastRunBehaviour = new CollisionRecovery(2000000000, sensorManager, movementManager); 
    addBehaviour(lastRunBehaviour);
    
    addBehaviours();
   
    //Start the sensor manager
    Thread t = new Thread(sensorManager);
    t.setName("SensorManager thread");
    t.start();
    initialized = true;
  }

  @Override
  public void doWork() throws Exception {
	  if (!initialized) return;
	  updateStatus();
    runBehaviour();
  }
  
  /**
   * This method should use the addBehaviour() function to add all wanted behaviors
   */
  protected abstract void addBehaviours();
  
  protected void addBehaviour(Behaviour b) {
	  behaviours.add(b);
	  Collections.sort(behaviours);
  }
  
  private void runBehaviour() {
	  if (behaviours.size() == 0) {
		  Logger.getInstance().error("No behaviours added to the controller...");
		  return;
	  } 
	  
	  for(Behaviour b : behaviours) {
		  if (b.shouldRun()) {
			  if(b != lastRunBehaviour) {
				  Logger.getInstance().log("Changing to new behaviour: " + b.getName());
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
