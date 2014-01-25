package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LevelPanel extends JPanel {

	final int HEIGHT = 500;
	final int WIDTH = 500;

	private Polygon p;

	// direction
	private double d = 0;

	private int x = -1;
	private int y = -1;

	public LevelPanel() {
		this.setSize(new Dimension(WIDTH, HEIGHT));

		// level = new int[500][500];

		setBackground(Color.black);

		buildCanvas(this);
		
		this.p = createArrowPoly();
	}

	private Polygon createArrowPoly() {
		int posX = WIDTH - 80;
		int posY = HEIGHT - 80;
		int xPoly[] = { -30, 0, 0, 30, 0, 0, -30 };
		int yPoly[] = { -10, -10, -15, 0, 15, 10, 10 };

		// position the polygon
		for (int i = 0; i < yPoly.length; i++) {
			xPoly[i] += posX;
			yPoly[i] += posY;
		}
		p = new Polygon(xPoly, yPoly, xPoly.length);

		return p;
	}

	private void buildCanvas(JComponent c) {
		JFrame frame = new JFrame();

		frame.setSize(WIDTH, HEIGHT);
		frame.add(c);

		frame.setVisible(true);
	}

	public void draw(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void direction(double e) {
		this.d = e;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.pink);
		if (x > 0 && y > 0)
			g.drawRect(x, y, 10, 10);

		// rotate the polygon
		Rectangle rect = p.getBounds();
		AffineTransform at = new AffineTransform();
		at.rotate(d, rect.getX() + rect.width / 2, rect.getY() + rect.height / 2);
		g.setColor(Color.green);
		Graphics2D g2d = (Graphics2D) g;
		g2d.fill(at.createTransformedShape(p));

	}
	// public static void main(String[] args) {
	// new LevelPanel();
	// }
}
