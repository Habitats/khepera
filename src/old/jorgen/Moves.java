package old.jorgen;

public enum Moves {
  LEFT, RIGHT, TURN_AROUND, FORWARD, FORWARDSLOW, BACKWARD, START, END, BACKWARDSLOW, POINT;

  private Moves opposite;

  static {
    START.opposite = END;
    END.opposite = START;
    LEFT.opposite = RIGHT;
    RIGHT.opposite = LEFT;
    FORWARD.opposite = FORWARD;
    FORWARDSLOW.opposite = FORWARDSLOW;
    BACKWARDSLOW.opposite = BACKWARDSLOW;
    POINT.opposite = POINT;
    TURN_AROUND.opposite = TURN_AROUND;
  }

  public Moves getBacktrackMove() {
    return this.opposite;
  }
}
