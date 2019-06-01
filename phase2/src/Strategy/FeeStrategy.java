package Strategy;

import Manager.RouteManager;
import Transit.Stop;
import java.io.Serializable;
import java.util.ArrayList;

/** The fee strategy for card */
public abstract class FeeStrategy implements Serializable {

  /** The start station of this trip */
  private Stop startStation;

  /** The end station of this trip */
  private Stop endStation;

  /** The route number of the bus/subway */
  private String routeNum;

  /** The cap of the fee */
  public static final double FEE_CAP = 6.0;

  /** The constructor of fee strategy class */
  FeeStrategy(Stop startStation, Stop endStation, String routeNum) {
    this.startStation = startStation;
    this.endStation = endStation;
    this.routeNum = routeNum;
  }

  /** The constructor of fee strategy class */
  FeeStrategy() {}

  /**
   * Override the method calculateFare
   * @param alreadyCharged The money has already charged in last two hours
   * @return The fare of this trip
   */
  public abstract double calculateFare(double alreadyCharged);

  /**
   * The helper method for calculating the fare
   * @param willCharged The fare should be taken for this trip
   * @param alreadyCharged The money has already charged in last two hours
   * @return The money should be charged
   */
  double calculateFareHelper(double willCharged, double alreadyCharged) {
    double reduced;
    double afterDeducted = willCharged + alreadyCharged;
    if (afterDeducted > FEE_CAP) {
      reduced = FEE_CAP - alreadyCharged;
    } else {
      reduced = willCharged;
    }
    return reduced;
  }

  /**
   * The helper method to calculate how many stops
   * @return the number of stops
   */
  public int NumStopReached() {
    ArrayList<Stop> stops = RouteManager.getRouteManager().getAllRoutes().get(routeNum).getRoute();
    int firstIndex = stops.indexOf(startStation);
    int secondIndex = stops.indexOf(endStation);
    int distance;
    if (firstIndex < secondIndex) {
      distance = secondIndex - firstIndex;
    } else {
      distance = firstIndex - secondIndex;
    }
    return distance;
  }
}