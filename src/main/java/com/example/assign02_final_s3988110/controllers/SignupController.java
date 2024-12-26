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
import java.util.regex.Pattern;

/**
 * Controller class: for handling user sign-up.
 */
public class SignupController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorMessageLabel;



    /**
     * validates the password based on specific criteria.
     *
     * @param password the password to validate..
     * @return true if the password meets the criteria, false otherwise.....
     */
    private boolean validatePassword(String password) {
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";
        return Pattern.matches(passwordPattern, password);
    }



    /**
     * navigates to the dashboard view.
     */
    private void navigateToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/dashboardView.fxml"));
            Parent dashboardRoot = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * handles the action for the sign-up button.
     */
    @FXML
    public void handleSignUpButtonAction() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (!validatePassword(password)) {
            errorMessageLabel.setText("Password must be at least 6 characters long,\ncontain one letter, one number, and one symbol.");
            return;
        }

        UserDaoImpl userDao = new UserDaoImpl();
        try {
            userDao.setup();
            if (userDao.getUserByUsername(username) != null) {
                errorMessageLabel.setText("Username already exists. Please choose a different username.");
                return;
            }
            User user = userDao.createUser(username, password, firstName, lastName);
            UserSingleton.getInstance().setUser(user);  //set the user in the singleton..
            navigateToDashboard();
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessageLabel.setText("Signup failed: Could not create user.");
        }
    }



    /**
     * handles the action when the login link is clicked.
     * navigates to the login view.
     */
    @FXML
    public void handleLoginLinkAction() {
        try {
            Parent loginRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/assign02_final_s3988110/views/loginView.fxml")));
            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
