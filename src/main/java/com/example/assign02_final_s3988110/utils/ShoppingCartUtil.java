package com.example.assign02_final_s3988110.utils;

import com.example.assign02_final_s3988110.models.Book;
import com.example.assign02_final_s3988110.models.CartItem;
import com.example.assign02_final_s3988110.DAO.CartDaoImpl;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import java.sql.SQLException;

/**
 * utility class for managing shopping cart-related operations.
 * this class provides methods to increase or decrease cart item quantities, add or update books in the cart,
 * and display alerts.
 */
public class ShoppingCartUtil {
    /**
     * increases the quantity of the specified cart item if it is less than the available stock.
     *
     * @param cartItem the cart item whose quantity is to be increased
     * @param availableQuantity the available stock of the book
     * @param cartTable the table view displaying the cart items
     * @param cartDao the data access object for the cart
     * @param errorMessageLabel the label for displaying error messages
     * @param username the username of the user
     * @throws SQLException if there is an error accessing the database
     */
    public static void increaseCartItemQuantity(CartItem cartItem, int availableQuantity, TableView<CartItem> cartTable, CartDaoImpl cartDao, Label errorMessageLabel, String username) throws SQLException {
        if (cartItem.getQuantity() < availableQuantity) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartTable.refresh();
            cartDao.addOrUpdateCartItem(username, cartItem);
        } else {
            showAlert("Exceeded available quantity", "You cannot add more than the available stock.");
        }
    }



    /**
     * decreases the quantity of the specified cart item.
     * removes the cart item if the quantity becomes zero.
     *
     * @param cartItem the cart item whose quantity is to be decreased
     * @param cartItems the list of cart items
     * @param cartTable the table view displaying the cart items
     * @param cartDao the data access object for the cart
     * @param username the username of the user
     */
    public static void decreaseCartItemQuantity(CartItem cartItem, ObservableList<CartItem> cartItems, TableView<CartItem> cartTable, CartDaoImpl cartDao, String username) {
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            try {
                cartDao.addOrUpdateCartItem(username, cartItem);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            cartItems.remove(cartItem);
            try {
                cartDao.deleteCartItem(username, cartItem.getBook().getTitle());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        cartTable.refresh();
    }



    /**
     * adds or updates the specified book in the cart.
     *
     * @param bookTitle the title of the book
     * @param quantity the quantity to add
     * @param availableQuantity the available stock of the book
     * @param cartItems the list of cart items
     * @param selectedBook the selected book
     */
    public static void addOrUpdateBookInCart(String bookTitle, int quantity, int availableQuantity, ObservableList<CartItem> cartItems, Book selectedBook) {
        CartItem existingCartItem = findCartItemInCart(bookTitle, cartItems);
        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + quantity;
            if (validateQuantity(newQuantity, availableQuantity)) {
                existingCartItem.setQuantity(newQuantity);
            } else {
                showAlert("Error", "Adding this quantity exceeds available stock.");
            }
        } else {
            CartItem newCartItem = new CartItem(selectedBook, quantity);
            cartItems.add(newCartItem);
        }
    }



    /**
     * finds the cart item with the specified book title in the list of cart items.
     *
     * @param bookTitle the title of the book
     * @param cartItems the list of cart items
     * @return the cart item with the specified book title, or null if not found
     */
    public static CartItem findCartItemInCart(String bookTitle, ObservableList<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getBook().getTitle().equals(bookTitle)) {
                return cartItem;
            }
        }
        return null;
    }



    /**
     * validates the specified quantity against the available stock.
     *
     * @param quantity the quantity to validate
     * @param availableQuantity the available stock of the book
     * @return true if the quantity is valid, false otherwise
     */
    public static boolean validateQuantity(int quantity, int availableQuantity) {
        return quantity <= availableQuantity;
    }



    /**
     * displays an alert with the specified title and message.
     *
     * @param title the title of the alert
     * @param message the message of the alert
     */
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}






