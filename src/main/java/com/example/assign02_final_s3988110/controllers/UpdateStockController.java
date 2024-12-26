package com.example.assign02_final_s3988110.controllers;

import com.example.assign02_final_s3988110.models.Book;
import com.example.assign02_final_s3988110.DAO.BookDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller class for updating the stock of books.
 * This class handles displaying the book information and updating the stock quantities.
 */
public class UpdateStockController {

    @FXML
    private TableColumn<Book, Void> increaseQuantityColumn;

    @FXML
    private TableColumn<Book, Void> decreaseQuantityColumn;

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, String> bookTitleColumn;

    @FXML
    private TableColumn<Book, Integer> currentStockColumn;

    @FXML
    private ComboBox<String> bookComboBox;

    @FXML
    private TextField quantityField;

    private String adminUsername;

    private final BookDaoImpl bookDao = new BookDaoImpl();
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();




    /**
     * Initializes the controller and sets up the table columns and ComboBox.
     * Loads the books into the ComboBox and table, and sets up action buttons.
     */
    @FXML
    public void initialize() {
        try {
            bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            currentStockColumn.setCellValueFactory(new PropertyValueFactory<>("physicalCopies"));
            loadBooksIntoComboBox();
            loadBooksIntoTable();
            addButtonToIncreaseQuantity();
            addButtonToDecreaseQuantity();
            bookComboBox.setOnAction(event -> {
                String selectedBookEntry = bookComboBox.getSelectionModel().getSelectedItem();
                if (selectedBookEntry != null) {
                    String[] parts = selectedBookEntry.split(" - Available: ");
                    String bookTitle = parts[0];
                    int availableQuantity = Integer.parseInt(parts[1]);
                    quantityField.clear();
                    quantityField.setPromptText("Current Stock: " + availableQuantity);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    /**
     * Constructor that initializes the controller with the admin's username.
     *
     * @param adminUsername the admin's username
     */
    public UpdateStockController(String adminUsername) {
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
     * Loads the books into the ComboBox for selection.
     * Sets the ComboBox items with the available books and their quantities.
     *
     * @throws SQLException if there is an error accessing the database
     */
    private void loadBooksIntoComboBox() throws SQLException {
        ObservableList<String> bookEntries = FXCollections.observableArrayList();
        List<Book> books = bookDao.getAllBooks();
        for (Book book : books) {
            String entry = book.getTitle() + " - Available: " + book.getPhysicalCopies();
            bookEntries.add(entry);
        }
        bookComboBox.setItems(bookEntries);
    }




    /**
     * Adds a button to each row to increase the quantity of the book stock.
     * The button increments the stock quantity.
     */
    private void addButtonToIncreaseQuantity() {
        increaseQuantityColumn.setCellFactory(param -> new TableCell<>() {
            private final Button increaseButton = new Button("+");
            {
                increaseButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    if (book != null) {
                        try {
                            book.setPhysicalCopies(book.getPhysicalCopies() + 1);
                            bookDao.updateBookStock(book.getTitle(), book.getPhysicalCopies());
                            getTableView().refresh();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(increaseButton);
                }
            }
        });
    }




    /**
     * Adds a button to each row to decrease the quantity of the book stock.
     * The button decrements the stock quantity.
     */
    private void addButtonToDecreaseQuantity() {
        decreaseQuantityColumn.setCellFactory(param -> new TableCell<>() {
            private final Button decreaseButton = new Button("-");
            {
                decreaseButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    if (book != null && book.getPhysicalCopies() > 0) {
                        try {
                            book.setPhysicalCopies(book.getPhysicalCopies() - 1);
                            bookDao.updateBookStock(book.getTitle(), book.getPhysicalCopies());
                            getTableView().refresh();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(decreaseButton);
                }
            }
        });
    }




    /**
     * Handles the action of updating the book stock quantity.
     * Validates the selected book and quantity before updating the stock.
     */
    @FXML
    private void handleUpdateQuantity() {
        String selectedBookEntry = bookComboBox.getSelectionModel().getSelectedItem();
        if (selectedBookEntry != null) {
            try {
                String[] parts = selectedBookEntry.split(" - Available: ");
                String bookTitle = parts[0];
                int additionalStock = Integer.parseInt(quantityField.getText());
                Book selectedBook = bookDao.getBookByTitle(bookTitle);
                int newStock = selectedBook.getPhysicalCopies() + additionalStock;
                bookDao.updateBookStock(bookTitle, newStock);
                loadBooksIntoComboBox();
                loadBooksIntoTable();
                quantityField.clear();
                bookComboBox.getSelectionModel().getSelectedItem();
                bookComboBox.setPromptText("Select a book");
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
            }
        }
    }




    /**
     * Loads the books into the table for display.
     *
     * @throws SQLException if there is an error accessing the database
     */
    private void loadBooksIntoTable() throws SQLException {
        List<Book> books = bookDao.getAllBooks();
        bookList.setAll(books);
        bookTable.setItems(bookList);
    }




    /**
     * Handles the action of navigating back to the admin dashboard.
     */
    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/adminView.fxml"));
            AdminDashboardController dashboardController = new AdminDashboardController(adminUsername);
            loader.setController(dashboardController);
            Parent dashboardRoot = loader.load();
            Stage stage = (Stage) bookTable.getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
