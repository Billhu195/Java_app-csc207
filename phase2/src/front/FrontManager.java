package front;

import Card.Card;
import Manager.UserManager;
import Transit.Stop;
import User.CardHolder;
import User.User;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/** Use for manage the front elements */
public abstract class FrontManager {
  /** the card holder uses in front manager*/
  private static CardHolder cardHolder;

  /**
   * put label with message in grid pane at index x and y
   *
   * @param message String, the message shows in label
   * @param grid GridPane, the current grid Pane
   * @param indexX int, the x index of label
   * @param indexY int, the y index of label
   */
  void addLabelGrid(String message, GridPane grid, int indexX, int indexY) {
    Label label = new Label(message);
    GridPane.setConstraints(label, indexX, indexY);
    grid.getChildren().add(label);
  }

  /**
   * put label with message in hbox
   *
   * @param message String, the message shows in label
   * @param hBox Hbox, the current hbox
   */
  void addLabelHBox(String message, HBox hBox) {
    Label label = new Label(message);
    hBox.getChildren().add(label);
  }

  /**
   * put label with message in vbox, and return it
   *
   * @param message String, the message shows in label
   * @param vBox Vbox, the current vbox
   * @return the label with message
   */
  Label addLabelVBox(String message, VBox vBox) {
    Label label = new Label(message);
    vBox.getChildren().add(label);
    if (!message.equals("")) {
      return null;
    }
    return label;
  }

  /**
   * put label with nothing in current grid pane at index x and y, usually for shows message of
   * methods, and return this label
   *
   * @param grid GridPane, the current grid Pane
   * @param indexX int, the x index of label
   * @param indexY int, the y index of label
   * @return the label with empty message
   */
  Label addEmptyLabel(GridPane grid, int indexX, int indexY) {
    Label label = new Label("");
    grid.add(label, indexX, indexY, indexX + 10, indexY + 2);
    return label;
  }

  /**
   * put text field in grid pane at index x and y, and return this test field
   *
   * @param grid GridPane, the current grid Pane
   * @param indexX int, the x index of label
   * @param indexY int, the y index of label
   * @return test field at current index
   */
  TextField addTextFieldGrid(GridPane grid, int indexX, int indexY) {
    TextField input = new TextField();
    GridPane.setConstraints(input, indexX, indexY);
    grid.getChildren().add(input);
    return input;
  }

  /**
   * put text field in grid pane at index x and y and set action of button into: set the name of
   * current user, and close current stage
   *
   * @param user CardHolder, current user
   * @param logIn Button, the button which need to set action
   * @param stage Stage, current stage
   * @param grid GridPane, the current grid Pane
   * @param indexX int, the x index of label
   * @param indexY int, the y index of label
   */
  void addChangeTextField(
      CardHolder user, Button logIn, Stage stage, GridPane grid, int indexX, int indexY) {
    TextField input = new TextField();
    logIn.setOnAction(
        v -> {
          user.setName(input.getText());
          v.consume();
          stage.close();
        });
    input.clear();
    GridPane.setConstraints(input, indexX, indexY);
    grid.getChildren().add(input);
  }

  /**
   * put button in grid pane at index x and y, and return this button
   *
   * @param name String, the name of this button
   * @param grid GridPane, the current grid Pane
   * @param indexX int, the x index of label
   * @param indexY int, the y index of label
   * @return button at current index
   */
  Button addButtonGrid(String name, GridPane grid, int indexX, int indexY) {
    Button button = new Button();
    button.setText(name);
    GridPane.setConstraints(button, indexX, indexY);
    grid.getChildren().add(button);
    return button;
  }

  /**
   * put button in vBox and return this button
   *
   * @param name String, the name of this button
   * @param vBox VBox, the current grid Pane
   * @return button at current index
   */
  Button addButtonVBox(String name, VBox vBox) {
    Button button = new Button();
    button.setText(name);
    vBox.getChildren().add(button);
    return button;
  }

  /**
   * put password field in grid pane at index x and y and set action of button into: get email from
   * textfield, log in the corresponding user if the password fits, and close the current stage
   *
   * @param email TextField, the email input
   * @param logIn Button, the button which need to set action
   * @param stage Stage, current stage
   * @param grid GridPane, the current grid Pane
   * @param indexX int, the x index of label
   * @param indexY int, the y index of label
   */
  void addLogInPasswordField(
      TextField email, Button logIn, Stage stage, GridPane grid, int indexX, int indexY) {
    PasswordField input = new PasswordField();
    HashMap<String, User> users = UserManager.getUserManager().getAllUsers();
    logIn.setOnAction(
        v -> {
          if (users.containsKey(email.getText())) {
            String password = users.get(email.getText()).getPassword();
            // Find the corresponding password with the input email
            if (!input.getText().equals(password)) {
              // if password fits
              MessageBox.display("", "Your password is incorrect!", Color.rgb(250, 0, 0));
            } else {
              CardHolder user =
                  (CardHolder) UserManager.getUserManager().getAllUsers().get(email.getText());
              UserBox userPage = new UserBox();
              stage.close();
              userPage.display(user);
            }
            input.clear();
          } else if (!email.getText().equals("")) {
            // if input email is empty
            MessageBox.display("", "Invalid input", Color.rgb(250, 0, 0));
          }
        });
    GridPane.setConstraints(input, indexX, indexY);
    grid.getChildren().add(input);
  }

