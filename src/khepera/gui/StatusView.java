package khepera.gui;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class to display the various status messages. This panel holds a number of different status
 * message fields that can easily be filled with various information through the Logger class
 * 
 * @author Patrick
 * 
 */
public class StatusView extends JPanel {
  private JLabel statusLabel;
  private ArrayList<JLabel> labels;
  public static final int MAX_NUMBER_OF_STATUS_FIELDS = 16;

  public StatusView() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    labels = new ArrayList<JLabel>();

    JLabel header = new JLabel(" - COOL STATS --------------");
    add(header);

    addLabels(this);

    Dimension size = new Dimension(500, 500);
    setPreferredSize(size);
    setMinimumSize(size);
  }

  /**
   * Initialize the status field labels
   * 
   * @param panel - the panel to draw on
   */
  private void addLabels(JPanel panel) {
    for (int i = 0; i < MAX_NUMBER_OF_STATUS_FIELDS; i++) {
      statusLabel = new JLabel();
      // statusLabel.setSize(new Dimension(90, 30));
      statusLabel.setText(" ");
      panel.add(statusLabel);
      labels.add(statusLabel);
    }
  }

  /**
   * Set a status message in a given field less than the max number of fields
   * 
   * @param statusMessage - the status message to be displayed
   * @param fieldNumber - the status field number to be updated
   */
  public void setStatus(String statusMessage, int fieldNumber) {
    labels.get(fieldNumber).setText(fieldNumber + ": " + statusMessage);
  }
}
