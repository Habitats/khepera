package old.pat.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GBC extends GridBagConstraints {

  private static final long serialVersionUID = 1L;

  public enum Align {
    LEFT, RIGHT, MID, TIGHT, BOTTOM, LEFT_BOTTOM, MID_BOTTOM, RIGHT_BOTTOM, ALONE, FULL_WIDTH, FULL_WIDTH_BOTTOM, RIGHT_NO_TOP, LEFT_NO_TOP, MID_NO_TOP, ONLY_LEFT, ONLY_RIGHT, ONLY_TOP, ONLY_BOTTOM;
  }

  public GBC(int gridx, int gridy, Align align) {
    this.gridx = gridx;
    this.gridy = gridy;

    // for even borders
    setFill(BOTH);
  }

  public GBC(int gridx, int gridy) {
    this.gridx = gridx;
    this.gridy = gridy;
    setFill(BOTH);
  }

  // how many grids shall it span
  public GBC setSpan(int gridwidth, int gridheight) {
    this.gridwidth = gridwidth;
    this.gridheight = gridheight;
    return this;
  }

  /*
   * Fill area if the component doesn't match the available space given HORIZONTAL = fill
   * horizontally VERTICAL = fill vertically BOTH = guess
   */
  public GBC setFill(int fill) { // NO_UCD
    this.fill = fill;
    return this;
  }

  // this one's complicated, check docs lol
  public GBC setWeight(double weightx, double weighty) { // NO_UCD
    this.weightx = weightx;
    this.weighty = weighty;
    return this;
  }

  // internal padding
  public GBC setInsets(int top, int left, int bottom, int right) { // NO_UCD
    this.insets = new Insets(top, left, bottom, right);
    return this;
  }

  public GBC setIpad(int ipadx, int ipady) { // NO_UCD
    this.ipadx = ipadx;
    this.ipady = ipady;
    return this;
  }
}
