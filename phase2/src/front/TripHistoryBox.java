package front;

import Card.RegularCard;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
/*the trip history page*/
class TripHistoryBox extends FrontManager {
  /**
   * Window which shows information the trip history of current card
   * @param currCard RegularCard, current card which need to show the trip history
   */
  public void display(RegularCard currCard){
    Stage historyStage = setSize();
    historyStage.initModality(Modality.APPLICATION_MODAL);
    GridPane gridOfHistory = new GridPane();
    gridOfHistory.setPadding(new Insets(50));
    gridOfHistory.setVgap(8);
    gridOfHistory.setHgap(10);

    LocalDateTime today = LocalDateTime.now();
    int currYear = today.getYear();
    int currMonth = today.getMonthValue();
    int currDay = today.getDayOfMonth();
    int maxDay = today.plusMonths(1).withDayOfMonth(1).minusDays(1).getDayOfMonth();
    addLabelGrid("Year: ", gridOfHistory, 0, 1);
    ChoiceBox year = addNumChoiceBox(currYear, currYear, currYear - 20, gridOfHistory, 1, 1);
    addLabelGrid("Month: ", gridOfHistory, 0, 2);
    ChoiceBox month = addNumChoiceBox(currMonth, 12, 1, gridOfHistory, 1, 2);
    addLabelGrid("Day: ", gridOfHistory, 0, 3);
    ChoiceBox day = addNumChoiceBox(currDay, maxDay, 1, gridOfHistory, 1, 3);

    //Confirm button will find the trip on chosen date base on the input of choice box year, month and day
    Button confirm = addButtonGrid("confirm date", gridOfHistory, 1, 4);
    confirm.setOnAction(e-> {
      LocalDate chosenDate = LocalDate.of((int)year.getValue(),(int)month.getValue(),(int)day.getValue());
      addLabelGrid("The trip(s) at " + chosenDate + " is(are): \n" + currCard.getStatistics().viewTripForDate(chosenDate), gridOfHistory, 1, 5);
    });

    //back button will return back to regular card window and close the current trip history window
    Button back = addButtonGrid("back", gridOfHistory, 0, 4);
    back.setOnAction(e -> {
      e.consume();
      historyStage.close();
      RegularCardBox regular = new RegularCardBox();
      regular.display(currCard);
    });

    historyStage.setTitle("Trip History");
    Scene historyScene = new Scene(gridOfHistory, 1024, 720);
    historyStage.setScene(historyScene);
    historyStage.show();
  }
}