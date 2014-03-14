package behaviour;

import managers.MovementManager;

public class MoveState extends State {

	int nextTransition = 0;
	int collisionTransition;
	int movementDoneTransition;
	int toMove;
	
	public MoveState(int toMove, int collisionTransition, int movementDoneTransition) {
		super();
		this.toMove = toMove;
		this.collisionTransition = collisionTransition;
		this.movementDoneTransition = movementDoneTransition;
	}
	
	@Override
	public int shouldTransition() {
		return nextTransition;
	}

	@Override
	public void doWork() {
		MovementManager.move();
		/**
		 * Sjekke om kollisjon eller og gått for langt, sett transition nummer etter.
		 */
	}
}
