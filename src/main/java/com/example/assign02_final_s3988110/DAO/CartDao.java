package com.example.assign02_final_s3988110.DAO;

import com.example.assign02_final_s3988110.models.CartItem;
import java.sql.SQLException;
import java.util.List;

/**
 * data Access Object (DAO) interface for managing shopping cart-related operations.
 * this interface provides methods for setting up the cart table, adding or updating cart items,
 * fetching cart items by username, clearing the cart, and deleting specific cart items.
 */
public interface CartDao {



    /**
     *sets up the table for cart-related operations.
     *
     * @throws SQLException if there is an error accessing the database
     */
    void setupCartTable() throws SQLException;



    /**
     *adds or updates a cart item for a specific user in the database.
     *
     * @param username the username of the user
     * @param cartItem the cart item to add or update
     * @throws SQLException if there is an error accessing the database
     */
    void addOrUpdateCartItem(String username, CartItem cartItem) throws SQLException;



    /**
     * retrieves a list of cart items for a specific user from the database.
     *
     * @param username the username of the user
     * @return a list of cart items for the user
     * @throws SQLException if there is an error accessing the database
     */
    List<CartItem> getCartItemsByUsername(String username) throws SQLException;



    /**
     * clears all cart items for a specific user from the database.
     *
     * @param username the username of the user
     * @throws SQLException if there is an error accessing the database
     */
    void clearCart(String username) throws SQLException;



    /**
     * deletes a specific cart item for a user from the database.
     *
     * @param username the username of the user
     * @param title the title of the book
     * @throws SQLException if there is an error accessing the database
     */
    void deleteCartItem(String username, String title) throws SQLException;
}




