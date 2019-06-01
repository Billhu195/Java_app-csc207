package Strategy;

import Transit.Stop;

/** The subway fee strategy for ChargeableCard */
public class SubwayFeeStrategy extends FeeStrategy {

  /** The number of the subway fee */
  private double subwayFee;

  /** The constructor for the subway fee Strategy. */
  public SubwayFeeStrategy(Stop startStation, Stop endStation, String routeNum, double subwayFee) {
    super(startStation, endStation, routeNum);
    this.subwayFee = subwayFee;
  }

  /**
   * Override the method calculateFare with subway fee
   *
   * @param alreadyCharged The money has already charged in last two hours
   * @return The fare of this trip
   */
  @Override
  public double calculateFare(double alreadyCharged) {
    return super.calculateFareHelper(NumStopReached() * subwayFee, alreadyCharged);
  }
}