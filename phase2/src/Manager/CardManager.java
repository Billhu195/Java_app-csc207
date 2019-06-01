package Manager;

import Card.*;
import Transit.Stop;
import Trip.Trip;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Manages Cards
 */
public class CardManager {

  /** logger */
  private static final Logger LOGGER = Logger.getLogger(CardManager.class
          .getName());
  /** create a object fileHandler to store data */
  private static FileHandler fileHandler = null;
  /** String filePath*/
  private static final String PATH = "./serializedCardObjects.ser";

  /** An ArrayList that keeps track of all Stop objects */
  private HashMap<Integer, Card> allCards;

  /**
   * Getter for allCards
   * @return HashMap<CardNumber, Card>
   */
  public HashMap<Integer, Card> getAllCards() {
    return allCards;
  }

  /** cardManager object get from CardManager*/
  private static CardManager cardManager;

  /**
   * * Getter for variable carManager from CardManager, if null deserialize
   * @return CardManager
   */
  public static CardManager getCardManager(){
    if (cardManager == null){
      try {
        cardManager = new CardManager();
      } catch (ClassNotFoundException | IOException e) {
        e.printStackTrace();
      }
    }
    return cardManager;
  }

  /**
   * Creates a new empty CardManger.
   * @throws IOException if not found input/output
   * @throws ClassNotFoundException If not found file path
   */
  private CardManager() throws ClassNotFoundException, IOException {
    allCards = new HashMap<>(); // hash map of Stop id: stop

    try {
      fileHandler = new FileHandler("phase2/log.txt");
      LOGGER.addHandler(fileHandler);
      SimpleFormatter formatter = new SimpleFormatter();
      fileHandler.setFormatter(formatter);

    } catch (IOException e) {
      e.printStackTrace();
    }
    LOGGER.setLevel(Level.ALL);
    assert fileHandler != null;
    fileHandler.setLevel(Level.ALL);

    // Populates the record list using stored data, if it exists.
    File file = new File(PATH);
    if (file.exists()) {
      readFromFile();
    } else {
      file.createNewFile();
    }
  }

