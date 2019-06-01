import java.util.ArrayList;
import java.util.Collections;

class Trip {/*a trip segment which only happen on one bus or subway*/

  /** the start time of this trip segment */
  private final Time START_TIME;
  /** the end time of this trip segment */
  private Time endTime;
  /** the start station of this trip segment */
  private final TTC START_STATION;
  /** the end station of this trip segment */
  private TTC endStation;
  /** TYPE name of station or stop */
  private final String TYPE;
  /** the NUMBER of the bus or subway line of trip segment */
  private final String NUMBER;
  /** the list of stops from the given line NUMBER */
  private ArrayList<TTC> listOfStops;
  /** helps on the max $6 COST on trip */
  private double deducted;
  /** true if this trip segment is linked on the tail of previous whole trip, else this trip segment
   * is at the first of the whole trip*/
  private boolean isContinuous;

  /**
   * get the TYPE of this trip segment
   * @return current TYPE
   */
  String getTYPE() {
    return TYPE;
  }

  /**
   * set the list of stops into new list of stops
   * @param listOfStops new list of stops
   */
  void setListOfStops(ArrayList<TTC> listOfStops) {
    this.listOfStops = listOfStops;
  }

  /**
   * set the end station into new end station
   * @param endStation class Time new end station
   */
  void setEndStation(TTC endStation) {
    this.endStation = endStation;
  }

  /**
   * set end time into new end time
   * @param endTime class Time new end time
   */
  void setEndTime(Time endTime) {
    this.endTime = endTime;
  }

  /**
   * get is continuous
   * @return return current is continuous
   */
  boolean getContinuous() {
    return isContinuous;
  }

  /**
   * Construct a new Trip
   * @param start_time current start time
   * @param start_station current start station
   * @param type current segment TYPE
   * @param number current line NUMBER
   */
  Trip(Time start_time, TTC start_station, String type, String number){
    this.START_TIME = start_time;
    this.START_STATION = start_station;
    this.deducted = 0;
    this.listOfStops = new ArrayList<>();
    this.TYPE = type;
    this.NUMBER = number;
  }

  /**
   * find out an array list segment from start station to end station
   * @param reference_list given a subway or bus line which contain start station and end station
   * @return an array list segment which start with start station and end with end station
   */
  private ArrayList<TTC> generateSegmentsHelper(ArrayList<TTC> reference_list) {

    ArrayList<TTC> copy = new ArrayList<> (reference_list);
    int start = 0;
    int end = 0;

    while (!copy.get(start).equals(START_STATION)) {
      start += 1;
    }
    while (!copy.get(end).equals(endStation)) {
      end += 1;
    }
    if (start < end) {
      return new ArrayList<>(copy.subList(start, end + 1));
    } else if (start == end) {
      return new ArrayList<>(); // if enter and exit at the same station.
    } else {
      Collections.reverse(copy);
      return new ArrayList<>(copy.subList(end, start + 1));
    }
  }

  /**
   * return the array list segment between start station and end station
   * @return an array list segment from start station to end station if start station and
   * end station are in given subway line or bus line
   * else return empty array list
   */
  ArrayList<TTC> generateSegments() {
    if ((START_STATION instanceof Station) && (endStation instanceof Station)) {
      ArrayList<TTC> route = TTC.SUBWAY_LIST.get(NUMBER);
      if (route.contains(START_STATION) && route.contains(endStation)) {
        return generateSegmentsHelper(route);
      } else {
        return new ArrayList<>();
      }
    } else {
      ArrayList<TTC> route = TTC.BUS_MAP.get((START_STATION).getNUMBER());
      if (route.contains(START_STATION) && route.contains(endStation)) {
        return generateSegmentsHelper(route);
      } else {
        return new ArrayList<>();
      }
    }
  }

  /**
   * find this trip segment is part of the last whole trip or the begin of the new whole trip
   * @param curr_card the card which contain the last whole trip
   */
  void decideContinuity(Card curr_card) {
    if (curr_card.getTRIPS().size() != 0) { // if trips is not empty, then can get the last item and decide if continuous
      int last_index = curr_card.getTRIPS().size() - 1;
      WholeTrip last_whole_trip = (curr_card.getTRIPS().get(last_index));
      Trip last_trip = last_whole_trip.getWHOLE_TRIP().get(last_whole_trip.getWHOLE_TRIP().size() - 1); // get the last trip
      // evaluate if continuous
      this.isContinuous = START_TIME.timeInterval(last_trip.endTime) <= 120 &&
          last_trip.endStation.equals(START_STATION);
    }
  }

  /**
   * set deducted into new deducted
   * @param deducted new deducted
   */
  void setDeducted(double deducted) {
    this.deducted = deducted;
  }

  /**
   * return a list of stops which in format"A -> B -> C"
   * @return the view of a stop chain
   */
  private String viewListOfStops() {
    StringBuilder result = new StringBuilder();
    for (TTC stops : this.listOfStops) {
      result.append(stops.toString());
      result.append(" -> ");
    }
    return (result.toString()).substring(0, result.length() - 2);
  }

  /**
   * return string format of a trip segment
   * @return in format of "START_TIME TYPE NUMBER: viewListOfStops COST: deducted "
   */
  @Override
  public String toString() {
    return this.START_TIME.toString() + " " + this.TYPE + this.NUMBER + ": " + viewListOfStops() +
        " COST: "
        + this.deducted + "  " ;
  }

  /**
   * return the start station
   * @return current start station
   */
  TTC getSTART_STATION() {
    return START_STATION;
  }
}
