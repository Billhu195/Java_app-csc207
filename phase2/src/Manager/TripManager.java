package Manager;

import Trip.*;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages trip
 */
public class TripManager {

  /** LOGGER */
  private static final Logger LOGGER = Logger.getLogger(StopManager.class.getName());
  /** CONSOLE_HANDLER */
  private static final Handler CONSOLE_HANDLER = new ConsoleHandler();

  /** An ArrayList that keeps track of all user objects */
  private HashMap<LocalDate, ArrayList<Trip>> allTrips;
  /** String File Path*/
  private static final String PATH = "./serializedTripObjects.ser";
  /** Object TripManager*/
  private static TripManager tripManager;

  /**
   * Getter for allTrips
   * @return HashMap that store date: trip
   */
  public HashMap<LocalDate, ArrayList<Trip>> getAllTrips() {
    return allTrips;
  }

  /**
   * Getter for Object tripManager and deserialize
   * @return TripManager
   */
  public static TripManager getTripManager(){
    if (tripManager == null){
      try {
        tripManager  = new TripManager();
      } catch (ClassNotFoundException | IOException e) {
        e.printStackTrace();
      }
    }
    return tripManager;
  }

  /**
   * Creates a new empty TripManager.
   * @throws IOException if not found filepath input/output
   * @throws ClassNotFoundException if not found file in file PATH
   */
  private TripManager() throws ClassNotFoundException, IOException {
    allTrips = new HashMap<>(); // hash map of Stop id: stop

    // Associate the handler with the LOGGER.
    LOGGER.setLevel(Level.ALL);
    CONSOLE_HANDLER.setLevel(Level.ALL);
    LOGGER.addHandler(CONSOLE_HANDLER);

    // Populates the record list using stored data, if it exists.
    File file = new File(PATH);
    if (file.exists()) {
      readFromFile();
    } else {
      file.createNewFile();
    }
  }

  /**
   * Read from file PATH which deserialize the map and trip data
   * @throws ClassNotFoundException if not found file in file PATH
   */
  private void readFromFile() throws ClassNotFoundException {
    try {
      InputStream file = new FileInputStream(PATH);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);

      //deserialize the Map
      allTrips = (HashMap<LocalDate, ArrayList<Trip>>) input.readObject();
      input.close();
    } catch (IOException ex) {
      LOGGER.log(Level.SEVERE, "Cannot read from input.", ex);
    }
  }

  /**
   * save the history trip into a file
   * @param date Locate date default is now
   * @param trip Trip object
   */
  public void add(LocalDate date, Trip trip) {
    if (allTrips.keySet().contains(date)) {
      ArrayList<Trip> TripList = allTrips.get(date);
      TripList.add(trip);
    } else {
      ArrayList<Trip> newTripList = new ArrayList<>();
      newTripList.add(trip);
      allTrips.put(date, newTripList);
    }

    LOGGER.log(Level.FINE, "Added a new Trip.");
    try {
      saveToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Writes the trips to file at filePath. Serialize
   * @throws IOException file PATH is not input
   */
  public void saveToFile() throws IOException {

    OutputStream file = new FileOutputStream(PATH);
    OutputStream buffer = new BufferedOutputStream(file);
    ObjectOutput output = new ObjectOutputStream(buffer);

    // serialize the Map
    output.writeObject(allTrips);
    output.close();
  }

  /**
   * To String format as Date: Trip of each date
   * @return String format as Date: Trip
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (LocalDate date : allTrips.keySet()) {
      result.append(date);
      result.append(": ");
      result.append("\n");
      for (Trip trip : allTrips.get(date)) {
        result.append(trip.toString());
        result.append("\n");
      }
    }
    return result.toString();
  }
}