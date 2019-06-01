package Statistics;

import Manager.TripManager;
import Trip.Trip;
import User.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * CardHolders and AdminUsers can view the following types of statistics
 */
public class ViewStatistics implements Serializable {
  /*** HashMap to store Time and Date for each CardHolder's trip */
  private HashMap<LocalDate, ArrayList<Trip>> trips;
  /** To store monthly card's expire Month*/
  private Integer expiryMonth;
  /** To Store a object User*/
  private User user;

  /**
   * To set the maximum time range for each user;
   * AdminUser can view stats up to 5 years;
   * CardUser can view stats up to 1 years;
   * @param user User, card user or admin user
   */
  public ViewStatistics(User user) {
    this.user = user;
    if (user instanceof AdminUser) {
      trips = TripManager.getTripManager().getAllTrips();
      expiryMonth = 60; // 5 years.
    } else if (user instanceof CardHolder) {
      trips = ((CardHolder) user).getUserTrips();
      expiryMonth = 12; // 1 year.
    }
  }

  /**
   * Create a time range in a list from current date year to the desire one;
   * @return ArrayList</LocalDate>
   */
  private ArrayList<LocalDate> availableDateToView() {
    LocalDate toDate = LocalDate.now();
    LocalDate fromDate = toDate.minusMonths(expiryMonth); // can only view recent three years.
    DateRange range = new DateRange(fromDate, toDate);
    ArrayList<LocalDate> dates = range.toList();
    Collections.sort(dates);
    return dates;
  }

  /**
   * From a date time to get a trip's sum up total money cost
   * @param date local time date, or the trip's date
   * @return Total money cost/revenue
   */
  public double totalMoneyForDate(LocalDate date) {
    double revenue = 0;

    if (trips.containsKey(date)) {
      for (Trip trip: trips.get(date)) {
        revenue += trip.getFeeTotal();
      }
    }
    return revenue;
  }

  /**
   * Return Total Station users have sum up travelled
   * @param date local date, or trip's date
   * @return total station passed
   */
  private int totalStationForDate(LocalDate date) {
    int stationPassed = 0;

    if (trips.containsKey(date)) {
      for (Trip trip : trips.get(date)) {
        stationPassed += trip.getNumStopPassed();
      }
    }
    return stationPassed;
  }

  /**
   * Given initial date to end date return String of sum up cost/revenue of all users
   *
   * @param fromDate init local date
   * @param toDate  end local date
   * @return String, Total revenue from date 2018/7/11 to date 2018/7/12 is 500 dollars
   */
  public String totalMoneyForRange(LocalDate fromDate, LocalDate toDate) {

    double revenue = 0;
    String result = "";
    LocalDate todayDate = LocalDate.now();
    long monthDuration = ChronoUnit.MONTHS.between(toDate , todayDate);
    if (monthDuration > expiryMonth) {
      // can only view data for the recent 1 (for user) 5 (for admin) years.
      // data before which point would be deleted.
      result = "Invalid Date Input!\nYou can only view data for the recent ";
      result += String.valueOf(expiryMonth / 12);
      result += " year(s)";
      return result;

      // check invalid data input.
    } else if (availableDateToView().get(0).isAfter(fromDate) || LocalDate.now().isBefore(toDate) ||
        fromDate.isAfter(toDate)) {
      return "Invalid Date Input!";
    } else {
      // the list of the dates between the from and the to date
      DateRange range = new DateRange(fromDate, toDate);
      ArrayList<LocalDate> dates = range.toList();

      for (LocalDate date : dates) {
          revenue += totalMoneyForDate(date);
      }

      if (user instanceof CardHolder) {
        result += "Total cost" ;
        result += " for user ";
        result += user.getEmail();
        result += "\n";
      } else {
        result += "Total revenue" ;
      }
      result += " from date ";
      result += fromDate.toString();
      result += "\n";
      result += " to date ";
      result += toDate.toString();
      result += " is: ";
      result += String.valueOf(revenue);
      result += " dollars.";
    }
    return result;
  }

