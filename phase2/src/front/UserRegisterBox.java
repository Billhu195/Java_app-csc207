package front;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
/*the register page of card holder*/
class UserRegisterBox extends FrontManager {
  /**Window which helps new users register their own account
   */
  public void display() {
    Stage regStage = setSize();
    regStage.initModality(Modality.APPLICATION_MODAL);
    GridPane gridOfRegister = new GridPane();
    gridOfRegister.setPadding(new Insets(50));
    gridOfRegister.setVgap(8);
    gridOfRegister.setHgap(10);

    addLabelGrid("enter your name", gridOfRegister, 0, 0);
    TextField name = addTextFieldGrid(gridOfRegister, 1, 0);
    addLabelGrid("enter your email", gridOfRegister, 0, 1);
    TextField email = addTextFieldGrid(gridOfRegister, 1, 1);
    addLabelGrid("enter your password", gridOfRegister, 0, 2);

    //Confirm button will do the create new account process base on the input of text fields name, input and password field
    //and goes to log in window and shows message
    Button confirm = addButtonGrid("confirm", gridOfRegister, 1, 4);
    addRegPasswordField(name, email, confirm, regStage, gridOfRegister, 1, 2);

    //back button will return back to log in window and close the current user register window
    Button back = addButtonGrid("back", gridOfRegister, 0, 4);
    LogInBox logInPage = new LogInBox();
    back.setOnAction(e -> {
      regStage.close();
      logInPage.display();
    });

    Scene regScene = new Scene(gridOfRegister, 1024, 720);
    regStage.setScene(regScene);
    regStage.setTitle("User Register page");
    regStage.show();
  }
}