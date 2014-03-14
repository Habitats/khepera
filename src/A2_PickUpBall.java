import old.pat.etc.AbstractController;

/**
 * Goes straigth forward looking for balls. Doesn't care about walls...
 * 
 * @author Patrick
 * 
 */
public class A2_PickUpBall extends AbstractController {

  boolean started = false;
  boolean done = false;

  @Override
  protected void findBall() {
    if (!closeToSomething()) {
      setStatus("Found nothing:(!", 15);
      forward(20);
    } else {
      setStatus("Found something!", 15);
      if (getAverageDistance(SENSOR_FRONTL) > 700 && getAverageDistance(SENSOR_FRONTR) > 700) {
        balls.pickUpBall();
        state = RobotState.GOING_HOME;
        return;
      }

      if (notFacing()) {
        if (getAverageDistance(SENSOR_FRONTL) > getAverageDistance(SENSOR_FRONTR) - 100)
          rotate(-1);
        else if (getAverageDistance(SENSOR_FRONTR) > getAverageDistance(SENSOR_FRONTL) - 100)
          rotate(1);
      } else
        forward(20);
    }
  }

  @Override
  protected void goHome() {
    move.goHomeTheSameWayYouCame();
  }

  private boolean notFacing() {
    return (!approximately(getAverageDistance(SENSOR_FRONTL), getAverageDistance(SENSOR_FRONTR),
        100)//
        || getAverageDistance(SENSOR_FRONTL) < 15//
    || getAverageDistance(SENSOR_FRONTR) < 15)//
    // && (getAverageDistance(SENSOR_FRONTL) > 100//
    // || getAverageDistance(SENSOR_FRONTR) > 100)//
    ;
  }

}
