package other;

import Manager.*;
import User.*;
import Card.*;
import Exception.*;
import java.time.LocalDate;

/**
 * A class that pre-loads data and does some tests
 */
abstract class Test {

  /** keeps track of the date of performing actions in the test.txt*/
  private static LocalDate date;

  /**
   * Handles different events according to the given information
   * @param event the type of event needed to be handled
   * @param info information about this event provided by the test.txt
   * @throws InvalidInputException raise InvalidInputException if the inputs are wrong
   */
  static void handleEvent(String event, String info) throws InvalidInputException {
    String[] newList = info.split(" ");
    switch (event) {
      case "Change_name":
        changeName(newList);
        break;
      case "Add_cardholder":
        addCardholder(newList);
        break;
      case "Activate_card":
        ActivateCard(newList);
        break;
      case "Suspend_card":
        suspendCard(newList);
        break;
      case "Add_balance":
        addBalance(newList);
        break;
      case "Tap_in":
        tap(event, newList);
        break;
      case "Tap_out":
        tap(event, newList);
        break;
      case "Date":
        SetDate(newList);
        break;
    }
  }

  /**
   * Activate Card
   * @param newList email + cardId + password
   * @throws InvalidInputException If input user not found
   */
  private static void ActivateCard(String[] newList) throws InvalidInputException {
    String email = newList[4];
    String cardID = newList[8];
    String password = newList[12];
    User user = UserManager.getUserManager().getAllUsers().get(email);
    if (user instanceof CardHolder) {
      ((CardHolder) user).ActivateCard(cardID, password);
    } else {
      throw new InvalidInputException("Invalid Input!");
    }
  }

  /**
   * SetDate (year, month, date) on the System format as LocalDate
   * @param newList String year + month + date
   */
  private static void SetDate(String [] newList) {
    date = LocalDate.of(Integer.parseInt(newList[0]), Integer.parseInt(newList[1]),
        Integer.parseInt(newList[2]));
  }

  /**
   * To change the user(CardHolder) name in the system
   * @param newList given emailID + newName
   */
  private static void changeName(String[] newList) {
    String email = newList[0].replace("\"", "");
    String newName = newList[6].replace("\"", "");
    User holder = UserManager.getUserManager().getAllUsers().get(email);
    ((CardHolder) holder).setName(newName);
  }

  /**
   * Intialize and create new cardholders
   * @param newList given CardHolder information: email, password, name.
   */
  private static void addCardholder(String[] newList){
    String name = newList[0].replace("\"", "");
    String email = newList[6].replace("\"", "");
    String password = "1234";
    if (!UserManager.getUserManager().getAllUsers().keySet().contains(email)){
      CardHolder holder = new CardHolder(email, password, "");
      holder.setName(name);
    }
  }

  /**
   * To Suspend a Card
   * @param newList formatted as email + cardID
   * @throws InvalidInputException If not get/found valid input
   */
  private static void suspendCard(String[] newList) {
    String email = newList[4].replace("\"", "");
    String cardID = newList[newList.length - 1].replace("\"", "");
    User holder = UserManager.getUserManager().getAllUsers().get(email);
    ((CardHolder) holder).suspendCard(
        CardManager.getCardManager().getAllCards().get(Integer.valueOf(cardID)));
  }

  /**
   * Add Balance into Card
   * @param newList CardId + money amount to be add in to the card
   */
  private static void addBalance(String[] newList){
    Integer ID = Integer.valueOf(newList[1].replace("\"", ""));
    int num = Integer.parseInt(newList[3].replace("\"", ""));
    Card card = CardManager.getCardManager().getAllCards().get(ID);
    ((Chargeable)card).addBalance(num);
  }

  /**
   * To store the tap in event data in Card
   * @param event Tapin Event contain CardID, tap stop, subway/bus, transit number, time
   * @param newList String[]
   */
  private static void tap(String event, String[] newList) {

    String stop = newList[8].trim();
    String cardID = newList[1];
    Card card = CardManager.getCardManager().getAllCards().get(Integer.valueOf(cardID));
    String transitType = newList[3];
    String transitNumber = newList[4];
    String[] time = newList[6].split(",");
    String timeString = date.getYear() + "/" + date.getMonth().getValue() + "/" +
        date.getDayOfMonth() + "/" + time[0] + "/" + time[1];
    card.Tap(event, transitType, transitNumber, timeString, stop);
  }
}