  /**
   * read file to get route, user, map data, card
   * @throws ClassNotFoundException If not found file path
   */
  private void readFromFile() throws ClassNotFoundException {
    try {
      InputStream file = new FileInputStream(PATH);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);

      // deserialize the Map
      allCards = (HashMap<Integer, Card>) input.readObject();
      input.close();
    } catch (IOException ex) {
      LOGGER.log(Level.SEVERE, "Cannot read from input.", ex);
    }
  }

  /**
   * read file to get route, user, map data, card
   * @throws ClassNotFoundException If not found file path
   */
  public void readFromCSVFile(String filePath)
          throws IOException {
    // FileInputStream can be used for reading raw bytes, like an image.
    Scanner scanner = new Scanner(new FileInputStream(filePath));
    String[] record;

    while(scanner.hasNextLine()) {
      record = scanner.nextLine().split("\\s+");
      String[] cardInfo = record[0].split(":");
      String password = record[1].trim();
      String cardType = cardInfo[0];
      String fareTYpe = cardInfo[1];

      CreateNewCard(cardType, fareTYpe, password);
    }
    scanner.close();
  }

  /**
   * To add card input our HashMap at allUsers and save it at a file
   * @param card cardID input
   */
  private void add(Card card) {
    if (!allCards.keySet().contains(card.getID())) {
      allCards.put(card.getID(), card);
    }
    // Log the addition of a student.
    try {
      saveToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add new Card correspond to their cardType, FareType and user account
   * @param type cardType
   * @param fareType Faretype
   * @param password password that match to the user
   * @return String for new Card
   */
  // fareType is "" for monthly, "Child" etc for regular.
  private void CreateNewCard(String type, String fareType, String password) {
    Card newCard;
    switch (type) {
      case "monthly":
        String[] dateInfo = fareType.split("/");
        int year = Integer.parseInt(dateInfo[0]);
        int month = Integer.parseInt(dateInfo[1]);
        int day = Integer.parseInt(dateInfo[2]);
        newCard = new MonthlyCard(LocalDate.of(year, month, day), password);
        break;
      case "regular":
        newCard = new RegularCard(fareType, password);
        break;
      default:
        return;
    }
    add(newCard);
  }

  /**
   * Writes the card to file at filePath. Serialize
   * @throws IOException if not found input/output
   */
  public void saveToFile() throws IOException {
    OutputStream file = new FileOutputStream(PATH);
    OutputStream buffer = new BufferedOutputStream(file);
    ObjectOutput output = new ObjectOutputStream(buffer);

    // serialize the Map
    output.writeObject(allCards);
    output.close();
  }

  /**
   * Add balance to user's card
   * @param card Card
   * @param amount amount 10 20 50
   * @return String of confirmation if user has been successful added balance or not
   */
  public String addBalance(Card card, double amount) {

    String message;
    if (card instanceof Chargeable) {
      if (amount == 50 || amount == 10 || amount == 20) {
        ((Chargeable) card).setBalance(((Chargeable) card).getBalance() + amount);
        message = ("Successfully added " + amount + " dollars to card " + card.getID());
        message += ". ";
        message += "The current balance is " + Double.toString(((Chargeable) card).getBalance());
        LOGGER.log(Level.INFO, message);
        try {
          saveToFile();
          UserManager.getUserManager().saveToFile();
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        message = "Message for user " + card.getCardHolder()
                + "You cannot add this balance to card " + card.getID() + ", please add $10, $20 or $50.";
      }
    }
    else {
      message =  "Message for user " + card.getCardHolder() +
              "This card " + card.getID() + " is non-chargeable. Please insert a chargeable card to add balance.";
    }
    return message;
  }

  /**
   * deduct transit fee from the card
   * @param card Chargeable Card type
   * @param fee double different FareType will have different fee
   */
  public void charge(Chargeable card, double fee) {
    card.setBalance(card.getBalance() - fee);
    try {
      saveToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    String message = ("Card " + card.getID() + " has been charged for " + fee + " dollars.");
    message += "\n";
    message += "The current balance is " + card.getBalance();
    LOGGER.log(Level.INFO, message);

  }

  /**
   * Convert string time to LocalTime
   * @param time string representation. example: 2018/9/9/7/14
   * @return LocalDate format of the time
   */
  private LocalDateTime makeLocalDateTime(String time) {

    String[] timeList = time.split("/");

    int year = Integer.valueOf(timeList[0]);
    int month = Integer.valueOf(timeList[1]);
    int day = Integer.valueOf(timeList[2]);
    int hour = Integer.valueOf(timeList[3]);
    int minute = Integer.valueOf(timeList[4]);

    return LocalDateTime.of(year, month, day, hour, minute);
  }

  /**
   * Tap activity.
   * @param card Card which taps
   * @param event tap in or tap out
   * @param transitType the type of the transit which the card performs the tap activity.
   * @param transitNumber the number of the transit which the card performs the tap activity.
   * @param time the time of the tap activity
   * @param stopName the name of the tap stop
   * @return message regarding the status and information of this tap activity.
   */
  public String Tap(Card card, String event, String transitType, String transitNumber, String time,
                    String stopName) {

    LocalDateTime tapInTime = makeLocalDateTime(time);
    Stop tapInStop = StopManager.getStopManager().getStopObject(stopName);

    String message;
    message = "Card: " + String.valueOf(card.getID()) + " " + event + " on transit " + transitType +
        " " + transitNumber +
            " at " + stopName + " at " + tapInTime.toString() + "\n";

    // look at the tap in time and tap in station, if time beyond this area, then restrict tap event.

    if (!card.isActivated()) {
      message += "This card is not available.";
      LOGGER.log(Level.WARNING, message);
    } else {
      if (event.equals("Tap_in")) {
        if (card instanceof Chargeable) {
          if (((Chargeable) card).getBalance() <= 0 ) {
            message += "Card balance is not enough.";
            message += "\n";
            LOGGER.log(Level.WARNING, message);
          } else {
            message += card.getTAPACTIVITY().TapIn(
                    card, tapInStop, tapInTime, transitType, transitNumber);
          }
        } else {
          message +=
                  card.getTAPACTIVITY().TapIn(
                          card, tapInStop, tapInTime, transitType, transitNumber);
        }
      } else {
        message +=
                card.getTAPACTIVITY().TapOut(
                        card, tapInStop, tapInTime, transitType, transitNumber);
        message += "\n";

        if (TripManager.getTripManager().getAllTrips().keySet().contains(
                tapInTime.toLocalDate())) {
          ArrayList<Trip> trips =
                  TripManager.getTripManager().getAllTrips().get(tapInTime.toLocalDate());
          Trip lastTrip = trips.get(trips.size() - 1);
          if (lastTrip.getEndTime().equals(tapInTime.toLocalTime())) {
            message += tripInfo (lastTrip);
          }
        }
      }
      message += "\n";
      LOGGER.log(Level.INFO, message);
    }
    try {
      saveToFile();
      UserManager.getUserManager().saveToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return message;
  }

  /**
   * String trip info
   * @param lastTrip last complete trip
   * @return string representation of the last trip
   */
  private String tripInfo (Trip lastTrip) {
    String message;
    message = "Complete trip: ";
    message += (lastTrip).toString();
    message += "\n";
    message += "The fee of this trip is ";
    message += (lastTrip).getFeeTotal();
    message += " dollars";

    return message;
  }

  /**
   * Make String representation of this CardManager
   * @return String representation of this CardManager
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (int id : allCards.keySet()) {
      result.append(id);
      result.append(": ");
      result.append(allCards.get(id).toString());
      result.append("\n");
    }
    return result.toString();
  }
}