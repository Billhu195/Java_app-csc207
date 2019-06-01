package front;

import User.AdminUser;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Admin Table View Page
 * Given a Time Range Display types of Revenue, Frequency of Stops to Admin
 */
class AdminTableView extends FrontManager {
    /**
     * A box for given a time range, view all statistics
     * View total revenue, revenue of date, frequency of travel stations
     *
     * @param Admin AdminUser passed from login
     */
    public void display(AdminUser Admin) {
        Stage ViewStatTableBut = setSize();
        ViewStatTableBut.initModality(Modality.APPLICATION_MODAL);
        ///Grid Size
        GridPane Statsgrid = new GridPane();
        Statsgrid.setPadding(new Insets(100));

        Statsgrid.setVgap(10);
        Statsgrid.setHgap(13);

        ///Choice box to choose init date, end date
        LocalDateTime today = LocalDateTime.now();
        int currYear = today.getYear();
        int currMonth = today.getMonthValue();
        int currDay = today.getDayOfMonth();
        int maxDay = today.plusMonths(1).withDayOfMonth(1).minusDays(1).getDayOfMonth();

        ///init date choice box
        addLabelGrid("Init year: ", Statsgrid, 0, 1);
        ChoiceBox initYear = addNumChoiceBox(currYear, currYear, currYear - 20, Statsgrid, 1, 1);
        addLabelGrid("Init month: ", Statsgrid, 0, 2);
        ChoiceBox initMonth = addNumChoiceBox(currMonth, 12, 1, Statsgrid, 1, 2);
        addLabelGrid("Init day: ", Statsgrid, 0, 3);
        ChoiceBox initDay = addNumChoiceBox(currDay, maxDay, 1, Statsgrid, 1, 3);

        ///end date choice box
        addLabelGrid("End year: ", Statsgrid, 0, 4);
        ChoiceBox endYear = addNumChoiceBox(currYear, currYear, currYear - 20, Statsgrid, 1, 4);
        addLabelGrid("End month: ", Statsgrid, 0, 5);
        ChoiceBox endMonth = addNumChoiceBox(currMonth, 12, 1, Statsgrid, 1, 5);
        addLabelGrid("End day: ", Statsgrid, 0, 6);
        ChoiceBox endDay = addNumChoiceBox(currDay, maxDay, 1, Statsgrid, 1, 6);

        ///Button for All action for view stats
        ///View Total Revenue
        Button totalMoney = addButtonGrid("View Total Revenue", Statsgrid, 2, 1);
        Label viewProfitLabel = addEmptyLabel(Statsgrid, 2,1);
        totalMoney.setOnAction(e -> {
            LocalDate initDate =
                    LocalDate.of(
                            (int) initYear.getValue(), (int) initMonth.getValue(), (int) initDay.getValue());
            LocalDate endDate =
                    LocalDate.of(
                            (int) endYear.getValue(), (int) endMonth.getValue(), (int) endDay.getValue());
            viewProfitLabel.setText(Admin.getViewStatistics().totalMoneyForRange(initDate, endDate));

        });

        /////View Total Station Usage
        Button totalStation = addButtonGrid("View Total Station Usage", Statsgrid, 10, 1);
        Label viewStationLabel = addEmptyLabel(Statsgrid, 10,1);
        totalStation.setOnAction(e->{
            LocalDate initDate =
                    LocalDate.of(
                            (int)initYear.getValue(), (int)initMonth.getValue(), (int)initDay.getValue());
            LocalDate endDate =
                    LocalDate.of(
                            (int)endYear.getValue(), (int)endMonth.getValue(), (int)endDay.getValue());
            viewStationLabel.setText(Admin.getViewStatistics().totalStationForRange(initDate, endDate));
        });

        /////View Revenue List
        Button money = addButtonGrid("View Revenue List", Statsgrid, 2, 4);
        Label moneyLabel = addEmptyLabel(Statsgrid, 2,5);
        money.setOnAction(e -> {
            LocalDate initDate =
                    LocalDate.of(
                            (int) initYear.getValue(), (int) initMonth.getValue(), (int) initDay.getValue());
            LocalDate endDate =
                    LocalDate.of(
                            (int) endYear.getValue(), (int) endMonth.getValue(), (int) endDay.getValue());
            moneyLabel.setText(Admin.getViewStatistics().MoneyForRange(initDate, endDate));
        });

        /////View Station List
        Button station = addButtonGrid("View Station List", Statsgrid, 10, 4);
        Label stationLabel = addEmptyLabel(Statsgrid, 10,5);
        station.setOnAction(e->{
            LocalDate initDate =
                    LocalDate.of(
                            (int)initYear.getValue(), (int)initMonth.getValue(), (int)initDay.getValue());
            LocalDate endDate =
                    LocalDate.of(
                            (int) endYear.getValue(), (int) endMonth.getValue(), (int) endDay.getValue());
            stationLabel.setText(Admin.getViewStatistics().StationForRange(initDate, endDate));
        });

        ///Button back
        Button back = addButtonGrid("back", Statsgrid, 0, 7);
        back.setOnAction(e -> {
            e.consume();
            ViewStatTableBut.close();
            AdminStatPage.display(Admin);
        });

        ViewStatTableBut.setTitle("Static History");
        Scene scene = new Scene(Statsgrid, 1024, 840);
        ViewStatTableBut.setScene(scene);
        ViewStatTableBut.show();
    }
}



