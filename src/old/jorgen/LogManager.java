package old.jorgen;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class LogManager {



  private LinkedList<Moves> moveLogg = new LinkedList<Moves>(Arrays.asList(Moves.TURN_AROUND,
      Moves.START));
  private LinkedList<Moves> backup = new LinkedList<Moves>();

  public LogManager() {}

  public void foreward() {
    this.moveLogg.addFirst(Moves.FORWARD);
  }

  public void left() {
    this.moveLogg.addFirst(Moves.LEFT);
  }

  public void right() {
    this.moveLogg.addFirst(Moves.RIGHT);
  }

  public void turnaround() {
    this.left();
    this.left();
  }

  public void forewardslow() {
    this.moveLogg.addFirst(Moves.FORWARDSLOW);
  }

  public void backwardslow() {
    this.moveLogg.addFirst(Moves.BACKWARDSLOW);

  }


  public void setRevertPoint() {
    this.moveLogg.addFirst(Moves.POINT);
    this.moveLogg.addFirst(Moves.TURN_AROUND);
  }

  int backstep = 0;

  public Moves stepBack(boolean toHome) {
    if (backstep == 0) {
      if (toHome) {
        this.moveLogg.remove(Moves.POINT);
        this.moveLogg.remove(Moves.TURN_AROUND);
      }
      this.moveLogg.addFirst(Moves.TURN_AROUND);
    }

    Moves move = this.moveLogg.removeFirst().getBacktrackMove();
    this.backstep += 1;

    if (toHome)
      this.backup.addFirst(move);


    if (!toHome && move == Moves.POINT) {
      move = Moves.END;
      this.backstep = 0;
    } else if (toHome && move == Moves.END) {
      this.moveLogg = this.backup;
      this.moveLogg.removeFirst();
      this.moveLogg.removeFirst();
      this.moveLogg.addLast(Moves.START);
      this.backstep = 0;
    }

    // for( Moves m : this.moveLogg)
    // System.out.print(m+" ");
    // System.out.println(" ");

    return move;
  }

  public void clearToPoint() {
    int pointIndex = this.moveLogg.indexOf(Moves.POINT);

    for (int i = 0; i <= pointIndex; i++) {
      this.moveLogg.removeFirst();
    }
  }


  public void createMap() {
    HashMap<Point, Integer> map = new HashMap<Point, Integer>();

    Point up = new Point(0, 1);
    Point down = new Point(0, -1);
    Point left = new Point(-1, 0);
    Point right = new Point(1, 0);

    Point position = new Point(0, 0);
    Point direction = right;


    for (Moves m : this.moveLogg) {
      switch (m) {
        case TURN_AROUND:
          direction = new Point(-1 * direction.x, -1 * direction.y);
          break;
        case LEFT:
          if (direction == left)
            direction = down;
          else if (direction == right)
            direction = up;
          else if (direction == up)
            direction = left;
          else if (direction == down)
            direction = right;
          break;
        case RIGHT:
          if (direction == left)
            direction = up;
          else if (direction == right)
            direction = down;
          else if (direction == up)
            direction = right;
          else if (direction == down)
            direction = left;
          break;
      }

      switch (m) {
        case FORWARD:
          for (int i = 0; i < 3; i++) {
            position.translate(direction.x, direction.y);
          }
          break;
        case FORWARDSLOW:
          position.translate(direction.x, direction.y);
          break;
        case BACKWARD:
          for (int i = 0; i < 3; i++) {
            position.translate(-direction.x, -direction.y);
          }
          break;
        case BACKWARDSLOW:
          position.translate(-direction.x, -direction.y);
          break;
      }

      map.put(position, 1);

    } // create hash map


    LinkedList<Point> keys = new LinkedList<Point>(map.keySet());

    Collections.sort(keys, new Comparator<Point>() {
      @Override
      public int compare(Point o1, Point o2) {
        return (int) (o1.getX() - o2.getX());
      }
    });

    int minX = (int) keys.peekFirst().getX();
    int maxX = (int) keys.peekLast().getX();

    Collections.sort(keys, new Comparator<Point>() {
      @Override
      public int compare(Point o1, Point o2) {
        return (int) (o1.getY() - o2.getY());
      }
    });

    int minY = (int) keys.peekFirst().getY();
    int maxY = (int) keys.peekLast().getY();



    int[] width = new int[Math.abs(minX) + maxX];
    ArrayList<int[]> height = new ArrayList<int[]>(Arrays.asList(width));



  }

}
