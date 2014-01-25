package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class StatusPanel {
	private JLabel statusLabel;
	private ArrayList<JLabel> labels;

	public StatusPanel() {
		JPanel statusPanel = new JPanel();
		JPanel levelPanel = new JPanel();
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
		labels = new ArrayList<JLabel>();
		addLabels(statusPanel);
		buildFrame(statusPanel);
		buildFrame(levelPanel);
	}

	private void addLabels(JPanel panel) {
		for (int i = 0; i < 10; i++) {
			statusLabel = new JLabel();
			statusLabel.setText(" ");
			panel.add(statusLabel);
			labels.add(statusLabel);
		}
	}

	private void buildFrame(JComponent component) {
		JFrame frame = new JFrame();

		frame.getContentPane().setBackground(Color.black);
		frame.getContentPane().add(component);

		frame.setTitle("Status Panel");
		frame.pack();

		frame.setLocationRelativeTo(frame.getRootPane());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setResizable(false);
		frame.setVisible(true);
	}

	public void setLabel(String status) {
		setLabel(status, 0);
	}

	public void setLabel(String status, int i) {
		labels.get(i).setText(status);
	}

}
