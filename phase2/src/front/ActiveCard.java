package front;

import User.CardHolder;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/** Active card page which use for active card feature*/
class ActiveCard extends FrontManager {
  /**
   * Window which helps for active a new card by current user
   * @param currUser CardHolder, the current user who need to active a new card
   */
  public void display(CardHolder currUser){
    Stage activeStage = setSize();
    activeStage.initModality(Modality.APPLICATION_MODAL);
    GridPane gridOfActive = new GridPane();
    gridOfActive.setPadding(new Insets(50));
    gridOfActive.setVgap(8);
    gridOfActive.setHgap(10);

    addLabelGrid("type in your card id: ", gridOfActive, 0, 0);
    TextField id = addTextFieldGrid(gridOfActive, 1, 0);
    addLabelGrid("type in your active password: ", gridOfActive, 0, 1);
    TextField activePassword = addTextFieldGrid(gridOfActive, 1, 1);

    //Confirm button will do the active card process base on the input of test field id and active password
    //and gives user message to show how it works
    Button confirm = addButtonGrid("confirm", gridOfActive, 2, 2);
    confirm.setOnAction(e -> {
      String Message = currUser.ActivateCard(id.getText(), activePassword.getText());
      String[] messageList = Message.split(":");
      if (messageList[0].equals("Invalid input")) {
        MessageBox.display("", Message, Color.rgb(250, 0, 0));
      } else {
        MessageBox.display("", Message, Color.rgb(0, 150, 0));
      }
    });

    //back button will return back to user window and close current active card window
    Button back = addButtonGrid("back", gridOfActive, 2, 1);
    back.setOnAction(e -> {
      e.consume();
      activeStage.close();
      UserBox newUser = new UserBox();
      newUser.display(currUser);
    });

    activeStage.setTitle("Active Card");
    Scene activeScene = new Scene(gridOfActive, 1024, 720);
    activeStage.setScene(activeScene);
    activeStage.show();
  }
}