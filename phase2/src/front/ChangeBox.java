package front;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import User.CardHolder;
/* Change the name or password of card holder*/
class ChangeBox extends FrontManager {
  /**
   * Window which helps for change the name or the password of current user by the type
   * @param vehicleType String, whether this change window uses for name or password
   * @param currUser CardHolder, the current user who need to active a new card
   */
  public void display(String vehicleType, CardHolder currUser) {
    Stage changeStage = setSize();
    changeStage.initModality(Modality.APPLICATION_MODAL);
    GridPane gridOfChanging = new GridPane();
    gridOfChanging.setPadding(new Insets(50));
    gridOfChanging.setVgap(8);
    gridOfChanging.setHgap(10);

    addLabelGrid("New " + vehicleType + ": ", gridOfChanging, 0, 0);
    //Apply button will do the changing process base on the input of text field or password field
    //and goes back setting window
    Button apply = addButtonGrid("Apply", gridOfChanging, 0, 2);
    if (vehicleType.equals("Name")) {
      addChangeTextField(currUser, apply, changeStage, gridOfChanging, 1, 0);
    } else {
      addChangePasswordField(currUser, apply, changeStage, gridOfChanging, 1, 0);
    }

    //back button will return back to setting window and close current changing window
    Button back = addButtonGrid("back", gridOfChanging, 0, 3);
    back.setOnAction(
        e -> {
          SettingBox settingPage = new SettingBox();
          e.consume();
          changeStage.close();
          settingPage.display(currUser);
        });

    changeStage.setTitle("Change " + vehicleType);
    Scene changeScene = new Scene(gridOfChanging, 1024, 720);
    changeStage.setScene(changeScene);
    changeStage.show();
  }
}