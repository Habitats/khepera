import khepera.AbstractController;
import khepera.behaviour.ExampleBehaviour;


/**
 * Since the actual controller needs to be in the default package in order to run this class is
 * added as an end point
 * 
 * @author Patrick
 * 
 */
public final class AwesomeController extends AbstractController {
  public AwesomeController() {
    super();
  }

  @Override
  protected void addBehaviours() {
	addBehaviour(new ExampleBehaviour(0, sensorManager, movementManager));
	addBehaviour(new CollectBehaviour(1, sensorManager, movementManager));
	
	}
}