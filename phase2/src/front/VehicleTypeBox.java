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
/*choose vehicle type for tap feature*/
class VehicleTypeBox extends FrontManager {
  /**
   * Window which helps for choose the type of vehicle to tap by current card on current stop
   * @param currCard Card, the card which user decide to use for tap
   * @param currStop the current stop which user decide to tap at
   */
  public void display(Card currCard, Stop currStop) {
    Stage typeStage = setSize();
    typeStage.initModality(Modality.APPLICATION_MODAL);
    GridPane gridOfType = new GridPane();
    gridOfType.setPadding(new Insets(50));
    gridOfType.setVgap(8);
    gridOfType.setHgap(10);

    addLabelGrid("Choose your vehicle Type: ", gridOfType, 2, 0);
    ChoiceBox vehicleType = addVehicleTypeChoiceBox(currStop, gridOfType, 3, 0);

    //Confirm button will find the vehicle type user choose base on the input of choice box vehicle type
    //and goes to vehicle number window to find which route of vehicle that user tap at
    Button confirm = addButtonGrid("Confirm", gridOfType, 0, 8);
    confirm.setOnAction(e -> {
      typeStage.close();
      VehicleNumBox num = new VehicleNumBox();
      String type = (String)vehicleType.getSelectionModel().getSelectedItem();
      num.display(currCard, type, currStop);
    });

    //back button will return back to choose card window and close current vehicle type window
    Button back = addButtonGrid("back", gridOfType, 1, 8);
    back.setOnAction(
        e -> {
          e.consume();
          typeStage.close();
          CardChooseBox choosePage = new CardChooseBox();
          choosePage.display(currCard.getCardHolder(), currStop);
        });

    typeStage.setTitle("Vehicle Type");
    Scene typeScene = new Scene(gridOfType, 1024, 720);
    typeStage.setScene(typeScene);
    typeStage.show();
  }
}