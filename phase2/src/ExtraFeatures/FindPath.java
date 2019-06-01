package ExtraFeatures;

import Card.Chargeable;
import Manager.*;
import Strategy.*;
import Transit.*;
import java.util.ArrayList;
import Exception.*;

/**
 * Find paths between two stations of user input
 */
public class FindPath extends DijkstraAlgorithm {

  /**
   * constructs FindPath to find a path according to the given type.
   * @param type "shortest" or "cheapest". * Decide the ways to handle edge values to find different
   * paths.
   */
  public FindPath(String type) {
    super(type);
  }

  /**
   * A helper function that checks if all stop in tracker are on the same route.
   * @param tracker a list of adjacent stops
   * @return true if if all stop in tracker are on the same route, otherwise false.
   */
  private boolean checkTracker(ArrayList<Stop> tracker) {
    for (String key : RouteManager.getRouteManager().getAllRoutes().keySet()) {
      Route route = RouteManager.getRouteManager().getAllRoutes().get(key);
      ArrayList<Boolean> isContinuous = new ArrayList<>();
      for (Stop stop : tracker) {
        if (!route.getRoute().contains(stop)) {
          isContinuous.add(false);
        } else {
          isContinuous.add(true);
        }
      }
      if (checkBooleanList(isContinuous)) {
        return true;
      }
    } return false;
  }

  /**
   * A helper functiom that performs "&&" AND operation on the given booleanList.
   * @param booleanList a list of booleans
   * @return the result of performing AND operation on booleanList.
   */
  private boolean checkBooleanList (ArrayList<Boolean> booleanList) {
    boolean rowResult = true;
    for (Boolean el: booleanList) {
      rowResult = el && rowResult;
    }
    return rowResult;
  }

  /**
   * Find the index of entry stop and exit stop for every segment of the trip in path (an ArrayList
   * of stops for the recommended path).
   * @param source start stop given by the user
   * @param target end stop given by the user
   * @return a list of indexes
   * @throws InvalidInputException If the start stop equals to end stop,
   * then it is an invalid input.
   */
  private ArrayList<Integer> FindIndexPath(Stop source, Stop target) throws InvalidInputException {
    ArrayList<Integer> index = new ArrayList<>();
    if (!source.equals(target)) {
      ArrayList<Stop> path = getPath(target); //an ArrayList of stops for the recommended path
      if ((path != null) && (!path.isEmpty())) {
        if (path.size() == 2) {
          index.add(0);
          index.add(1);
          return index;
        } else {
          index.add(0);
          int i = 2;
          int j = 0;
          ArrayList<Stop> tracker = new ArrayList<>();
          for (; i <= path.size(); ) {
            for (; j < i; j++) {
              Stop currStop = path.get(j);
              if (!tracker.contains(currStop)) {
              tracker.add(currStop);
              }
              if (tracker.size() >= 2) {
                if (!checkTracker(tracker)) {
                  // if after adding the currStop, the adjacent stops in the list are not on the
                  // same route, then record the index of the stop before the currStop (which
                  // is the transfer point)
                  int next = path.indexOf(tracker.get(tracker.size() - 2));
                  index.add(next);
                  index.add(next);
                  // finding the the next segment of the path by setting j = next and tracker to
                  // and empty ArrayList,
                  j = next;
                  tracker = new ArrayList<>();
                  tracker.add(path.get(next));
                }
              }
              if (j >= path.size()) {
                break;
              }
            }
            i++;
          }
          // add the last index of the path to indexList.
          index.add(path.size() - 1);
          return index;
        }
      }
    } else { // if path is null or empty.
        throw new InvalidInputException("Invalid Input!");
    }
    return index;
  }

  /**
   * Find different choices ( ArrayList<ArrayList<String>>) of route information
   * (ArrayList<String> stores route number and route type for the transfer) for each
   * transfer.
   *
   * @param indexList list of indexes of transfer point
   * @param path recommended path
   * @return different choices of route information for each transfer.
   */
  private ArrayList<ArrayList<ArrayList<String>>> FindRouteList(
      ArrayList<Integer> indexList, ArrayList<Stop> path) {

    int i = 0;
    ArrayList<ArrayList<ArrayList<String>>> resultList = new ArrayList<>();
    for (; i < (indexList.size() / 2); i++) {
      ArrayList<ArrayList<String>> referList = new ArrayList<>();
      int currentIndex = indexList.get(2 * i);
      int endIndex = indexList.get(2 * i + 1);
      Stop startStop = path.get(currentIndex);
      for (int j = currentIndex + 1; j <= endIndex; j++) {
        ArrayList<ArrayList<String>> temp;
        ArrayList<ArrayList<String>> temp2 = new ArrayList<>();
        temp = startStop.FindSameRoute(path.get(j));
        if (referList.isEmpty()) {
          referList = new ArrayList<>(temp);
        } else {
          for (ArrayList<String> aTemp : temp) {
            if (referList.contains(aTemp)) {
              temp2.add(aTemp);
            }
          }
        referList = new ArrayList<>(temp2);
        }
      }
      resultList.add(referList);
    }
    return resultList;
  }

  /**
   * Calculate the time duration of the recommended path
   * @param startIndex  the beginning index of a path segment
   * @param endIndex the end index of a path segment
   * @param path recommended path
   * @param routeNum transit route number of this path segment
   * @return the time duration of the path.
   */
  private int calculatePathTime(int startIndex, int endIndex, ArrayList<Stop> path, String routeNum) {
    Route route = RouteManager.getRouteManager().getAllRoutes().get(routeNum);
    int speed = route.getSpeed();
    int time = 0;
    for (int i = startIndex ; i < endIndex; i ++) {
      Stop current = path.get(i);
      Stop next = path.get(i + 1);
      int edgeValue = current.getNeighbours().get(next); // the arbitrary distance between two stops.
      time += edgeValue * speed; // different route has different speed.
    }
    return time;
  }

