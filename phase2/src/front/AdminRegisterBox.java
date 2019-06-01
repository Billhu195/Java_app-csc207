package front;

import Manager.UserManager;
import User.AdminUser;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * AdminRegister Box for new Admin to add into the system
 */
class AdminRegisterBox extends FrontManager {
    /**
     * AdminRegisterBox
     * User input new AdminID and Password
     */
        public void display()  {
            Stage Register_Admin_window = new Stage();
            Register_Admin_window.setTitle("Admin_Register");

            //button Register
            Button RegisterButton = new Button("Register");

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(10, 10, 10, 10));

            grid.setVgap(8);
            grid.setHgap(10);

            //AdminId box
            Label New_AdminID = new Label();
            GridPane.setConstraints(New_AdminID, 0, 0);
            TextField adminID = new TextField("AdminID");

            GridPane.setConstraints(adminID, 1, 0);

            //Admin password box
            Label PassWord = new Label();
            GridPane.setConstraints(PassWord, 0, 1);
            PasswordField AdminPassword = new PasswordField();

            GridPane.setConstraints(AdminPassword, 1, 1);
            GridPane.setConstraints(RegisterButton, 1, 2);

            // when you press the login button check if the id and password is in the system
            RegisterButton.setOnAction(
                e -> {
                    if (!UserManager.getUserManager().getAllUsers().containsKey(adminID.getText())) {
                        new AdminUser(adminID.getText(), AdminPassword.getText());

                        MessageBox.display("Register Success", "Registration Success", Color.GREEN);

                    } else {
                        MessageBox.display("Register Failed", "UserID is already in the System", Color.RED);
                    }
                });

            //Bind button grid and scene
            grid.getChildren().addAll(New_AdminID, adminID, PassWord, AdminPassword, RegisterButton);
            Scene Reg_AdminScene = new Scene(grid, 300, 200);

            Register_Admin_window.setScene(Reg_AdminScene);
            Register_Admin_window.showAndWait();
        }
    }