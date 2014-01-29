
public class LogEntry implements Comparable<LogEntry>{
	
	public static final int STATE_UNKNOWN = 0;
	public static final int STATE_AIR = 1;
	public static final int STATE_SOMETHING = 2;
	public static final int STATE_WALL = 3;
	public static final int STATE_BALL = 4;
	
	int x, y;
	int state = 0;
	
	public LogEntry(int x, int y, int state) {
		this.x = x;
		this.y = y;
		this.state = state;
	}

	@Override
	public int compareTo(LogEntry arg0) {
		if ((int) (arg0.y - y) < 0) {
			return (int) (arg0.y - y);
		}
		return (int) (arg0.x - x);
	}
	
	
}
