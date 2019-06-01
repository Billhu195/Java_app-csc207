import java.util.ArrayList;
class Card {/*Travel Card*/
  /** use to count the ID number */
  private static int count = 0;
  /** ID of the card. Different cards have different IDs.*/
  private final int ID;
  /** true if this travel card was tapped in, else when travel card was tapped out */
  boolean isTappedIn;
  /** how much money in this card */
  public double balance;
  /** a list of all completed trips that this travel card has traveled */
  private final ArrayList<WholeTrip> TRIPS;
  /** true if this card can use, else means this card suspended */
  boolean available;
  /** card holder of this card */
  private CardHolder cardHolder;

  /**
   * Construct a new card
   */
  Card(){
    this.ID = count++;
    setBalance(19);
    this.TRIPS = new ArrayList<>();
    this.isTappedIn = false;
    this.available = false;
  }

  /**
   * get ID of this card
   * @return this card's ID
   */
  int getID() {
    return ID;
  }

  /**
   * get balance of this card
   * @return this card's balance
   */
  double getBalance() {
    return balance;
  }

  /**
   *set the balance of this card into new balance
   * @param balance new balance
   */
  void setBalance(double balance) {
    if (balance < 0) {
      try {
        throw new CardException();
      } catch (CardException e) {
        e.printStackTrace();
      }
    }
    this.balance = balance;
  }

  /**
   * get list of TRIPS which this card travelled
   * @return list of TRIPS which stored in this card
   */
  ArrayList<WholeTrip> getTRIPS() {
    return TRIPS;
  }

  /**
   * Add the balance to this Card.
   * @param amount the amount need to charge in this card
   */
  void addBalance(int amount){
    this.balance += amount;
  }

  /**
   * return the string shows three recent trip which this card holder traveled
   * @return "You recently have not done any trip." or
   * "The 1 recent Trip: ...|The 2 recent Trip: ...|The 3 recent Trip: ...|"
   */
  String viewThreeRecentTrips() {
    if (this.TRIPS.size() == 0) {
      return "This card recently has not done any trip.";
    } else {
      StringBuilder result = new StringBuilder();
      result.append("The recent TRIPS of Card ");
      result.append(this.ID);
      result.append(": ");
      for (int i = 0; i < this.TRIPS.size(); i++) {
        result.append("The ");
        result.append(i + 1);
        result.append(" recent trip");
        result.append(": ");
        result.append(TRIPS.get(i).getStringFormat());
        result.append(System.lineSeparator());
      }

      return new String(result);
    }
  }

  /**
   * find the amount of fee which would be charged when the card tapped in a bus
   * @param currTrip the current trip segment
   * @param wholeTrip the current whole trip
   */
  private void chargeBusFee(Trip currTrip, WholeTrip wholeTrip){
    double reduced = wholeTrip.decideFeeCapHelper(Stop.COST);
    currTrip.setDeducted(reduced);
    wholeTrip.decideFeeCap(reduced, this);
    CountDailyMoney money = this.cardHolder.getTotalMoney();
    money.setTodayCost(money.getTodayCost() + reduced);
  }

  /**
   * find the new deduce of trip fee when card tapped out a subway
   * @param stationPassed the number of station passed from tap in a subway
   * @param currTrip the current trip segment
   * @param wholeTrip the current whole trip
   */
  private void chargeSubwayFee(int stationPassed, Trip currTrip, WholeTrip wholeTrip){
    double money = stationPassed * Station.COST;
    double reduced = wholeTrip.decideFeeCapHelper(money);
    currTrip.setDeducted(reduced);
    wholeTrip.decideFeeCap(reduced, this);

    CountDailyMoney moneyDaily = this.cardHolder.getTotalMoney();
    moneyDaily.setTodayCost(moneyDaily.getTodayCost() + reduced);
  }

  /**
   * what will happen when this card tap in a station or stop
   * @param currTrip the current trip segment
   *
   */
  void tapInTrip(Trip currTrip) {
    if (this.TRIPS.size() > 0) { // If TRIPS is not empty.
      int lastIndex = this.TRIPS.size() - 1;
      WholeTrip lastWholeTrip = (this.TRIPS.get(lastIndex));
      currTrip.decideContinuity(this); // see if the current segment of trip joints
      //  the last segment of the trip

      if (currTrip.getContinuous()) { // if is_continuous
        // then update the last WholeTrip's list of TRIPS
        lastWholeTrip.getWHOLE_TRIP().add(currTrip);

        // if bus, then deduct the appropriate amount immediately.
        if (currTrip.getTYPE().equals("Bus")) {
          chargeBusFee(currTrip, lastWholeTrip);
        }

      } else { // if not continuous, then this segment is the start of a whole new trip.
        WholeTrip newWholeTrip = new WholeTrip(currTrip);
        this.TRIPS.add(newWholeTrip);
        if (currTrip.getTYPE().equals("Bus")) {
          chargeBusFee(currTrip, newWholeTrip);
        }
      }

    } else { // if the Card.TRIPS is empty, then start a wholeTrip with this segment of trip.
      WholeTrip newWholeTrip = new WholeTrip(currTrip);
      this.TRIPS.add(newWholeTrip);
      if (currTrip.getTYPE().equals("Bus")) {
        chargeBusFee(currTrip, newWholeTrip);
      }
    }
  }

  /**
   * what will happen when this card tap out a station or stop
   * @param currTrip the current trip segment
   * @param holder the card holder of this card
   */
  void tapOutTrip(Trip currTrip, CardHolder holder) {
    ArrayList<TTC> segment = currTrip.generateSegments(); // make the currTrip an ArrayList of TTC

    // if size is not 0, update the trips' last_station and last_date
    if (segment.size() > 0) {
      int lastIndex = this.TRIPS.size() - 1;
      WholeTrip lastWholeTrip = (this.TRIPS.get(lastIndex));
      int stationPassed = segment.size() - 1;

      // charge the user immediately when tapped out a subway station.
      if (currTrip.getTYPE().equals("Subway")) {
        chargeSubwayFee(stationPassed, currTrip, lastWholeTrip);
      }

      // update the total stations being passed in the current system.
      Main.currSystem.setData(Main.currSystem.getTotalCost(),
          Main.currSystem.getTotalStation() + stationPassed);

      currTrip.setListOfStops(segment);

      // set the string format of the WholeTrip.
      if (lastWholeTrip.getWHOLE_TRIP().size() == 1) {
        lastWholeTrip.setStringFormat(currTrip.toString());
      } else {
        lastWholeTrip.setStringFormat(lastWholeTrip.getStringFormat() + currTrip.toString());
      }
      holder.decideLastTrip(lastWholeTrip);
    }
  }

  /**
   * get the card holder of this card
   * @return current card holder
   */
  CardHolder getCardHolder() {
    return cardHolder;
  }

  /**
   * set the card holder of this card into new card holder
   * @param cardHolder new card holder
   */
  void setCardHolder(CardHolder cardHolder) {
    this.cardHolder = cardHolder;
  }
}

