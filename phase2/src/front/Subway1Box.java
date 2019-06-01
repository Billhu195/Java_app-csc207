package front;

import Manager.StopManager;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import User.CardHolder;
import Transit.Stop;
/*the map of subway 1*/
class Subway1Box extends MapBox{
  /**
   * Window which contain the map of subway 1 by current user and can goes to each stop's station window by click the corresponding button
   * @param currUser CardHolder, current user who want to operate on this window
   */
  public void display(CardHolder currUser){
    Stage subwayStage = setSize();
    subwayStage.initModality(Modality.APPLICATION_MODAL);
    GridPane GridOfSubway = new GridPane();
    GridOfSubway.setPadding(new Insets(50));
    GridOfSubway.setVgap(8);
    GridOfSubway.setHgap(10);

    //add stop button with corresponding stop of subway 1 on map, and link them to corresponding station window
    HashMap<String, Stop> stops = StopManager.getStopManager().getAllStops();
    addStationButton(stops.get("Vaughan"), currUser, subwayStage, GridOfSubway, 0, 0);
    addStationButton(stops.get("Finch"), currUser, subwayStage, GridOfSubway, 8, 0);
    addStationButton(stops.get("Dupont"), currUser, subwayStage, GridOfSubway, 2, 2);
    addStationButton(stops.get("Rosedale"), currUser, subwayStage, GridOfSubway, 8, 2);
    addStationButton(stops.get("Spadina"), currUser, subwayStage, GridOfSubway, 2, 4);
    addStationButton(stops.get("St.George"), currUser, subwayStage, GridOfSubway, 4, 4);
    addStationButton(stops.get("Bloor"), currUser, subwayStage, GridOfSubway, 8, 4);
    addStationButton(stops.get("Museum"), currUser, subwayStage, GridOfSubway, 4, 6);
    addStationButton(stops.get("Queen's Park"), currUser, subwayStage, GridOfSubway, 4, 8);
    addStationButton(stops.get("Union"), currUser, subwayStage, GridOfSubway, 6, 10);

    StackPane subwayStack = new StackPane();
    GridOfSubway.setOpacity(1);
    subwayStack.getChildren().add(GridOfSubway);
    subwayStack.setPrefSize(450, 450);

    //back button will return back to main map window and close current subway 1 window
    Button back = addButtonGrid("back", GridOfSubway, 12, 10);
    back.setOnAction(e -> {
      MapBox mapPage = new MapBox();
      e.consume();
      subwayStage.close();
      mapPage.display(currUser);
      });
    subwayStage.setTitle("The subway 1 map");
    Scene subwayScene = new Scene(subwayStack, 1024, 720);
    subwayStage.setScene(subwayScene);
    subwayStage.show();
  }
}