package old.olav;

import java.util.ArrayList;
import java.util.HashMap;


public class Log {
  private HashMap<Integer, HashMap<Integer, LogEntry>> log;
  private int minX, minY, maxX, maxY;

  int[][] map;

  ArrayList<LogChangeListener> listeners;

  public Log() {
    minX = 0;
    maxX = 0;
    minY = 0;
    maxY = 0;
    listeners = new ArrayList<LogChangeListener>();
    log = new HashMap<Integer, HashMap<Integer, LogEntry>>();
  }

  // Is logged on (Y,X) of entry in hashmaps. will overwrite any other entry on same coordinates.
  public void log(LogEntry entry) {
    HashMap<Integer, LogEntry> xs = null;
    if (log.containsKey(entry.y)) {
      xs = log.get(entry.y);
    } else {
      xs = new HashMap<Integer, LogEntry>();
      log.put(entry.y, xs);
    }
    xs.put(entry.x, entry);

    if (entry.x < minX)
      minX = entry.x;
    else if (entry.x > maxX)
      maxX = entry.x;

    if (entry.y < minY)
      minY = entry.y;
    else if (entry.y > maxY)
      maxY = entry.y;
    fireLogChangeEvent(new LogChangeEvent(LogChangeEvent.ENRTY_CHANGED, entry.x, entry.y,
        "Found something"));
  }

  public int get(int x, int y) {
    if (!log.containsKey(y)) {
      return 0;
    }
    if (!log.get(y).containsKey(x)) {
      return 0;
    }
    System.out.println(log.get(y).get(x).state);
    return log.get(y).get(x).state;
  }

  /***
   * 4-neighbourhood check
   * 
   * @param x
   * @param y
   * @return
   */
  public boolean isBall(int x, int y) {
    int connects = 0;
    if (get(x + 1, y) == 1) {
      connects++;
    }
    if (get(x - 1, y) == 1) {
      connects++;
    }
    if (get(x, y + 1) == 1) {
      connects++;
    }
    if (get(x, y - 1) == 1) {
      connects++;
    }
    if (connects == 4)
      return true;
    return false;
  }

  public boolean isWall(int x, int y) {
    if (get(x + 1, y) > 1 && get(x + 1, y) < 4) {
      return true;
    }
    if (get(x - 1, y) > 1 && get(x - 1, y) < 4) {
      return true;
    }
    if (get(x, y + 1) > 1 && get(x, y + 1) < 4) {
      return true;
    }
    if (get(x, y - 1) > 1 && get(x, y - 1) < 4) {
      return true;
    }
    return false;
  }

  private void createMap() {
    int dx = maxX - minX;
    int dy = maxY - minY;
    System.out.println(dx + "\t" + dy);
    map = new int[dy + 1][dx + 1];
  }

  public void interpretLog() {
    createMap();
    int dx = maxX - minX;
    int dy = maxY - minY;
    for (int y = 0; y < dy; y++) {
      for (int x = 0; x < dx; x++) {
        map[y][x] = get(x + minX, y + minY);
      }
    }
    interpretMap();
  }

  private void interpretMap() {
    for (int y = 0; y < maxY - minY; y++) {
      for (int x = 0; x < maxX - minX; x++) {
        System.out.println("INTER");
        int state = map[y][x];
        if (state == LogEntry.STATE_UNKNOWN || state == LogEntry.STATE_AIR)
          continue;
        if (state == 2) {
          if (isWall(x + minX, y + minY)) {
            map[y][x] = LogEntry.STATE_WALL;
            log.get(y).put(x, new LogEntry(x, y, LogEntry.STATE_WALL));
            fireLogChangeEvent(new LogChangeEvent(LogChangeEvent.WALL_FOUND, x, y, "Wall detected"));
          }
          if (isBall(x + minX, y + minY)) {
            map[y][x] = LogEntry.STATE_BALL;
            log.get(y).put(x, new LogEntry(x, y, LogEntry.STATE_BALL));
            fireLogChangeEvent(new LogChangeEvent(LogChangeEvent.BALL_FOUND, x, y, "Ball detected"));
          }
        }
      }
    }
  }

  public void fireLogChangeEvent(LogChangeEvent e) {
    for (LogChangeListener l : listeners) {
      l.fireLogChange(e);
    }
  }

  public void printMap() {
    interpretLog();
    for (int y = 0; y < maxY - minY; y++) {
      for (int x = 0; x < maxX - minX; x++) {
        System.out.print(map[maxY - minY - y][x] + " ");
      }
      System.out.println();
    }
    System.out.println();
  }
}
