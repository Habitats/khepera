package khepera.state;

import khepera.managers.MovementManager;
import khepera.managers.MovementManager.Direction;

public class Move extends State {
  private int collisionTransition;
  private int movementDoneTransition;
  private int distanceToMove;
  private Direction dir;

  public Move(int distanceToMove, MovementManager.Direction dir, int collisionTransition, int movementDoneTransition) {
    this.distanceToMove = distanceToMove;
    this.collisionTransition = collisionTransition;
    this.movementDoneTransition = movementDoneTransition;
    this.dir = dir;
  }

  @Override
  public void doWork() {
    // Call moving this babe forward
     movementManager.move(distanceToMove, dir);
//     motionTest();
    setTransitionFlag(movementDoneTransition);

    if (sensorManager.isWallInFront() || sensorManager.isObjectInProximity() == 2 || sensorManager.isObjectInProximity() == 3) {
      setTransitionFlag(collisionTransition);
    }
  }

  private void motionTest() {
    movementManager.move(2000, Direction.FORWARD);
    movementManager.move(3000, Direction.BACKWARD);
    movementManager.rotate(90, Direction.RIGHT);
    movementManager.move(500, Direction.FORWARD);
    movementManager.move(500, Direction.BACKWARD);
    movementManager.rotate(90, Direction.RIGHT);
    movementManager.move(2000, Direction.FORWARD);
    movementManager.move(1000, Direction.BACKWARD);
    movementManager.rotate(90, Direction.RIGHT);
    movementManager.rotate(180, Direction.LEFT);
  }

  @Override
  public void resetState() {}
}
