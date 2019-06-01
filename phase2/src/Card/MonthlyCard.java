package Card;

import Trip.NonChargeableTap;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A type of Card that can be charged and renewed by month.
 * A monthly card cannot be charged (being deducted balance from)
 */
public class MonthlyCard extends Card implements NonChargeable, Serializable {

  /** The expire date of this card. */
  private LocalDate expireDate;

  /**
   * constructs a new MonthlyCard.
   * @param date the date of this MonthlyCard's expire date when first purchased.
   * @param activePassword the activation password for this MonthlyCard.
   */
  public MonthlyCard(LocalDate date, String activePassword){
    super(new NonChargeableTap(), activePassword);
    super.setActivated(true); // the card was activated, set the field activated in the super class
    this.expireDate = date;
  }

  /**
   * Check if this MonthlyCard is ac
   * @param inputDate the date inputted by user to check if the card is expired on this date.
   * @return the expire status of this monthly card.
   */
  public boolean checkExpire(LocalDate inputDate){
    return (inputDate.isBefore(expireDate));
  }

  /**
   * Renew this MonthlyCard by month (the expiry date would be updated)
   * @param monthNum renew the card with the given number of months. monthNum could be 1, 2, or 3.
   * @return a message informing the user of the renewal status.
   */
  public String renew (int monthNum) {
    if (monthNum == 1 || monthNum == 2 || monthNum == 3) {
      expireDate = expireDate.plusMonths(monthNum);
      try {
        saveData(); // serialize this change.
      } catch (IOException e) {
        e.printStackTrace();
      }
      return "You have renewed your monthly card for " + monthNum + " months";
    }
    return "Invalid input: please renew your card with 1, 2, or 3 months.";
  }

  /**
   * Gets the expiry date of this MonthlyCard
   * @return the expiry date of this card.
   */
  public LocalDate getExpireDate() {
    return this.expireDate;
  }

  /**
   * make this MonthlyCard a string. The string would include the expiry date and the ID of this
   * card.
   * @return the string representation of this MonthlyCard.
   */
  @Override
  public String toString() {
    return "MonthlyCard{" +
        "expire date=" + expireDate + " ID=" + this.getID() + '}';
  }
}