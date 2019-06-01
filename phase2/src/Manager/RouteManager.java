package Manager;

import Transit.Route;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manager for routes
 */
public class RouteManager {

  /** logger */
  private static final Logger LOGGER =
      Logger.getLogger(StopManager.class.getName());
  /** consoleHandler */
  private static final Handler CONSOLE_HANDLER = new ConsoleHandler();

  /** An ArrayList that keeps track of all route objects */
  private HashMap<String, Route> allRoutes;
  /** String filePath*/
  private static final String PATH = "./serializedRouteObjects.ser";
  /** routeManger object get from routeManager*/
  private static RouteManager routeManager;

  /**
   * * Getter for variable routeManager from RouteManager, if null deserialize
   * @return RouteManager
   */
  public static RouteManager getRouteManager(){
    if (routeManager == null){
      try {
        routeManager  = new RouteManager();
      } catch (ClassNotFoundException | IOException e) {
        e.printStackTrace();
      }
    }
    return routeManager;
  }

  /**
   * Getter for allRoute as HashMap
   * @return HashMap key String(route), value Route(object)
   */
  public HashMap<String, Route> getAllRoutes() {
    return allRoutes;
  }

  /**
   * Creates a new empty RouteManger.
   * @throws IOException If did not input the file name
   * @throws ClassNotFoundException If not found file path
   */
  private RouteManager() throws ClassNotFoundException, IOException {
    allRoutes = new HashMap<>(); // hash map of Stop id: stop

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
   * Populates the records map from the file at path filePath.
   * Reads the file map.txt to create Route objects.
   * @param filePath the path of the data file
   * @throws FileNotFoundException if filePath is not a valid path
   */
  public void readFromCSVFile(String filePath)
      throws IOException {

    // FileInputStream can be used for reading raw bytes, like an image.
    Scanner scanner = new Scanner(new FileInputStream(filePath));
    String[] record;

    while(scanner.hasNextLine()) {
      // subway 1: vaughan, Dupont, Spadina, St.George, Museum, Queen'
      record = scanner.nextLine().split(":");
      String[] leftList = record[0].split("\\s+");//subway or bus and their number
      String[] middleList = record[1].split(",");//stations or stops
      String[] startTime = record[3].split(",");
      String[] endTime = record[4].split(",");
      int interval = Integer.parseInt(record[5].split(",")[0]);
      int speed = Integer.parseInt(record[6].split(",")[0]);
      String type = leftList[0].trim();// subway or bus
      String num = leftList[1].trim();// number of subway or bus
      LocalTime start = LocalTime.of(Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]));
      LocalTime end = LocalTime.of(Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]));

      ArrayList<Stop> route = new ArrayList<>();

      // looping over the middleList and create new Stop objects.
      for (String stopName : middleList) {
        Stop stop;
        String trimmedName = stopName.trim();
        stop = StopManager.getStopManager().getStopObject(trimmedName);
        route.add(stop);
      }
      Route newRoute = new Route(type, num, route, start, end, interval, speed);
      allRoutes.put(num, newRoute);
    }
    scanner.close();
  }

  /**
   * read file to get route, user, map data
   * @throws ClassNotFoundException If not found file path
   */
  private void readFromFile() throws ClassNotFoundException {
    try {
      InputStream file = new FileInputStream(PATH);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);

      // deserialize the Map
      allRoutes = (HashMap<String, Route>) input.readObject();
      input.close();
    } catch (IOException ex) {
      LOGGER.log(Level.SEVERE, "Cannot read from input.", ex);
    }
  }

  /**
   * To add route input our HashMap at allUsers and save it at a file
   * @param route Route
   *
   */
  public void add(Route route){
    if (!allRoutes.keySet().contains((route.getRouteNum()))) {
      allRoutes.put(route.getRouteNum(), route);
    }
    String message = "Added a new route" + route.toString();
    // Log the addition of a route.
    LOGGER.log(Level.FINE, message);
    try {
      saveToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Writes the route to file at filePath. Serialize
   * @throws IOException if not found input/output
   */
  public void saveToFile() throws IOException {

    OutputStream file = new FileOutputStream(PATH);
    OutputStream buffer = new BufferedOutputStream(file);
    ObjectOutput output = new ObjectOutputStream(buffer);

    // serialize the Map
    output.writeObject(allRoutes);
    output.close();
  }

  /**
   * To String printout route
   * @return route.toString + next index route.toString
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (String routeNum : allRoutes.keySet()) {
      result.append(allRoutes.get(routeNum).toString());
      result.append("\n");
    }
    return result.toString();
  }
}