  /**
   * Given initial date to end date return a String of sum up stations passed of all users
   * @param fromDate init local date
   * @param toDate  end local date
   * @return sum up all user's total travelled stations
   */
  // Total station passed from date 2018/7/11 to date 2018/7/12 is 5
  public String totalStationForRange(LocalDate fromDate, LocalDate toDate) {

    int stationPassed = 0;
    String result = "";
    LocalDate todayDate = LocalDate.now();
    long monthDuration = ChronoUnit.MONTHS.between(toDate , todayDate);
    if (monthDuration > expiryMonth) {
      result = "Invalid Date Input!\nYou can only view data for the recent ";
      result += String.valueOf(expiryMonth / 12);
      result += " year(s)";
      return result;
    } else if (availableDateToView().get(0).isAfter(fromDate) || LocalDate.now().isBefore(toDate) ||
        fromDate.isAfter(toDate)) {
      return "Invalid Date Input!";
    } else {
      DateRange range = new DateRange(fromDate, toDate);
      ArrayList<LocalDate> dates = range.toList();

      for (LocalDate date : dates) {
          stationPassed += totalStationForDate(date);
      }
      result += "Total station from date ";
      result += fromDate.toString();
      result += "\n";
      result += " to date ";
      result += toDate.toString();
      result += " is: ";
      result += String.valueOf(stationPassed);
    }
    return result;
  }

  /**
   * Given initial date to end date return a String of cost/revenue of all users
   * @param fromDate init local date
   * @param toDate  end local date
   * @return String of each date's revenue
   */
  // Total revenue for date 7/23: 50 dollars
  // Total revenue passed for date 7/24: 60 dollars
  public String MoneyForRange(LocalDate fromDate, LocalDate toDate) {
    LocalDate todayDate = LocalDate.now();
    long monthDuration = ChronoUnit.MONTHS.between(toDate , todayDate);
    StringBuilder result = new StringBuilder();
    if (monthDuration > expiryMonth) {
      result.append("Invalid Date Input!\nYou can only view data for the recent ");
      result.append(String.valueOf(expiryMonth/12));
      result.append(" year(s)");
      return result.toString();
    } else if (availableDateToView().get(0).isAfter(fromDate) || LocalDate.now().isBefore(toDate) ||
        fromDate.isAfter(toDate)) {
      return "Invalid Date Input!";
    }
    else {
      ArrayList<LocalDate> dates = new ArrayList<>();
      if (fromDate.equals(toDate)) {
        dates.add(toDate);
      } else {
        DateRange range = new DateRange(fromDate, toDate);
        dates = range.toList();
      }
      for (LocalDate date : dates) {
        if (trips.keySet().contains(date)) {
          if (user instanceof CardHolder) {
            result.append("Total cost for date ");
          } else {
            result.append("Total revenue for date ");
          }
          result.append("\n");
          result.append(date.toString());
          result.append(": ");
          result.append(totalMoneyForDate(date));
          result.append(" dollars");
          result.append("\n");
        }
      }
    }
    if (result.length() == 0) {
      if (user instanceof CardHolder) {
        result.append("There's no money cost from ").append("\n").append(fromDate.toString()).append(" \nto ")
            .append(toDate.toString());
      } else {
        result.append("There's no revenue from ").append("\n").append(fromDate.toString()).append(" \nto ")
            .append(toDate.toString());
      }
    }
    return result.toString();
  }

  /**
   * Given initial date to end date range return a String of stations of all users of each date
   * @param fromDate init local date
   * @param toDate  end local date
   * @return Each dates's station travelled
   */
  // Total station passed for date 7/23: 50
  // Total station passed for date 7/24: 60
  public String StationForRange(LocalDate fromDate, LocalDate toDate) {
    LocalDate todayDate = LocalDate.now();
    long monthDuration = ChronoUnit.MONTHS.between(toDate , todayDate);
    StringBuilder result = new StringBuilder();
    if (monthDuration > expiryMonth) {
      result.append("Invalid Date Input!\nYou can only view data for the recent ");
      result.append(String.valueOf(expiryMonth/12));
      result.append(" year(s)");
      return result.toString();
    } else if (availableDateToView().get(0).isAfter(fromDate) || LocalDate.now().isBefore(toDate) ||
        fromDate.isAfter(toDate)) {
      return "Invalid Date Input!";
    } else {
      ArrayList<LocalDate> dates = new ArrayList<>();
      if (fromDate.equals(toDate)) {
        dates.add(toDate);
      } else {
        DateRange range = new DateRange(fromDate, toDate);
        dates = range.toList();
      }
      for (LocalDate date : dates) {
        if (trips.keySet().contains(date)) {
          result.append("Total station passed for date ");
          result.append("\n");
          result.append(date.toString());
          result.append(": ");
          result.append(totalStationForDate(date));
          result.append("\n");
        }
      }
    }
    if (result.length() == 0) {
      result.append("There's no station passed from ").append("\n").append(fromDate.toString()).append(" \nto ")
          .append(toDate.toString());
    }
    return result.toString();
  }
}