  /**
   * calculate the total distance of the recommended path
   * @param path recommended path
   * @return distance of the path
   */
  private int calculatePathDistance(ArrayList<Stop> path) {
    int i = 0;
    int distance = 0;
    for (; i < path.size() - 1; i ++) {
      Stop current = path.get(i);
      Stop next = path.get(i + 1);
      int edgeValue = current.getNeighbours().get(next);
      distance += edgeValue;
    }
    return distance;
  }

  /**
   * the total cost of this recommended path (calculated based on different fare rates)
   * @param index the list of indexes indicating path segments.
   * @param path the recommended path
   * @param cardType different types of card (which have different fare type)
   * @param transitionString route information of trip segments.
   * @return total cost of the recommended path
   */
  private double calculatePathCost(ArrayList<Integer> index, ArrayList<Stop> path,
      String cardType, ArrayList<ArrayList<ArrayList<String>>> transitionString) {

    double cost = 0;
    int i = 0;
    for (; i < (index.size() / 2); i++) {
      int currentIndex = index.get(2 * i);
      int endIndex = index.get(2 * i + 1);
      ArrayList<String> routeInfo = transitionString.get(i).get(0);
      Stop startStop = path.get(currentIndex);
      Stop endStop = path.get(endIndex);

      // calculate cost for the segment of the path according to cardType and feeStrategy type.
      if (!cardType.equals("monthlycard")) {
        if (routeInfo.get(0).equals("bus")) {
          FeeStrategy feeStrategy = new BusFeeStrategy(Chargeable.decideFee(cardType).get(0));
          cost += feeStrategy.calculateFare(cost);
        } else {
          FeeStrategy feeStrategy =
              new SubwayFeeStrategy(
                  startStop, endStop, routeInfo.get(1), (Chargeable.decideFee(cardType).get(1)));
          cost = cost + feeStrategy.calculateFare(cost);
        }
      }
    }
    return cost;
  }

  /**
   *
   * @param index the list of indexes indicating path segments.
   * @param path the recommended path
   * @param transitionString different options of taking transit for each path segment
   * (stores route number and type)
   * @return total time and the string representation of the recommended path.
   */
  private ArrayList<String> TransitionString (ArrayList<Integer> index, ArrayList<Stop> path,
      ArrayList<ArrayList<ArrayList<String>>> transitionString) {

    int time = 0;
    ArrayList<String> result = new ArrayList<>();
    StringBuilder transition = new StringBuilder();

    int i = 0;
    for (; i < (index.size()/2); i ++) {
      int currentIndex = index.get(2 * i);
      int endIndex = index.get(2 * i + 1);
      ArrayList<String> routeInfo = transitionString.get(i).get(0);
      Stop startStop = path.get(currentIndex);
      Stop endStop = path.get(endIndex);
      String routeType = routeInfo.get(0);
      String routeNum = routeInfo.get(1);
      String info = routeType + routeNum;

      // calculate time for each path segment
      time += calculatePathTime(currentIndex, index.get(i + 1), path, routeNum);

      // make path segment string representations.
      transition.append("Get on ").append(info).append(" at ").append(startStop.getNAME())
          .append("\n");
      transition.append("Get off ").append(info).append(" at ").append(endStop.getNAME())
          .append("\n");
    }
    result.add(String.valueOf(time));
    result.add(transition.toString());
    return result;
  }

  /**
   *
   * @param transit string representation of the recommended path
   * @param time total time of the path
   * @param distance total distance of the path
   * @param cost total cost of the path
   * @param cardType type of card.
   * @return String representation of the path plus all the path information (time, distance, cost)
   */
  private ArrayList<String> makeStringPath(String transit, String time, String distance, String cost, String cardType) {
    StringBuilder result = new StringBuilder(transit);
    ArrayList<String> temp = new ArrayList<>();

    result.append("The time duration of this recommended trip is ");
    result.append(time);
    result.append("\n");
    result.append("The distance of this recommended trip is ");
    result.append(distance);
    result.append("\n");
    if (!cardType.equals("monthlycard")) {
      result.append("Total cost of this recommended trip is ");
      result.append(cost);
    }
    temp.add(result.toString());
    temp.add(String.valueOf(time));
    temp.add(String.valueOf(cost));
    temp.add(String.valueOf(distance));

    return temp;
  }

  /**
   * Find path according to the given source and target stops.
   * Calculate path cost based on cardType.
   * @param source the source stop given by the input
   * @param target the target stop given by the input
   * @param cardType card type.
   * @return all the string information of this recommended path
   */
  public ArrayList<String> findPath(Stop source, Stop target, String cardType) {
    execute(source);
    ArrayList<Stop> path = getPath(target);
    ArrayList<String> temp = new ArrayList<>();

    if (path != null && !path.isEmpty()) {

      ArrayList<Integer> index = new ArrayList<>();
      ArrayList<ArrayList<ArrayList<String>>> transitionString = new ArrayList<>();
      try {
        index = FindIndexPath(source, target);
        transitionString = FindRouteList(index, path);
      } catch (InvalidInputException e) {
        e.printStackTrace();
      }
      int distance = calculatePathDistance(path);
      double cost = calculatePathCost(index, path, cardType, transitionString);
      ArrayList<String> stringPath = TransitionString(index, path, transitionString);
      int time = Integer.parseInt(stringPath.get(0));
      String transition = stringPath.get(1);

      return makeStringPath(transition, String.valueOf(time), String.valueOf(distance),
          String.valueOf(cost), cardType);
    }
    temp.add("No path is available.");
    return temp;
  }
}