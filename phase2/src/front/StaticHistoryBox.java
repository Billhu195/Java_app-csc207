package front;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import User.CardHolder;
/*The static history page*/
class StaticHistoryBox extends FrontManager {
  /**
   * Window which shows information of static history of current user
   * @param user CardHolder, the current user
   */
  public void display(CardHolder user) {
    Stage moneyHistory = setSize();
    moneyHistory.initModality(Modality.APPLICATION_MODAL);
    GridPane gridOfHistory = new GridPane();
    gridOfHistory.setPadding(new Insets(100));

    gridOfHistory.setVgap(10);
    gridOfHistory.setHgap(13);

    ///Choice box Labels
    LocalDateTime today = LocalDateTime.now();
    int currYear = today.getYear();
    int currMonth = today.getMonthValue();
    int currDay = today.getDayOfMonth();
    int maxDay = today.plusMonths(1).withDayOfMonth(1).minusDays(1).getDayOfMonth();
    ///Start box
    addLabelGrid("Init year: ", gridOfHistory, 0, 1);
    ChoiceBox initYear = addNumChoiceBox(currYear, currYear, currYear - 20, gridOfHistory, 1, 1);
    addLabelGrid("Init month: ", gridOfHistory, 0, 2);
    ChoiceBox initMonth = addNumChoiceBox(currMonth, 12, 1, gridOfHistory, 1, 2);
    addLabelGrid("Init day: ", gridOfHistory, 0, 3);
    ChoiceBox initDay = addNumChoiceBox(currDay, maxDay, 1, gridOfHistory, 1, 3);
    ////End box
    addLabelGrid("End year: ", gridOfHistory, 0, 4);
    ChoiceBox endYear = addNumChoiceBox(currYear, currYear, currYear - 20, gridOfHistory, 1, 4);
    addLabelGrid("End month: ", gridOfHistory, 0, 5);
    ChoiceBox endMonth = addNumChoiceBox(currMonth, 12, 1, gridOfHistory, 1, 5);
    addLabelGrid("End day: ", gridOfHistory, 0, 6);
    ChoiceBox endDay = addNumChoiceBox(currDay, maxDay, 1, gridOfHistory, 1, 6);

    //Total money button will view the total money of current user base on the input of choice boxes initYear, initMonth, initDay, endYear, endMonth, endDay
    Button totalMoney = addButtonGrid("View Total Cost", gridOfHistory, 2, 1);
    Label viewCostLabel = addEmptyLabel(gridOfHistory, 2, 1);
    totalMoney.setOnAction(e->{
      LocalDate initDate = LocalDate.of((int)initYear.getValue(),(int)initMonth.getValue(),(int)initDay.getValue());
      LocalDate endDate = LocalDate.of((int)endYear.getValue(),(int)endMonth.getValue(),(int)endDay.getValue());
      //addLabelGrid(user.getViewStatistics().totalMoneyForRange(initDate, endDate), gridOfHistory, 2, 2);
      viewCostLabel.setText(user.getViewStatistics().totalMoneyForRange(initDate, endDate));
    });

    //Total station button will view the total station of current user base on the input of choice boxes initYear, initMonth, initDay, endYear, endMonth, endDay
    Button totalStation = addButtonGrid("View Total Station Travelled", gridOfHistory, 10, 1);
    Label viewStationLabel = addEmptyLabel(gridOfHistory, 10, 1);
    totalStation.setOnAction(e->{
      LocalDate initDate = LocalDate.of((int)initYear.getValue(),(int)initMonth.getValue(),(int)initDay.getValue());
      LocalDate endDate = LocalDate.of((int)endYear.getValue(),(int)endMonth.getValue(),(int)endDay.getValue());
      //addLabelGrid(user.getViewStatistics().totalStationForRange(initDate, endDate), gridOfHistory,6, 2);
      viewStationLabel.setText(user.getViewStatistics().totalStationForRange(initDate, endDate));
    });

    //Money button will view the money of current user separately base on the input of choice boxes initYear, initMonth, initDay, endYear, endMonth, endDay
    Button money = addButtonGrid("View Cost List", gridOfHistory, 2, 4);
    Label viewCostList = addEmptyLabel(gridOfHistory, 2, 5);
    money.setOnAction(e->{
      LocalDate initDate = LocalDate.of((int)initYear.getValue(),(int)initMonth.getValue(),(int)initDay.getValue());
      LocalDate endDate = LocalDate.of((int)endYear.getValue(),(int)endMonth.getValue(),(int)endDay.getValue());
      //addLabelGrid(user.getViewStatistics().MoneyForRange(initDate, endDate), gridOfHistory, 2, 5);
      viewCostList.setText(user.getViewStatistics().MoneyForRange(initDate, endDate));
    });

    //Station button will view the station of current user separately base on the input of choice boxes initYear, initMonth, initDay, endYear, endMonth, endDay
    Button station = addButtonGrid("View Station Travelled", gridOfHistory, 10, 4);
    Label viewStation = addEmptyLabel(gridOfHistory, 10, 5);
    station.setOnAction(e->{
      LocalDate initDate = LocalDate.of((int)initYear.getValue(),(int)initMonth.getValue(),(int)initDay.getValue());
      LocalDate endDate = LocalDate.of((int)endYear.getValue(),(int)endMonth.getValue(),(int)endDay.getValue());
      //addLabelGrid(user.getViewStatistics().StationForRange(initDate, endDate), gridOfHistory, 6, 5);
      viewStation.setText(user.getViewStatistics().StationForRange(initDate, endDate));
    });

    //back button will return back to user window and close current static history window
    Button back = addButtonGrid("back", gridOfHistory, 0, 7);
    back.setOnAction(e -> {
     UserBox userBox = new UserBox();
     e.consume();
     moneyHistory.close();
     userBox.display(user);
    });

    moneyHistory.setTitle("Statistic History");
    Scene scene = new Scene(gridOfHistory, 1024, 840);
    moneyHistory.setScene(scene);
    moneyHistory.show();
  }
}