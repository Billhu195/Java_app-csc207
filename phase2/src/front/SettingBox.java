package front;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import User.CardHolder;
/*The setting page*/
class SettingBox extends FrontManager {
  /**
   * Window which helps for changing the name or password of current user
   * @param currUser CardHolder, the current user
   */
  public void display(CardHolder currUser){
    Stage settingStage = setSize();
    settingStage.initModality(Modality.APPLICATION_MODAL);
    GridPane gridOfSetting = new GridPane();
    gridOfSetting.setPadding(new Insets(50));
    gridOfSetting.setVgap(8);
    gridOfSetting.setHgap(10);

    //Change name button will do the change user name process base on current user
    //and goes to change window to change the name
    Button changeName = addButtonGrid("Change Your Name", gridOfSetting, 0, 0);
    ChangeBox NameChange = new ChangeBox();
    changeName.setOnAction(e -> {
      settingStage.close();
      NameChange.display("Name", currUser);
    });

    //Change password button will do the change user password process base on current user
    //and goes to change window to change the password
    Button changePassword = addButtonGrid("Change Your Password", gridOfSetting, 0, 1);
    ChangeBox PasswordChange = new ChangeBox();
    changePassword.setOnAction(e -> {
      settingStage.close();
      PasswordChange.display("Password", currUser);
    });

    //back button will return back to user window and close current setting window
    Button back = addButtonGrid("back", gridOfSetting, 0, 2);
    back.setOnAction(e -> {
      UserBox userPage = new UserBox();
      e.consume();
      settingStage.close();
      userPage.display(currUser);
    });

    settingStage.setTitle("Setting");
    Scene settingScene = new Scene(gridOfSetting, 1024, 720);
    settingStage.setScene(settingScene);
    settingStage.show();
  }
}