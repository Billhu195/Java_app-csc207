package Card;

import Trip.Trip;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/** An chargeable interface for all cards that can be charged. */
public interface Chargeable extends Tappable {

  /**
   * add balance to the card
   *
   * @param amount the amount of the money need to be added
   */
  String addBalance(int amount);

  /**
   * charge money from card
   *
   * @param fee the amount of the money need to be charged
   */
  void charge(double fee);

  /**
   * add a trip into the card field Trips (a HashMap of date to ArrayList of Trip)
   *
   * @param date the date of that trip
   * @param trip the trip need to be added
   */
  void addTrip(LocalDate date, Trip trip);

  /**
   * get the card balance
   *
   * @return the card balance
   */
  double getBalance();

  /**
   * set the card balance
   *
   * @param balance the new card balance
   */
  void setBalance(double balance);

  /**
   * get the HashMap which contains all the trips in the card
   *
   * @return the HashMap that contains all trips
   */
  HashMap<LocalDate, ArrayList<Trip>> getTrips();

  /**
   * The method to get the last trip on that date
   *
   * @param date the date we want to check
   * @return the last trip on that date
   */
  Trip getLastTripOfDate(LocalDate date);

  /**
   * The method to get the subway fee
   *
   * @return the subway fee
   */
  double getSubwayFee();

  /**
   * The method to get the bus fee
   *
   * @return the bus fee
   */
  double getBusFee();

  /**
   * decide the subway fee and bus fee rate for a particular card type
   *
   * @param type the type of the card (e.g. Child)
   * @return An ArrayList contains the bus fee and the subway fee
   */
  static ArrayList<Double> decideFee(String type) {

    double busFee = 0;
    double subwayFee = 0;
    ArrayList<Double> result = new ArrayList<>();

    if ("Child".equals(type) || "regular child".equals(type)) {
      busFee = 1.0;
      subwayFee = 0.25;

    } else if ("Adult".equals(type) || "regular adult".equals(type)) {
      busFee = 2.0;
      subwayFee = 0.5;

    } else if ("Senior".equals(type) // Senior has the same transit fare rate as Student.
        || "regular senior".equals(type)
        || "Student".equals(type)
        || "regular student".equals(type)) {
      busFee = 1.5;
      subwayFee = 0.25;
    }
    result.add(busFee);
    result.add(subwayFee);
    return result;
  }
}