package khepera;

import khepera.managers.MovementManager;
import khepera.managers.SensorManager;
import edu.wsu.KheperaSimulator.RobotController;

public class AbstractController extends RobotController {
  public AbstractController() {
    SensorManager sensorManager = new SensorManager(this);
    MovementManager movementManager = new MovementManager(this);
  }

  @Override
  public void doWork() throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void close() throws Exception {
    // TODO Auto-generated method stub

  }

}
