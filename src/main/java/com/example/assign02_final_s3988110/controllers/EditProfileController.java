package com.example.assign02_final_s3988110.controllers;

import com.example.assign02_final_s3988110.models.User;
import com.example.assign02_final_s3988110.models.UserSingleton;
import com.example.assign02_final_s3988110.DAO.UserDaoImpl;
import com.example.assign02_final_s3988110.utils.ValidationUtil;
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

/**
 * controller class for the Edit Profile view.
 * provides functionality to view and edit the user profile information.
 */
public class EditProfileController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label errorMessageLabel;
    private final UserDaoImpl userDao = new UserDaoImpl();
    /**
     * the currently logged-in user.
     */
    User currentUser = UserSingleton.getInstance().getUser();



    /**
     * initializes the controller class.
     * sets up the initial values for the form fields.
     */
    @FXML
    public void initialize() {
        if (currentUser != null) {
            usernameField.setText(currentUser.getUsername());
            firstNameField.setText(currentUser.getFirstName());
            lastNameField.setText(currentUser.getLastName());
            usernameField.setDisable(true);
        }
    }



    /**
     * initializes the form field with the provided user data.
     *
     * @param user the user whose data will be used to populate the form fields
     */
    public void initData(User user) {
        if (user != null) {
            usernameField.setText(user.getUsername());
            firstNameField.setText(user.getFirstName());
            lastNameField.setText(user.getLastName());
        }
    }



    /**
     * handles the save button action.
     * validates the input data and updates the user profile information.
     */
    @FXML
    private void handleSaveButtonAction() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!ValidationUtil.isPasswordValid(password)) {
            errorMessageLabel.setText("Password must be at least 8 characters, with uppercase, lowercase, and digits.");
            return;
        }

        if (!ValidationUtil.doPasswordsMatch(password, confirmPassword)) {
            errorMessageLabel.setText("Passwords do not match.");
            return;
        }

        User currentUser = UserSingleton.getInstance().getUser();
        if (currentUser != null) {
            currentUser.setFirstName(firstName);
            currentUser.setLastName(lastName);
            currentUser.setPassword(password);
            try {
                userDao.updateUser(currentUser.getUsername(), currentUser.getFirstName(), currentUser.getLastName(), currentUser.getPassword());
                errorMessageLabel.setText("Profile updated successfully.");
            } catch (SQLException e) {
                errorMessageLabel.setText("Error updating profile. Please try again.");
            }
        }
    }



    /**
     * handles the clear button action.
     * clears the input fields and error message label.
     */
    @FXML
    private void handleClearButtonAction() {
        firstNameField.clear();
        lastNameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        errorMessageLabel.setText("");
    }



    /**
     * handles the back button action.
     * navigates back to the dashboard view.
     */
    @FXML
    private void handleBackButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/dashboardView.fxml"));
            Parent dashboardRoot = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
            stage.setTitle("Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
