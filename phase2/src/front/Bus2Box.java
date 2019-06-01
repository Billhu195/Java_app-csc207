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
/* Map of bus 2*/
class Bus2Box extends MapBox{
  /**
   * Window which contain the map of bus 2 by current user and can goes to each stop's station window by click the corresponding button
   * @param currUser CardHolder, current user who want to operate on this window
   */
  public void display(CardHolder currUser) {
    Stage busStage = setSize();
    busStage.initModality(Modality.APPLICATION_MODAL);
    GridPane gridOfBus = new GridPane();
    gridOfBus.setPadding(new Insets(50));
    gridOfBus.setVgap(8);
    gridOfBus.setHgap(10);

    //add stop button with corresponding stop of bus 2 on map, and link them to corresponding station window
    HashMap<String, Stop> stops = StopManager.getStopManager().getAllStops();
    addStationButton(stops.get("Spadina"), currUser, busStage, gridOfBus, 2, 4);
    addStationButton(stops.get("St.George"), currUser, busStage, gridOfBus, 4, 4);
    addStationButton(stops.get("Museum"), currUser, busStage, gridOfBus, 4, 6);
    addStationButton(stops.get("Queen's Park"), currUser, busStage, gridOfBus, 4, 8);
    addStationButton(stops.get("Union"), currUser, busStage,  gridOfBus, 6, 10);
    addStationButton(stops.get("Danforth"), currUser, busStage, gridOfBus, 8, 10);
    addStationButton(stops.get("Scar"), currUser, busStage, gridOfBus, 10, 10);

    StackPane busStack = new StackPane();
    gridOfBus.setOpacity(1);
    busStack.getChildren().add(gridOfBus);
    busStack.setPrefSize(450, 450);

    //back button will return back to main map window and close current bus 2 window
    Button back = addButtonGrid("back", gridOfBus, 12, 10);
    back.setOnAction(
        e -> {
          MapBox mapPage = new MapBox();
          e.consume();
          busStage.close();
          mapPage.display(currUser);
        });
    busStage.setTitle("The bus 2 map");
    Scene busScene = new Scene(busStack, 1024, 720);
    busStage.setScene(busScene);
    busStage.show();
  }
}