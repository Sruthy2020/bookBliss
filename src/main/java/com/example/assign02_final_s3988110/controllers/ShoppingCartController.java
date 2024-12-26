package com.example.assign02_final_s3988110.controllers;

import com.example.assign02_final_s3988110.models.*;
import com.example.assign02_final_s3988110.DAO.*;
import com.example.assign02_final_s3988110.utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for managing the shopping cart view in the application.
 * This controller handles operations related to the cart, including adding books, adjusting quantities,
 * and displaying the total amount of items in the cart.
 */
public class ShoppingCartController {

    @FXML
    private ComboBox<String> bookComboBox;
    @FXML
    private TextField quantityField;
    @FXML
    private TableView<CartItem> cartTable;
    @FXML
    private Label totalAmountLabel;
    @FXML
    private Label errorMessageLabel;

    @FXML
    private TableColumn<CartItem, String> bookTitleColumn;
    @FXML
    private TableColumn<CartItem, Integer> quantityColumn;
    @FXML
    private TableColumn<CartItem, Double> totalPriceColumn;
    @FXML
    private TableColumn<CartItem, Void> increaseQuantityColumn;
    @FXML
    private TableColumn<CartItem, Void> decreaseQuantityColumn;

    private final ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private final CartDaoImpl cartDao = new CartDaoImpl();
    private final BookDaoImpl bookDao = new BookDaoImpl();
    User user = UserSingleton.getInstance().getUser();


