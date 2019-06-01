package front;

import User.AdminUser;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Admin Statistic Page
 * Let Admin to Choose what type of Statistic to View
 */
class AdminStatPage extends FrontManager {
    /**
     * Statistic Page to view
     * View Stats on Time Range, Number of Card Holder, Number of Chargeable Card, Number of Non-Chargeable Card
     * and button to DeleteOldStat up to 5 years
     * @param currAdmin AdminUser passed from login
     */
    public static void display(AdminUser currAdmin) {
        Stage adminWindowStage = new Stage();
        adminWindowStage.setTitle("AdminStatPage");

        // button Register
        Button ViewStatTableBut = new Button("View Stats on Time Range");
        Button NumCardHoldersBut = new Button("Number of Card Holder");
        Button NumbChargeableCardBut = new Button("Number of Chargeable Card");
        Button NumNonChargeableCardBut = new Button("Number of Non-Chargeable Card");
        Button deleteOldDataBut = new Button("Delete Old Data");

        ///Button Stat From AdminUser java class
        NumCardHoldersBut.setOnAction(e -> {
            String cardHolders = "Number of Total Card Holder: " + AdminUser.NumCardHolders();
            MessageBox.display("AdminUser_Stat", cardHolders, Color.rgb(0,0,0));
        });

        NumbChargeableCardBut.setOnAction(e -> {
            String chargeableCards = "Number of Chargeable Card: " + Integer.toString(AdminUser.NumChargeableCards());
            MessageBox.display("AdminUser_Stat", chargeableCards, Color.rgb(0,0,0));
        });

        NumNonChargeableCardBut.setOnAction(e -> {
            String numNonChargeable = "Number of Non-Chargeable Card: " + Integer.toString(AdminUser.NumNonChargeableCards());
            MessageBox.display("AdminUser_Stat",  numNonChargeable, Color.rgb(0,0,0));
        });

        deleteOldDataBut.setOnAction(e ->
                MessageBox.display("AdminUser_Stat", currAdmin.deleteOldData(), Color.rgb(0,0,0))
        );

        // Switch to ViewStatTable Admin
        ViewStatTableBut.setOnAction(
            e -> {
                AdminTableView tableView = new AdminTableView();

                try {
                    tableView.display(currAdmin);
                    adminWindowStage.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });

        ///Add button vbox and Scene.
        VBox vBox = new VBox();
        vBox.getChildren().addAll(
                ViewStatTableBut,
                NumCardHoldersBut,
                NumbChargeableCardBut,
                NumNonChargeableCardBut,
                deleteOldDataBut);
        Scene AdminScene = new Scene(vBox);

        adminWindowStage.setScene(AdminScene);
        adminWindowStage.showAndWait();
    }}
