import khepera.AbstractController;
import khepera.behaviour.CollectBehaviour;
import khepera.behaviour.ExploreBehaviour;
import khepera.behaviour.MoveToLightBehaviour;
import khepera.behaviour.PlaceOnLightBehaviour;


/**
 * Since the actual controller needs to be in the default package in order to run this class is
 * added as an end point
 * 
 * @author Patrick
 * 
 */
public final class A extends AbstractController {
  public A() {
    super();
  }

  @Override
  protected void addBehaviours() {
		addBehaviour(new ExploreBehaviour(0, sensorManager, movementManager));
		addBehaviour(new MoveToLightBehaviour(2, sensorManager, movementManager));
		addBehaviour(new CollectBehaviour(6, sensorManager, movementManager));
		addBehaviour(new PlaceOnLightBehaviour(8, sensorManager, movementManager));
		
//		addBehaviour(new CollisionTestBehaviour(0, sensorManager, movementManager));
		
	}
}