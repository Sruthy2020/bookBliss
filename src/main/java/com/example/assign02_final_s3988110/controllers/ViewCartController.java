package com.example.assign02_final_s3988110.controllers;

import com.example.assign02_final_s3988110.models.CartItem;
import com.example.assign02_final_s3988110.models.UserSingleton;
import com.example.assign02_final_s3988110.DAO.CartDaoImpl;
import com.example.assign02_final_s3988110.DAO.UserDaoImpl;
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

/**
 * Controller class for viewing the shopping cart.
 * This class handles displaying the cart contents and the total amount.
 */
public class ViewCartController {
    @FXML
    private TableView<CartItem> cartTable;
    @FXML
    private TableColumn<CartItem, String> bookTitleColumn;
    @FXML
    private TableColumn<CartItem, Integer> quantityColumn;
    @FXML
    private TableColumn<CartItem, Double> totalPriceColumn;
    @FXML
    private Label totalAmountLabel;

    private ObservableList<CartItem> cartItems;
    private CartDaoImpl cartDao;


    /**
     * Initializes the controller and loads the cart items into the table.
     * Sets up the table columns with their respective cell value factories.
     */
    @FXML
    public void initialize() {
        cartDao = new CartDaoImpl();
        UserDaoImpl userDao = new UserDaoImpl();
        cartItems = FXCollections.observableArrayList();
        cartTable.setItems(cartItems);
        bookTitleColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().titleProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        totalPriceColumn.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());
        UserSingleton userSingleton = UserSingleton.getInstance();
        if (userSingleton.getUser() != null) {
            loadUserCartItems();
        }
    }



    /**
     * Loads the cart items for the currently logged-in user.
     * Uses the UserSingleton to get the current user's cart items.
     */
    private void loadUserCartItems() {
        try {
            UserSingleton userSingleton = UserSingleton.getInstance();
            if (userSingleton.getUser() != null) {
                cartItems.setAll(cartDao.getCartItemsByUsername(userSingleton.getUser().getUsername()));
                cartTable.setItems(cartItems);
                updateTotalPrice();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert();
        }
    }



    /**
     * Updates the total price displayed in the cart.
     * Sums the total price of all items in the cart and updates the label.
     */
    private void updateTotalPrice() {
        double total = cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
        totalAmountLabel.setText("Total: $" + total);
    }



    /**
     * Handles the action of navigating back to the dashboard view.
     */
    @FXML
    private void handleBackButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/dashboardView.fxml"));
            Parent dashboardRoot = loader.load();
            Stage stage = (Stage) cartTable.getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Handles the action of clearing the cart.
     * Clears all items from the cart for the currently logged-in user.
     */
    @FXML
    private void handleClearCart() {
        try {
            UserSingleton userSingleton = UserSingleton.getInstance();
            if (userSingleton.getUser() != null) {
                cartDao.clearCart(userSingleton.getUser().getUsername());
                cartItems.clear();
                cartTable.refresh();
                updateTotalPrice();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * Handles the action of navigating to the shopping cart view.
     */
    @FXML
    private void handleAddToCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/shoppingCartView.fxml"));
            Parent shoppingCartRoot = loader.load();
            Stage stage = (Stage) cartTable.getScene().getWindow();
            stage.setScene(new Scene(shoppingCartRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Displays an alert with the given title and message.
     */
    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setContentText("There was an error loading the cart items.");
        alert.show();
    }
}

