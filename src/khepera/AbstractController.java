package khepera;

import java.util.ArrayList;
import java.util.Collections;

import khepera.behaviour.Behaviour;
import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import edu.wsu.KheperaSimulator.RobotController;

public abstract class AbstractController extends RobotController {
  private SensorManager sensorManager;
  private MovementManager movementManager;
  private long startTime;
  private ArrayList<Behaviour> behaviours;
  
  public AbstractController() {
    startTime = System.currentTimeMillis();
//    sensorManager = SensorManager.getInstance(this); // this method is overloaded, so we may leave the "this" keyword after this call.
    sensorManager = new SensorManager(this);
    movementManager = new MovementManager(this);
    behaviours = new ArrayList<Behaviour>();
  }

  @Override
  public void doWork() throws Exception {
    updateStatus();
    movementManager.forward(400);
    updateStatus();
    // direction correction
    movementManager.rotate(80, MovementManager.Direction.RIGHT, true);
    updateStatus();
    movementManager.forward(800);
    updateStatus();
    movementManager.rotate(180, MovementManager.Direction.LEFT, true);
    
    //Run behaviour
    runBehaviour();
  }
  
  protected void addBehaviour(Behaviour b) {
	  behaviours.add(b);
	  Collections.sort(behaviours);
  }
  
  private void runBehaviour() {
	  if (behaviours.size() == 0) {
		  Logger.getInstance().error("No behaviours added to the controller...");
	  } 
	  
	  for(Behaviour b : behaviours) {
		  if (b.shouldRun()) {
			  b.doWork();
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
