package khepera;

import java.awt.Color;

/**
 * Coordinate class to represent an x,y coordinate pair. Mainly used in conjunction with the robots
 * internal map representation
 * 
 * @author Patrick
 * 
 */
public class Coord {

  public final int x;
  public final int y;

  private Color color;

  public Coord(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Normalizing method for the coordinates since the mapping between pixles and coordinates is a
   * little off
   * 
   * @return normalized cooridnate values
   */
  public Coord getNormalized() {
    return new Coord(x / 15, y / 15);
  }

  /**
   * @return the color of this specific coordnate
   */
  public Color getColor() {
    return color;
  }

  /**
   * Set the color of this coordinate on the map
   * 
   * @param color - the color
   */
  public void setColor(Color color) {
    this.color = color;
  }


}
