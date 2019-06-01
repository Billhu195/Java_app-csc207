import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;

class AdminSystem implements Serializable {

  /** An ArrayList of double to storage total COST and stations */
  private ArrayList<Double> data;
  /** date is used to storage current date */
  private final String DATE;
  /** numberOfDays used to storage the number of days that system has ran */
  private static int numberOfDays = 0;

  /** Constructor of Admin System */
  AdminSystem(String date){
    this.DATE = date;
    this.data = new ArrayList<>(); // an ArrayList that contains two items, totalCost and totalStation
    this.data.add(0.0);
    this.data.add(0.0);
    numberOfDays += 1;

    // somehow load the data back.
    // Deserialization
    try {
      // Reading the object from a file
      FileInputStream file = new FileInputStream("data-" + DATE + ".bin");
      ObjectInputStream in = new ObjectInputStream(file);

      // Method for deserialization of object
      //noinspection unchecked
      this.data = (ArrayList<Double>) in.readObject();

      in.close();
      file.close();

    } catch (IOException ex) {
//      System.out.println("IOException is caught");
    } catch (ClassNotFoundException ex) {
      System.out.println("ClassNotFoundException is caught");
    }
  }

  /**
   * Update data
   * @param cost the new total COST
   * @param station the new total stations
   */
  void setData(double cost, double station) {
    this.data = new ArrayList<>();
    this.data.add(cost);
    this.data.add(station);

    try {
      // Saving of object in a file
      FileOutputStream file = new FileOutputStream("data-" + DATE + ".bin");
      ObjectOutputStream out = new ObjectOutputStream(file);

      // Method for serialization of object
      out.writeObject(data);

      out.close();
      file.close();

    } catch (IOException ex) {
      System.out.println("IOException is caught");
    }
  }

  /**
   * return the number of days in this system
   * @return current number of days
   */
  private int getNumberOfDays() {
    return numberOfDays;
  }

  /**
   * return the date of this system
   * @return current date
   */
  String getDATE() {
    return DATE;
  }

  /**
   * return the total cost in this system
   * @return total cost
   */
  Double getTotalCost() {
    return this.data.get(0);
  }

  /**
   * return the total station number in this system
   * @return total station number
   */
  Double getTotalStation() {
    return this.data.get(1);
  }

  /**
   * write file to storage the information
   */
  void writeFile(){
    String fileName = "Day " + this.DATE + " report";
    try (Writer writer = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(fileName), "utf-8"))) {

      writer.write("Today's total COST is: " + this.getTotalCost() + ". ");
      writer.write("Today's total station is: " + this.getTotalStation() + ". ");
      writer.write("Today's number is: " + this.getNumberOfDays());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
