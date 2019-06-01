package front;

import Manager.StopManager;
import Transit.Stop;
import User.CardHolder;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
/* The map page*/
class MapBox extends FrontManager {
  /**
   * Window which contain the map of all subways and buses by current user and can goes to each stop's station window by click the corresponding button
   * @param currUser CardHolder, current user who want to operate on this window
   */
  void display(CardHolder currUser) {
    Stage mapStage = new Stage();
    mapStage.setMaxWidth(1239);
    mapStage.setMinWidth(1239);
    mapStage.setMaxHeight(743);
    mapStage.setMinHeight(743);
    mapStage.initModality(Modality.APPLICATION_MODAL);
    BorderPane mapBorder = new BorderPane();
    mapBorder.setPadding(new Insets(50));

    //Left vbox which contain the label shows which color represent which vehicle, and buttons which link to small map windows which only contain one vehicle
    VBox left = new VBox();
    left.setPrefWidth(100);
    addLabelVBox("Red = subway 1", left);
    addLabelVBox("Blue = subway 2", left);
    addLabelVBox("Yellow = subway 3", left);
    addLabelVBox("Green = bus 1", left);
    addLabelVBox("Purple = bus 2", left);
    addLabelVBox("Orange = bus 3", left);
    Button subway1 = addButtonVBox("subway 1", left);
    Subway1Box subway1Map = new Subway1Box();
    subway1.setOnAction(e -> {
      mapStage.close();
      subway1Map.display(currUser);
    });
    Button subway2 = addButtonVBox("subway 2", left);
    Subway2Box subway2Map = new Subway2Box();
    subway2.setOnAction(e-> {
      mapStage.close();
      subway2Map.display(currUser);
    });
    Button subway3 = addButtonVBox("subway 3", left);
    Subway3Box subway3Map = new Subway3Box();
    subway3.setOnAction(e -> {
      mapStage.close();
      subway3Map.display(currUser);
    });
    Bus1Box bus1Map = new Bus1Box();
    Button bus1 = addButtonVBox("bus 1", left);
    bus1.setOnAction(e-> {
      mapStage.close();
      bus1Map.display(currUser);
    });
    Button bus2 = addButtonVBox("bus 2", left);
    Bus2Box bus2Map = new Bus2Box();
    bus2.setOnAction(e-> {
      mapStage.close();
      bus2Map.display(currUser);
    });
    Button bus3 = addButtonVBox("bus 3", left);
    Bus3Box bus3Map = new Bus3Box();
    bus3.setOnAction(e-> {
      mapStage.close();
      bus3Map.display(currUser);
    });

    //Mid stack pane which contain the main map
    StackPane mid = new StackPane();
    GridPane gridOfMap = new GridPane();
    mid.setPrefSize(450, 450);
    mid.setPadding(new Insets(10));
    gridOfMap.setVgap(8);
    gridOfMap.setHgap(10);

    //add stop button with corresponding stop of all subways and buses on map, and link them to corresponding station window
    HashMap<String, Stop> stops = StopManager.getStopManager().getAllStops();
    addStationButton(stops.get("Vaughan"), currUser, mapStage, gridOfMap, 0, 0);
    addStationButton(stops.get("Finch"), currUser, mapStage, gridOfMap, 8, 0);
    addStationButton(stops.get("McCowan"), currUser, mapStage, gridOfMap, 12, 0);
    addStationButton(stops.get("Dupont"), currUser, mapStage, gridOfMap, 2, 2);
    addStationButton(stops.get("Rosedale"), currUser, mapStage, gridOfMap, 8, 2);
    addStationButton(stops.get("Lawrence"), currUser, mapStage, gridOfMap, 12, 2);
    addStationButton(stops.get("Kipling"), currUser, mapStage, gridOfMap, 0, 4);
    addStationButton(stops.get("Spadina"), currUser, mapStage, gridOfMap, 2, 4);
    addStationButton(stops.get("St.George"), currUser, mapStage, gridOfMap, 4, 4);
    addStationButton(stops.get("Bay"), currUser, mapStage, gridOfMap, 6, 4);
    addStationButton(stops.get("Bloor"), currUser, mapStage, gridOfMap, 8, 4);
    addStationButton(stops.get("Sherbourne"), currUser, mapStage, gridOfMap, 10, 4);
    addStationButton(stops.get("Kennedy"), currUser, mapStage, gridOfMap, 12, 4);
    addStationButton(stops.get("Museum"), currUser, mapStage, gridOfMap, 4, 6);
    addStationButton(stops.get("Wellesley"), currUser, mapStage, gridOfMap, 8, 6);
    addStationButton(stops.get("Queen's Park"), currUser, mapStage, gridOfMap, 4, 8);
    addStationButton(stops.get("College"), currUser, mapStage, gridOfMap, 8, 8);
    addStationButton(stops.get("Union"), currUser, mapStage, gridOfMap, 6, 10);
    addStationButton(stops.get("Danforth"), currUser, mapStage, gridOfMap, 8, 10);
    addStationButton(stops.get("Scar"), currUser, mapStage, gridOfMap, 10, 10);

    //back button will return back to user window and close current map window
    Button back = addButtonGrid("back", gridOfMap, 12, 10);
    back.setOnAction(
        e -> {
          UserBox userPage = new UserBox();
          e.consume();
          mapStage.close();
          userPage.display(currUser);
        });

    gridOfMap.setOpacity(1);
    mid.getChildren().add(gridOfMap);
    mid.setPrefSize(450, 250);
    mapBorder.setCenter(mid);
    mapBorder.setLeft(left);
    mapStage.setTitle("The main map");
    Scene mapScene = new Scene(mapBorder, 1239, 743);
    mapStage.setScene(mapScene);
    mapStage.show();
  }
}