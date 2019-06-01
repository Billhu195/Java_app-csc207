package User;

import Manager.UserManager;
import java.io.IOException;
import java.io.Serializable;
import Transit.*;
import Card.*;
import Trip.*;
/*User object which contain basic method of admin user and card holder*/
public abstract class User implements Serializable {
  /* The email of User*/
  private String email;
  /*The password of User*/
  private String password;
  /*Let User object be able to merge by UserManager*/
  private static UserManager userManager;

  /**
   * Construct a new User by given email and password
   * @param email String, email of user
   * @param password, String, password of user
   */
  User(String email, String password) {
    this.email = email;
    this.password = password;
  }

  /**
   * Get the current user manager
   * @return the UserManager
   */
  public static UserManager getUserManager() {
    return userManager;
  }

  /**
   * Set the current user manager into new user manager
   * @param userManager UserManager, the new user manager
   */
  public static void setUserManager(UserManager userManager) {
    User.userManager = userManager;
  }

  /**
   * Get the current email
   * @return the String email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Get the current password
   * @return the String password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Set the current password into new password
   * @param password String, new password
   */
  public void setPassword(String password){
    this.password = password;
    try {
      saveData();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Called to serialize changes
   * @throws IOException saveToFile() method could raise IOException
   */
  void saveData() throws IOException {
    Stop.getStopManager().saveToFile();
    Card.getCardManager().saveToFile();
    Trip.getTripManager().saveToFile();
    User.getUserManager().saveToFile();
    Route.getRouteManager().saveToFile();
  }
}