  /**
   * put password field in grid pane at index x and y and set action of button into: set the
   * password of current user, open the setting box, and close the current stage
   *
   * @param user CardHolder, current user
   * @param logIn Button, the button which need to set action
   * @param stage Stage, current stage
   * @param grid GridPane, the current grid Pane
   * @param indexX int, the x index of label
   * @param indexY int, the y index of label
   */
  void addChangePasswordField(
      CardHolder user, Button logIn, Stage stage, GridPane grid, int indexX, int indexY) {
    PasswordField input = new PasswordField();
    logIn.setOnAction(
        v -> {
          user.setPassword(input.getText());
          v.consume();
          stage.close();
          SettingBox setting = new SettingBox();
          setting.display(user);
        });
    input.clear();
    GridPane.setConstraints(input, indexX, indexY);
    grid.getChildren().add(input);
  }

  /**
   * put password field in grid pane at index x and y and set action of button into: get email from
   * textfield, get name from textfield, create a new card holder, return to log in window and close
   * the current stage
   *
   * @param name TextField, the name input
   * @param email TextField, the email input
   * @param logIn Button, the button which need to set action
   * @param stage Stage, current stage
   * @param grid GridPane, the current grid Pane
   * @param indexX int, the x index of label
   * @param indexY int, the y index of label
   */
  void addRegPasswordField(TextField name, TextField email, Button logIn, Stage stage,
      GridPane grid, int indexX, int indexY) {
    PasswordField input = new PasswordField();
    logIn.setOnAction(
        v -> {
          if (!UserManager.getUserManager().getAllUsers().containsKey(email.getText())) {
            // if this email not exist in user manager, make a new card holder
            new CardHolder(email.getText(), input.getText(), name.getText());
            stage.close();
            LogInBox logInPage = new LogInBox();
            logInPage.display();
            MessageBox.display("", "You have successfully registered an account!", Color.rgb(0, 150, 0));
          } else {
            MessageBox.display("", "This account already exist!", Color.rgb(250, 0, 0));
          }
        });
    input.clear();
    GridPane.setConstraints(input, indexX, indexY);
    grid.getChildren().add(input);
  }

  /**
   * make a new stage and set its size into wide 1024 and height 720
   *
   * @return the new stage
   */
  static Stage setSize() {
    Stage window = new Stage();
    window.setMaxWidth(1024);
    window.setMinWidth(1024);
    window.setMaxHeight(720);
    window.setMinHeight(720);
    return window;
  }

  /**
   * make a new choice box contain all id of cards in current user's wallet
   *
   * @param user CardHolder, current user
   * @return the new choice box
   */
  ChoiceBox addWalletChoiceBox(CardHolder user) {
    ChoiceBox<Integer> choice = new ChoiceBox<>();
    HashMap<Integer, Card> wallet = user.getWallet();
    if (!wallet.isEmpty()) {
      for (int key : wallet.keySet()) {
        int id = wallet.get(key).getID();
        choice.getItems().add(id);
      }
    }
    return choice;
  }

  /**
   * make a new choice box contain all route number which pass through current stop, and put it into
   * current grid pane at given index
   *
   * @param stop Stop, current stop
   * @param grid, GridPane, current grid pane
   * @param indexX int, the x index of choice box
   * @param indexY int, the y index of choice box
   * @return the new choice box
   */
  ChoiceBox addVehicleNumChoiceBox(Stop stop, GridPane grid, int indexX, int indexY) {
    ChoiceBox<String> choice = new ChoiceBox<>();
    HashMap<String, ArrayList<String>> veichleNumMap = stop.getConnections();
    String type;
    if (veichleNumMap.get("subway") != null) {
      // if exist some subway can pass through this stop
      type = "subway";
      if (!veichleNumMap.isEmpty()) {
        if (veichleNumMap.get(type).size() != 0) {
          for (int i = 0; i < veichleNumMap.get(type).size(); i++) {
            String vehicleNum = veichleNumMap.get(type).get(i);
            choice.getItems().add(vehicleNum);
          }
        }
      }
    }
    if (veichleNumMap.get("bus") != null) {
      // if exist some bus can pass through this stop
      type = "bus";
      if (!veichleNumMap.isEmpty()) {
        if (veichleNumMap.get(type).size() != 0) {
          for (int i = 0; i < veichleNumMap.get(type).size(); i++) {
            String vehicleNum = veichleNumMap.get(type).get(i);
            choice.getItems().add(vehicleNum);
          }
        }
      }
    }
    GridPane.setConstraints(choice, indexX, indexY);
    grid.getChildren().add(choice);
    return choice;
  }

