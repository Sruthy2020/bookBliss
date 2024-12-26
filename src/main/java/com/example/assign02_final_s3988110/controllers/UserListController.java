package com.example.assign02_final_s3988110.controllers;

import com.example.assign02_final_s3988110.models.User;
import com.example.assign02_final_s3988110.DAO.UserDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller class for managing the user list view.
 * This class handles displaying user information and navigating to the admin dashboard.
 */
public class UserListController {

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    private String adminUsername;
    @FXML
    private TextField usernameField;
    private final UserDaoImpl userDao = new UserDaoImpl();



    /**
     * Initializes the controller and loads the user data into the table.
     * Sets up the table columns with their respective cell value factories.
     */
    @FXML
    public void initialize() {
        try {
            usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            loadUserData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * Constructor that initializes the controller with the admin's username.
     *
     * @param adminUsername the admin's username
     */
    public UserListController(String adminUsername) {
        this.adminUsername = adminUsername;
    }



    /**
     * Sets the admin's username.
     *
     * @param adminUsername the admin's username
     */
    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }



    /**
     * Loads the user data from the database and sets it in the table view.
     *
     * @throws SQLException if there is an error accessing the database
     */
    private void loadUserData() throws SQLException {
        List<User> users = userDao.getAllUsers();
        ObservableList<User> userList = FXCollections.observableArrayList(users);
        userTable.setItems(userList);
    }



    /**
     * Handles the action of navigating back to the admin dashboard.
     * Loads the admin dashboard view and passes the admin's username.
     */
    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/adminView.fxml"));
            AdminDashboardController dashboardController = new AdminDashboardController(adminUsername);
            loader.setController(dashboardController);
            Parent dashboardRoot = loader.load();
            Stage stage = (Stage) userTable.getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
