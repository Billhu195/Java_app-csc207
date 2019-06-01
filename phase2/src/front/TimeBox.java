package front;

import Card.Card;
import Transit.Stop;
import java.time.LocalDateTime;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
/*choose tap time*/
class TimeBox extends FrontManager {

  /**
   * Window which helps for choose the vehicle number of vehicle to tap by current card on current stop with vehicle type
   * @param currCard Card, the card which user decide to use for tap
   * @param vehicleType String, the current vehicle type
   * @param vehicleNum String, the current vehicle number
   * @param currStop Stop, the current stop which user decide to tap at
   */
  public void display(Card currCard, String vehicleType, String vehicleNum, Stop currStop) {
    Stage timeStage = setSize();
    timeStage.initModality(Modality.APPLICATION_MODAL);
    GridPane gridOfTime = new GridPane();
    gridOfTime.setPadding(new Insets(50));
    gridOfTime.setVgap(8);
    gridOfTime.setHgap(10);

    LocalDateTime today = LocalDateTime.now();
    int currYear = today.getYear();
    int currMonth = today.getMonthValue();
    int currDay = today.getDayOfMonth();
    int maxDay = today.plusMonths(1).withDayOfMonth(1).minusDays(1).getDayOfMonth();
    int currHour = today.getHour();
    int currMin = today.getMinute();
    addLabelGrid("Year: ", gridOfTime, 0, 1);
    ChoiceBox year = addNumChoiceBox(currYear, currYear, currYear - 20, gridOfTime, 1, 1);
    addLabelGrid("Month: ", gridOfTime, 0, 2);
    ChoiceBox month = addNumChoiceBox(currMonth, 12, 1, gridOfTime, 1, 2);
    addLabelGrid("Day: ", gridOfTime, 0, 3);
    ChoiceBox day = addNumChoiceBox(currDay, maxDay, 1, gridOfTime, 1, 3);
    addLabelGrid("Hour: ", gridOfTime, 0, 4);
    ChoiceBox hour = addNumChoiceBox(currHour, 23, 0, gridOfTime, 1, 4);
    addLabelGrid("Minutes: ", gridOfTime, 0, 5);
    ChoiceBox min = addNumChoiceBox(currMin, 59, 0, gridOfTime, 1, 5);

    //Confirm button will find the time which user choosed base on the input of choice box year, month, day, hour and minutes
    //and goes to tap window to decide whether tap in or tap out
    Button confirm = addButtonGrid("Confirm", gridOfTime, 0, 8);
    confirm.setOnAction(e -> {
      TapBox tapPage = new TapBox();
      String time = "" + year.getValue() + "/" + month.getValue() + "/" + day.getValue() + "/" + hour.getValue() + "/" + min.getValue();
      timeStage.close();
      tapPage.display(currCard, vehicleType, vehicleNum, time, currStop);
    });

    //back button will return back to vehicle number window and close the current time window
    Button back = addButtonGrid("back", gridOfTime, 1, 8);
    back.setOnAction(
        e -> {
          e.consume();
          timeStage.close();
          VehicleNumBox numBox = new VehicleNumBox();
          numBox.display(currCard, vehicleType, currStop);
        });

    timeStage.setTitle("Time");
    Scene timeScene = new Scene(gridOfTime, 1024, 720);
    timeStage.setScene(timeScene);
    timeStage.show();
  }
}