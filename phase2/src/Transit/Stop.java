package Transit;

import Manager.RouteManager;
import Manager.StopManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/** Stop class */
public class Stop implements Serializable {

  /** The HashMap used to see where does this stop connect to (a list of bus or subway numbers) */
  private HashMap<String, ArrayList<String>> connections;

  /** The name of this Stop. */
  private final String NAME;

  /** The HashMap used to see a list of neighbours and the distance between a certain Stop */
  private HashMap<Stop, Integer> neighbours;

  /** The manager used for Stop class */
  private static StopManager stopManager;

  /** The constructor of Stop class */
  public Stop(String NAME) {
    this.NAME = NAME;
    this.neighbours = new HashMap<>();
    this.connections = new HashMap<>();
  }

  /**
   * The method for getting StopManager
   * @return The stopManager
   */
  public static StopManager getStopManager() {
    return stopManager;
  }

  /**
   * The method for setting stopManager
   * @param stopManager the stopManager we want to set
   */
  public static void setStopManager(StopManager stopManager) {
    Stop.stopManager = stopManager;
  }

  /**
   * The method for getting the NAME
   * @return the Stop name
   */
  public String getNAME() {
    return NAME;
  }

  /**
   * The method for getting the connections
   * @return the HashMap connections
   */
  public HashMap<String, ArrayList<String>> getConnections() {
    return connections;
  }

  /**
   * The method for getting the neighbours
   * @return The HashMap neighbours
   */
  public HashMap<Stop, Integer> getNeighbours() {
    return neighbours;
  }

  /**
   * The method to update the HashMap connection.
   * @param type The type of the Transit
   * @param routeNumber The number of the Transit
   */
  public void addConnection(String type, String routeNumber) {
    if (type.equals("subway")
        | type.equals("bus")) { // the given string type has to be subway or bus
      if (connections.containsKey(type)) {
        ArrayList<String> list = connections.get(type);
        if (!list.contains(routeNumber)) { // if the value does not exist under the key.
          list.add(routeNumber);
        }
      } else {
        ArrayList<String> listOfRoute = new ArrayList<>();
        listOfRoute.add(routeNumber);
        connections.put(type, listOfRoute);
      }
    }
  }

  /**
   * The method to update the HashMap neighbour.
   * @param stop the neighbour Stop
   * @param edgeDistance the distance to the neighbour Stop
   */
  public void addNeighbour(Stop stop, Integer edgeDistance) {
    boolean isContains = false;
    for (Stop neighbour : this.neighbours.keySet()) {
      if (neighbour.NAME.equals(stop.NAME)) {
        isContains = true;
      }
    }
    if (!isContains) {
      neighbours.put(stop, edgeDistance);
    }
  }

  /**
   * A helper function that collects all the route number of a stop's connection.
   * @param stop A stop object
   * @return list of route number which the stop connects to.
   */
  private ArrayList<String> routeNumCollection(Stop stop) {
    ArrayList<String> routeNumList = new ArrayList<>();
    for (ArrayList<String> routeNum : stop.getConnections().values()) {
      routeNumList.addAll(routeNum);
    }
    return routeNumList;
  }

  /**
   * The method to determine is on the same Route
   * @param stop The Stop we want to check
   * @return true if on the same Route, false if not.
   */
  public boolean isOnTheSameRoute(Stop stop) {
    for (String routeNum : routeNumCollection(stop)) {
      if (routeNumCollection(this).contains(routeNum)){
        return true;
      }
    }
    return false;
  }

  /**
   * The method to make an ArrayList which contains the name of the Stop for a certain Route.
   * @param route The Route we want to get String format
   * @return An ArrayList which is the String format of that Route
   */
  private ArrayList<String> makeStringRoute(Route route) {
    ArrayList<String> nameSet = new ArrayList<>();
    for (Stop stop : route.getRoute()) {
      nameSet.add(stop.getNAME());
    }
    return nameSet;
  }

  /**
   * The method to find the same Route for the given Stop and current Stop
   * @param stop The other Stop we want to find for the same Route
   * @return The same Route
   */
  public ArrayList<ArrayList<String>> FindSameRoute(Stop stop) {
    ArrayList<ArrayList<String>> sameRoute = new ArrayList<>();
    for (String key : RouteManager.getRouteManager().getAllRoutes().keySet()) {
      Route route = RouteManager.getRouteManager().getAllRoutes().get(key);
      ArrayList<String> stringRoute = makeStringRoute(route);
      boolean contain =
          stringRoute.contains(stop.getNAME()) && stringRoute.contains(this.getNAME());
      if (contain) {
        ArrayList<String> routeInfo = new ArrayList<>();
        routeInfo.add(route.getRouteType());
        routeInfo.add(route.getRouteNum());
        sameRoute.add(routeInfo);
      }
    }
    return sameRoute;
  }

  /**
   * The equal method
   * @param o The Object we want to compare
   * @return true if Object o is a Stop, false if not
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Stop stop = (Stop) o;
    return Objects.equals(NAME, stop.getNAME());
  }

  /**
   * The method to hash the code
   * @return the unique number for this NAME
   */
  @Override
  public int hashCode() {
    return Objects.hash(NAME);
  }

  /**
   * The toString method for Stop class
   * @return The String format for Stop
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(NAME);
    result.append(": [");
    for (Stop ne : this.neighbours.keySet()) {
      result.append(ne.NAME);
      result.append(" ");
    }
    result.append("]");
    return result.toString();
  }
}