package khepera.gui;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import khepera.Coord;
import khepera.managers.MovementManager.RobotState;

public class RobotFrame extends JFrame {

  private StatusView statusView;

  private LevelPanel levelPanel;

  public RobotFrame(GuiController controller) {
    setName("Robot Management");
    levelPanel = new LevelPanel();
    statusView = new StatusView();

    controller.addStatusView(statusView);

    setLayout(new GridBagLayout());

    add(levelPanel, new GBC(0, 0));
    add(statusView, new GBC(1, 0));

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

  public StatusView getStatusPanel() {
    return statusView;
  }

  public LevelPanel getLevelPanel() {
    return levelPanel;
  }

  // TODO Auto-generated method stub
  public void direction(double directionInRadians) {
    levelPanel.direction(directionInRadians);
  }

  public void drawRobotTail(Coord c, RobotState state) {
    Color tailColor = null;
    if (state == RobotState.GOING_HOME)
      tailColor = Color.orange;
    else if (state == RobotState.LOOKING_FOR_BALL)
      tailColor = Color.red;
    else if (state == RobotState.IDLE)
      tailColor = Color.blue;
    c.setColor(tailColor);

    levelPanel.addTail(c);
  }

  public void drawSomething(Coord c) {
    c.setColor(Color.green);
    levelPanel.addSomething(c);
  }
}
