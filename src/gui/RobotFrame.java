package gui;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import etc.AbstractController.RobotState;
import etc.Coord;

public class RobotFrame extends JFrame {

	private StatusPanel statusPanel;
	private LevelPanel levelPanel;

	public RobotFrame() {
		setName("Robot Management");
		levelPanel = new LevelPanel();
		statusPanel = new StatusPanel();
		setLayout(new GridBagLayout());

		add(levelPanel, new GBC(0, 0));
		add(statusPanel, new GBC(1, 0));

		buildFrame(this);
	}

	private void buildFrame(JFrame frame) {

		frame.getContentPane().setBackground(Color.black);

		frame.setTitle("Status Panel");
		frame.pack();

		// frame.setLocationRelativeTo(frame.getRootPane());
		frame.setLocation(0, 0);
		// frame.setSize(new Dimension(800, 500));
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		// frame.setResizable(false);
		frame.setVisible(true);
	}

	public StatusPanel getStatusPanel() {
		return statusPanel;
	}

	public LevelPanel getLevelPanel() {
		return levelPanel;
	}

	public void setStatus(String s, int i) {
		statusPanel.setLabel(s, i);
	}

	// TODO Auto-generated method stub
	public void direction(double directionInRadians) {
		levelPanel.direction(directionInRadians);
	}

	public void draw(Coord c, RobotState state) {
		Color tailColor = null;
		if (state == RobotState.GOING_HOME)
			tailColor = Color.orange;
		else if (state == RobotState.LOOKING_FOR_BALL)
			tailColor = Color.red;
		else if (state == RobotState.IDLE)
			tailColor = Color.blue;

		levelPanel.draw(c.x, c.y, tailColor);
	}

	public static void main(String[] args) {
		new RobotFrame();
	}
}
