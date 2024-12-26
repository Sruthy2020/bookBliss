package com.example.assign02_final_s3988110.DAO;

import com.example.assign02_final_s3988110.models.Order;
import java.sql.SQLException;
import java.util.List;

/**
 * data Access Object (DAO) interface for managing order-related operations.
 * this interface provides methods for setting up the orders table, adding orders,
 * fetching orders by username, fetching all orders, updating orders, and deleting orders.
 */
public interface OrderDao {
    /**
     * sets up the table for order-related operations.
     *
     * @throws SQLException if there is an error accessing the database
     */
    void setup() throws SQLException;



    /**
     * adds a new order to the database.
     *
     * @param order the order to add
     * @throws SQLException if there is an error accessing the database
     */
    void addOrder(Order order) throws SQLException;



    /**
     * retrieves a list of orders for a specific user from the database.
     *
     * @param username the username of the user
     * @return a list of orders for the user
     * @throws SQLException if there is an error accessing the database
     */
    List<Order> getOrdersByUsername(String username) throws SQLException;



    /**
     * retrieves a list of all orders from the database.
     *
     * @return a list of all orders
     * @throws SQLException if there is an error accessing the database
     */
    List<Order> getAllOrders() throws SQLException;



    /**
     * retrieves an order by its order number from the database.
     *
     * @param orderNumber the order number
     * @return the order with the specified order number
     * @throws SQLException if there is an error accessing the database
     */
    Order getOrderByNumber(String orderNumber) throws SQLException;



    /**
     * updates the details of an existing order in the database.
     *
     * @param order the order to update
     * @throws SQLException if there is an error accessing the database
     */
    void updateOrder(Order order) throws SQLException;



    /**
     * deletes an order by its order number from the database.
     *
     * @param orderNumber the order number
     * @throws SQLException if there is an error accessing the database
     */
    void deleteOrder(String orderNumber) throws SQLException;
}
