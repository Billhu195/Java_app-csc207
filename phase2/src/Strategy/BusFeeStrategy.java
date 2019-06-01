package Strategy;

import Transit.Stop;

/** The bus fee strategy for Chargeable Card */
public class BusFeeStrategy extends FeeStrategy {

  /** The number of the bus fee */
  private final double busFee;

  /** The constructor for the Bus Fee Strategy. */
  public BusFeeStrategy(Stop startStation,Stop endStation, String routeNum, double busFee) {
    super(startStation, endStation, routeNum);
    this.busFee = busFee;
  }

  /** The constructor for the Bus Fee Strategy. */
  public BusFeeStrategy(double busFee) {
    this.busFee = busFee;
  }

  /**
   * Override the method calculateFare with bus fee
   * @param alreadyCharged The money has already charged in last two hours
   * @return The fare of this trip
   */
  @Override
  public double calculateFare(double alreadyCharged) {
    return calculateFareHelper(busFee, alreadyCharged);
  }
}