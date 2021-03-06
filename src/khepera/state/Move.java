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

  @Override
  public void resetState() {}
}
