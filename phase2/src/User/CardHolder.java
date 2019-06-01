package User;

import ExtraFeatures.FindPath;
import Manager.CardManager;
import Statistics.ViewStatistics;
import Transit.Stop;
import Trip.Trip;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import Card.*;
import java.util.HashMap;

/*a people who contain the trip card*/
public class CardHolder extends User {
  /* name of the card holder */
  private String name;
  /* a wallet which contain all cards which belongs to this card holder */
  private HashMap<Integer, Card> wallet;
  /* a hashmap which contain all history trips of current card holder*/
  private HashMap<LocalDate, ArrayList<Trip>> userTrips;
  /* let current card holder's statistics be able to merge and view */
  private final ViewStatistics viewStatistics;

  /**
   * Construct a new CardHolder by given email, password, and username
   * @param email the given email
   * @param password the given password
   * @param username the given username
   */
  public CardHolder(String email, String password, String username){
    super(email, password);
    this.wallet = new HashMap<>();
    this.userTrips = new HashMap<>();
    this.setName(username);
    this.viewStatistics = new ViewStatistics(this);
    getUserManager().add(this);
  }

  /**
   * Get the current view statistic
   * @return the ViewStatistic
   */
  public ViewStatistics getViewStatistics() {
    return viewStatistics;
  }

  /**
   * Get the current user trips
   * @return Hashmap<LocalDate, ArrayList<Trip>> user trips
   */
  public HashMap<LocalDate, ArrayList<Trip>> getUserTrips() {
    return userTrips;
  }

  /**
   * Set the name of the card holder into a new name
   * @param name new name
   */
  public void setName(String name) {
    this.name = name;
    try {
      saveData();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get the current name
   * @return String name
   */
  public String getName() {
    return name;
  }

  /**
   * Add a new card into wallet of this card holder
   * @param newCard new card
   */
  private void addCardToWallet(Card newCard){
    if (wallet != null && !wallet.isEmpty()) {
      if (!wallet.keySet().contains(newCard.getID())) {
        this.wallet.put(newCard.getID(), newCard);
      }
    } else {
      this.wallet = new HashMap<Integer, Card>();
      this.wallet.put(newCard.getID(), newCard);
    }
    try {
      User.getUserManager().saveToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get the current wallet
   * @return HashMap<Integer, Card> wallet
   */
  public HashMap<Integer, Card> getWallet() {
    return wallet;
  }

  /**
   * Suspend a lost card which belongs to this card holder
   * @param lossCard the lost card
   */
  public String suspendCard(Card lossCard)  {
    if (wallet.values().contains(lossCard)) {
      if (!lossCard.isSuspended() && lossCard.isActivated()) {
        lossCard.setSuspended(true);
        try {
          saveData();
        } catch (IOException e) {
          e.printStackTrace();
        }
        return ("You have successfully suspended this card.");
      } else {
        return ("this card is already being suspended");
      }
    } else {
      return "Invalid input";
    }
  }

  /**
   * Active a new card with id by its active password, and return message
   * @param id String, the card's id
   * @param password String, the card's active password
   * @return String message which need to return
   */
  public String ActivateCard(String id, String password) {
    String message;
    if (CardManager.getCardManager().getAllCards().keySet().contains(Integer.valueOf(id))) {
      Card card = CardManager.getCardManager().getAllCards().get(Integer.valueOf(id));
      if (!card.isSuspended()) {
        if (!card.isActivated()) {
          if (card.getActivePassword().equals(password)) {
            card.setActivated(true);
            card.setCardHolder(this);
            addCardToWallet(card);
            message = this.getEmail() + " have successfully added a card " + id;
            try {
              saveData();
            } catch (IOException e) {
              e.printStackTrace();
            }
            return message;
          } else {
            return "Invalid input: wrong password! please input again.";
          }
        } else {
          return "This card was already being activated. Please do not re-activate this card.";
        }
      } else {
        return "This card was suspended. You cannot activate this card";
      }
    } else {
      return "Invalid input: wrong id! this card does not exist.";
    }
  }

  /**
   * Find both cheapest path and the shortest path by the type of card and the start and end stations, and return message
   * @param startStation Stop, the start station of paths
   * @param endStation Stop, the end station of paths
   * @param cardType String , the card's type
   * @return String message
   */
  public String findPath(Stop startStation, Stop endStation, String cardType) {
    if (startStation.equals(endStation)) {
      return "Invalid Input.";
    }
    FindPath shortest = new FindPath("shortest");
    String shortestRoute = shortest.findPath(startStation, endStation, cardType).get(0);
    double shortestRouteCost = Double.parseDouble(shortest.findPath(startStation, endStation, cardType).get(2));

    String result = "";
    if (!cardType.equals("monthly card")) {
      FindPath cheapest = new FindPath("cheapest");
      String cheapestRoute = cheapest.findPath(startStation, endStation, cardType).get(0);
      double cheapestRouteCost = Double.parseDouble(shortest.findPath(startStation, endStation, cardType).get(2));

      if (shortestRouteCost == cheapestRouteCost) {
        result += "The shortest and the cheapest route is \n" + shortestRoute;
        if (!shortestRoute.equals(cheapestRoute)) {
          result += "\n";
          result += "Another option: \n" + cheapestRoute;
        }
      } else {
        result += "The shortest route is \n" + shortestRoute;
        result += "\n";
        result += "\n";
        result += "The cheapest route is \n" + cheapestRoute;
      }
      return result;
    } else {
      result += "The shortest route is \n" + shortestRoute;
      return result;
    }
  }

  /**
   * Add the new trip to the CardHolder's userTrip if the trip is done by a card owned by this
   * CardHolder.
   * @param date date of performing this trip
   * @param trip trip done by the cardHolder.
   */
  public void addTrip(LocalDate date, Trip trip) {
    if (!userTrips.isEmpty() && userTrips.keySet().contains(date)) {
      ArrayList<Trip> TripList = userTrips.get(date);
      TripList.add(trip);
      // serialize changes
      try {
        saveData();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      ArrayList<Trip> newTripList = new ArrayList<>();
      newTripList.add(trip);
      this.userTrips.put(date, newTripList);

      try {
        saveData();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Return a string which represent this card holder
   * @return String of CardHolder
   */
  @Override
  public String toString() {
    return "CardHolder{" +
        "name='" + name + '\'' +
        " email='" + this.getEmail() + '\'' +
        '}';
  }
}