import java.util.ArrayList;

class Station extends TTC{
  /**
   * Static double fee for each ride on station/subway;
   */
  static final double COST = 0.5;

  /**
   * Create a HashMap for subway, subway number as key, subway stations as value.
   * @param name String subway name
   * @param subway_number String subway number as key
   */
  Station(String name, String subway_number) {
    super(name, subway_number);
    if (!TTC.SUBWAY_LIST.containsKey(subway_number)) {
      ArrayList<TTC> stations = new ArrayList<>();
      stations.add(this);
      TTC.SUBWAY_LIST.put(subway_number, stations);
    } else {
      TTC.SUBWAY_LIST.get(subway_number);
    }
  }

  /**
   * Return a String formatted as Station station_name
   * @return String
   */
  @Override
  public String toString() {
    return "Station" + " " + this.getNAME();
  }
}
