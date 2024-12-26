package org.example.assign02_final_s3988110.DAO;

import com.example.assign02_final_s3988110.DAO.BookDaoImpl;
import com.example.assign02_final_s3988110.DAO.Database;
import com.example.assign02_final_s3988110.DAO.OrderDaoImpl;
import com.example.assign02_final_s3988110.DAO.UserDaoImpl;
import com.example.assign02_final_s3988110.models.Order;
import com.example.assign02_final_s3988110.models.OrderItem;
import com.example.assign02_final_s3988110.models.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDaoImplTest {

    private OrderDaoImpl orderDao;
    private UserDaoImpl userDao;
    private BookDaoImpl bookDao;

    private void clearOrdersTable() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM orders");
        }
    }


    @BeforeEach
    void setUp() {
        orderDao = new OrderDaoImpl();
        userDao = new UserDaoImpl();
        bookDao = new BookDaoImpl();
        try {
            userDao.setup();
            bookDao.setup();
            orderDao.setup();
            clearUsersTable();
        } catch (SQLException e) {
            fail("Setup failed: " + e.getMessage());
        }
    }

    private void clearUsersTable() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM users");
        }
    }


    @Test
    void testSetup() {
        try {
            orderDao.setup();
            assertTrue(true, "Order tables setup completed successfully");
        } catch (SQLException e) {
            fail("Setup failed: " + e.getMessage());
        }
    }

    @Test
    void testAddOrder() {
        try {
            User user = new User("testUser");
            userDao.createUser(user.getUsername(), "password", "First", "Last");

            List<OrderItem> items = new ArrayList<>();
            items.add(new OrderItem("Absolute Java", 2, 50.0));
            Order order = new Order("ORD123", "2024-10-26", 100.0, items, user);
            orderDao.addOrder(order);

            Order retrievedOrder = orderDao.getOrderByNumber("ORD123");
            assertNotNull(retrievedOrder, "Order should be added and retrieved");
            assertEquals("ORD123", retrievedOrder.getOrderNumber(), "Order number should match");
        } catch (SQLException e) {
            fail("Failed to add order: " + e.getMessage());
        }
    }

    @Test
    void testGetOrdersByUsername() {
        try {
            User user = new User("testUser");
            userDao.createUser(user.getUsername(), "password", "First", "Last");

            List<OrderItem> items = new ArrayList<>();
            items.add(new OrderItem("Absolute Java", 2, 50.0));
            Order order = new Order("ORD123", "2024-10-26", 100.0, items, user);
            orderDao.addOrder(order);

            List<Order> orders = orderDao.getOrdersByUsername("testUser");
            assertFalse(orders.isEmpty(), "Orders should be found for the user");
            assertEquals("ORD123", orders.get(0).getOrderNumber(), "Order number should match");
        } catch (SQLException e) {
            fail("Failed to retrieve orders by username: " + e.getMessage());
        }
    }

    @Test
    void testGetAllOrders() {
        try {
            User user = new User("testUser");
            userDao.createUser(user.getUsername(), "password", "First", "Last");

            List<OrderItem> items = new ArrayList<>();
            items.add(new OrderItem("Absolute Java", 2, 50.0));
            Order order = new Order("ORD123", "2024-10-26", 100.0, items, user);
            orderDao.addOrder(order);

            List<Order> orders = orderDao.getAllOrders();
            assertFalse(orders.isEmpty(), "Orders should be found");
           } catch (SQLException e) {
            fail("Failed to retrieve all orders: " + e.getMessage());
        }
    }

    @Test
    void testUpdateOrder() {
        try {
            User user = new User("testUser");
            userDao.createUser(user.getUsername(), "password", "First", "Last");

            List<OrderItem> items = new ArrayList<>();
            items.add(new OrderItem("Absolute Java", 2, 50.0));
            Order order = new Order("ORD123", "2024-10-26", 100.0, items, user);
            orderDao.addOrder(order);

            order.setDate("2024-11-01");
            order.setTotalPrice(120.0);
            items.add(new OrderItem("Clean Code", 1, 60.0));
            order.setOrderItems(items);
            orderDao.updateOrder(order);

            Order updatedOrder = orderDao.getOrderByNumber("ORD123");
            assertNotNull(updatedOrder, "Updated order should be retrieved");
            assertEquals("2024-11-01", updatedOrder.getDate(), "Date should be updated");
            assertEquals(120.0, updatedOrder.getTotalPrice(), "Total price should be updated");
        } catch (SQLException e) {
            fail("Failed to update order: " + e.getMessage());
        }
    }

    @Test
    void testDeleteOrder() {
        try {
            User user = new User("testUser");
            userDao.createUser(user.getUsername(), "password", "First", "Last");

            List<OrderItem> items = new ArrayList<>();
            items.add(new OrderItem("Absolute Java", 2, 50.0));
            Order order = new Order("ORD123", "2024-10-26", 100.0, items, user);
            orderDao.addOrder(order);

            orderDao.deleteOrder("ORD123");
            Order deletedOrder = orderDao.getOrderByNumber("ORD123");
            assertNull(deletedOrder, "Order should be deleted");
        } catch (SQLException e) {
            fail("Failed to delete order: " + e.getMessage());
        }
    }
    @AfterEach
    void tearDown() {
        try {
            clearOrdersTable();
            clearUsersTable();
        } catch (SQLException e) {
            fail("Teardown failed: " + e.getMessage());
        }
    }
}
