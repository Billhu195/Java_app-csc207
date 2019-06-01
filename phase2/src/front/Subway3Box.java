package front;

import Manager.StopManager;
import Transit.Stop;
import User.CardHolder;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
/*the map of subway 3*/
class Subway3Box extends MapBox {
  /**
   * Window which contain the map of subway 3 by current user and can goes to each stop's station window by click the corresponding button
   * @param currUser CardHolder, current user who want to operate on this window
   */
  public void display(CardHolder currUser) {
    Stage subwayStage = setSize();
    subwayStage.initModality(Modality.APPLICATION_MODAL);
    GridPane gridOfSubway = new GridPane();
    gridOfSubway.setPadding(new Insets(50));
    gridOfSubway.setVgap(8);
    gridOfSubway.setHgap(10);

    //add stop button with corresponding stop of subway 3 on map, and link them to corresponding station window
    HashMap<String, Stop> stops = StopManager.getStopManager().getAllStops();
    addStationButton(stops.get("Finch"), currUser, subwayStage, gridOfSubway, 8, 0);
    addStationButton(stops.get("McCowan"), currUser, subwayStage, gridOfSubway, 12, 0);
    addStationButton(stops.get("Lawrence"), currUser, subwayStage, gridOfSubway, 12, 2);
    addStationButton(stops.get("Kennedy"), currUser, subwayStage, gridOfSubway, 12, 4);

    StackPane subwayStack = new StackPane();
    gridOfSubway.setOpacity(1);
    subwayStack.getChildren().add(gridOfSubway);
    subwayStack.setPrefSize(450, 450);

    //back button will return back to main map window and close current subway 3 window
    Button back = addButtonGrid("back", gridOfSubway, 12, 10);
    back.setOnAction(
        e -> {
          MapBox mapPage = new MapBox();
          e.consume();
          subwayStage.close();
          mapPage.display(currUser);
        });
    subwayStage.setTitle("The subway 3 map");
    Scene subwayScene = new Scene(subwayStack, 1024, 720);
    subwayStage.setScene(subwayScene);
    subwayStage.show();
  }
}