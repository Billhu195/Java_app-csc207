package front;

import Card.RegularCard;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
/** Add balance page which use for add balance feature*/
class AddBalanceBox extends FrontManager {
  /**
   * Window which helps for add balance to current card
   * @param currCard RegularCard, the current card which need to add balance
   */
  public void display(RegularCard currCard){
    Stage balanceStage = setSize();
    balanceStage.initModality(Modality.APPLICATION_MODAL);
    GridPane gridOfBalance = new GridPane();
    gridOfBalance.setPadding(new Insets(50));
    gridOfBalance.setVgap(8);
    gridOfBalance.setHgap(10);

    addLabelGrid("We are assume that you already input the true visa card information.", gridOfBalance, 0, 0);
    addLabelGrid("Now please select how much do you want to add", gridOfBalance, 0, 1);
    ChoiceBox<Integer> recharge = new ChoiceBox<>();
    GridPane.setConstraints(recharge, 1, 1);
    gridOfBalance.getChildren().add(recharge);
    recharge.getItems().addAll(10, 20, 50);

    //Add button will do the add balance process base on the input of choice box recharge
    //and gives user message to show how it works
    Button add = addButtonGrid("Add", gridOfBalance, 1, 0);
    add.setOnAction(
        e -> {
          int value = recharge.getValue();
          String message = currCard.addBalance(value);
          if (value == 10) {
            MessageBox.display("", message, Color.rgb(0, 150, 0));
          }
          else if (value == 20){
            MessageBox.display("", message, Color.rgb(0, 150, 0));
          }
          else {
            MessageBox.display("", message, Color.rgb(0, 150, 0));
          }
        });

    //back button will return back to regular card window and close current add balance window
    Button back = addButtonGrid("back", gridOfBalance, 1, 2);
    back.setOnAction(e -> {
      e.consume();
      balanceStage.close();
      RegularCardBox regularPage = new RegularCardBox();
      regularPage.display(currCard);
    });

    balanceStage.setTitle("Add Balance");
    Scene balanceScene = new Scene(gridOfBalance, 1024, 720);
    balanceStage.setScene(balanceScene);
    balanceStage.show();
  }
}