package Trip;

import Card.*;
import Transit.Stop;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class NonChargeableTap implements TapActivity, Serializable {

  /**
   * To check if the card can be used
   * @param givenCard Card
   * @param tapInStop Stop when user tap in and store stop
   * @param tapInTime LocalDate Time when user tap and in store time
   * @param feeStrategyType String feeType
   * @param routeNum String bus/subway route number
   * @return String if user tap in
   */
  @Override
  public String TapIn(Card givenCard, Stop tapInStop, LocalDateTime tapInTime,
      String feeStrategyType, String routeNum) {
    NonChargeable card = (NonChargeable) givenCard;
    LocalDate date = tapInTime.toLocalDate();
    if (card.isActivated() && card.checkExpire(date)) {
      return "Valid tap in";
    } else {
      return "Invalid tap in: this card is expired or not registered.";
    }
  }

  /**
   * Let user tap out
   * @param givenCard Card
   * @param tapInStop Stop when user tap out and store stop
   * @param tapOutTime LocalDate Time when user tap out and store time
   * @param feeStrategyType String feeType
   * @param routeNum String bus/subway number
   * @return String if user tap out
   */
  @Override
  public String TapOut(Card givenCard, Stop tapInStop, LocalDateTime tapOutTime,
      String feeStrategyType, String routeNum) {
    return "Valid Tap out";
  }
}