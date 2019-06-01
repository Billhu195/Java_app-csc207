package Manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import User.*;

/**
 * Manages Users
 */
public class UserManager {

  /** LOGGER */
  private static final Logger LOGGER = Logger.getLogger(StopManager.class.getName());
  /** consoleHandler */
  private static final Handler consoleHandler = new ConsoleHandler();

  /** An ArrayList that keeps track of all user objects */
  private HashMap<String, User> allUsers;
  /** String filePath */
  private static final String PATH = "./serializedUserObjects.ser";
  /** userManager object get from UserManager*/
  private static UserManager userManager;

  /**
   * Getter for allUsers as HashMap
   * @return HashMap key String(UserID), value User(object)
   */
  public HashMap<String, User> getAllUsers() {
    return allUsers;
  }

  /**
   * * Getter for variable userManager from UserManager, if null deserialize
   * @return UserManager
   */
  public static UserManager getUserManager(){
    if (userManager == null){
      try {
        userManager  = new UserManager();
      } catch (ClassNotFoundException | IOException e) {
        e.printStackTrace();
      }
    }
    return userManager;
  }

  /**
   * Creates a new empty StopManager.
   * @throws  IOException If did not input the file name
   * @throws  ClassNotFoundException If not found file PATH
   */
  private UserManager() throws ClassNotFoundException, IOException {
    allUsers = new HashMap<>(); // hash map of Stop id: stop

    // Associate the handler with the LOGGER.
    LOGGER.setLevel(Level.ALL);
    consoleHandler.setLevel(Level.ALL);
    LOGGER.addHandler(consoleHandler);

    // Populates the record list using stored data, if it exists.
    File file = new File(PATH);
    if (file.exists()) {
      readFromFile();
    } else {
      file.createNewFile();
    }
  }

  /**
   * read file to get route, user, map data
   * @throws ClassNotFoundException If not found file PATH
   */
  private void readFromFile() throws ClassNotFoundException {
    try {
      InputStream file = new FileInputStream(PATH);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);

      // deserialize the Map
      allUsers = (HashMap<String, User>) input.readObject();

      input.close();
    } catch (IOException ex) {
      LOGGER.log(Level.SEVERE, "Cannot read from input.", ex);
    }
  }

  /**
   * Load the data from the csv file.
   * @param filePath filepath for csv
   * @throws IOException if not found file PATH input
   */
  public void readFromCSVFile(String filePath) throws IOException {

    // FileInputStream can be used for reading raw bytes, like an image.
    Scanner scanner = new Scanner(new FileInputStream(filePath));
    String[] record;

    while(scanner.hasNextLine()) {
      record = scanner.nextLine().split("\\s+");
      String userType = record[0];
      String email = record[1];
      String password = record[2];
      String name;
      if (userType.equals("CardHolder")) {
        CardHolder user = new CardHolder(email, password, "");
        if (record.length == 4) {
          name = record[3];
          user.setName(name);
        }
      } else {
        new AdminUser(email, password);
      }

    }
    scanner.close();
  }

  /**
   * To add user input our HashMap at allUsers and save it at a file
   * @param user userID input
   */
  public void add(User user) {
    if (!allUsers.keySet().contains(user.getEmail())) {
      allUsers.put(user.getEmail(), user);
    }
    LOGGER.log(Level.FINE, "Added a new user.");
    try {
      saveToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Writes the user to file at filePath. Serialize
   * @throws IOException if not found input/output
   */
  public void saveToFile() throws IOException {

    OutputStream file = new FileOutputStream(PATH);
    OutputStream buffer = new BufferedOutputStream(file);
    ObjectOutput output = new ObjectOutputStream(buffer);

    // serialize the Map
    output.writeObject(allUsers);
    output.close();
  }

  /**
   * To Check if the User is in our system and their ID and Password is a match
   * @param email UserID from input
   * @param UserPassword UserPassword from input
   * @return True if the User is in our System, otherwise False
   */
  public boolean UserLogin(String email, String UserPassword) {
    if (allUsers.containsKey(email)){
      String password = allUsers.get(email).getPassword();
      return UserPassword.equals(password);
    } else {
      return false;
    }
  }

  /**
   * To String printout User's in our system
   * @return UserID(email)
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (String email : allUsers.keySet()) {
      result.append(email);
      result.append(": ");
      result.append(allUsers.get(email).toString());
      result.append("\n");
    }
    return result.toString();
  }
}