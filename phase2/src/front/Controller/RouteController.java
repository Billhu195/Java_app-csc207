package front.Controller;

import Manager.StopManager;
import Transit.Stop;
import User.CardHolder;
import front.StationBox;
import front.FrontManager;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/** The controller class for all routes' fxml files */
public class RouteController {

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
   * The method for a click on Back button, change scene to Map.fxml
   *
   * @param mouseEvent The mouse clicked
   */
  @FXML
  public void BackButtonOnClick(MouseEvent mouseEvent) throws IOException {
    Parent mapparent = FXMLLoader.load(getClass().getResource("Map.fxml"));
    Scene mapscene = new Scene(mapparent);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(mapscene);
    window.show();
  }
}