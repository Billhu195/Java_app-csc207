package front;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.*;

/** the home page*/
public class HomeBox extends FrontManager {

  /**
   * shows home page
   */
  public void display() {
    Stage homeStage = setSize();
    homeStage.initModality(Modality.APPLICATION_MODAL);

    GridPane gridOfHome = new GridPane();
    gridOfHome.setPadding(new Insets(50));
    gridOfHome.setVgap(8);
    gridOfHome.setHgap(10);

    Button admin = addButtonGrid("Admin", gridOfHome, 0, 0);
    AdminFront adminPage = new AdminFront();
    admin.setOnAction(e -> {
      homeStage.close();
      adminPage.display();
    });
    Button user = addButtonGrid("User", gridOfHome, 1, 0);
    LogInBox logInPage = new LogInBox();
    user.setOnAction(e -> {
      homeStage.close();
      logInPage.display();
    });

    homeStage.setTitle("Home page");
    Scene homeScene = new Scene(gridOfHome, 1024, 720);
    homeStage.setScene(homeScene);
    homeStage.show();
  }
}