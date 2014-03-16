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


public class LevelPanel extends JPanel {

  final int HEIGHT = 500;
  final int WIDTH = 500;

  private Polygon p;

  // direction
  private double d = 0;

  private List<Coord> tail;
  private List<Coord> something;

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

  public void direction(double e) {
    this.d = e;
    repaint();
  }

  public void addTail(Coord c) {
    tail.add(c);
    repaint();
  }

  public void addSomething(Coord c) {
    something.add(c);
    repaint();
  }

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

  private void drawRobot(Graphics g) {
    synchronized (tail) {
      if (tail.size() > 0) {
        Coord c = tail.get(tail.size() - 1);
        g.setColor(Color.YELLOW);
        g.fillRect(c.x + 250, c.y + 250, 10, 10);
      }
    }
  }

  public void drawSomething(Graphics g) {
    synchronized (something) {
      for (Coord c : something) {
        g.setColor(c.getColor());
        g.fillRect(c.x + 250, c.y + 250, 10, 10);
      }
    }
  }

  private void drawArrow(Graphics g) {
    // rotate the polygon
    Rectangle rect = p.getBounds();
    AffineTransform at = new AffineTransform();
    at.rotate(d, rect.getX() + rect.width / 2, rect.getY() + rect.height / 2);
    g.setColor(Color.cyan);
    Graphics2D g2d = (Graphics2D) g;
    g2d.fill(at.createTransformedShape(p));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    drawSomething(g);
    drawTail(g);
    drawRobot(g);
    drawArrow(g);

  }

}
