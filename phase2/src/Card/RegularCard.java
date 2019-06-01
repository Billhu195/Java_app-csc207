package Card;

import Manager.CardManager;
import Trip.*;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/** A class for the regular card which is Chargeable. */
public class RegularCard extends Card implements Chargeable, Serializable {

  /** how much money in this card */
  public double balance;

  /**
   * a list of all completed trips that this travel card has traveled.
   * exceptional tap activities would not be stored.
   */
  private HashMap<LocalDate, ArrayList<Trip>> trips;

  /** the bus fee for this card */
  private double busFee;

  /** the subway fee for this card */
  private double subwayFee;

  /**
   * the card statistics for this card.
   * RegularCard could view its statistics through this instance.
   */
  private CardStatistics statistics;

  /** the type of this RegularCard in string. */
  private String type;

  /** constructs a RegularCard */
  public RegularCard(String type, String activePassword){
    super(new ChargeableTap(), activePassword);
    this.type = type;
    this.balance = 19; // initial balance when first activated the card
    this.trips = new HashMap<>();
    ArrayList<Double> fee = Chargeable.decideFee(type);
    this.busFee = fee.get(0); // the bus fare rate for this type of RegularCard
    this.subwayFee = fee.get(1); // the subway fare rate for this type of RegularCard
    this.statistics = new CardStatistics(trips);
  }

  /**
   * The method to get the statistics of this card
   *
   * @return the card statistics
   */
  public CardStatistics getStatistics() {
    return statistics;
  }

  /**
   * The method to get the bus fee
   *
   * @return the bus fee
   */
  public double getBusFee() {
    return busFee;
  }

  /**
   * The method to get the subway fee
   *
   * @return the subway fee
   */
  public double getSubwayFee() {
    return subwayFee;
  }

  /**
   * Add the balance to this Card.
   *
   * @param amount the amount need to charge in this card
   */
  public String addBalance(int amount){
    return CardManager.getCardManager().addBalance(this, amount);
  }

  /**
   * The method to charge money from card
   *
   * @param fee the amount of the money need to be charged
   */
  public void charge(double fee) {
    getCardManager().charge(this, fee);
  }

  /**
   * get balance of this card
   * @return this card's balance
   */
  public double getBalance() {
    return balance;
  }

  /**
   * The method to set the card balance
   *
   * @param balance the card balance
   */
  public void setBalance(double balance) {
    this.balance = balance;
  }

  /**
   * The method to get the HashMap which contains all the trips in the card
   *
   * @return the HashMap that contains all trips
   */
  public HashMap<LocalDate, ArrayList<Trip>> getTrips() {
    return trips;
  }

  /**
   * The method to add a trip in the card
   *
   * @param date the date of that trip
   * @param trip the trip need to be added
   */
  public void addTrip(LocalDate date, Trip trip) {
    ArrayList<Trip> tripList;
    if (this.trips.keySet().contains(date)) {
      tripList = this.trips.get(date);
      tripList.add(trip);
    } else {
      tripList = new ArrayList<>();
      tripList.add(trip);
      this.trips.put(date, tripList);
    }
    try {
      getCardManager().saveToFile(); // serialize this change.
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * The method to get the last trip on the inputted date
   *
   * @param date the date we want to check
   * @return the last trip on that date
   */
  public Trip getLastTripOfDate (LocalDate date) {
    // check if this card has done any trips on this date
    ArrayList<Trip> trips = this.trips.get(date);
    return trips.get(trips.size() - 1);
  }

  /**
   * Informs the ID, type, and the activate password of this Regular Card upon request.
   * @return String representation of this Regular Card.
   */
  @Override
  public String toString() {
    return "RegularCard{" +
        "type='" + type + '\'' +  "ID=" + this.getID() +
        "Active Password=" + this.getActivePassword() +'}';
  }
}