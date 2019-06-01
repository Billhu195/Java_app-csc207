class EventHandler extends Main{

  /**
   * handle different events according to the given String event message.
   * @param event the type of the event e.g. Change_name
   * @param info a string describes the event, including all the necessary information
   */
  void handleEvent(String event, String info){
    String[] newList = info.split(" ");
    switch (event) {
      case "Change_name":
        changeName(newList);
        break;
      case "Add_cardholder":
        addCardholder(newList);
        break;
      case "Add_card":
        addCard(newList);
        break;
      case "Suspend_card":
        suspendCard(newList);
        break;
      case "Add_balance":
        addBalance(newList);
        break;
      case "Tap_in":
        tapIn(newList);
        break;
      case "Tap_out":
        tapOut(newList);
        break;
      case "Three_recent_trips_Holder":
        System.out.println(users.get(newList[0]).viewThreeRecentTrips());
        break;
      case "Date":
        SystemOperation(newList);
        break;
      case "Three_recent_trips_Card":
        System.out.println(cards.get(Integer.valueOf(newList[0])).viewThreeRecentTrips());
        break;
    }
  }

  /**
   * method for a user to change name
   * @param newList the information of event
   */
  private void changeName(String[] newList){
    String email = newList[4].replace("\"", "");
    String newName = newList[10].replace("\"", "");
    CardHolder holder = Main.users.get(email);
    holder.setName(newName);
    System.out.println("You have changed your account name to " + newName);
  }

  /**
   * method for system to add a card holder
   * @param newList the information of event
   */
  private void addCardholder(String[] newList){
    String name = newList[0].replace("\"", "");
    String email = newList[6].replace("\"", "");
    CardHolder newHolder = new CardHolder(name, email);
    users.put(email, newHolder);
    System.out.println("You have created a new account with email: " + email);
  }

  /**
   * method for a user to add a new card to his wallet
   * @param newList the information of event
   */
  private void addCard(String[] newList){
    String email = newList[4].replace("\"", "");
    CardHolder holder = Main.users.get(email);
    Card newCard = new Card();// create new card
    holder.addCard(newCard);
    newCard.setCardHolder(holder);
    cards.put(newCard.getID(), newCard);
    System.out.println("You have successfully added a card to your account.");
  }

  /**
   * method for a user to suspend a card in his wallet
   * @param newList the information of event
   */
  private void suspendCard(String[] newList){
    String email = newList[4].replace("\"", "");
    String cardID = newList[newList.length - 1].replace("\"", "");
    CardHolder holder = Main.users.get(email);
    holder.suspendCard(cards.get(Integer.valueOf(cardID)));
    System.out.println("You have suspended your card " + cardID);
  }

  /**
   * method for a CardHolder to add balance to one of his cards
   * CardHolder can only add $10, $20, or $50 to this card.
   * @param newList the information of event
   */
  private void addBalance(String[] newList){
    Integer ID = Integer.valueOf(newList[1].replace("\"", ""));
    int num = Integer.parseInt(newList[3].replace("\"", ""));
    Card card = cards.get(ID);
    if (num == 50 || num == 10 || num == 20) {
      card.addBalance(num);
      System.out.println("You have successfully added " + num + " dollars to your card " + ID);
    } else {
      System.out.println("You cannot add this balance, please add $10, $20 or $50.");
    }
  }

  /**
   * A helper method to calculate the amount of fee being deducted
   * for a invalid tap_in or tap_out
   * @param card the card which should be punished
   */
  private void punishment(Card card) {
    card.setBalance(card.getBalance() - TTC.CAP);
    Main.currSystem.setData(Main.currSystem.getTotalCost() +
        TTC.CAP, Main.currSystem.getTotalStation());// update current system

    CountDailyMoney moneyDaily = card.getCardHolder().getTotalMoney();
    moneyDaily.setTodayCost(moneyDaily.getTodayCost() + TTC.CAP);
  }

  /**
   * method for handle the tap_in event
   * @param newList the information of event
   */
  private void tapIn(String[] newList){
    // handle the information of event
    Card card = cards.get(Integer.valueOf(newList[4].replace("\"", "")));
    String type = newList[0].replace("\"", "");
    String TTCName = newList[1].replace("\"", "");
    String bus_num = newList[7].replace("\"", "");

    String time = newList[9].replace("\"", "");
    String[] timeList = time.split(",");
    Time newTime = new Time(Integer.parseInt(timeList[0]), Integer.parseInt(timeList[1]));

    // handle this tapped in event.
    // make new segment of trip.
    Trip newTrip;

    if (type.replace("\"", "").equals("Stop")) {
      Stop thisStop = stops.get(TTCName + bus_num);
      newTrip = new Trip(newTime, thisStop, "Bus", bus_num);

    } else { // if this segment of the trip is subway.
      Station thisStation = stations.get(TTCName + bus_num);
      newTrip = new Trip(newTime, thisStation, "Subway", bus_num);
    }

    if (card.isTappedIn) {
      // if tapped in more than once in a row,
      // remove the last tap in from the list
      int last_index = card.getTRIPS().size() - 1;
      WholeTrip last_whole_trip = (card.getTRIPS().get(last_index));

      Trip last_trip = last_whole_trip.getWHOLE_TRIP().get(
          last_whole_trip.getWHOLE_TRIP().size() - 1);

      // if tapped in multiple time at the same location, no charge, otherwise charge 6.
      if (!last_trip.getSTART_STATION().equals(newTrip.getSTART_STATION())) {
        punishment(card);
        System.out.println("Bad tap habit! You have been charged 6 dollars for punishment.");
      }

      if (last_whole_trip.getWHOLE_TRIP().size() == 1) {
        card.getTRIPS().remove(last_index);
      } else {
        last_whole_trip.getWHOLE_TRIP().remove(last_whole_trip.getWHOLE_TRIP().size() - 1);
      }
    }

    card.isTappedIn = true;
    card.tapInTrip(newTrip);
  }

  /**
   * method for handle the tap_out event
   * @param newList the information of event
   */
  private void tapOut(String[] newList){
    Card card = cards.get(Integer.valueOf(newList[4].replace("\"", "")));
    if (card.isTappedIn){
      // handle the information of event
      CardHolder holder = card.getCardHolder();
      String TTCName = newList[1].replace("\"", "");
      String bus_num = newList[7].replace("\"", "");
      TTC thisTTC = stops.get(TTCName + bus_num);
      String time = newList[9].replace("\"", "");
      String[] timeList = time.split(",");
      Time newTime = new Time(Integer.parseInt(timeList[0]), Integer.parseInt(timeList[1]));

      // handle this tapped out event.
      int last_index = card.getTRIPS().size() - 1;
      WholeTrip last_whole_trip = (card.getTRIPS().get(last_index));

      Trip last_trip = last_whole_trip.getWHOLE_TRIP().get(
          last_whole_trip.getWHOLE_TRIP().size() - 1);

      last_trip.setEndStation(thisTTC);
      last_trip.setEndTime(newTime);
      card.isTappedIn = false;
      card.tapOutTrip(last_trip, holder);
    }
    else { // no tapped in, two tap out in a row, this trip is never added.
      punishment(card);
      System.out.println("Bad tap habit! You have been charged 6 dollars for punishment.");
    }
  }

  /**
   * method for creating a new Admin System
   * @param newList a list that contain total COST and stations for that date
   */
  private void SystemOperation(String[] newList) {
    int year = Integer.parseInt(newList[0]);
    int month = Integer.parseInt(newList[1]);
    int day = Integer.parseInt(newList[2]);
    String date = year + "-" + month + "-" + day;
    Main.currSystem = new AdminSystem(date);
    for (String user : Main.users.keySet()) {
      Main.users.get(user).setTotalMoney(new CountDailyMoney(Main.users.get(user),
          Main.currSystem.getDATE()));
    }
  }
}
