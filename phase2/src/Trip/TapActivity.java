package Trip;

import Transit.Stop;
import java.time.LocalDateTime;
import Card.*;

public interface TapActivity {
  /**
   * Interface for cardholder when user tap in to output data
   * @param card Card
   * @param tapInStop Stop when user tap in and store stop
   * @param tapInTime LocalDate Time when user tap and in store time
   * @param feeStrategyType String feeType
   * @param routeNum String bus/subway route number
   * @return String
   */
  String TapIn(Card card, Stop tapInStop, LocalDateTime tapInTime,
      String feeStrategyType, String routeNum);

  /**
   * Interface for cardholder when user tap out output data
   * @param card Card
   * @param tapInStop Stop when user tap out and store stop
   * @param tapOutTime LocalDAte Time when user tap out and store time
   * @param feeStrategyType String feeType
   * @param routeNum String bus/subway number
   * @return String
   */
  String TapOut(Card card, Stop tapInStop, LocalDateTime tapOutTime,
      String feeStrategyType, String routeNum);

}