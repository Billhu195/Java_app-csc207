package Card;

import User.CardHolder;

/** An interface for all cards that can be tapped in or tapped out. */
public interface Tappable {

  /** true if this card is available to use, false if not */
  boolean isActivated();

  /** true if this card is suspended, false if not */
  boolean isSuspended();

  /**
   * The method to set isActivated
   *
   * @param activated true if this card is activated to use, false if not
   */
  void setActivated(boolean activated);

  /**
   * The method to get the card holder
   *
   * @return The card holder
   */
  CardHolder getCardHolder();

  /**
   * The method to set the card holder
   *
   * @param cardHolder the card holder
   */
  void setCardHolder(CardHolder cardHolder);

  /**
   * The method to get the card ID
   *
   * @return the card ID
   */
  int getID();

  /**
   * The method to set isSuspended
   *
   * @param suspended true if this card is suspended, false if not
   */
  void setSuspended(boolean suspended);
}