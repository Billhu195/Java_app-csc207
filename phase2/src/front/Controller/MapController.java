package front.Controller;

import Manager.StopManager;
import Transit.Stop;
import User.CardHolder;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import front.*;

/** The controller for the Map.fxml */
public class MapController {

  /**
   * The method for a click on Stop button, change scene to StationBox
   *
   * @param mouseEvent The mouse clicked
   */
  @FXML
  public void StopButtonOnClick(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    String text = button.getText();
    CardHolder cardHolder = FrontManager.getCardHolder();
    Stop stop = StopManager.getStopManager().getAllStops().get(text);
    StationBox stationBox = new StationBox();
    stationBox.display(cardHolder, stop);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.close();
  }

  /**
   * The method for a click on Stop button, change scene to routes' fxml files
   *
   * @param mouseEvent The mouse clicked
   */
  @FXML
  public void LineButtonOnClick(MouseEvent mouseEvent) throws IOException {
    Button button = (Button) mouseEvent.getSource();
    String text = button.getText() + ".fxml";
    Parent mapparent = FXMLLoader.load(getClass().getResource(text));
    Scene mapscene = new Scene(mapparent);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(mapscene);
    window.show();
  }

  /**
   * The method for a click on Stop button, change scene to UserBox
   *
   * @param mouseEvent The mouse clicked
   */
  @FXML
  public void BackButtonOnClick(MouseEvent mouseEvent) throws IOException {
    CardHolder cardHolder = FrontManager.getCardHolder();
    UserBox userBox = new UserBox();
    userBox.display(cardHolder);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.close();
  }
}