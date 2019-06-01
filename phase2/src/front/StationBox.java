package front;

import Manager.RouteManager;
import Manager.StopManager;
import Transit.Route;
import Transit.Stop;
import User.CardHolder;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
/* The station page*/
public class StationBox extends FrontManager {
  /**
   * Window which shows information of current stop with current user
   * @param currUser CardHolder, the current user who need to do action on this stop
   * @param stop Stop, the current stop
   */
  public void display(CardHolder currUser, Stop stop) {
    Stage tapping = setSize();
    tapping.initModality(Modality.APPLICATION_MODAL);
    GridPane gridOfStop = new GridPane();
    gridOfStop.setPadding(new Insets(50));
    gridOfStop.setVgap(8);
    gridOfStop.setHgap(10);

    //Tap button will do begin the tap process base on current user and current stop
    //and goes to choose window to find which card current user use for tap
    addLabelGrid("Initialize new tap activity", gridOfStop, 0, 0);
    Button tap = addButtonGrid("Tap Activity", gridOfStop, 1, 0);
    CardChooseBox chooseBox = new CardChooseBox();
    tap.setOnAction(e-> {
      tapping.close();
      chooseBox.display(currUser, stop);
    });

    //Confirm button will do find the time of next vehicle base on choice box routes
    //and show the result into message
    addLabelGrid("Choose your route to see next vehicle: ", gridOfStop, 0, 2);
    ChoiceBox routes = addVehicleNumChoiceBox(stop, gridOfStop, 1, 2);
    Button confirm = addButtonGrid("confirm", gridOfStop, 3, 2);
    Label messageNxt = addEmptyLabel(gridOfStop, 4, 2);
    confirm.setOnAction( e -> {
      String routeNum = (String)routes.getSelectionModel().getSelectedItem();
      Route route = RouteManager.getRouteManager().getAllRoutes().get(routeNum);
      messageNxt.setText(route.nextTransit(stop, LocalTime.now()));
    });

    //Find path button will do find the cheapest path and shortest path base on choice box targetStop and choice box cardType
    //and show the result into message
    addLabelGrid("Find available path from this stop to ", gridOfStop, 0, 3);
    ChoiceBox<String> targetStop = new ChoiceBox<>();
    HashMap<String, Stop> stops =  StopManager.getStopManager().getAllStops();
    if (!stops.isEmpty()){
      for (String key: stops.keySet()){
        targetStop.getItems().add(key);
      }
    }
    GridPane.setConstraints(targetStop, 1, 3);
    gridOfStop.getChildren().add(targetStop);
    addLabelGrid(" using card type ", gridOfStop, 0, 4);
    ChoiceBox<String> cardType = new ChoiceBox<>();
    cardType.getItems().addAll("monthly card", "regular child", "regular adult", "regular student", "regular senior");
    GridPane.setConstraints(cardType, 1, 4);
    gridOfStop.getChildren().add(cardType);

    ///FindPath Button
    Button findPath = addButtonGrid("Find Path", gridOfStop, 2, 4);
    Label messagePath = addEmptyLabel(gridOfStop, 0, 5);
    findPath.setOnAction( e -> {
      Stop selectStop = StopManager.getStopManager().getAllStops().get(targetStop.getSelectionModel().getSelectedItem());
      String currCardType = cardType.getSelectionModel().getSelectedItem();
      messagePath.setText(currUser.findPath(stop, selectStop, currCardType));
      messagePath.setTextFill(Color.rgb(0,0,0));
      try {
        StopManager.getStopManager().saveToFile();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });

    //back button will return back to main map window and close current station window
    Button back = addButtonGrid("back", gridOfStop, 2, 5);
    back.setOnAction(
        e -> {
          e.consume();
          tapping.close();
//          MapBox map = new MapBox();
//          map.display(currUser);
          Stage stage = (Stage) back.getScene().getWindow();
          try {
            Parent root = FXMLLoader.load(getClass().getResource("Controller/Map.fxml"));
            Scene mapscene = new Scene(root);
            Stage window = (Stage) stage.getScene().getWindow();
            window.setScene(mapscene);
            window.show();
            FrontManager.setCardHolder(currUser);
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        });

    tapping.setTitle("Station");
    Scene scene = new Scene(gridOfStop, 1024, 720);
    tapping.setScene(scene);
    tapping.show();
  }
}