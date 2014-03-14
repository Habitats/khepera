package old.jorgen;

import edu.wsu.KheperaSimulator.RobotController;


public class Controller extends RobotController {

  public enum States {
    MOVE_TO_WALL, // use sensors to find a wall to follow
    FOLLOW_WALL, // follow a wall and log steps
    GO_HOME, // backtrack the walk log home
    READY, HOLDING_BALL, LOST_WALL, POSSIBLE_BALL_RIGHT, POSSIBLE_BALL_LEFT, TOO_CLOSE_WALL, BACKTRACK, POSSIBLE_BALL_FRONT;
  }

  private final static boolean LOG = true;
  private final static boolean NO_LOG = false;
  private final static boolean BACKTRACK_HOME = true;
  private final static boolean BACKTRACK_POINT = false;

  MovementManager movement;
  States state;

  boolean busy = true;


  public Controller() {
    setWaitTime(5); // set the wait time until doWork() is invoked again
    this.state = States.MOVE_TO_WALL;

    this.movement = MovementManager.getInstance(this);
    // this.busy = false;
    //


  }

  @Override
  public void doWork() throws java.lang.Exception {
    // DO NOT DELETE

    // if( busy )
    // return;
    // else
    // busy = true;



    switch (this.state) {
      case MOVE_TO_WALL:
        if (this.movement.gotoWall(LOG))
          this.state = States.FOLLOW_WALL;// States.FOLLOW_WALL;
        break;
      case FOLLOW_WALL:
        this.movement.followAlongWall();
        break;
      case LOST_WALL:
        if (this.movement.gotoWall(NO_LOG))
          this.state = States.FOLLOW_WALL;
        break;
      case TOO_CLOSE_WALL:
        if (this.movement.fromWall(NO_LOG))
          this.state = States.FOLLOW_WALL;
        break;
      case GO_HOME:
        if (this.movement.goBack(BACKTRACK_HOME))
          this.state = States.READY;
        break;
      case BACKTRACK:
        if (this.movement.goBack(BACKTRACK_POINT))
          this.state = States.FOLLOW_WALL;
        break;
      case POSSIBLE_BALL_LEFT:
        if (this.movement.surveyBall(0))
          this.state = States.GO_HOME;
        break;
      case POSSIBLE_BALL_RIGHT:
        if (this.movement.surveyBall(5))
          this.state = States.GO_HOME;
        break;
      case POSSIBLE_BALL_FRONT:
        if (this.movement.surveyBall(2))
          this.state = States.GO_HOME;
        break;
      case HOLDING_BALL:
        this.state = States.GO_HOME;
        break;
      case READY:
        break;
    }

    // busy = false;
    System.out.println("Current state: " + this.state + " Movement: " + this.movement.getState());
  }

  @Override
  public void close() throws java.lang.Exception {
    // DO NOT DELETE
    this.movement.close();
  }

  public void foundBall() {
    this.state = States.HOLDING_BALL;

  }

  public void tooClose() {
    this.state = States.TOO_CLOSE_WALL;
  }

  public void lostWall() {
    this.state = States.LOST_WALL;
  }

  public void checkBall(int side) {
    if (side == 0)
      this.state = States.POSSIBLE_BALL_LEFT;
    else if (side == 2 || side == 3)
      this.state = States.POSSIBLE_BALL_FRONT;
    else
      this.state = States.POSSIBLE_BALL_RIGHT;
  }

  public void backtrack() {
    this.state = States.BACKTRACK;

  }

  public void followWall() {
    this.state = States.FOLLOW_WALL;

  }


}
