package com.example.assign02_final_s3988110.controllers;

import com.example.assign02_final_s3988110.models.Book;
import com.example.assign02_final_s3988110.DAO.BookDaoImpl;
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
 * controller class for managing the book stock view.
 * this class handles displaying book information and navigating to the admin dashboard.
 */
public class BookStockController {

    private String adminUsername;
    @FXML
    private TableView<Book> bookStockTable;
    @FXML
    private TableColumn<Book, String> bookTitleColumn;
    @FXML
    private TableColumn<Book, String> bookAuthorColumn;
    @FXML
    private TableColumn<Book, Integer> physicalCopiesColumn;
    @FXML
    private TableColumn<Book, Double> bookPriceColumn;
    @FXML
    private TableColumn<Book, Integer> soldCopiesColumn;
    @FXML
    private TextField usernameField;

    private final BookDaoImpl bookDao = new BookDaoImpl();



    /**
     * Initializes the controller and sets up the table columns.
     * Loads the book stock data into the table.
     */
    @FXML
    public void initialize() {
        try {
            bookDao.setup();
            bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
            physicalCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("physicalCopies"));
            bookPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            soldCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("soldCopies"));
            loadBookStock();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
     * Loads the book stock data from the database and sets it in the table view.
     *
     * @throws SQLException if there is an error accessing the database
     */
    private void loadBookStock() throws SQLException {
        List<Book> books = bookDao.getAllBooks();
        ObservableList<Book> bookList = FXCollections.observableArrayList(books);
        bookStockTable.setItems(bookList);
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
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
