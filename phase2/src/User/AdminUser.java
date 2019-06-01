package User;

import Card.*;
import Manager.CardManager;
import Manager.TripManager;
import Manager.UserManager;
import Statistics.ViewStatistics;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import Trip.*;

/* Admin user who will get statistic from the system*/
public class AdminUser extends User {
  /* let current admin user's statistics be able to merge and view */
  private final ViewStatistics viewStatistics;

  /**
   * Construct a new AdminUser by given email and password
   * @param email String email
   * @param password String password
   */
  public AdminUser(String email, String password) {
    super(email, password);
    viewStatistics = new ViewStatistics(this);
    getUserManager().add(this);
  }

  /**
   * Get the current view statistic
   * @return ViewStatistics current view statistics
   */
  public ViewStatistics getViewStatistics() {
    return viewStatistics;
  }

  /**
   * Return the number of card holders in the user hash mpa
   * @return int, the number of card holders
   */
  public static int NumCardHolders() {
    int num = 0;
    HashMap<String, User> users = UserManager.getUserManager().getAllUsers();

    if (!users.isEmpty()) {
      for (String key : users.keySet()) {
        if (users.get(key) instanceof CardHolder) {
          num += 1;
        }
      }
    }
    return num;
  }

  /**
   * sum up the number of total Chargeable Cards at the system.
   * @return int, number of total chargeable cards
   */
  public static int NumChargeableCards() {
    int num = 0;
    HashMap<Integer, Card> cards = CardManager.getCardManager().getAllCards();

    if (!cards.isEmpty()) {
      for (Integer key : cards.keySet()) {
        if (cards.get(key) instanceof Chargeable) {
          num += 1;
        }
      }
    }
    return num;
  }

  /**
   * sum up the total non Chargeable Card at the system
   * @return int, number of non chargeable card
   */
  public static int NumNonChargeableCards() {
    int num = 0;
    HashMap<Integer, Card> cards = CardManager.getCardManager().getAllCards();

    if (!cards.isEmpty()) {
      for (Integer key : cards.keySet()) {
        if (cards.get(key) instanceof NonChargeable) {
          num += 1;
        }
      }
    }
    return num;
  }

  /**
   * Given current date roll back to 5 year's same month and delete those trip before
   * @param trips Trip
   * @param expiryMonth long (input from GUI the same month 5 years ago)
   */
  private void deleteOldDataHelper(HashMap<LocalDate, ArrayList<Trip>> trips, long expiryMonth) {
    LocalDate todayDate = LocalDate.now();
    for (LocalDate date : trips.keySet()) {
      long monthDuration = ChronoUnit.MONTHS.between(date , todayDate);
      if (monthDuration > expiryMonth) {
        trips.remove(date);
      }
    }
  }

  /**
   * delete the data that has been generated 5 years from now
   * @return String if deleted success message displayed
   */
  // delete the trip data after number of months.
  public String deleteOldData() {
    deleteOldDataHelper(TripManager.getTripManager().getAllTrips(), 60 ); // 5 years
    HashMap<String, User> userMap = UserManager.getUserManager().getAllUsers();
    for (String email : userMap.keySet()) {
      User user = userMap.get(email);
      if (user instanceof CardHolder) {
        deleteOldDataHelper(((CardHolder) user).getUserTrips(), 12 ); // 1 year
        if (!((CardHolder) user).getWallet().isEmpty()) {
          for (Integer ID : ((CardHolder) user).getWallet().keySet()) {
            Card card = ((CardHolder) user).getWallet().get(ID);
            if (card instanceof Chargeable) {
              deleteOldDataHelper(((Chargeable)card).getTrips(), 12);
            }
          }
        }
      }
    }
    // serialize changes
    try {
      saveData();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "Database was updated. Old data were deleted if there is any.";
  }

  /**
   * Return the String format of AdminUser
   * @return String of AdminUser
   */
  @Override
  public String toString() {
    return "AdminUser{" +
        " email='" + this.getEmail() + '\'' +
        '}';
  }
}