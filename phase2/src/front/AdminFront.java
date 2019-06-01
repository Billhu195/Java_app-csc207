package front;

import Exception.InvalidInputException;
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
 * Admin Front Login Page, let Admin to login or register
 */
class AdminFront extends FrontManager {
    /**
     * AdminLogin Page display, after login We will display a message if the login is success or not
     * If success the stage will switch to a stage to view statistics; Login Page will have
     * AdminId:
     * Password:
     * Login button, Register New Admin button
     */
    public void display() {
        Stage Admin_window = new Stage();
        Admin_window.setTitle("Admin_Login");

        Button loginButton = new Button("LogIn");
        Button newAdminButton = new Button("Register New Admin");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));

        grid.setVgap(8);
        grid.setHgap(10);

        //AdminId Label
        Label AdminID = new Label("AdminID");
        GridPane.setConstraints(AdminID, 0, 0);
        //TextField adminID
        TextField adminID = new TextField();
        GridPane.setConstraints(adminID, 1, 0);

        //Admin password Label
        Label PassWord = new Label("Password");
        GridPane.setConstraints(PassWord, 0, 1);
        PasswordField AdminPassword = new PasswordField();
        GridPane.setConstraints(AdminPassword, 1, 1);

        //Admin button add in GridPane
        GridPane.setConstraints(loginButton, 1, 2);
        GridPane.setConstraints(newAdminButton, 2,2);

        // when you press the login button check if the id and password is in the system
        loginButton.setOnAction(
                e -> {
                    try {
                        if (UserManager.getUserManager()
                                .UserLogin(adminID.getText(), AdminPassword.getText())) {

                            if (!(UserManager.getUserManager().getAllUsers().get(adminID.getText())
                                    instanceof AdminUser)) {
                                MessageBox.display("Login Failed", "User Does Not Exist in Admin", Color.RED);
                                throw new InvalidInputException("Login Failed");
                            }
                            AdminUser user =
                                    (AdminUser) UserManager.getUserManager().getAllUsers().get(adminID.getText());
                            MessageBox.display("Login Success", "Login Success", Color.GREEN);
                            ///Switch to AdminStatPage
                            AdminStatPage.display(user);
                            Admin_window.close();

                        } else {
                            MessageBox.display("Login Failed", "UserID/Password is invalid!", Color.RED);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });

        ///Register box and button for new admin
        AdminRegisterBox newRegister = new AdminRegisterBox();
        newAdminButton.setOnAction(e -> newRegister.display());

        ///Add button grid and scene
        grid.getChildren().addAll(AdminID, adminID, PassWord, AdminPassword, loginButton, newAdminButton);
        Scene AdminScene = new Scene(grid, 400, 400);

        Admin_window.setScene(AdminScene);
        Admin_window.showAndWait();
    }
}