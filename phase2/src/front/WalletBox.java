package front;

import User.CardHolder;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Card.RegularCard;
import Card.MonthlyCard;
import Card.Card;
/*the wallet page of current card holder*/
class WalletBox extends FrontManager {
  /**
   * Window which shows information of wallet of current user
   * @param currUser CardHolder, the current user who need to show the wallet
   */
  public void display(CardHolder currUser) {
    Stage walletStage = setSize();
    walletStage.initModality(Modality.APPLICATION_MODAL);
    BorderPane borderOfWallet = new BorderPane();
    borderOfWallet.setPadding(new Insets(50));

    //Mid vbox is contain everything at middle of the border of wallet
    VBox mid = new VBox();
    addLabelVBox("choose a card: ", mid);
    ChoiceBox wallet = addWalletChoiceBox(currUser);
    mid.getChildren().add(wallet);

    //Choose card button will do the choose card process base on the input of choice box wallet
    //and goes to regular card window or monthly card window depends on the type of card
    Button chooseCard = addButtonVBox("check this card", mid);
    chooseCard.setOnAction(e -> {
      int id = (int) wallet.getValue();
      Card currCard = currUser.getWallet().get(id);
      if (currCard instanceof RegularCard){
        RegularCardBox regularCard = new RegularCardBox();
        walletStage.close();
        regularCard.display((RegularCard) currCard);
      }
      else{
        MonthlyCardBox monthlyCard = new MonthlyCardBox();
        walletStage.close();
        monthlyCard.display((MonthlyCard) currCard);
      }
    });

    //back button will return back to user window and close the current wallet window
    Button back = addButtonVBox("back", mid);
    UserBox userPage = new UserBox();
    back.setOnAction(e -> {
      walletStage.close();
      userPage.display(currUser);
    });

    borderOfWallet.setCenter(mid);
    walletStage.setTitle("Wallet page");
    Scene walletScene = new Scene(borderOfWallet, 1024, 720);
    walletStage.setScene(walletScene);
    walletStage.show();
  }
}