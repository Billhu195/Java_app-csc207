package other;
import Card.Card;
import Transit.Route;
import Transit.Stop;
import Trip.Trip;
import User.*;
import Manager.CardManager;
import Manager.RouteManager;
import Manager.StopManager;
import Manager.TripManager;
import Manager.UserManager;
import front.HomeBox;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import Exception.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/** The main class for the project */
public class Main extends Application{

  /**
   * main method of project
   * @param args default argument
   */
  public static void main(String[] args) throws IOException, InvalidInputException {
    boolean canExecute = false;
    File file = new File("./serializedTripObjects.ser");
    if (!file.exists()) {
      canExecute = true;
    }
    createManager();
    if (canExecute) {
      readManager();
      Test();
    }
    launch(args);
  }

  /**
   * Website of this project GUI
   * @param primaryStage The first stage shown
   */
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Web Page");

    BorderPane borderOfUser = new BorderPane();
    borderOfUser.setPadding(new Insets(500, 300, 500, 300));

    Button web = new Button();
    web.setText("Home Page");
    HomeBox home = new HomeBox();
    web.setOnAction(e -> home.display());

    borderOfUser.setCenter(web);

    StackPane layoutWeb = new StackPane();
    layoutWeb.getChildren().add(borderOfUser);
    Scene website = new Scene(layoutWeb, 1024, 720);

    primaryStage.setScene(website);
    primaryStage.show();
  }

  /**
   * Creates and initialize manager classes and store them in corresponding classes.
   */
  private static void createManager() {

    Stop.setStopManager(StopManager.getStopManager());

    Card.setCardManager(CardManager.getCardManager());

    Trip.setTripManager(TripManager.getTripManager());

    User.setUserManager(UserManager.getUserManager());

    Route.setRouteManager(RouteManager.getRouteManager());
  }

  /**
   * Load data from files.
   * @throws IOException readFromCSVFile would raise IOExceptions
   */
  private static void readManager()
      throws IOException {
    // Loads data from a CSV for first launch of the program
    Stop.getStopManager().readFromCSVFile("phase2/src/map.txt");

    Card.getCardManager().readFromCSVFile("phase2/src/Card.txt");

    User.getUserManager().readFromCSVFile("phase2/src/Users.txt");

    Route.getRouteManager().readFromCSVFile("phase2/src/map.txt");
  }

  /**
   * Perform some tests and pre-load trips and other information into the system.
   */
  private static void Test() throws InvalidInputException {

    String line;
    String fileName = "phase2/src/test.txt";
    try {
      FileReader fileReader;
      BufferedReader bufferedReader;

      fileReader = new FileReader(fileName);
      bufferedReader = new BufferedReader(fileReader);
      while ((line = bufferedReader.readLine()) != null) {
        String[] newList = line.split(":");
        Test.handleEvent(newList[0], newList[1]); // handle event in Manager.EventHandler.java
      }
      bufferedReader.close();
    }

    catch(FileNotFoundException ex) {
      System.out.println(
          "Unable to open file: " + fileName);
    } catch(IOException ex) {
      System.out.println(
          "Error reading file " + fileName);
    }
  }
}