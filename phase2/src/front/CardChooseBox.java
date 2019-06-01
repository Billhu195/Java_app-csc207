package front;

import Card.Card;
import Transit.Stop;
import User.CardHolder;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
/* Use for choosing card when doing tap feature*/
class CardChooseBox extends FrontManager {
  /**
   * Window which helps for choose a card to tap by current user and current stop
   * @param currUser CardHolder, the current user who need to active a new card
   * @param currStop Stop, the current stop which need a card to tap with
   */
  public void display(CardHolder currUser, Stop currStop) {
    Stage chooseStage = setSize();
    chooseStage.initModality(Modality.APPLICATION_MODAL);
    GridPane gridOfChoose = new GridPane();
    gridOfChoose.setPadding(new Insets(50));
    gridOfChoose.setVgap(8);
    gridOfChoose.setHgap(10);

    addLabelGrid("Choose your card:", gridOfChoose, 0, 0);
    ChoiceBox wallet = addWalletChoiceBox(currUser);
    GridPane.setConstraints(wallet, 1, 0);
    gridOfChoose.getChildren().add(wallet);

    //Confirm button will do the choose card process base on the input of choice box wallet
    //and goes to vehicle type window to find whether the user tap in the subway or bus
    Button confirm = addButtonGrid("Confirm", gridOfChoose, 0, 8);
    confirm.setOnAction(e -> {
      int id = (int) wallet.getValue();
      Card currCard = currUser.getWallet().get(id);
      chooseStage.close();
      VehicleTypeBox typePage = new VehicleTypeBox();
      typePage.display(currCard, currStop);
    });

    //back button will return back to station window and close current card choose window
    Button back = addButtonGrid("back", gridOfChoose, 1, 8);
    back.setOnAction(
        e -> {
          e.consume();
          chooseStage.close();
          StationBox stationPage = new StationBox();
          stationPage.display(currUser, currStop);
        });

    chooseStage.setTitle("Choose Card");
    Scene chooseScene = new Scene(gridOfChoose, 1024, 720);
    chooseStage.setScene(chooseScene);
    chooseStage.show();
  }
}