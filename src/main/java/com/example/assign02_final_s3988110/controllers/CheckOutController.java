package com.example.assign02_final_s3988110.controllers;

import com.example.assign02_final_s3988110.models.*;
import com.example.assign02_final_s3988110.DAO.BookDaoImpl;
import com.example.assign02_final_s3988110.DAO.CartDaoImpl;
import com.example.assign02_final_s3988110.DAO.OrderDao;
import com.example.assign02_final_s3988110.DAO.OrderDaoImpl;
import com.example.assign02_final_s3988110.utils.CheckoutValidationUtil;
import com.example.assign02_final_s3988110.utils.OrderDetailsUtil;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for managing the checkout process.
 * This class handles displaying cart items, calculating the total amount, and processing the order.
 */
public class CheckOutController {

    @FXML
    private TextField cardHolderNameField;

    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField cvvField;

    @FXML
    private TextField expiryDateField;

    @FXML
    private TextField usernameField;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private TableView<CartItem> cartTable;

    @FXML
    private TableColumn<CartItem, String> bookTitleColumn;

    @FXML
    private TableColumn<CartItem, Integer> quantityColumn;

    @FXML
    private TableColumn<CartItem, Double> totalPriceColumn;

    private ObservableList<CartItem> cartItems;
    private final OrderDao orderDao = new OrderDaoImpl();
    private final BookDaoImpl bookDao = new BookDaoImpl();
    private final CartDaoImpl cartDao = new CartDaoImpl();
    private final OrderDetailsUtil orderDetailsUtil = new OrderDetailsUtil();



    /**
     *initializes the controller and sets up the table columns.
     *loads the user's cart items into the table.
     */
    @FXML
    public void initialize() {
        try {
            orderDao.setup();
            bookTitleColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().titleProperty());
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            totalPriceColumn.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());
            loadUserCartItems();
        } catch (SQLException e) {
            showAlert("Setup Error", "Error setting up the orders table.");
            e.printStackTrace();
        }
    }



    /**
     *generates an order from the cart items.
     *
     * @return the generated order
     */
    private Order generateOrderFromCart() {
        String orderNumber = OrderDetailsUtil.generateOrderNumber();
        String date = orderDetailsUtil.getCurrentDate();
        double totalOrderPrice = cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(cartItem.getBook().getTitle(), cartItem.getQuantity(), cartItem.getTotalPrice());
            orderItems.add(orderItem);
        }
        return new Order(orderNumber, date, totalOrderPrice, orderItems, UserSingleton.getInstance().getUser());
    }



    /**
     *sets the cart items in the table view and updates the total price.
     *
     * @param cartItems the list of cart items
     */
    public void setCartItems(ObservableList<CartItem> cartItems) {
        this.cartItems = cartItems;
        cartTable.setItems(cartItems);
        updateTotalPrice();
    }




    /**
     *updates the total price based on the cart items and updates the total amount label.
     */
    private void updateTotalPrice() {
        double totalAmount = cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
        totalAmountLabel.setText("Total Price: $" + String.format("%.2f", totalAmount));
    }



    /**
     *loads the user's cart items from the database and sets them in the table view.
     */
    private void loadUserCartItems() {
        try {
            cartItems = FXCollections.observableArrayList(cartDao.getCartItemsByUsername(UserSingleton.getInstance().getUser().getUsername()));
            cartTable.setItems(cartItems);
            updateTotalPrice();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Load Error", "There was an error loading the cart items.");
        }
    }



    /**
     *handles the action for the confirm button.
     *validates the input fields and processes the order if valid.
     */
    @FXML
    public void handleConfirmButtonAction() {
        String cardHolderName = cardHolderNameField.getText();
        String cardNumber = cardNumberField.getText();
        String cvv = cvvField.getText();
        String expiryDate = expiryDateField.getText();

        if (UserSingleton.getInstance().getUser() == null) {
            showAlert("User Error", "No user is logged in!");
            return;
        }

        if (cardHolderName.isEmpty() || cardNumber.isEmpty() || cvv.isEmpty() || expiryDate.isEmpty()) {
            showAlert("Input Error", "Please fill in all the fields!");
            return;
        }

        if (!CheckoutValidationUtil.validateCardNumber(cardNumber)) {
            showAlert("Card Error", "Invalid card number! It must be exactly 16 digits.");
            return;
        }


        if (!CheckoutValidationUtil.validateCVV(cvv)) {
            showAlert("CVV Error", "Invalid CVV! It must be exactly 3 digits.");
            return;
        }

        if (!CheckoutValidationUtil.validateExpiryDate(expiryDate)) {
            showAlert("Expiry Error", "Invalid expiry date! The expiry date must be in the future.[MM/YY]");
            return;
        }

        try {
            Order order = generateOrderFromCart();
            orderDao.addOrder(order);
            for (OrderItem item : order.getOrderItems()) {
                updateBookStockAndSoldCopies(item.getBookTitle(), item.getQuantity());
            }
            clearUserCart(UserSingleton.getInstance().getUser().getUsername());
        } catch (SQLException e) {
            showAlert("Processing Error", "Error processing the order. Please try again.");
            e.printStackTrace();
            return;
        }

        showAlert("Success", "Payment confirmed and order processed successfully!");
        cartItems.clear();
        navigateToOrderDetails();
    }



    /**
     *updates the book stock and sold copies based on the quantity purchased.
     *
     * @param bookTitle the title of the book
     * @param quantityPurchased the quantity purchased
     * @throws SQLException if there is an error accessing the database
     */
    private void updateBookStockAndSoldCopies(String bookTitle, int quantityPurchased) throws SQLException {
        Book book = bookDao.getBookByTitle(bookTitle);
        if (book != null) {
            int updatedStock = book.getPhysicalCopies() - quantityPurchased;
            int updatedSoldCopies = book.getSoldCopies() + quantityPurchased;
            book.setPhysicalCopies(updatedStock);
            book.setSoldCopies(updatedSoldCopies);
            bookDao.updateBook(book);
        }
    }



    /**
     *clears the user's cart in the database.
     *
     * @param username the username of the user
     * @throws SQLException if there is an error accessing the database
     */
    private void clearUserCart(String username) throws SQLException {
        cartDao.clearCart(username);
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



    /**
     *navigates to the order details view.
     */
    private void navigateToOrderDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/orderDetailsView.fxml"));
            Parent orderDetailsRoot = loader.load();
            Stage stage = (Stage) totalAmountLabel.getScene().getWindow();
            stage.setScene(new Scene(orderDetailsRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     *handles the action for the back button.
     *navigates back to the dashboard view.
     */
    @FXML
    private void handleBackButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/dashboardView.fxml"));
            Parent dashboardRoot = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
