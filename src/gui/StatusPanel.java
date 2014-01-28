package gui;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusPanel extends JPanel {
	private JLabel statusLabel;
	private ArrayList<JLabel> labels;

	public StatusPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		labels = new ArrayList<JLabel>();

		JLabel header = new JLabel(" - COOL STATS --------------");
		add(header);

		addLabels(this);
		// buildFrame(statusPanel);
//		setBackground(Color.gray);

		Dimension size = new Dimension(200, 500);
		setPreferredSize(size);
		setMinimumSize(size);
	}

	private void addLabels(JPanel panel) {
		for (int i = 0; i < 16; i++) {
			statusLabel = new JLabel();
			// statusLabel.setSize(new Dimension(90, 30));
			statusLabel.setText(" ");
			panel.add(statusLabel);
			labels.add(statusLabel);
		}
	}

	public void setLabel(String status) {
		setLabel(status, 0);
	}

	public void setLabel(String status, int i) {
		labels.get(i).setText(" " + status);
	}

}
