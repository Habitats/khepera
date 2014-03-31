package old.olav;

public class LogChangeEvent {
  public static final int ENRTY_CHANGED = 0;
  public static final int WALL_FOUND = 1;
  public static final int BALL_FOUND = 2;


  int type;
  String msg;
  int posX;
  int posY;

  public LogChangeEvent(int type, int posX, int posY, String msg) {
    this.type = type;
    this.msg = msg;
    this.posX = posX;
    this.posY = posY;
  }

}
