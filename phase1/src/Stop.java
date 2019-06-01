import java.util.ArrayList;

class Stop extends TTC{
  /**
   * static double for fee as each ride for bus.
   */
  static final double COST = 2.0;

  /**
   *Create a HashMap for each bus stop; bus number is the key, stops is a value;
   * @param name String bus name
   * @param bus_number String bus number as key
   */
  Stop(String name, String bus_number) {
    super(name, bus_number);
    // in main, Stop 502 = new Stop(), the stop constructor already is a ttc
    if (!TTC.BUS_MAP.containsKey(bus_number)) {
      ArrayList<TTC> stops = new ArrayList<>();
      stops.add(this);
      TTC.BUS_MAP.put(bus_number, stops);
    } else {
      TTC.BUS_MAP.get(bus_number);
    }
  }

  /**
   * Return a String as formatted as Stop 'bus name'
   * @return String
   */
  @Override
  public String toString() {
    return "Stop" + " " + this.getNAME();
  }
}
