import java.util.HashMap;


public class Log {
	private HashMap<Integer, HashMap<Integer, LogEntry>> log;
	private int minX, minY, maxX, maxY;
	
	public Log() {
		minX = 0;
		maxX = 0;
		minY = 0;
		maxY = 0;
		
		log = new HashMap<Integer, HashMap<Integer, LogEntry>>();
	}
	
	//Is logged on (Y,X) of entry in hashmaps. will overwrite any other entry on same coordinates.
	public void log(LogEntry entry) {
		HashMap<Integer, LogEntry> xs = null;
		if (log.containsKey(entry.y)){
			 xs = log.get(entry.y);
		}
		else {
			xs = new HashMap<Integer, LogEntry>();
		}
		xs.put(entry.x, entry);
		
		if (entry.x < minX) minX = entry.x;
		else if (entry.x > maxX) maxX = entry.x;
		
		if (entry.y < minY) minY = entry.y;
		else if (entry.y > minY) minY = entry.y;
	}
	
	private void createMap() {
		int dx = maxX - minX;
		int dy = maxY - minY;
		int xOffset = minX;
		int yOffset = minY;
		
	}
	
	public String printLog() {
		createMap();
		return "NULL";
	}
}
