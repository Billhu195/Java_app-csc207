package Trip;

import Manager.TripManager;
import Transit.*;
import Card.*;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Trip implements Serializable {
  /*a trip segment which only happen on one bus or subway*/

  /** Stop has been travelled in a single trip*/
  private ArrayList<Stop> stopsOfTrip;
  /** TimeLog happened in the system*/
  private ArrayList<LocalTime> timeLog;
  /** the total fee of a single trip*/
  private double feeTotal;
  /** boolean check if the card is already tap in*/
  private boolean isTappedIn;
  /** String format use for Trip*/
  private String stringFormat;
  /** store number of stop has been travelled*/
  private int numStopPassed;
  /** tripManager object*/
  private static TripManager tripManager;

  /**
   * Construct a new Trip by given card, start station, and start time
   * @param card Chargeable, card use for tap in this trip
   * @param startStation Stop, the start station of this trip
   * @param startTime LocalDateTime, the start time of this trip
   */
  public Trip(Chargeable card, Stop startStation, LocalDateTime startTime) {
    this.stopsOfTrip = new ArrayList<>();
    this.stopsOfTrip.add(startStation);
    this.timeLog = new ArrayList<>();
    this.timeLog.add(startTime.toLocalTime());
    this.feeTotal = 0;
    this.isTappedIn = true;
    this.stringFormat = "";
    tripManager.add(startTime.toLocalDate(), this);
    card.getCardHolder().addTrip(startTime.toLocalDate(), this);
    card.addTrip(startTime.toLocalDate(), this);
  }

  /**
   * Get current trip manager
   * @return current TripManager
   */
  public static TripManager getTripManager() {
    return tripManager;
  }

  /**
   * Set current trip manager into new trip manager
   * @param tripManager TripManager, new trip manager
   */
  public static void setTripManager(TripManager tripManager) {
    Trip.tripManager = tripManager;
  }

  /**
   * Get current passed number of stops
   * @return int current numStopPassed
   */
  public int getNumStopPassed() {
    return numStopPassed;
  }

  /**
   * Get current stops of trip
   * @return the current stops of trip
   */
  ArrayList<Stop> getStopsOfTrip() {
    return stopsOfTrip;
  }

  /**
   * Get current end stop
   * @return the last stop of stops of trip
   */
  Stop getEndStop() {
    return this.stopsOfTrip.get(stopsOfTrip.size() - 1);
  }

  /**
   * Get the current end time
   * @return the last time object of time log
   */
  public LocalTime getEndTime() {
    return this.getTimeLog().get(timeLog.size() - 1);
  }

  /**
   * Get the first time of this trip
   * @return the first time object of time log
   */
  LocalTime getFirstTime() {
    return this.getTimeLog().get(0);
  }

  /**
   * Get the current time log of this trip
   * @return the current time log
   */
  ArrayList<LocalTime> getTimeLog() {
    return timeLog;
  }

  /**
   * Get the current is tapped in
   * @return boolean, current is tapped in
   */
  boolean isTappedIn() {
    return isTappedIn;
  }

  /**
   * Set is tapped in into new is tapped in
   * @param tappedIn new is tapped in
   */
  void setTappedIn(boolean tappedIn) {
    isTappedIn = tappedIn;
    try {
      getTripManager().saveToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get the current total fee
   * @return the current total fee
   */
  public double getFeeTotal() {
    return feeTotal;
  }

  /**
   * Add the extra passed number of stops in current passed number of stops
   * @param numStopPassed int, the extra passed number of stops
   */
  void addNumStopPassed(int numStopPassed) {
    this.numStopPassed += numStopPassed;
    try {
      getTripManager().saveToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add the extra string format to current string format
   * @param stringFormat String, the extra string format
   */
  void addStringFormat(String stringFormat) {
    if (this.stringFormat.equals("")) {
      this.stringFormat = stringFormat;
    } else {
      this.stringFormat += ("  " + stringFormat);
    }
    try {
      getTripManager().saveToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add the new stop into stops of trip
   * @param stop Stop, the new stop
   */
  void addStopsOfTrip(Stop stop) {
    this.stopsOfTrip.add(stop);
    try {
      getTripManager().saveToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add an extra time object into time log
   * @param time LocalTime, extra time object
   */
  void addTimeLog(LocalTime time) {
    this.timeLog.add(time);
    try {
      getTripManager().saveToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add extra total fee to current extra total fee
   * @param feeTotal double, extra total fee
   */
  void addFeeTotal(double feeTotal) {
    this.feeTotal += feeTotal;
    try {
      Trip.getTripManager().saveToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Return the string format of current Trip
   * @return String, the string format of Trip
   */
  @Override
  public String toString() {
    return this.stringFormat;
  }
}