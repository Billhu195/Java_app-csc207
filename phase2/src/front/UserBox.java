package front;
import User.CardHolder;
import java.io.IOException;
import java.time.LocalDate;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
/*The current user's account page*/
public class UserBox extends FrontManager {
  /**
   * Window which shows information of current user
   * @param currUser CardHolder, current user who need to shows the information about himself or herself
   */
  public void display(CardHolder currUser) {
    Stage userStage = setSize();
    userStage.initModality(Modality.APPLICATION_MODAL);
    BorderPane borderOfUser = new BorderPane();
    borderOfUser.setPadding(new Insets(50));

    //Top hbox will contain the user name and email at the top of the border
    HBox top = new HBox();
    addLabelHBox("User name: ", top);
    addLabelHBox(currUser.getName(), top);
    addLabelHBox(" ", top);
    addLabelHBox("Email: ", top);
    addLabelHBox(currUser.getEmail(), top);

    //Left vbox will contain the setting button, map button, daily money label, view static history button and back button
    VBox left = new VBox();
    Button setting = addButtonVBox("Setting", left);
    SettingBox settingPage = new SettingBox();
    setting.setOnAction(e -> {
      userStage.close();
      settingPage.display(currUser);
    });
    Button map = addButtonVBox("map", left);
    MapBox mapPage = new MapBox();
    map.setOnAction(e -> {
      userStage.close();
      Stage stage = (Stage) map.getScene().getWindow();
      try {
        Parent root = FXMLLoader.load(getClass().getResource("Controller/Map.fxml"));
        Scene mapscene = new Scene(root);
        Stage window = (Stage) stage.getScene().getWindow();
        window.setScene(mapscene);
        window.show();
        FrontManager.setCardHolder(currUser);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    addLabelVBox("Today you have spent: " + String.valueOf(currUser.getViewStatistics().totalMoneyForDate(LocalDate.now())), left);
    Button viewStaticHistory = addButtonVBox("view statistics", left);
    StaticHistoryBox staticHistory = new StaticHistoryBox();
    viewStaticHistory.setOnAction(e -> {
      userStage.close();
      staticHistory.display(currUser);
    });

    //back button will return back to home window and close the current user window
    Button back = addButtonVBox("back", left);
    HomeBox homePage = new HomeBox();
    back.setOnAction(e -> {
      userStage.close();
      homePage.display();
    });

    //Mid vbox will contain wallet button and and active card button
    VBox mid = new VBox();
    Button wallet = addButtonVBox("wallet", mid);
    WalletBox newWallet = new WalletBox();
    wallet.setOnAction(e-> {
      userStage.close();
      newWallet.display(currUser);
    });
    Button activeCard = addButtonVBox("active a new card", mid);
    ActiveCard activePage = new ActiveCard();
    activeCard.setOnAction(e -> {
      userStage.close();
      activePage.display(currUser);
    });

    borderOfUser.setTop(top);
    borderOfUser.setCenter(mid);
    borderOfUser.setLeft(left);
    userStage.setTitle("User page");
    Scene userScene = new Scene(borderOfUser, 1024, 720);
    userStage.setScene(userScene);
    userStage.show();
  }
}