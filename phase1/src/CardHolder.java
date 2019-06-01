import java.util.ArrayList;

class CardHolder {/*a people who contain the card*/
  /** name of the card holder */
  private String name;
  /** EMAIL of card holder, can not change */
  private final String EMAIL;
  /** a wallet which contain all cards which belongs to this card holder */
  public ArrayList<Card> wallet;
  /** how much does this card holder spend on his or her cards */
  private CountDailyMoney totalMoney;
  /** store three recent trips which traveled by this card holder */
  private final ArrayList<WholeTrip> RECENT_TRIP;
  public String password;

  public static ArrayList<String> activePasswordList = new ArrayList<>();


  /**
   * Construct a new CardHolder
   */
  CardHolder(String name, String email, String password){
    setName(name);
    this.EMAIL = email;
    this.wallet = new ArrayList<>();
    this.RECENT_TRIP = new ArrayList<>();
    this.totalMoney = new CountDailyMoney(this, Main.currSystem.getDATE()); // money spent on each day.
    setPassword(password);
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * get the name of the card holder
   * @return the name of the card holder
   */
  String getName() {
    return name;
  }

  /**
   * set the name of the card holder into a new name
   * @param name new name
   */
  void setName(String name) {
    this.name = name;
  }

  /**
   * return the string shows three recent trip which this card holder traveled
   * @return "You recently have not done any trip." or
   * "The 1 recent Trip: ...|The 1 recent Trip: ...|The 1 recent Trip: ...|"
   */
  String viewThreeRecentTrips() {
    if (this.RECENT_TRIP.size() == 0) {
      return "You recently have not done any trip.";
    } else {
    StringBuilder result = new StringBuilder();
    result.append("Your recent trips:  ");
    for (int i = 0; i < this.RECENT_TRIP.size(); i++) {
      result.append("The ");
      result.append(i + 1);
      result.append(" recent trip");
      result.append(": ");
      result.append(RECENT_TRIP.get(i).getStringFormat());
      result.append(System.lineSeparator());
    }
    return new String(result);
    }
  }

  /**
   * get the total money spent by this CardHolder of this day.
   * @return the current total money
   */
  CountDailyMoney getTotalMoney() {
    return totalMoney;
  }

  /**
   * set the total money spent by this CardHolder on this day.
   * @param totalMoney the money spent by users on this day.
   */
  void setTotalMoney(CountDailyMoney totalMoney) {
    this.totalMoney = totalMoney;
  }

  /**
   * add a new card into wallet of this card holder
   * @param newCard new card
   */
  void addCard(Card newCard){
    this.wallet.add(newCard);
  }

  /**
   * suspend a lost card which belongs to this card holder
   * @param lossCard the lost card
   */
  void suspendCard(Card lossCard){
    if (lossCard.available){
      lossCard.available = false;
    }
    else {
      System.out.println("this card is suspended");
    }
  }

  /**
   * load the newest trip of this card holder into RECENT_TRIP of this card holder
   * @param wholeTrip the newest trip
   */
  void decideLastTrip(WholeTrip wholeTrip){
    if (this.RECENT_TRIP.size() > 0) {
      if (!wholeTrip.equals(this.RECENT_TRIP.get(0))) {
        this.RECENT_TRIP.add(0, wholeTrip);
        if (this.RECENT_TRIP.size() > 3) {
          this.RECENT_TRIP.remove(3);
        }
      }
    } else {
      this.RECENT_TRIP.add(0, wholeTrip);
    }
  }

  public Card findCard(int id){
    ArrayList<Card> wallet = this.wallet;
    for (int i = 0; i < wallet.size(); i++) {
      if (wallet.get(i).getID() == id) {
        return wallet.get(i);
      }
    }
    return null;
  }

  public String getEMAIL() {
    return EMAIL;
  }
}
