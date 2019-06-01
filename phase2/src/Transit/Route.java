package Transit;

import Manager.RouteManager;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

/** Route class */
public class Route implements Serializable {

  /** The type of route. e.g. bus/subway */
  private String routeType;

  /** The route number of bus/subway */
  private String routeNum;

  /** An ArrayList of the whole route */
  private ArrayList<Stop> route;

  /** The time that the first bus/subway arrives */
  private LocalTime startTime;

  /** The time that the last bus/subway arrives */
  private LocalTime endTime;

  /** The time of the interval between two buses/subways */
  private int interval;

  /** The speed of the bus/subway */
  private int speed;

  /** The Manager used for the Route class */
  private static RouteManager routeManager;

  /** The constructor for the Route class */
  public Route(
      String routeType,
      String routeNum,
      ArrayList<Stop> route,
      LocalTime startTime,
      LocalTime endTime,
      int interval,
      int speed) {
    this.routeType = routeType;
    this.routeNum = routeNum;
    this.route = route;
    this.startTime = startTime;
    this.endTime = endTime;
    this.interval = interval;
    this.speed = speed;
  }

  /**
   * The method for getting the RouteManager
   * @return RouteManager
   */
  public static RouteManager getRouteManager() {
    return routeManager;
  }

  /**
   * The method used to set the routeManager
   * @param routeManager The manager used for the Route class
   */
  public static void setRouteManager(RouteManager routeManager) {
    Route.routeManager = routeManager;
  }

  /**
   * The method for getting the Route number
   * @return Route number
   */
  public String getRouteNum() {
    return routeNum;
  }

  /**
   * The method for getting the ArrayList of the whole Route
   * @return the ArrayList of the whole Route
   */
  public ArrayList<Stop> getRoute() {
    return route;
  }

  /**
   * The method for getting the type of the Route
   * @return Route type
   */
  String getRouteType() {
    return routeType;
  }

  /**
   * The method for getting the start time that the first bus/subway arrives
   * @return Start time
   */
  private LocalTime getStartTime() {
    return startTime;
  }

  /**
   * The method for getting the end time that the first bus/subway arrives
   * @return End time
   */
  private LocalTime getEndTime() {
    return endTime;
  }

  /**
   * The method for getting the time of the interval between two bus/subway
   * @return time interval
   */
  private int getInterval() {
    return interval;
  }

  /**
   * The method for getting the speed of the bus/subway
   * @return speed
   */
  public int getSpeed() {
    return speed;
  }

  /**
   * The method to calculate the time of the next bus/subway for a Stop in this Route. This method
   * will calculate both forward direction and backward direction.
   * @param currStop The current Stop that the user is.
   * @param now The current time
   * @return A string tells the user when and where is the next bus/subway.
   */
  public String nextTransit(Stop currStop, LocalTime now) {
    String result = "";
    ArrayList<Stop> list = this.getRoute();
    int index = list.indexOf(currStop); // The index for the currStop in the route list
    int forwardTime;
    int backwardTime;
    String nextStop = "";
    String prevStop = "";
    if (index == list.size() - 1) { // There is no bus/subway coming from backward direction
      backwardTime = 0;
      forwardTime = nextTransitTime(currStop, now, true);
      prevStop = list.get(index - 1).getNAME();
    } else if (index == 0) { // There is no bus/subway coming from forward direction
      backwardTime = nextTransitTime(currStop, now, false);
      forwardTime = 0;
      nextStop = list.get(index + 1).getNAME();
    } else {
      forwardTime = nextTransitTime(currStop, now, true);
      backwardTime = nextTransitTime(currStop, now, false);
      prevStop = list.get(index - 1).getNAME();
      nextStop = list.get(index + 1).getNAME();
    }
    // first line for the forward direction
    if (forwardTime == -1) { // if there is no bus/subway for forward direction
      result += "There is no bus/subway coming from ";
      result += prevStop;
      result += "\n";
    } else if (forwardTime != 0) {
      result += "The next bus/subway from ";
      result += prevStop;
      result += " is in ";

      result += forwardTime;
      result += " min";
      result += "\n";
    }

    // second line for the backward direction
    if (backwardTime == -1) { // if there is no bus/subway for backward direction
      result += "There is no bus/subway coming from ";
      result += nextStop;
      result += "\n";
    } else if (backwardTime != 0) {
      result += "The next bus/subway from ";
      result += nextStop;
      result += " is in ";
      result += backwardTime;
      result += " min";
    }
    return result;
  }

