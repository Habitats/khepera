package behaviour;

public abstract class State {
	public abstract int shouldTransition();
	public abstract void doWork();
}
