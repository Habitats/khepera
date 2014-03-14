package old.pat.gui;

import java.util.HashMap;

public class StatusModel {
  private HashMap<Integer, String> statusFields;

  public StatusModel() {
    statusFields = new HashMap<Integer, String>();
  }

  public void setStatus(String status, int i) {
    statusFields.put(i, status);
  }

  public String getStatus(int i) {
    return statusFields.get(i);
  }
}
