package com.example.assign02_final_s3988110.DAO;

import com.example.assign02_final_s3988110.models.Order;
import com.example.assign02_final_s3988110.models.OrderItem;
import com.example.assign02_final_s3988110.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * implementation of the OrderDao interface for managing order-related operations.
 * this class provides methods for setting up the orders table, adding orders,
 * fetching orders by username, fetching all orders, updating orders, and deleting orders.
 */
public class OrderDaoImpl implements OrderDao {
    private final String ORDER_TABLE = "orders";
    private final String ORDER_ITEMS_TABLE = "order_items";

    /**
     * sets up the table for order-related operations.
     *
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public void setup() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement()) {
            String createOrdersTable = "CREATE TABLE IF NOT EXISTS " + ORDER_TABLE + " ("
                    + "orderNumber VARCHAR(100) PRIMARY KEY,"
                    + "username VARCHAR(100) NOT NULL,"
                    + "date VARCHAR(100) NOT NULL,"
                    + "totalPrice DOUBLE NOT NULL,"
                    + "FOREIGN KEY (username) REFERENCES users(username))";
            stmt.executeUpdate(createOrdersTable);

            String createOrderItemsTable = "CREATE TABLE IF NOT EXISTS " + ORDER_ITEMS_TABLE + " ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "orderNumber VARCHAR(100) NOT NULL,"
                    + "bookTitle VARCHAR(100) NOT NULL,"
                    + "quantity INT NOT NULL,"
                    + "price DOUBLE NOT NULL,"
                    + "FOREIGN KEY (orderNumber) REFERENCES orders(orderNumber),"
                    + "FOREIGN KEY (bookTitle) REFERENCES books(title))";
            stmt.executeUpdate(createOrderItemsTable);

            System.out.println("Tables '" + ORDER_TABLE + "' and '" + ORDER_ITEMS_TABLE + "' have been created or already exist.");
        } catch (SQLException e) {
            System.err.println("Error during table creation: ");
            e.printStackTrace();
        }
    }



    /**
     * adds a new order to the database.
     *
     * @param order the order to add
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public void addOrder(Order order) throws SQLException {
        String insertOrderSql = "INSERT INTO " + ORDER_TABLE + " (orderNumber, username, date, totalPrice) VALUES (?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(insertOrderSql)) {
            stmt.setString(1, order.getOrderNumber());
            stmt.setString(2, order.getUser().getUsername());
            stmt.setString(3, order.getDate());
            stmt.setDouble(4, order.getTotalPrice());
            stmt.executeUpdate();
            for (OrderItem item : order.getOrderItems()) {
                addOrderItem(order.getOrderNumber(), item);
            }
            System.out.println("Order added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding order: ");
            e.printStackTrace();
        }
    }



    /**
     * adds a new order item to the database.
     *
     * @param orderNumber the order number
     * @param item the order item to add
     * @throws SQLException if there is an error accessing the database
     */
    private void addOrderItem(String orderNumber, OrderItem item) throws SQLException {
        String insertOrderItemSql = "INSERT INTO " + ORDER_ITEMS_TABLE + " (orderNumber, bookTitle, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(insertOrderItemSql)) {
            stmt.setString(1, orderNumber);
            stmt.setString(2, item.getBookTitle());
            stmt.setInt(3, item.getQuantity());
            stmt.setDouble(4, item.getPricePerUnit());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding order item: ");
            e.printStackTrace();
        }
    }



    /**
     * retrieves a list of orders for a specific user from the database.
     *
     * @param username the username of the user
     * @return a list of orders for the user
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public List<Order> getOrdersByUsername(String username) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM " + ORDER_TABLE + " WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    List<OrderItem> items = getOrderItems(rs.getString("orderNumber"));
                    Order order = new Order(
                            rs.getString("orderNumber"),
                            rs.getString("date"),
                            rs.getDouble("totalPrice"),
                            items,
                            new User(username)
                    );
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving orders for user: " + username);
            e.printStackTrace();
        }
        return orders;
    }



    /**
     * retrieves a list of all orders from the database.
     *
     * @return a list of all orders
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM " + ORDER_TABLE;
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                List<OrderItem> items = getOrderItems(rs.getString("orderNumber"));
                Order order = new Order(
                        rs.getString("orderNumber"),
                        rs.getString("date"),
                        rs.getDouble("totalPrice"),
                        items,
                        new User(rs.getString("username"))
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all orders.");
            e.printStackTrace();
        }
        return orders;
    }



    /**
     * retrieves an order by its order number from the database.
     *
     * @param orderNumber the order number
     * @return the order with the specified order number, or null if not found
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public Order getOrderByNumber(String orderNumber) throws SQLException {
        String sql = "SELECT * FROM " + ORDER_TABLE + " WHERE orderNumber = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, orderNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    List<OrderItem> items = getOrderItems(rs.getString("orderNumber"));
                    return new Order(
                            rs.getString("orderNumber"),
                            rs.getString("date"),
                            rs.getDouble("totalPrice"),
                            items,
                            new User(rs.getString("username"))
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving order by number: " + orderNumber);
            e.printStackTrace();
        }
        return null;
    }



    /**
     * retrieves the order items for a specific order number from the database.
     *
     * @param orderNumber the order number
     * @return a list of order items for the order
     * @throws SQLException if there is an error accessing the database
     */
    private List<OrderItem> getOrderItems(String orderNumber) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM " + ORDER_ITEMS_TABLE + " WHERE orderNumber = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, orderNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem(
                            rs.getString("bookTitle"),
                            rs.getInt("quantity"),
                            rs.getDouble("price")
                    );
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving order items: ");
            e.printStackTrace();
        }
        return items;
    }



    /**
     * updates the details of an existing order in the database.
     *
     * @param order the order to update
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public void updateOrder(Order order) throws SQLException {
        String sql = "UPDATE " + ORDER_TABLE + " SET date = ?, totalPrice = ?, username = ? WHERE orderNumber = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, order.getDate());
            stmt.setDouble(2, order.getTotalPrice());
            stmt.setString(3, order.getUser().getUsername());
            stmt.setString(4, order.getOrderNumber());
            stmt.executeUpdate();


            deleteOrderItems(order.getOrderNumber());
            for (OrderItem item : order.getOrderItems()) {
                addOrderItem(order.getOrderNumber(), item);
            }
            System.out.println("Order updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating order.");
            e.printStackTrace();
        }
    }



    /**
     * deletes all order items for a specific order number from the database.
     *
     * @param orderNumber the order number
     * @throws SQLException if there is an error accessing the database
     */
    private void deleteOrderItems(String orderNumber) throws SQLException {
        String sql = "DELETE FROM " + ORDER_ITEMS_TABLE + " WHERE orderNumber = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, orderNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting order items for order number: " + orderNumber);
            e.printStackTrace();
        }
    }



    /**
     * deletes an order by its order number from the database.
     *
     * @param orderNumber the order number
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public void deleteOrder(String orderNumber) throws SQLException {
        String sql = "DELETE FROM " + ORDER_TABLE + " WHERE orderNumber = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, orderNumber);
            stmt.executeUpdate();
            System.out.println("Order deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting order: ");
            e.printStackTrace();
        }
    }

}



