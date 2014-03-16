package khepera;

import java.awt.Color;

public class Coord {

  public final int x;
  public final int y;

  private Color color;

  public Coord(int x, int y) {
    this.x = x;
    this.y = y;
  }

  // normalize for map display
  public Coord getNormalized() {
    return new Coord(x / 15, y / 15);
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }


}
