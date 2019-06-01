import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/** The main class for the project */
class Main {

  /** A hash map which used to storage all CardHolders */
  static HashMap<String, CardHolder> users = new HashMap<>();
  /** A hash map which used to storage all Cards */
  static HashMap<Integer, Card> cards = new HashMap<>();
  /** A hash map which used to storage all Stations */
  static HashMap<String, Station> stations = new HashMap<>();
  /** A hash map which used to storage all Stops */
  static HashMap<String, Stop> stops = new HashMap<>();
  /** Admin system which used to storage total COST and stations for that day */
  static AdminSystem currSystem;

  /**
   * main method of project
   * @param args default argument
   */
  public static void main(String[] args) {
    String fileName = "phase1/src/map.txt";// read map.txt
    String line;
    try {
      FileReader fileReader = new FileReader(fileName);
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      while ((line = bufferedReader.readLine()) != null) {
        String[] list = line.split(":");
        String[] leftList = list[0].split(",");//subway or bus and their number
        String[] rightList = list[1].split(",");//stations or stops
        String type = leftList[0].trim();// subway or bus
        String num = leftList[1].trim();// number of subway or bus
        ArrayList<TTC> map = new ArrayList<>();
        if (type.equals("subway")) {
          for (String aRightList : rightList) {
            Station newStation = new Station(aRightList.trim(), num);
            map.add(newStation);
            stations.put(aRightList.trim() + num, newStation);// update the Hash map stations
          }
          TTC.SUBWAY_LIST.put(num, map);
        } else {
          for (String aRightList : rightList) {
            Stop newStop = new Stop(aRightList.trim(), num);
            map.add(newStop);
            stops.put(aRightList.trim() + num, newStop);// update the Hash map stops
          }
          TTC.BUS_MAP.put(num, map);
        }
      }

      bufferedReader.close();

      EventHandler eventHandler= new EventHandler();
      fileName = "phase1/src/events.txt"; // read events.txt
      fileReader = new FileReader(fileName);
      bufferedReader = new BufferedReader(fileReader);
      while ((line = bufferedReader.readLine()) != null) {
        String[] newList = line.split(":");
        if (newList[0].equals("exit")) {
          currSystem.writeFile();
          for (String user : Main.users.keySet()) {
            Main.users.get(user).getTotalMoney().writeFile();
          }
        } else {
          eventHandler.handleEvent(newList[0], newList[1]); // handle event in EventHandler.java
        }
      }
      bufferedReader.close();
    }

    catch(FileNotFoundException ex) {
      System.out.println(
          "Unable to open file: " + fileName);
    }

    catch(IOException ex) {
      System.out.println(
          "Error reading file " + fileName);
    }

  }
}
