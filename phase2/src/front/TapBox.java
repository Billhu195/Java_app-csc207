package front;

import Card.*;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Transit.Stop;
/*the page which operate tap feature*/
class TapBox extends FrontManager {
  /**
   * Window which helps for choose tap in or tap out at current station's current vehicle num and current vehicle type at current time to tap by current card
   * @param currCard Card, the card which user decide to use for tap
   * @param vehicleType String, the current vehicle type
   * @param vehicleNum String, the current vehicle number
   * @param currTime String, the current time
   * @param currStop Stop, the current stop which user decide to tap at
   */
  public void display(Card currCard, String vehicleType, String vehicleNum, String currTime, Stop currStop) {
    Stage tapStage = setSize();
    tapStage.initModality(Modality.APPLICATION_MODAL);
    GridPane gridOfTap = new GridPane();
    gridOfTap.setPadding(new Insets(50));
    gridOfTap.setVgap(8);
    gridOfTap.setHgap(10);

    //TapIn or tapOut button will do the tap in or tap out process base on current card, current vehicle type, current vehicle number, current time and current stop
    //and shows message to user
    Button tapIn = addButtonGrid("Tap In", gridOfTap, 0, 0);
    Button tapOut = addButtonGrid("Tap Out", gridOfTap, 0, 1);
    Label message = addEmptyLabel(gridOfTap, 1, 1);
    tapIn.setOnAction(
        e -> {
          String warning = currCard.Tap("Tap_in", "" + vehicleType, vehicleNum, currTime, currStop.getNAME());
          message.setText(warning);
        });
    tapOut.setOnAction(e -> {
      String warning = currCard.Tap("Tap_out", "" + vehicleType, vehicleNum, currTime, currStop.getNAME());
      message.setText(warning);
    });

    //back button will return back to time window and close the current tap window
    Button back = addButtonGrid("back", gridOfTap, 1, 0);
    back.setOnAction(
        e -> {
          e.consume();
          tapStage.close();
          Stage fxmlStage = (Stage) back.getScene().getWindow();
          try {
            Parent fxmlRoot = FXMLLoader.load(getClass().getResource("Controller/Map.fxml"));
            Scene mapScene = new Scene(fxmlRoot);
            Stage fxmlWindow = (Stage) fxmlStage.getScene().getWindow();
            fxmlWindow.setScene(mapScene);
            fxmlWindow.show();
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        });

    tapStage.setTitle("Tap");
    Scene tapScene = new Scene(gridOfTap, 1024, 720);
    tapStage.setScene(tapScene);
    tapStage.show();
  }
}