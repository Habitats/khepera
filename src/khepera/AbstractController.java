package khepera;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import edu.wsu.KheperaSimulator.RobotController;

public class AbstractController extends RobotController {
  private SensorManager sensorManager;
  private MovementManager movementManager;

  public AbstractController() {
    sensorManager = new SensorManager(this);
    movementManager = new MovementManager(this);
  }

  @Override
  public void doWork() throws Exception {
    // TODO Auto-generated method stub
    movementManager.forward(400);
    movementManager.rotate(90, MovementManager.Direction.RIGHT);
    movementManager.forward(800);
    movementManager.rotate(180, MovementManager.Direction.LEFT);

  }

  @Override
  public void close() throws Exception {
    // TODO Auto-generated method stub

  }

}
