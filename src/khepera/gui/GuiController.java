package khepera.gui;


import java.util.ArrayList;

import khepera.Coord;
import khepera.managers.MovementManager.RobotState;


public class GuiController {
  private RobotFrame robotFrame;
  private ArrayList<StatusView> statusViews;
  private StatusModel statusModel;

  public GuiController() {
    statusViews = new ArrayList<StatusView>();

    statusModel = new StatusModel();

    robotFrame = new RobotFrame(this);
  }

  public void drawRobotTail(Coord normalized, RobotState state) {
    robotFrame.drawRobotTail(normalized, state);
  }

  public void direction(double directionInRadians) {
    robotFrame.direction(directionInRadians);
  }

  public void drawSomething(Coord normalized) {
    robotFrame.drawSomething(normalized);
  }

  // MVC happens here
  public void setStatus(String s, int i) {
    statusModel.setStatus(s, i);
    for (StatusView view : statusViews) {
      view.setStatus(statusModel.getStatus(i), i);
    }
  }

  public void addStatusView(StatusView statusView) {
    statusViews.add(statusView);
  }
}
