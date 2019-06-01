package front;

import Card.Card;
import Transit.Stop;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
/*choose vehicle number of tap feature*/
class VehicleNumBox extends FrontManager {
  /**
   * Window which helps for choose the vehicle number of vehicle to tap by current card on current stop with vehicle type
   * @param currCard Card, the card which user decide to use for tap
   * @param vehicleType String, the current vehicle type
   * @param currStop Stop, the current stop which user decide to tap at
   */
  public void display(Card currCard, String vehicleType, Stop currStop) {
    Stage numStage = setSize();
    numStage.initModality(Modality.APPLICATION_MODAL);
    GridPane gridOfNum = new GridPane();
    gridOfNum.setPadding(new Insets(50));
    gridOfNum.setVgap(8);
    gridOfNum.setHgap(10);

    addLabelGrid("Choose your vehicle number: ", gridOfNum, 2, 2);
    ChoiceBox vehicleNum = addSomeVehicleNumChoiceBox(currStop, vehicleType, gridOfNum, 3, 2);

    //Confirm button will find the vehicle number which user choose base on the input of choice box vehicle num
    //and goes to time window to find the time when that user tap
    Button confirm = addButtonGrid("Confirm", gridOfNum, 0, 8);
    confirm.setOnAction(e -> {
      String num = (String) vehicleNum.getSelectionModel().getSelectedItem();
      numStage.close();
      TimeBox timePage = new TimeBox();
      timePage.display(currCard, vehicleType, num, currStop);
    });

    //back button will return back to vehicle type window and close current vehicle num window
    Button back = addButtonGrid("back", gridOfNum, 1, 8);
    back.setOnAction(
        e -> {
          e.consume();
          numStage.close();
          VehicleTypeBox typeBox = new VehicleTypeBox();
          typeBox.display(currCard, currStop);
        });

    numStage.setTitle("Vehicle Num");
    Scene numScene = new Scene(gridOfNum, 1024, 720);
    numStage.setScene(numScene);
    numStage.show();
  }
}