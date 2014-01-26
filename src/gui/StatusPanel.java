package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusPanel {
	private JLabel statusLabel;
	private ArrayList<JLabel> labels;

	public StatusPanel() {
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
		labels = new ArrayList<JLabel>();
		addLabels(statusPanel);
		buildFrame(statusPanel);
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

		// frame.setLocationRelativeTo(frame.getRootPane());
		frame.setLocation(0, 510);
		frame.setSize(new Dimension(300, 300));
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
