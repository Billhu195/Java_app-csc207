package front;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Card.RegularCard;
/* The regular card information page*/
class RegularCardBox extends FrontManager {
  /**
   * Window which shows information of current regular card
   * @param currCard Card, the current regular card
   */
  public void display(RegularCard currCard){
    Stage regularStage = setSize();
    regularStage.initModality(Modality.APPLICATION_MODAL);
    BorderPane regularBorder = new BorderPane();
    regularBorder.setPadding(new Insets(50));

    //Top hbox which shows the id of current regular card
    HBox top = new HBox();
    addLabelHBox("Regular card id: " + currCard.getID(), top);

    //Mid vbox which shows the balance, card holder, recent trip and button view trip history of current regular card and button back
    VBox mid = new VBox();
    addLabelVBox("current balance: " + currCard.balance, mid);
    addLabelVBox("current card holder: " + currCard.getCardHolder().getName(), mid);

    addLabelVBox("Here is three recent trips", mid);
    addLabelVBox(currCard.getStatistics().viewThreeRecentTrips(), mid);
    Button viewTripHistory = addButtonVBox("view trip history", mid);
    TripHistoryBox tripHistory = new TripHistoryBox();
    viewTripHistory.setOnAction(e -> {
      regularStage.close();
      tripHistory.display(currCard);
    });

    //back button will return back to wallet window and close the current regular card window
    Button back = addButtonVBox("back", mid);
    back.setOnAction(e -> {
      WalletBox wallet = new WalletBox();
      e.consume();
      regularStage.close();
      wallet.display(currCard.getCardHolder());
    });

    //Left vbox contain add balance and suspend card button which will open the add balance window and shows message separately
    VBox left = new VBox();
    Button addBalance = addButtonVBox("add balance", left);
    AddBalanceBox addPage = new AddBalanceBox();
    addBalance.setOnAction(e -> {
      regularStage.close();
      addPage.display(currCard);
    });
    Button suspendCard = addButtonVBox("suspend card", left);
    Label message = addLabelVBox("",left);
    suspendCard.setOnAction(e -> {
        message.setText(currCard.getCardHolder().suspendCard(currCard));
        message.setTextFill(Color.rgb(250, 0, 0));
    });

    regularBorder.setTop(top);
    regularBorder.setCenter(mid);
    regularBorder.setLeft(left);

    regularStage.setTitle("Regular Card page");
    Scene regularScene = new Scene(regularBorder, 1024, 720);
    regularStage.setScene(regularScene);
    regularStage.show();
  }
}