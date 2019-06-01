package Card;

import java.time.LocalDate;

/** An interface for all NonChargeable cards which cannot be deducated money from. */
public interface NonChargeable extends Tappable {

  /** true if the card is not expired, false if not */
  boolean checkExpire(LocalDate date);
}