    /**
     * Initializes the shopping cart view by setting up ComboBox, loading the cart items,
     * and configuring table columns for displaying cart contents.
     */
    public void initialize() {
        try {
            cartDao.setupCartTable();
            bookDao.setup();

            loadUserCart();
            loadBooksIntoComboBox();
            setupCartTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * Loads the user's cart items into the TableView.
     * Retrieves items associated with the current user and adds them to the cartItems list.
     */
    private void loadUserCart() {
        User currentUser = UserSingleton.getInstance().getUser();
        try {
            cartItems.setAll(cartDao.getCartItemsByUsername(currentUser.getUsername()));
            cartTable.setItems(cartItems);
            updateTotalPrice();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * Loads available books into the ComboBox for selection.
     * Retrieves a list of book titles and populates the ComboBox.
     */
    private void loadBooksIntoComboBox() throws SQLException {
        ObservableList<String> bookEntries = FXCollections.observableArrayList();
        List<Book> books = bookDao.getAllBooks();

        for (Book book : books) {
            String entry = book.getTitle() + " - Available: " + book.getPhysicalCopies();
            bookEntries.add(entry);
        }

        bookComboBox.setItems(bookEntries);
        bookComboBox.setOnAction(event -> {
            String selectedBookEntry = bookComboBox.getSelectionModel().getSelectedItem();
            if (selectedBookEntry != null) {
                String[] parts = selectedBookEntry.split(" - Available: ");
                String bookTitle = parts[0];
                int availableQuantity = Integer.parseInt(parts[1]);
                quantityField.clear();
                quantityField.setPromptText("Max " + availableQuantity);
            }
        });
    }



    /**
     * Sets up the cart table's columns and cell factories.
     * This method configures how the data is displayed in the cart table.
     */
    private void setupCartTable() {
        bookTitleColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().titleProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        totalPriceColumn.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());

        addButtonToIncreaseQuantity();
        addButtonToDecreaseQuantity();
    }



    /**
     * Adds a button to increase the quantity of the selected item in the cart.
     * The button will only allow increasing the quantity up to the available stock.
     * If the quantity is increased successfully, the total price is updated.
     */
    private void addButtonToIncreaseQuantity() {
        increaseQuantityColumn.setCellFactory(param -> new TableCell<>() {
            private final Button increaseButton = new Button("+");

            {
                increaseButton.setOnAction(event -> {
                    CartItem cartItem = getTableView().getItems().get(getIndex());
                    if (cartItem != null) {
                        int availableQuantity = cartItem.getBook().getPhysicalCopies();
                        if (cartItem.getQuantity() < availableQuantity) {
                            try {
                                ShoppingCartUtil.increaseCartItemQuantity(cartItem, availableQuantity, cartTable, cartDao, errorMessageLabel, UserSingleton.getInstance().getUser().getUsername());
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            updateTotalPrice();
                        } else {
                            showAlert("Error", "You cannot add more than the available stock.");
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
     * Adds a button to decrease the quantity of the selected item in the cart.
     * When clicked, the button will call the method to decrease the item quantity
     * and update the total price in the cart.
     */
    private void addButtonToDecreaseQuantity() {
        decreaseQuantityColumn.setCellFactory(param -> new TableCell<>() {
            private final Button decreaseButton = new Button("-");

            {
                decreaseButton.setOnAction(event -> {
                    CartItem cartItem = getTableView().getItems().get(getIndex());
                    if (cartItem != null) {
                        ShoppingCartUtil.decreaseCartItemQuantity(cartItem, cartItems, cartTable, cartDao, UserSingleton.getInstance().getUser().getUsername());
                        updateTotalPrice();
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
     * Handles adding a selected book and its quantity to the shopping cart.
     * Retrieves the selected book and checks for a valid quantity. If valid, the book is added to the cart.
     * If an error occurs during the process, an appropriate alert is shown.
     */
    @FXML
    private void handleAddToCart() {
        String selectedBookEntry = bookComboBox.getValue();
        String quantityText = quantityField.getText();

        try {
            //checking if a book is selected...
            if (selectedBookEntry == null || selectedBookEntry.isEmpty()) {
                showAlert("Error", "Please select a book from the list.");
                return;
            }

            //parsugn the quantity..
            int quantity = Integer.parseInt(quantityText);
            String[] parts = selectedBookEntry.split(" - Available: ");
            String selectedBookTitle = parts[0];
            Book selectedBook = bookDao.getBookByTitle(selectedBookTitle);

            if (selectedBook != null) {
                int availableQuantity = selectedBook.getPhysicalCopies();

                //checking if the requested quantity is valid..
                if (quantity > 0) {
                    //checking if the requested quantity exceeds available quantity..
                    if (quantity <= availableQuantity) {
                        // updating the cart and database..
                        ShoppingCartUtil.addOrUpdateBookInCart(selectedBookTitle, quantity, availableQuantity, cartItems, selectedBook);
                        cartDao.addOrUpdateCartItem(user.getUsername(), new CartItem(selectedBook, quantity));

                        //refreshing the cart table and update total price....
                        cartTable.setItems(cartItems);
                        updateTotalPrice();
                    } else {
                        showAlert("Exceeded available quantity", "You cannot add more than the available stock.");
                    }
                } else {
                    showAlert("Error", "Quantity must be greater than zero.");
                }
            } else {
                showAlert("Error", "Selected book not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not add item to the cart due to a database error.");
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid quantity. Please enter a valid number.");
        }
    }



    /**
     * Updates the total price label by summing the total prices of all items in the cart.
     * The total price is displayed in the totalAmountLabel.
     */
    private void updateTotalPrice() {
        double total = cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
        totalAmountLabel.setText("Total: $" + total);
    }



    /**
     * Clears the shopping cart by removing all items from the database and the local cart.
     * After clearing the cart, it refreshes the cart table and updates the total price.
     */
    @FXML
    private void handleClearCart() {
        try {
            cartDao.clearCart(UserSingleton.getInstance().getUser().getUsername());
            cartItems.clear();
            cartTable.refresh();
            updateTotalPrice();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * Handles the checkout process.
     * If the cart is empty, an alert is shown. Otherwise, it navigates to the checkout view
     * and passes the current cart items to the checkout controller.
     */
    @FXML
    private void handleCheckout() {
        if (cartItems.isEmpty()) {
            showAlert("Error", "Your cart is empty. Please add items before checking out.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/checkoutView.fxml"));
            Parent checkoutRoot = loader.load();

            CheckOutController checkoutController = loader.getController();
            checkoutController.setCartItems(cartItems);

            Stage stage = (Stage) cartTable.getScene().getWindow();
            stage.setScene(new Scene(checkoutRoot));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Handles the action for the back button.
     * Navigates the user back to the dashboard view and initializes the dashboard controller if necessary.
     */
    @FXML
    private void handleBackButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/dashboardView.fxml"));
            Parent dashboardRoot = loader.load();
            Stage stage = (Stage) cartTable.getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
            stage.setTitle("Dashboard");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     *displays an alert with the given title and message.
     *
     * @param title the title of the alert
     * @param message the message of the alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

}





