package Manager;

import Transit.Stop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;
/**
 * Manages the saving and loading of Students.
 */
public class StopManager {

  /** LOGGER */
  private static final Logger LOGGER = Logger.getLogger(StopManager.class.getName());
  /** CONSOLE_HANDLER */
  private static final Handler CONSOLE_HANDLER = new ConsoleHandler();

  /** An ArrayList that keeps track of all Stop objects */
  private HashMap<String, Stop> allStops;
  /** String filePath*/
  private static final String PATH = "./serializedStopObjects.ser";

  /** stopManager object get from StopManager*/
  private static StopManager stopManager;

  /**
   * * Getter for variable StopManager from StopManager, if null deserialize
   * @return StopManager
   */
  public static StopManager getStopManager(){
    if (stopManager == null){
      try {
        stopManager  = new StopManager();
      } catch (ClassNotFoundException | IOException e) {
        e.printStackTrace();
      }
    }
    return stopManager;
  }

  /**
   * Getter for allStop as HashMap
   * @return HashMap key String(stop), value Stop(object)
   */
  public HashMap<String, Stop> getAllStops() {
    return allStops;
  }

  /**
   * Creates a new empty StopManager.
   * @throws IOException If did not input the file name
   * @throws ClassNotFoundException If not found file PATH
   */
  private StopManager() throws ClassNotFoundException, IOException {
    allStops = new HashMap<>(); // hash map of Stop id: stop
    // Associate the handler with the LOGGER.
    LOGGER.setLevel(Level.ALL);
    CONSOLE_HANDLER.setLevel(Level.ALL);
    LOGGER.addHandler(CONSOLE_HANDLER);

    // Populates the record list using stored data, if it exists.
    File file = new File(PATH);
    if (file.exists()) {
      readFromFile();
    } else {
      file.createNewFile();
    }
  }

  /**
   * Getter to get Stop
   * @param stopName Stop
   * @return Stop stop instance object
   */
  public Stop getStopObject(String stopName) {
    return allStops.get(stopName);
  }

  /**
   * To Find the nearby Stop/stations at your current location(stop/stations)
   * @param index Stop index/edge distance
   * @param stop near Stop edge
   * @param route the route for the busline/subway from map
   * @param edgeDistance edge distance between stops
   */
  // a helper method that add neighbour to the adjacent stops
  private void indexAddNeighbour(int index, Stop stop, ArrayList<Stop> route ,ArrayList<Integer> edgeDistance) {
    if (index == 0) {
      stop.addNeighbour(route.get(1), edgeDistance.get(index));
    } else if (index == route.size() - 1) {
      stop.addNeighbour(route.get(index - 1), edgeDistance.get(edgeDistance.size() - 1));
    } else {
      stop.addNeighbour(route.get(index - 1), edgeDistance.get(index - 1));
      stop.addNeighbour(route.get(index + 1), edgeDistance.get(index));
    }
  }

  /**
   * Populates the records map from the file at PATH filePath.
   * Reads the file map.txt to create stop objects.
   * @param filePath the PATH of the data file
   * @throws FileNotFoundException if filePath is not a valid PATH
   */
  public void readFromCSVFile(String filePath)
          throws IOException {
    // FileInputStream can be used for reading raw bytes, like an image.
    Scanner scanner = new Scanner(new FileInputStream(filePath));
    String[] record;

    while(scanner.hasNextLine()) {
      record = scanner.nextLine().split(":");
      String[] leftList = record[0].split("\\s+");//subway or bus and their number
      String[] middleList = record[1].split(",");//stations or stops
      String[] rightList = record[2].split(",");
      String type = leftList[0].trim();// subway or bus
      String num = leftList[1].trim();// number of subway or bus

      ArrayList<Stop> route = new ArrayList<>();

      // looping over the middleList and create new Stop objects.
      for (String stopName : middleList) {
        Stop stop;
        String trimmedName = stopName.trim();
        if (allStops.keySet().contains(trimmedName)) {
          stop = allStops.get(trimmedName);
          route.add(stop); // if in stopPool, get the same stop object to create alias.
        } else {
          stop = new Stop(trimmedName); // create a new stop object
          allStops.put(trimmedName, stop);
          route.add(stop);
        }
        stop.addConnection(type, num); // add connection with the transit type and transit number)
      }

      ArrayList<Integer> edgeValues = new ArrayList<>();

      // put edge values into an ArrayList
      for (String edge : rightList) {
        Integer edgeValue = Integer.valueOf(edge.trim());
        edgeValues.add(edgeValue);
      }

      // loop for the second time (Stop objects, if find the same object in AllStop, just add
      // all the
      // looping over the route list and add neighbours to each Stop object, decide whether or not
      // to put the object into the allStop list.
      for (int i = 0; i < route.size(); i++) {
        Stop current = route.get(i);
        indexAddNeighbour(i, current, route, edgeValues);

      }
    }
    scanner.close();
  }

  /**
   * Writes the stop to file at filePath. Serialize
   * @throws ClassNotFoundException if not found file PATH
   */
  private void readFromFile() throws ClassNotFoundException {
    try {
      InputStream file = new FileInputStream(PATH);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);

      // deserialize the Map
      allStops = (HashMap<String, Stop>) input.readObject();
      input.close();
    } catch (IOException ex) {
      LOGGER.log(Level.SEVERE, "Cannot read from input.", ex);
    }
  }

  /**
   * Adds record to this StopManager.
   * @param stop a record of stop to be added.
   */
  public void add(Stop stop) {
    allStops.put(stop.getNAME(), stop);
    LOGGER.log(Level.FINE, "Added a new Stop " + stop.toString());
    try {
      saveToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Writes the stop to file at filePath. Serialize
   * @throws IOException if not found file PATH input
   */
  public void saveToFile() throws IOException {

    OutputStream file = new FileOutputStream(PATH);
    OutputStream buffer = new BufferedOutputStream(file);
    ObjectOutput output = new ObjectOutputStream(buffer);

    // serialize the Map
    output.writeObject(allStops);
    output.close();
  }

  /**
   *To String for all stop
   * @return String stop.toString() + next index stop.toString()
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (Stop stop : allStops.values()) {
      result.append(stop.toString()).append("\n");
    }
    return result.toString();
  }
}