  /**
   * The helper method for calculating the time of the next forward direction bus/subway Also this
   * method can be used to determine is there a bus/subway coming.
   * @param currStop Current Stop that the user is
   * @param now The current time
   * @param forward true if it is forward direction, false if not
   * @return An int number which is the time for the next bus. -1 means no bus/subway coming.
   */
  private int nextTransitTime(Stop currStop, LocalTime now, boolean forward) {
    LocalTime startTime = this.getStartTime();
    LocalTime endTime = this.getEndTime();
    int result;
    int date = 0;
    LocalTime prevEnd = endTime;
    ArrayList<Stop> list = this.getRoute();
    ArrayList<Stop> copy = new ArrayList<>(list);
    if (!forward) {
      Collections.reverse(copy);
    }
    int index = copy.indexOf(currStop);
    while (index > 0) { // Update the startTime and endTime to the time of the first bus/subway
      Stop thisStop = copy.get(index);
      Stop prevStop = copy.get(index - 1);
      Integer timeCost = thisStop.getNeighbours().get(prevStop) * this.speed;
      startTime = startTime.plusMinutes(timeCost);
      endTime = endTime.plusMinutes(timeCost);
      index--;
    }
    // if endTime is on next day
    if (endTime.compareTo(prevEnd) < 0 || endTime.compareTo(startTime) < 0) {
      date++;
    }
    result = calculateNextTransitTime(now, startTime, endTime, this.getInterval(), date);
    return result;
  }

  /**
   * The helper method to calculate the actual time for the next bus/subway
   * @param now The current time
   * @param startTime The start time of the Stop
   * @param endTime The end time of the Stop
   * @param interval The time interval of the Route
   * @param date 1 if the end time is on the next day, 0 if not.
   * @return The time for the next bus/subway
   */
  private int calculateNextTransitTime(
      LocalTime now, LocalTime startTime, LocalTime endTime, int interval, int date) {
    int result;
    int endTimeToMin = (endTime.getHour() + date * 24) * 60 + endTime.getMinute();
    int startTimeToMin = startTime.getHour() * 60 + startTime.getMinute();
    int nowToMin = now.getHour() * 60 + now.getMinute();
    if (date > 0 && now.compareTo(endTime) < 0) {
      nowToMin = nowToMin + date * 24 * 60;
    }
    if (nowToMin < startTimeToMin
        && now.compareTo(this.startTime) > 0) { // if the first bus/subway is still coming
      result = startTimeToMin - nowToMin;
    } else if (nowToMin >= startTimeToMin
        && nowToMin
            < endTimeToMin) { // if the last bus/subway has already gone or the first bus/subway
      // hasn't started yet
      int time = nowToMin - startTimeToMin;
      result = interval - time % interval;
    } else { // if the first bus/subway has already gone
      return -1;
    }
    return result;
  }

  /**
   * The method to check if there is bus/subway coming for both directions
   * @param currStop The Stop that needs to be checked
   * @param now The current time
   * @return true if both directions have bus/subway which are coming, false if not.
   */
  public boolean checkNext(Stop currStop, LocalTime now) {
    int forwardTime = nextTransitTime(currStop, now, true);
    int backwardTime = nextTransitTime(currStop, now, false);
    return forwardTime != -1 && backwardTime != -1 && forwardTime != 0 && backwardTime != 0;
  }

  /**
   * The toString method for Route class
   * @return The String format for Route
   */
  @Override
  public String toString() {
    StringBuilder stops = new StringBuilder();
    for (Stop stop : route) {
      stops.append(stop.getNAME());
      stops.append(" ");
    }
    return stops.toString();
  }
}