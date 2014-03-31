package old.jorgen;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

class SensorManager extends Thread {

  private enum Sensor {
    LEFT, LEFTDIAGONAL, FRONTLEFT, FRONTRIGHT, RIGHTDIAGONAL, RIGHT;

    private int sensorIndex;

    static {
      LEFT.sensorIndex = 0;
      LEFTDIAGONAL.sensorIndex = 1;
      FRONTLEFT.sensorIndex = 2;
      FRONTRIGHT.sensorIndex = 3;
      RIGHTDIAGONAL.sensorIndex = 4;
      RIGHT.sensorIndex = 5;
    }

    public int distance(int[] distanceSensorData) {
      return distanceSensorData[this.sensorIndex];
    }

    public int getIndex() {
      return this.sensorIndex;
    }
  }

  int nSensors = 6;
  private int nMeasures = 5;
  private boolean running = true;

  private ArrayList<LinkedList<Integer>> distanceSensorReadings =
      new ArrayList<LinkedList<Integer>>();
  private ArrayList<LinkedList<Integer>> lightSensorReadings = new ArrayList<LinkedList<Integer>>();
  private int[] distanceSensorData;
  private int[] lightSensorData;
  private int ballDistance = 300;

  private Controller controller;


  public SensorManager(Controller rc) {
    this.controller = rc;

    this.distanceSensorData = new int[nSensors];
    this.lightSensorData = new int[nSensors];

    for (int i = 0; i < nSensors; i++) {
      this.distanceSensorReadings.add(new LinkedList<Integer>());
      this.lightSensorReadings.add(new LinkedList<Integer>());
    }


  }

  public void signalStop() {
    this.running = false;
  }

  public Point getClosestWall() throws InterruptedException {
    /*
     * Only walls at 90 degrees
     */
    int closest = -1;
    int distance = -1;
    Sensor[] sensors = {Sensor.LEFT, Sensor.FRONTLEFT, Sensor.FRONTRIGHT, Sensor.RIGHT}; // left,
                                                                                         // front,
                                                                                         // front,
                                                                                         // right

    if (this.distanceSensorData[0] == 0) {
      // something weird with our initial data
      this.updateSensors();
    }

    for (Sensor sensor : sensors) {
      if (distance == -1 || sensor.distance(distanceSensorData) > distance) {
        distance = sensor.distance(distanceSensorData);
        closest = sensor.getIndex();
      }
    }


    if (distance < 14) {
      // Sensor sensor = new Sensor[]{Sensor.LEFT, Sensor.FRONTLEFT, Sensor.FRONTRIGHT,
      // Sensor.RIGHT}[new Random().nextInt(4)];
      Sensor sensor = Sensor.FRONTLEFT;
      return new Point(sensor.getIndex(), sensor.distance(distanceSensorData));
    }
    return new Point(closest, distance);
  }

  public int getProximityFront() {
    return Math.max(Sensor.FRONTLEFT.distance(distanceSensorData),
        Sensor.FRONTRIGHT.distance(distanceSensorData));
  }

  public int[] getDistanceSensorData() {
    return this.distanceSensorData;
  }

  public int[] getLightSensorData() {
    return this.lightSensorData;
  }


  @Override
  public void run() {

    try {
      // Due to framework bug
      Thread.sleep(2);
    } catch (InterruptedException e) {
    }

    while (running) {
      this.updateSensors();
    }
  }

  private void updateSensors() {
    int[] distanceSensorBuffer = new int[nSensors];
    int[] lightSensorBuffer = new int[nSensors];

    for (int i = 0; i < this.nSensors; i++) {

      LinkedList<Integer> distance = this.distanceSensorReadings.get(i);
      LinkedList<Integer> light = this.lightSensorReadings.get(i);

      // Loop over the distance sensors
      if (distance.size() >= (this.nMeasures - 1)) {
        // FIFO data structure
        distance.removeLast();
        light.removeLast();
      }
      while (distance.size() < this.nMeasures) {
        // Fill up the empty structure
        distance.push(this.controller.getDistanceValue(i));
        light.push(this.controller.getLightValue(i));
      }

      int sumDistance = 0, sumLight = 0;
      for (int y = 0; y < distance.size(); y++) {
        sumDistance += distance.get(y);
        sumLight += light.get(y);
      }

      distanceSensorBuffer[i] = sumDistance / distance.size();
      lightSensorBuffer[i] = sumLight / light.size();
    }

    this.distanceSensorData = distanceSensorBuffer;
    this.lightSensorData = lightSensorBuffer;
  }


  public int checkBall(int followSensor) {
    /*
     * check for consistent distances, else ball? - use some kind of measurement history
     */
    if (followSensor == Sensor.RIGHT.getIndex()
        && Sensor.LEFTDIAGONAL.distance(distanceSensorData) < ballDistance) {
      if (Sensor.LEFT.distance(distanceSensorData) > ballDistance) {
        return Sensor.LEFT.getIndex();
      } else if (Sensor.FRONTLEFT.distance(distanceSensorData) > ballDistance) {
        return Sensor.FRONTLEFT.getIndex();
      } else if (Sensor.FRONTRIGHT.distance(distanceSensorData) > ballDistance) {
        return Sensor.FRONTRIGHT.getIndex();
      }
    } else if (followSensor == Sensor.LEFT.getIndex()
        && Sensor.RIGHTDIAGONAL.distance(distanceSensorData) < ballDistance) {
      if (Sensor.RIGHT.distance(distanceSensorData) > ballDistance) {
        return Sensor.RIGHT.getIndex();
      } else if (Sensor.FRONTLEFT.distance(distanceSensorData) > ballDistance) {
        return Sensor.FRONTLEFT.getIndex();
      } else if (Sensor.FRONTRIGHT.distance(distanceSensorData) > ballDistance) {
        return Sensor.FRONTRIGHT.getIndex();
      }
    }

    return -1;
  }

}