  /**
   * make a new choice box contain the type of vehicle which pass through current stop, and put it
   * into current grid pane at given index
   *
   * @param stop Stop, current stop
   * @param grid, GridPane, current grid pane
   * @param indexX int, the x index of choice box
   * @param indexY int, the y index of choice box
   * @return the new choice box
   */
  ChoiceBox addVehicleTypeChoiceBox(Stop stop, GridPane grid, int indexX, int indexY) {
    ChoiceBox<String> choice = new ChoiceBox<>();
    HashMap<String, ArrayList<String>> stops = stop.getConnections();
    if (stops.get("subway") != null) {
      choice.getItems().add("subway");
    }
    if (stops.get("bus") != null) {
      choice.getItems().add("bus");
    }
    GridPane.setConstraints(choice, indexX, indexY);
    grid.getChildren().add(choice);
    return choice;
  }

  /**
   * make a new choice box contain the vehicle number of current type of vehicle which pass through
   * current stop, and put it into current grid pane at given index
   *
   * @param stop Stop, current stop
   * @param vehicleType String, current type of vehicle
   * @param grid GridPane, current grid pane
   * @param indexX int, the x index of choice box
   * @param indexY int, the y index of choice box
   * @return the new choice box
   */
  ChoiceBox addSomeVehicleNumChoiceBox(Stop stop, String vehicleType, GridPane grid, int indexX,
      int indexY) {
    ChoiceBox<String> choice = new ChoiceBox<>();
    HashMap<String, ArrayList<String>> veichleNum = stop.getConnections();
    if (!veichleNum.isEmpty()) {
      if (vehicleType.equals("subway") || vehicleType.equals("bus")) {
        if (veichleNum.get(vehicleType).size() != 0) {
          for (int i = 0; i < veichleNum.get(vehicleType).size(); i++) {
            String num = veichleNum.get(vehicleType).get(i);
            choice.getItems().add(num);
          }
        }
      }
    }
    GridPane.setConstraints(choice, indexX, indexY);
    grid.getChildren().add(choice);
    return choice;
  }

  /**
   * make a new choice box contain the integer which with two part: from range to min, and from max
   * to range, and put it into current grid pane at given index
   *
   * @param range int, the first element on choice box
   * @param max int, the maximum possible value of range
   * @param min int, the minimum possible value of range
   * @param grid GridPane, current grid pane
   * @param indexX int, the x index of choice box
   * @param indexY int, the y index of choice box
   * @return the new choice box
   */
  ChoiceBox addNumChoiceBox(
      int range, int max, int min, GridPane grid, int indexX, int indexY) {
    ChoiceBox<Integer> choice = new ChoiceBox<>();
    for (int i = range; i >= min; i--) {
      choice.getItems().add(i);
    }
    for (int i = max; i > range; i--) {
      choice.getItems().add(i);
    }
    GridPane.setConstraints(choice, indexX, indexY);
    grid.getChildren().add(choice);
    return choice;
  }

  /**
   * put button in grid pane at index x and y, and set this button's action to open a new station
   * box using current stop and current user, and close current stage
   *
   * @param stop Stop, the current stop
   * @param user CardHolder, the current user
   * @param stage Stage, the current stage
   * @param grid GridPane, the current grid Pane
   * @param indexX int, the x index of label
   * @param indexY int, the y index of label
   */
  void addStationButton(
      Stop stop, CardHolder user, Stage stage, GridPane grid, int indexX, int indexY) {
    Button station = new Button();
    if (stop != null) {
      String name = stop.getNAME();
      station.setText(name);
      GridPane.setConstraints(station, indexX, indexY);
      grid.getChildren().add(station);
      StationBox stationBox = new StationBox();
      station.setOnAction(
          e -> {
            stage.close();
            stationBox.display(user, stop);
          });
    }
  }

  /**
   * Get current card holder
   * @return current card holder
   */
  public static CardHolder getCardHolder() {
    return cardHolder;
  }

  /**
   * Set current card holder into new card holder
   * @param cardHolder new card holder
   */
  static void setCardHolder(CardHolder cardHolder) {
    FrontManager.cardHolder = cardHolder;
  }
}