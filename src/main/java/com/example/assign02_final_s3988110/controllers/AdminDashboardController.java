package com.example.assign02_final_s3988110.controllers;

import com.example.assign02_final_s3988110.models.Book;
import com.example.assign02_final_s3988110.utils.BookUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller class for managing the admin dashboard view.
 * This class handles displaying popular books and navigating to different admin functionalities.
 */
public class AdminDashboardController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private TableView<Book> popularBooksListView;
    @FXML
    private TableColumn<Book, String> bookTitleColumn;
    @FXML
    private TableColumn<Book, Integer> booksSoldColumn;

    private String adminUsername;
    private BookUtil bookUtil = new BookUtil();



    /**
     * constructor that initializes the controller with the admin's username.
     *
     * @param adminUsername the admin's username
     */
    public AdminDashboardController(String adminUsername) {
        this.adminUsername = adminUsername;
    }



    /**
     * iinitializes the controller and sets up the table columns.
     * Loads the top 5 popular books into the table.
     */
    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome, " + adminUsername + "!");
        bookTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        booksSoldColumn.setCellValueFactory(cellData -> cellData.getValue().soldCopiesProperty().asObject());
        loadTop5PopularBooks();
    }



    /**
     * loads the top 5 popular books from the database and sets them in the table view.
     */
    private void loadTop5PopularBooks() {
        try {
            ObservableList<Book> bookList = bookUtil.getTop5PopularBooks();
            popularBooksListView.setItems(bookList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * handles the action for the logout button.
     *navigates back  to the login view.
     */
    @FXML
    private void handleLogoutAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/loginView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     *handles the action for the view book stock button.
     *navigates to the view book stock view.
     */
    @FXML
    private void handleViewBookStock() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/viewBookStock.fxml"));
            Parent bookStockRoot = loader.load();
            BookStockController bookStockController = loader.getController();
            bookStockController.setAdminUsername(adminUsername);
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(bookStockRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     *handles the action for the update quantity button.
     *navigates to the update stock view.
     */
    @FXML
    private void handleUpdateQuantity() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/updateStock.fxml"));
            UpdateStockController updateStockController = new UpdateStockController(adminUsername);
            loader.setController(updateStockController);
            Parent updateStockRoot = loader.load();
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(updateStockRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     *handles the action for the view all orders button.
     *navigates to the view all orders view.
     */
    @FXML
    private void handleViewAllOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/viewAllOrders.fxml"));
            OrderListController orderListController = new OrderListController(adminUsername);
            loader.setController(orderListController);
            Parent orderListRoot = loader.load();
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(orderListRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Handles the action for the manage users button.
     * Navigates to the view all users view.
     */
    @FXML
    private void handleManageUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/viewAllUsers.fxml"));
            UserListController userListController = new UserListController(adminUsername);
            loader.setController(userListController);
            Parent userListRoot = loader.load();
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(userListRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
