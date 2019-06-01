package front;

import Card.MonthlyCard;
import java.time.LocalDate;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
/* The monthly card information page*/
class MonthlyCardBox extends FrontManager {
  /**
   * Window which shows information of current monthly card
   * @param currCard Card, the current monthly card
   */
  public void display(MonthlyCard currCard){
    Stage monthlyStage = setSize();
    monthlyStage.initModality(Modality.APPLICATION_MODAL);
    BorderPane monthlyBorther = new BorderPane();
    monthlyBorther.setPadding(new Insets(50));

    //Top hbox which shows the id of current monthly card
    HBox top = new HBox();
    addLabelHBox("Monthly card id: " + currCard.getID(), top);

    //Mid grid pane contain check expire date button, choice box month and button back
    GridPane mid = new GridPane();
    Button checkExpire = addButtonGrid("check card status", mid, 0, 1);
    checkExpire.setOnAction( e -> message(currCard));
    addLabelGrid("Choose the number of month you wish to renew: ", mid, 0, 0);
    ChoiceBox<Integer> months = new ChoiceBox<>();
    months.getItems().addAll(1, 2, 3);
    mid.getChildren().add(months);
    GridPane.setConstraints(months, 1, 0);
    Button renew = addButtonGrid("Renew card", mid, 3, 0);
    renew.setOnAction( e -> MessageBox.display("", currCard.renew(months.getValue()), Color.rgb(0, 150, 0)));

    //back button will return back to wallet window and close the current monthly card window
    Button back = addButtonGrid("back", mid, 0, 2);
    back.setOnAction(e -> {
      e.consume();
      monthlyStage.close();
      WalletBox wallet = new WalletBox();
      wallet.display(currCard.getCardHolder());
    });

    monthlyBorther.setTop(top);
    monthlyBorther.setCenter(mid);
    monthlyStage.setTitle("Monthly Card page");
    Scene monthlyScene = new Scene(monthlyBorther, 1024, 720);
    monthlyStage.setScene(monthlyScene);
    monthlyStage.show();
  }

  /**
   * return the message of whether current monthly card is expire or not
   * @param currCard MonthlyCard current monthly card
   */
  private void message(MonthlyCard currCard){
    if (currCard.checkExpire(LocalDate.now())){
      MessageBox.display("Message", "Your monthly card is not expired, "
          + "but it will expire on date" + currCard.getExpireDate().toString(), Color.rgb(0, 150, 0));
    }
    else{
      MessageBox.display("Message", "Your monthly card expired", Color.rgb(250, 0, 0));
    }
  }
}