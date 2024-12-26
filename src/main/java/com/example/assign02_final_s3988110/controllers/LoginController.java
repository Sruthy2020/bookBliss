package com.example.assign02_final_s3988110.controllers;

import com.example.assign02_final_s3988110.models.User;
import com.example.assign02_final_s3988110.models.UserSingleton;
import com.example.assign02_final_s3988110.DAO.UserDaoImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Controller class for handling login and navigation to the dashboard or sign-up...
 */
public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label welcomeText;
    @FXML
    private Label errorMessageLabel;

    private final UserDaoImpl userDao = new UserDaoImpl();

    /**
     * login button action.
     */
    @FXML
    protected void handleLoginButtonAction() {
        String inputUsername = usernameField.getText();
        String inputPassword = passwordField.getText();
        try {
            userDao.setup();
            User user = userDao.getUser(inputUsername, inputPassword);
            if (user != null && user.authenticate(inputUsername, inputPassword)) {
                UserSingleton.getInstance().setUser(user);
                navigateToDashboard(inputUsername);
            } else {
                errorMessageLabel.setText("Invalid credentials, please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessageLabel.setText("Error: Could not connect to the database.");
        }
    }



    /**
     * sign-up button action.
     */
    @FXML
    private void handleSignUpButtonAction() {
        try {
            Parent signUpRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/assign02_final_s3988110/views/signupView.fxml")));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(signUpRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Navigates to the dashboard view.
     *
     * @param username: the username of the logged-in user
     */
    private void navigateToDashboard(String username) {
        try {
            FXMLLoader loader;
            //for admin user
            if ("admin".equals(username)) {
                loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/adminView.fxml"));
                AdminDashboardController adminDashboardController = new AdminDashboardController(username);
                loader.setController(adminDashboardController);
            } else {
                //for normal users
                loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/dashboardView.fxml"));
            }
            Parent dashboardRoot = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
            stage.setTitle(username.equals("admin") ? "Admin Dashboard" : "Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
