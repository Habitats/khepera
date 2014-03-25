package khepera.state;


public class PickUpBall extends State {

	int success;
	int failure;
	int transition = 0;
	
  public PickUpBall(int onSuccessTransition, int onFailureTransition) {
    this.success = onSuccessTransition;
    this.failure = onFailureTransition;
  }

  @Override
  public void doWork() {
	  movementManager.pickUpBall();
	  if (sensorManager.isObjectHeld()) transition = success;
	  else transition = failure;
	  setTransitionFlag(transition);
  }

  @Override
  public void resetState() {
  }
}
