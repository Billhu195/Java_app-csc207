import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class CountDailyMoney {

  /** the current card holder who needs to count daily money */
  private final CardHolder HOLDER;
  /** today's cost of current card holder */
  private double todayCost;
  /** today's date */
  private final String DATE;

  /** Constructor of CountDailyMoney */
  CountDailyMoney(CardHolder holder, String date) {
    this.HOLDER = holder;
    this.DATE = date;
    this.todayCost = 0;
  }

  /**
   * set today's cost into new today's cost
   * @param todayCost new today's cost
   */
  void setTodayCost(double todayCost) {
    this.todayCost = todayCost;
  }

  /**
   * return today's cost
   * @return current cost of today
   */
  double getTodayCost() {
    return todayCost;
  }

  /**
   * write a report of current card holder, include the current date and how much money he or she
   * spent during today
   */
  void writeFile(){
    // Get the name of an output file from standard input.
    String fileName = "User " + this.HOLDER.getName() + " report";
    try (FileWriter file = new FileWriter(fileName, true)) {
      BufferedWriter modify = new BufferedWriter(file);
      String message = this.DATE + " You have spent "+ this.getTodayCost()+ System.lineSeparator();
      modify.append(message);
      modify.flush();
      modify.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}