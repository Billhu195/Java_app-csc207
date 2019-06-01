package Card;

import Trip.Trip;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * stores methods that view different statistics of ChargeableCard.
 */
public class CardStatistics implements Serializable {

  /**
   * Trips done by a ChargeableCard, which are stored by date in ChargeableCards.
   */
  private final HashMap<LocalDate, ArrayList<Trip>> trips;

  /**
   * constructs a CardStatistics
   * @param trips Trips done by a ChargeableCard
   */
  CardStatistics(HashMap<LocalDate, ArrayList<Trip>> trips) {
    this.trips = trips;

  }

  /**
   * A helper function for viewThreeRecentTrips(). Return num number of trips for a particular date.
   * @param date the date on which some trips might be performed by a ChargeableCard.
   * @param num number of trips requested to return in total.
   * @return some trips done by a ChargeableCard on a particular day that are
   * requested by viewThreeRecentTrips()
   */
  private String viewTripForDate(LocalDate date, int num) {
    StringBuilder result = new StringBuilder();
    if (trips.keySet().contains(date)) {
      ArrayList<Trip> todayTrips = trips.get(date);
      result.append(date.toString()).append(": \n");
      int first = todayTrips.size() - 1;
      int last = todayTrips.size() - 1 - num;
      for (int i = first; i > last ; i--){
        result.append(todayTrips.get(i).toString());
        result.append("\n");
      }
      return new String(result);
    }
    else {
      return "";
    }
  }

  /**
   * return shows the three recent trip which the ChargeableCard traveled.
   * @return String formation of the three recent trips including date, total cost, and trip.
   * Also inform the card holder if the card has not done any trip.
   */
  public String viewThreeRecentTrips() {
    if (trips.isEmpty()) {
      return "This card has not done any trip.";
    } else {
      ArrayList<LocalDate> dates = new ArrayList<>(trips.keySet());
      Collections.sort(dates);

      int num = 0;
      StringBuilder result = new StringBuilder();
      for (int i = dates.size() - 1; i >= 0 ; i--) {
        ArrayList<Trip> tripList = trips.get(dates.get(i));
        int previous_num = num;
        num += tripList.size();
        for (int j = tripList.size() - 1; j >= 0 ; j--) {
          if (num >= 3) {
            result.append(viewTripForDate(dates.get(i), 3 - previous_num));
            break;
          } else {
            result.append(viewTripForDate(dates.get(i), tripList.size()));
          }
        }
      }
      return result.toString();
    }
  }

  /**
   *
   * @param date the date on which card holder wants to see the trips that the ChargeableCard
   * has performed.
   * @return all trips performed by the ChargeableCard on that date. If no trips
   */
  public String viewTripForDate(LocalDate date) {
    StringBuilder result = new StringBuilder();
    if (trips.keySet().contains(date)) {
      ArrayList<Trip> todayTrips = trips.get(date);
      result.append(date.toString()).append(": \n");
      for (int i = todayTrips.size() - 1; i >= 0 ; i--){
        result.append(todayTrips.get(i).toString());
        result.append(" cost: ");
        result.append(todayTrips.get(i).getFeeTotal());
        result.append("\n");
      }
      return new String(result);
    }
    else {
      return ("This card has not done any trip for " + date);
    }
  }
}