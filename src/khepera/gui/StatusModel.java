package khepera.gui;

import java.util.HashMap;

/**
 * Model class to hold the information of the status view
 * 
 * @author Patrick
 * 
 */
public class StatusModel {
  private HashMap<Integer, String> statusFields;

  /**
   * Default constructor that initializes a hash with status messages
   */
  public StatusModel() {
    statusFields = new HashMap<Integer, String>();
  }

  /**
   * Set a new, or update a status field
   * 
   * @param statusMessage - the new status message
   * @param fieldNumber - which status field to use
   */
  public void setStatus(String statusMessage, int fieldNumber) {
    statusFields.put(fieldNumber, statusMessage);
  }

  /**
   * Get a specific status message
   * 
   * @param fieldNumber - which field
   * @return - the status message in the given field
   */
  public String getStatus(int fieldNumber) {
    return statusFields.get(fieldNumber);
  }
}
