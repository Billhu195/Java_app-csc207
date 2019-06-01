import java.util.ArrayList;

class WholeTrip {

  ArrayList<Trip> getWHOLE_TRIP() {
    return WHOLE_TRIP;
  }

  /** A List contain all trips */
  private final ArrayList<Trip> WHOLE_TRIP;
  /** The money that has been deducted through this Trip */
  private double deducted;
  /** The string format for this trip */
  private String stringFormat;

  /** Constructor of WholeTrip */
  WholeTrip(Trip tripSegment) {
    this.WHOLE_TRIP = new ArrayList<>();
    this.WHOLE_TRIP.add(tripSegment);
    this.deducted = 0;
  }

  /**
   * The helper function for decideFeeCap to decide the money of this trip
   * @param fee the COST of that trip
   * @return the money which should be deducted
   */
  double decideFeeCapHelper(double fee){
    double reduced;
    double afterDeducted = this.deducted + fee;
    if (afterDeducted >= TTC.CAP){
      reduced = TTC.CAP - deducted;
      this.deducted = TTC.CAP;
    }
    else{
      reduced = fee;
      this.deducted += fee;
    }
    return reduced;
  }

  /**
   * The function used to calculate the money of the trip
   * @param reduced the number of money that should not be deducted
   * @param currCard the card which should be deducted
   */
  void decideFeeCap(double reduced, Card currCard){

    Main.currSystem.setData(Main.currSystem.getTotalCost() + reduced,Main.currSystem.getTotalStation());
    currCard.setBalance(currCard.getBalance() - reduced);
  }

  /** A function used to get the string format for this trip */
  String getStringFormat() {
    return stringFormat;
  }

  /**
   * To set the String format use in WholeTrip
   * @param stringFormat String
   */
  void setStringFormat(String stringFormat) {
    this.stringFormat = stringFormat;
  }
}
