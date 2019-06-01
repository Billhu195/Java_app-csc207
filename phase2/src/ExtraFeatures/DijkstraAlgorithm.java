package ExtraFeatures;

import Transit.Stop;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Citation: http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
 *
 * Code is modified specific to this project to find different kinds of available paths between
 * two stations.
 */
class DijkstraAlgorithm {

  private ArrayList<Stop> settledNodes; // stops which have been handled.
  private ArrayList<Stop> unSettledNodes; // stops waiting for handling.
  private HashMap<Stop, Stop> predecessors; // stores the previous stop for a stop.
  private HashMap<Stop, Integer> distance; // distance between the stop and the starting point.
  private final String type; // Decide the ways to handle edge values to find different paths.

  /**
   * constructs the DijkstraAlgorithm
   * @param type "shortest" or "cheapest".
   * Decide the ways to handle edge values to find different paths.
   */
  DijkstraAlgorithm(String type){
    this.type = type;
  }

  /**
   * Construct the two HashMaps predecessors (set the previous stop for every stop in the map)
   * and distance according to the given source stop.
   * @param source the starting stop.
   */
  void execute(Stop source) {
    settledNodes = new ArrayList<>();
    unSettledNodes = new ArrayList<>();
    distance = new HashMap<>();
    predecessors = new HashMap<>();
    distance.put(source, 0); // the distance of the source stop to itself is 0
    unSettledNodes.add(source); // handle this source stop
    while (unSettledNodes.size() > 0) {
      Stop node = getMinimum(unSettledNodes);
      settledNodes.add(node);
      unSettledNodes.remove(node);
      findMinimalDistances(node);
    }
  }

  /**
   * Set the distance and the previous stop value for each neighbour of this node by
   * finding the minimum distance between node and its neighbour (target).
   * @param node the current node which we are looking at
   */
  private void findMinimalDistances(Stop node) {
    ArrayList<Stop> adjacentNodes = getNeighbors(node);
    for (Stop target : adjacentNodes) {
      if (getShortestDistance(target)
          > getShortestDistance(node) // distance from the start.
              + getDistance(node, target)) { // edge value between them
        distance.put(target, getShortestDistance(node) + getDistance(node, target));
        predecessors.put(target, node); // next: previous
        unSettledNodes.add(target);
      }
    }
  }

  /**
   * Gets the distance (edge value) between node and target according to the given type.
   * This method is specific to this project.
   * @param node the current stop
   * @param target the target stop
   * @return return the integer value of the distance between the stops.
   */
  private int getDistance(Stop node, Stop target) {
    if (this.type.equals("shortest")) {
      if (getNeighbors(node).contains(target)) {
        return node.getNeighbours().get(target); // regular edge value.
      } else { // the two given stops are not adjacent, which should not happen.
        throw new RuntimeException("Unable to get distance of"+node.toString()+"->"+target.toString());
      }
    } else { // type is "cheapest"
      return 1; // to find the cheapest path, set the distance between the two stops as 1 to make
      // the path has as less stops traveled as possible to minimize cost.
    }
  }

  /**
   * Get neighbours for the node.
   * @param node A stop
   * @return all neighbours of this stop.
   */
  private ArrayList<Stop> getNeighbors(Stop node) {
    ArrayList<Stop> neighbors = new ArrayList<>();
    for (Stop stop : node.getNeighbours().keySet()) {
      if (!isSettled(stop)) { // avoid repeated value
        neighbors.add(stop);
      }
    }
    return neighbors;
  }

  /**
   * Get the stop in the stopList which has the shortest distance to the source stop.
   * @param stopList a list of stops
   * @return the stop which has the minimum distance to the source stop among the stopList.
   */
  private Stop getMinimum(ArrayList<Stop> stopList) {
    Stop minimum = null;
    for (Stop vertex : stopList) {
      if (minimum == null) {
        minimum = vertex;
      } else {
        if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
          minimum = vertex;
        }
      }
    }
    return minimum;
  }

  /**
   * Check if this stop has already been handled.
   * @param stop a stop upon request
   * @return a boolean representing if this stop is in the hashMap settledNodes.
   */
  private boolean isSettled(Stop stop) {
    ArrayList<String> settledNames = new ArrayList<>();
    for (Stop settledNode : settledNodes) {
      settledNames.add(settledNode.getNAME());
    }
    return settledNames.contains(stop.getNAME());
  }

  /**
   * Find the stop distance value whose distance is a minimum.
   * @param destination the neighbour of the node which we are looking at.
   * @return the node distance value whose distance is a minimum.
   */
  private int getShortestDistance(Stop destination) {
    Integer d = distance.get(destination);
    if (d == null) { // set that path distance to a maximum value if the destination is unavailable.
      return Integer.MAX_VALUE;
    } else { // otherwise return the corresponding distance value in the HashMap distance.
      return d;
    }
  }

  /**
   * returns the path from the source to the selected target and
   * NULL if no path exists
   * @param target target stop requested by the user.
   * @return a list of stop (possible path from the source stop to target stop).
   */
  ArrayList<Stop> getPath(Stop target) {
    ArrayList<Stop> path = new ArrayList<>();
    Stop step = target;
    // check if a path exists
    if (predecessors == null || predecessors.get(step) == null) {
      return null;
    } else {
      path.add(step);
      while (predecessors.get(step) != null) {
        step = predecessors.get(step);
        path.add(step);
      }
      // Put it into the correct order
      Collections.reverse(path);
    }
    return path;
  }
}