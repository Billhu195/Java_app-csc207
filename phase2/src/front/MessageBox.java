package front;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * An Alert Box send out Message when called
 */
class MessageBox {
    /**
     * Display a AlertBox with message to notify user
     * @param boxTitle Message box title
     * @param message Message box display message
     * @param messageColor string color format
     */
    public static void display(String boxTitle, String message, Paint messageColor) {
        Stage messageStage = new Stage();

        messageStage.initModality(Modality.APPLICATION_MODAL);
        messageStage.setTitle(boxTitle);
        messageStage.setMinWidth(300);
        messageStage.setMinHeight(300);

        Label messageLabel = new Label();
        messageLabel.setText(message);
        messageLabel.setTextFill(messageColor);

        Button close = new Button("Close");
        close.setOnAction(e -> messageStage.close());

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.BASELINE_CENTER);
        vBox.getChildren().addAll(messageLabel, close);

        Scene messageScene = new Scene(vBox);
        messageStage.setScene(messageScene);
        messageStage.show();
    }
}