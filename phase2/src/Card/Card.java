package Card;

import Manager.*;
import Transit.*;
import Trip.*;
import User.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

/**
 * Card is an object which can be tapped on machine.
 */
public abstract class Card implements Serializable, Tappable {
  /** used to count the ID number */
  private static int count;

  /** ID of the card. Different cards have different IDs.*/
  private final int ID;

  /** true if this card is activated by a user.*/
  private boolean activated;

  /** the CardHolder of this card */
  private CardHolder cardHolder;

  /**
   * the type of TapActivity of (Chargeable or NonChargeable of this card instance, which decides
   * how the trip information and tap events are stored.
   */
  private final TapActivity TAPACTIVITY;

  /** the password used to activate this card instance if the field "activated" is false. */
  private final String activePassword;

  /**
   * true if this card was suspended by its CardHolder.
   */
  private boolean suspended;

  /** The manager class of Card. Static to all Card instances. */
  private static CardManager cardManager;

  /**
   * Constructs a new card.
   */
  Card(TapActivity TAPACTIVITY, String activePassword){
    this.ID = count++;
    this.activated = false;
    this.suspended = false;
    this.TAPACTIVITY = TAPACTIVITY;
    this.activePassword = activePassword;
  }

  /**
   * Get the manager class for Card.
   * @return the manager class of Card.
   */
  public static CardManager getCardManager() {
    return cardManager;
  }

  /**
   * Set the manager class for Card.
   * @param cardManager carManager
   */
  public static void setCardManager(CardManager cardManager) {
    Card.cardManager = cardManager;
  }

  /**
   * Gets the activation status of this card.
   * @return the activation status of this card.
   */
  public boolean isActivated() {
    return activated;
  }

  /**
   * Changes the activation status of this card.
   * @param activated true if this card is activated to use, false if not
   */
  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  /**
   * Checks if this card was suspended.
   * @return the card status.
   */
  public boolean isSuspended() {
    return suspended;
  }

  /**
   * Sets the card status depending on it is being suspended or not.
   * @param suspended true if this card is suspended, false if not
   */
  public void setSuspended(boolean suspended) {
    this.suspended = suspended;
  }

  /**
   * gets ID of this card
   * @return this card's ID
   */
  public int getID() {
    return ID;
  }

  /**
   * gets the card holder of this card
   * @return current card holder
   */
  public CardHolder getCardHolder() {
    return cardHolder;
  }

  /**
   * sets the card holder of this card when first being activated
   * @param cardHolder the card holder who activates this card
   */
  public void setCardHolder(CardHolder cardHolder) {
    this.cardHolder = cardHolder;
  }

  /**
   * Gets the type of TapActivity (Chargeable or NonChargeable) for this Card instance.
   * @return the type of TapActivity (Chargeable or NonChargeable)
   */
  public TapActivity getTAPACTIVITY() {
    return TAPACTIVITY;
  }

  /**
   * Gets the activation password of this card.
   * @return activation password of this card.
   */
  public String getActivePassword() {
    return activePassword;
  }

  // time: 2017/08/01/10/20

  /**
   * CardHolders can use cards (which are activated and not suspended) to perform tap activities.
   *
   * @param event "Tap_in" or "Tap_out"; the type of the tap activity.
   * @param transitType "bus" or "subway"; type of the transit route where the user tapped.
   * @param transitNumber ex. "501" "502"; the number of the transit route where the user tapped.
   * @param time ex. "2017/08/01/10/20"; the time when user tapped.
   * @param stopName ex. "Spadina" the name of the stop when user tapped.
   * @return string information about this tap activity.
   */
  public String Tap(String event, String transitType, String transitNumber, String time,
      String stopName) {
    return cardManager.Tap(this, event, transitType, transitNumber, time,
        stopName);
  }

  /**
   * When objects are updated, serialize the change by calling manager classes.
   * @throws IOException Manager class saveToFile() function throws IOException.
   */
  void saveData() throws IOException {
    Stop.getStopManager().saveToFile();
    Card.getCardManager().saveToFile();
    Trip.getTripManager().saveToFile();
    User.getUserManager().saveToFile();
    Route.getRouteManager().saveToFile();
  }

  /**
   * Decide if this instance of Card object is equal to o.
   * @param o object for comparison.
   * @return whether or not this instance is equal to 0.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Card card = (Card) o;
    return ID == card.ID;
  }

  /**
   * Two Card objects are equal if they have the same ID value.
   * @return the hash code of the card object according to its ID value.
   */
  @Override
  public int hashCode() {
    return Objects.hash(ID);
  }
}