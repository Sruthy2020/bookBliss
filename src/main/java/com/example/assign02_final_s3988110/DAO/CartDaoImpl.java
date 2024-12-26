package com.example.assign02_final_s3988110.DAO;

import com.example.assign02_final_s3988110.models.Book;
import com.example.assign02_final_s3988110.models.CartItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *implementation of the CartDao interface for managing shopping cart-related operations.
 *this class provides methods for setting up the cart table, adding or updating cart items,
 * fetching cart items by username, clearing the cart, and deleting specific cart items.
 */
public class CartDaoImpl implements CartDao {
    private final String CART_TABLE = "carts";

    /**
     * sets up the table for cart-related operations.
     *
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public void setupCartTable() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement()) {
            String createCartTable = "CREATE TABLE IF NOT EXISTS " + CART_TABLE + " ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "username VARCHAR(100) NOT NULL,"
                    + "title VARCHAR(100) NOT NULL,"
                    + "quantity INT NOT NULL,"
                    + "FOREIGN KEY (username) REFERENCES users(username),"
                    + "FOREIGN KEY (title) REFERENCES books(title))";
            // Execute the table creation query
            stmt.executeUpdate(createCartTable);
            System.out.println("Table '" + CART_TABLE + "' has been created or already exists.");
        } catch (SQLException e) {
            System.err.println("Error during table creation: ");
            e.printStackTrace();
        }
    }



    /**
     * adds or updates a cart item for a specific user in the database.
     *
     * @param username the username of the user
     * @param cartItem the cart item to add or update
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public void addOrUpdateCartItem(String username, CartItem cartItem) throws SQLException {
        String checkSql = "SELECT * FROM " + CART_TABLE + " WHERE username = ? AND title = ?";
        String insertSql = "INSERT INTO " + CART_TABLE + " (username, title, quantity) VALUES (?, ?, ?)";
        String updateSql = "UPDATE " + CART_TABLE + " SET quantity = ? WHERE username = ? AND title = ?";
        try (Connection connection = Database.getConnection()) {
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setString(1, username);
                checkStmt.setString(2, cartItem.getBook().getTitle());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, cartItem.getQuantity());
                            updateStmt.setString(2, username);
                            updateStmt.setString(3, cartItem.getBook().getTitle());
                            updateStmt.executeUpdate();
                            System.out.println("Cart item updated successfully.");
                        }
                    } else {
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                            insertStmt.setString(1, username);
                            insertStmt.setString(2, cartItem.getBook().getTitle());
                            insertStmt.setInt(3, cartItem.getQuantity());
                            insertStmt.executeUpdate();
                            System.out.println("Cart item added successfully.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding/updating cart item: ");
            e.printStackTrace();
        }
    }



    /**
     * retrieves a list of cart items for a specific user from the database.
     *
     * @param username the username of the user
     * @return a list of cart items for the user
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public List<CartItem> getCartItemsByUsername(String username) throws SQLException {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM " + CART_TABLE + " WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("title");
                    int quantity = rs.getInt("quantity");
                    BookDaoImpl bookDao = new BookDaoImpl();
                    Book book = bookDao.getBookByTitle(title);
                    CartItem cartItem = new CartItem(book, quantity);
                    cartItems.add(cartItem);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving cart items: ");
            e.printStackTrace();
        }
        return cartItems;
    }



    /**
     * clears all cart items for a specific user from the database.
     *
     * @param username the username of the user
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public void clearCart(String username) throws SQLException {
        String sql = "DELETE FROM " + CART_TABLE + " WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
            System.out.println("Cart cleared successfully.");
        } catch (SQLException e) {
            System.err.println("Error clearing cart: ");
            e.printStackTrace();
        }
    }



    /**
     * deletes a specific cart item for a user from the database.
     *
     * @param username the username of the user
     * @param title the title of the book
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public void deleteCartItem(String username, String title) throws SQLException {
        String sql = "DELETE FROM " + CART_TABLE + " WHERE username = ? AND title = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, title);
            stmt.executeUpdate();
            System.out.println("Cart item deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting cart item: ");
            e.printStackTrace();
        }
    }
}




