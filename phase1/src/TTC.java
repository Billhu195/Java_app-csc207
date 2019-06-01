import java.util.ArrayList;
import java.util.HashMap;

class TTC{
  /**
   * HashMap pool for subway system,
   * key: String subway_number, value: ArrayList<TTC> stations
   */
  static final HashMap<String, ArrayList<TTC>> SUBWAY_LIST = new HashMap<>();
  /**
   * HashMap pool for bus system,
   * key: String bus_number, value: ArrayList<TTC> stops
   */
  static final HashMap<String, ArrayList<TTC>> BUS_MAP = new HashMap<>();
  /**
   * Private NUMBER, NAME, COST are use to pass down to other sub-class
   */
  private final String NUMBER;
  private final String NAME;
  /**
   * Set static final maximum fee for each trip as 6
   */
  static final double CAP = 6;

  /**
   * Constructor for TTC set NAME, COST, NUMBER;
   * @param name Station/stop NAME
   * @param number station/stop NUMBER
   */
  TTC(String name, String number) {
    this.NAME = name;
    this.NUMBER = number;
  }

  /**
   * Expose/getter for TTC NUMBER
   * @return String NUMBER
   */
  String getNUMBER() {
    return NUMBER;
  }

  /**
   * Expose/getter for TTC NAME;
   * @return String NAME
   */
  String getNAME() {
    return NAME;
  }

  /**
   * Boolean equal to check the two bus/subway is the same;
   * @param item class TTC passed down parameter
   * @return boolean
   */
  boolean equals(TTC item) {
    return item.getNAME().equals(this.NAME);
  }

  /**
   * Return the TTC bus/subway 's NAME as a String
   * @return String
   */
  @Override
  public String toString() {
    return NAME;
  }
}

