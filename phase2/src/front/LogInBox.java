package front;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;

/** the log in page*/
class LogInBox extends FrontManager {
  /**
   * Window which helps users to log in their account
   */
  public void display(){
    Stage logInStage = setSize();
    logInStage.initModality(Modality.APPLICATION_MODAL);
    GridPane gridOfLogIn = new GridPane();
    gridOfLogIn.setPadding(new Insets(50));
    gridOfLogIn.setVgap(8);
    gridOfLogIn.setHgap(10);

    addLabelGrid("Email: ", gridOfLogIn, 0, 0);
    TextField email = addTextFieldGrid(gridOfLogIn, 1, 0);
    addLabelGrid("Password: ", gridOfLogIn, 0, 1);
    //Log in button do the log in process base on the input of test field email and password field password
    Button logIn = addButtonGrid("Log In", gridOfLogIn, 2, 0);
    addLogInPasswordField(email, logIn, logInStage, gridOfLogIn, 1, 1);
    //Register button goes to register window and close current log in window
    Button register = addButtonGrid("Register", gridOfLogIn, 2, 1);
    UserRegisterBox registerPage = new UserRegisterBox();
    register.setOnAction(e -> {
      logInStage.close();
      registerPage.display();
    });

    //back button will return back to home window and close current log in window
    Button cancel = addButtonGrid("Cancel", gridOfLogIn, 2, 2);
    HomeBox homePage = new HomeBox();
    cancel.setOnAction(e -> {
      logInStage.close();
      homePage.display();
    });

    logInStage.setTitle("Log in page");
    Scene logScene = new Scene(gridOfLogIn, 1024, 720);
    logInStage.setScene(logScene);
    logInStage.show();
  }
}