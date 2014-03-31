package khepera.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import khepera.Coord;


/**
 * Class to represent the robots view of the map
 * 
 * @author Patrick
 * 
 */
public class LevelPanel extends JPanel {

  final int HEIGHT = 500;
  final int WIDTH = 500;

  private Polygon p;

  // direction
  private double direction = 0;

  private List<Coord> tail;
  private List<Coord> something;

  /**
   * Default constructor, initialization of the panel happens here.
   */
  public LevelPanel() {
    Dimension size = new Dimension(WIDTH, HEIGHT);
    setPreferredSize(size);
    setMinimumSize(size);

    tail = Collections.synchronizedList(new ArrayList<Coord>());
    something = Collections.synchronizedList(new ArrayList<Coord>());

    // level = new int[500][500];

    setBackground(Color.black);

    this.p = createArrowPoly();
  }

  /**
   * Helper method for creation of the directional arrow.
   * 
   * @return - a polygon that looks like an arrow
   */
  private Polygon createArrowPoly() {
    int posX = WIDTH - 70;
    int posY = HEIGHT - 70;
    int xPoly[] = {-30, 0, 0, 30, 0, 0, -30};
    int yPoly[] = {-10, -10, -15, 0, 15, 10, 10};

    // position the polygon
    for (int i = 0; i < yPoly.length; i++) {
      xPoly[i] += posX;
      yPoly[i] += posY;
    }
    p = new Polygon(xPoly, yPoly, xPoly.length);

    return p;
  }

  /**
   * Set the direction the robot is facing.
   * 
   * @param direction - the direction in radians
   */
  public void direction(double direction) {
    this.direction = direction;
    repaint();
  }

  /**
   * Add a tail element to show the robots path
   * 
   * @param coordinate - the tail coordinate
   */
  public void addTail(Coord coordinate) {
    tail.add(coordinate);
    repaint();
  }

  /**
   * Add a "something" element to show "something". This can be used to draw a specified color
   * wherever you want on the map.
   * 
   * @param coordinate - where to draw
   */
  public void addSomething(Coord coordinate) {
    something.add(coordinate);
    repaint();
  }

  /**
   * Helper method for drawing the actual path of the robot. It iterates through an arraylist with
   * coordnates and draws every one of them.
   * 
   * @param g - the graphics element to draw on.
   */
  private void drawTail(Graphics g) {
    synchronized (tail) {
      if (tail.size() > 0) {
        for (Coord c : tail) {
          g.setColor(c.getColor());
          g.fillRect(c.x + 250, c.y + 250, 10, 10);
        }
      }
    }
  }

  /**
   * Helper method for drawing the actual robot
   * 
   * @param g - the graphics element to draw on
   */
  private void drawRobot(Graphics g) {
    synchronized (tail) {
      if (tail.size() > 0) {
        Coord c = tail.get(tail.size() - 1);
        g.setColor(Color.YELLOW);
        g.fillRect(c.x + 250, c.y + 250, 10, 10);
      }
    }
  }

  /**
   * Helper method for drawing "something"
   * 
   * @param g - the graphics element to draw on
   */
  public void drawSomething(Graphics g) {
    synchronized (something) {
      for (Coord c : something) {
        g.setColor(c.getColor());
        g.fillRect(c.x + 250, c.y + 250, 10, 10);
      }
    }
  }

  /**
   * Helper method for drawing the directional arrow
   * 
   * @param g - the graphics element to draw on
   */
  private void drawArrow(Graphics g) {
    // rotate the polygon
    Rectangle rect = p.getBounds();
    AffineTransform at = new AffineTransform();
    at.rotate(direction, rect.getX() + rect.width / 2, rect.getY() + rect.height / 2);
    g.setColor(Color.cyan);
    Graphics2D g2d = (Graphics2D) g;
    g2d.fill(at.createTransformedShape(p));
  }

  /**
   * Paining happens here.
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    drawSomething(g);
    drawTail(g);
    drawRobot(g);
    drawArrow(g);
  }
}
