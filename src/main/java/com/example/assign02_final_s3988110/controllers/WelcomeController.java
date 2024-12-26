package com.example.assign02_final_s3988110.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.util.Objects;

/**
 * controller class for the Welcome view.
 */
public class WelcomeController {

    /**
     * the action when the Login button is clicked.
     *
     * @param event: by clicking the Login button
     */
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        System.out.println("Login button clicked");
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/com/example/assign02_final_s3988110/views/loginView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * when the Sign Up button is clicked.
     *
     * @param event: by clicking the Sign Up button
     */
    @FXML
    private void handleSignUpButtonAction(ActionEvent event) {
        System.out.println("Sign up button clicked");
        try {
            Parent signUpRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/assign02_final_s3988110/views/signupView.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(signUpRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
