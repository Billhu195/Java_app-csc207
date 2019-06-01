package Trip;

import Card.*;
import Manager.RouteManager;
import Manager.TripManager;
import Strategy.BusFeeStrategy;
import Strategy.FeeStrategy;
import Strategy.SubwayFeeStrategy;
import Transit.Route;
import Transit.Stop;
import User.User;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/** Chargeable Tap class is the Tap Activity for all Chargeable cards. Cannot tap in if not within operation time. */
public class ChargeableTap implements TapActivity, Serializable {

  /** The message after tap. e.g.Telling the user this is an invalid trip */
  private String message = "";

  /**
   * Punishment of user: deducted 6 dollars by invalid tap_in or tap_out
   * @param card the card should be punished
   */
  private void punishment(Chargeable card) {
    card.charge(FeeStrategy.FEE_CAP);
  }

  /**
   * Remove the last invalid tap event such as tap in twice in a row.
   * @param lastTrip the last trip
   * @param endTime the end time
   * @param endStop the end stop
   * @param date current date
   * @param card the card that had invalid trip
   */
  private void removeInvalidTrip(Trip lastTrip, LocalTime endTime,
      Stop endStop, LocalDate date, Chargeable card) {
    lastTrip.getTimeLog().remove(endTime);
    lastTrip.getStopsOfTrip().remove(endStop);
    if (lastTrip.getStopsOfTrip().isEmpty() && lastTrip.getTimeLog().isEmpty()) {
      card.getTrips().get(date).remove(lastTrip);
      card.getCardHolder().getUserTrips().get(date).remove(lastTrip);
        TripManager.getTripManager().getAllTrips().get(date).remove(lastTrip);
    }
    if (card.getTrips().get(date).isEmpty()) { // remove today key if the trip is empty
      card.getTrips().remove(date);
    }
    if (card.getCardHolder().getUserTrips().get(date).isEmpty()) {
      card.getCardHolder().getUserTrips().remove(date);
    }
    if (TripManager.getTripManager().getAllTrips().get(date).isEmpty()) {
      TripManager.getTripManager().getAllTrips().remove(date);
    }
    try {
      saveData();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Check if the user is doing a valid tap in event.
   * @param card the card which taps in
   * @param tapInStop the location of the tap in event
   * @param tapInTime the time of the tap in event
   * @return a boolean that decides if this tap in event is valid
   */
  private boolean isValidTapIn(Chargeable card, Stop tapInStop, LocalDateTime tapInTime) {
    boolean isTappedIn;
    Stop endStop;
    LocalTime endTime;
    LocalDate date;
    // when tap in, need to check card balance.
    if (card.getBalance() <= 0) {
      message = "Invalid tap in: Please charge your card. You cannot enter the station at this time.";
      return false;
    } else if ((!card.isActivated())) {
      message = "Invalid tap in: This card was not activated.";
      return false;
    } else if (card.isSuspended()) {
      message = "Invalid tap in: This card was suspended.";
      return false;
    } else {
      date = tapInTime.toLocalDate();

      if (card.getTrips().keySet().contains(date)) {
        Trip lastTrip = card.getLastTripOfDate(date);
        isTappedIn = lastTrip.isTappedIn();
        // isTapped in should be false.
        // if true, then an error occurs and need to remove the error tap in, but could still start
        // a new trip.
        if (isTappedIn) {
          endStop = lastTrip.getEndStop();
          endTime = lastTrip.getEndTime();

          if (tapInStop.equals(endStop)) {
            message = "Valid tap in: You tapped in twice at the same station.";
          } else {
            punishment(card);
            message = "Valid tap in: Bad tap habit! Your card is charged for 6 dollars for punishment.";
            // after punishment, the card might not have enough balance.
            if (card.getBalance() <= 0) {
              message = "Invalid tap in: Not enough money after punishment for bad tap habit.";
              return false;
            }
          }
          // if the previous is tapped in, remove the last invalid trip
          removeInvalidTrip(lastTrip, endTime, endStop, date, card);
        }
      }
      // if the card has enough balance despite of the previous activity.
      return true;
    }
  }

  /**
   * Perform the tap in action
   * @param givenCard the card which taps in
   * @param tapInStop Stop when user tap in and store stop
   * @param tapInTime LocalDate Time when user tap and store time
   * @param feeStrategyType String feeType
   * @param routeNum String bus/subway route number
   * @return the string message about the result and status of this tap in activity.
   */
  public String TapIn(Card givenCard, Stop tapInStop, LocalDateTime tapInTime,
      String feeStrategyType, String routeNum) {

    Chargeable card = (Chargeable) givenCard;
    LocalDate tapInDate = tapInTime.toLocalDate();
    message = "";
    Route route = RouteManager.getRouteManager().getAllRoutes().get(routeNum);
    if (isValidTapIn(card, tapInStop, tapInTime) && route.checkNext(tapInStop, tapInTime.toLocalTime())) {
      if (card.getTrips().containsKey(tapInDate)) { // if today has trip.
        Trip lastTrip = card.getLastTripOfDate(tapInDate);
        Stop endStop = lastTrip.getEndStop();
        LocalTime firstTime = lastTrip.getFirstTime();

        // if Card.trips is not empty, then check if it is continuous with the last trip.
        // check if continuous with the last trip segment (within 120 minutes and same stop)
        long interval = ChronoUnit.MINUTES.between(firstTime, tapInTime);
        boolean isContinuous = (interval < 120.00 && tapInStop.equals(endStop));
        if (isContinuous) {
          lastTrip.addStopsOfTrip(tapInStop);
          lastTrip.addTimeLog(tapInTime.toLocalTime());
          lastTrip.setTappedIn(true);
        } else { // if not is_continuous.
          new Trip(card, tapInStop, tapInTime);
        }
      } else { // if Card trips is empty.
        new Trip(card, tapInStop, tapInTime);
      }
      // Now, Card.trips for this date has at least one trip.
      Trip lastTrip = card.getLastTripOfDate(tapInDate);
      if (feeStrategyType.equals("bus")) {
        FeeStrategy strategy = new BusFeeStrategy(card.getBusFee());
        double fee =
            strategy.calculateFare(
                lastTrip.getFeeTotal()); // returns the fee that needed to be deducted
        lastTrip.addFeeTotal(fee); // set the fee total of this trip.
        card.charge(fee); // charge the card.
      }
      lastTrip.setTappedIn(true);
      }
      if (message.equals("")) {
        message = "Valid Tap In";
    }
    if (!route.checkNext(tapInStop, tapInTime.toLocalTime())) {
      message = "Invalid Tap In: Transit system is now closed.";
    }
    try { // serialize the data
      saveData();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return message;
  }

  /**
   * Check if the user is doing a valid tap out event.
   * @param card the card which taps out
   * @param tapInStop the location of the tap out event
   * @param tapInTime the time of the tap out event
   * @return a boolean that decides if this tap out event is valid
   */
  private boolean isValidTapOut(Chargeable card, Stop tapInStop, LocalDateTime tapInTime) {

    LocalDate date = tapInTime.toLocalDate();
    if (card.isActivated()) {
      if (!card.getTrips().containsKey(date)) {// if today has no trip.
        message = "Invalid tap out: Bad tap habit! Your card is charged for 6 dollars for punishment.";
        punishment(card);
        return false;

      } else { // today has done some trip
        Trip lastTrip = card.getLastTripOfDate(date);
        boolean isTappedIn = lastTrip.isTappedIn();
        Stop endStop = lastTrip.getEndStop();
        LocalTime endTime = lastTrip.getEndTime();

        if (isTappedIn && endStop.isOnTheSameRoute(tapInStop)) {
          if (endStop.equals(tapInStop)) { // consider the case when user tap in and tap out at the same location.
            message = "Valid tap out: You have enter and exit at the same stop.";
            removeInvalidTrip(lastTrip, endTime, endStop, date, card);
            lastTrip.setTappedIn(false);
            return false;
          } else {
            message = "Valid tap out";
            lastTrip.setTappedIn(false);
            return true;
          }
        } else { // if isTappedIn, on the same route as the last tap in event and trips is not empty
          // last tap is a tap out or tap in at a different route as this one.
          message = "Invalid tap out: Bad tap habit! Your card is charged for 6 dollars for punishment.";
          punishment(card);
          // if it is not a valid tap out event, remove the last invalid tap activity.
          removeInvalidTrip(lastTrip, endTime, endStop, date, card);
          lastTrip.setTappedIn(false);
          return false;
        }
      }
    } else {
      message = "Invalid tap out: This card is not available.";
    }
    return false;
  }

  /**
   * Perform the tap out action
   * @param givenCard the card which taps out
   * @param tapInStop Stop when user tap out and store stop
   * @param tapInTime LocalDate Time when user tap and store time
   * @param feeStrategyType String feeType
   * @param routeNum String bus/subway route number
   * @return the string message about the result and status of this tap out activity.
   */
  public String TapOut(Card givenCard, Stop tapInStop, LocalDateTime tapInTime, String feeStrategyType, String routeNum) {
    Chargeable card = (Chargeable) givenCard;
    LocalDate date = tapInTime.toLocalDate();
    message = "";

    if (isValidTapOut(card, tapInStop, tapInTime)) {
      Trip lastTrip = card.getLastTripOfDate(date);
      Stop startStop = lastTrip.getEndStop();
      LocalTime startTime = lastTrip.getEndTime();
      lastTrip.addStopsOfTrip(tapInStop);
      lastTrip.addTimeLog(tapInTime.toLocalTime());
      lastTrip.addStringFormat(MakeStringTrip(startStop, tapInStop, startTime, tapInTime.toLocalTime(), routeNum, feeStrategyType));

      if (feeStrategyType.equals("subway")) {
        FeeStrategy subwayStrategy = new SubwayFeeStrategy(startStop, tapInStop, routeNum, card.getSubwayFee());
        double fee = subwayStrategy.calculateFare(lastTrip.getFeeTotal()); // returns the fee that needed to be deducted
        lastTrip.addFeeTotal(fee); // set the fee total of this trip.
        card.charge(fee); // charge the card.
        int numStationPassed = subwayStrategy.NumStopReached();
        lastTrip.addNumStopPassed(numStationPassed); // add the number of stations that is being passed.
      } else {
        FeeStrategy busStrategy = new BusFeeStrategy(startStop, tapInStop, routeNum, card.getBusFee());
        int numStationPassed = busStrategy.NumStopReached();
        lastTrip.addNumStopPassed(numStationPassed); // add the number of stations that is being passed.
      }
    }
    try {
      saveData();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return message;
  }

  /**
   * Return the string format of current start stop, current end stop, current start time,
   * current end time, current route number, and current route type
   * @param startStop Stop, current start stop
   * @param endStop Stop, current end stop
   * @param startTime LocalTime, current start time
   * @param endTime LocalTime, current end time
   * @param routeNum String, current route number
   * @param routeType String, current route type
   * @return the String format
   */
  private String MakeStringTrip(Stop startStop, Stop endStop, LocalTime startTime,
      LocalTime endTime, String routeNum, String routeType) {
    return routeType + " " + routeNum + ": " + startTime + " " + startStop.getNAME() +
        " -> " + endTime + " " + endStop.getNAME();
  }

  /**
   * Serialize the data.
   * @throws IOException saveToFile() would raise IOException.
   */
  private void saveData() throws IOException {
    Stop.getStopManager().saveToFile();
    Card.getCardManager().saveToFile();
    Trip.getTripManager().saveToFile();
    User.getUserManager().saveToFile();
    Route.getRouteManager().saveToFile();
  }
}