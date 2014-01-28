package etc;

import java.util.ArrayList;
import java.util.List;

import etc.AbstractController.RobotState;

public class History {

	private List<RobotEvent> forward;
	private List<RobotEvent> backward;

	public History() {
		forward = new ArrayList<RobotEvent>();
		backward = new ArrayList<RobotEvent>();
	}

	public RobotEvent getLastEvent() {
		return forward.get(forward.size() - 1);
	}

	public void addEvent(RobotEvent e) {
		if (e.getState() == RobotState.LOOKING_FOR_BALL) {
			forward.add(e);
			backward.clear();
		} else if (e.getState() == RobotState.GOING_HOME) {
			backward.add(e);
		}
	}

	public void removeEvent(RobotEvent e) {
		forward.remove(e);
	}

	public void removeEvent(int i) {
		forward.remove(i);
	}

	public RobotEvent getEvent(int i) {
		return forward.get(i);
	}

	public RobotEvent pop() {
		RobotEvent e = getLastEvent();
		removeEvent(e);
		return e;
	}

	@Override
	public String toString() {
		return String.format("Backward: %d\n Forward: %d", backward.size(), forward.size());
	}

	public int backWardSize() {
		return backward.size();
	}

	public int forwardSize() {
		return forward.size();
	}
}
