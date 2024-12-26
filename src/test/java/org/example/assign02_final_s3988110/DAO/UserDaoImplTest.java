package org.example.assign02_final_s3988110.DAO;

import com.example.assign02_final_s3988110.DAO.Database;
import com.example.assign02_final_s3988110.DAO.UserDaoImpl;
import com.example.assign02_final_s3988110.models.User;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoImplTest {

    private UserDaoImpl userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl();
        try {
            userDao.setup();
            clearUsersTable();
        } catch (SQLException e) {
            fail("Setup failed: " + e.getMessage());
        }
    }

    private void clearUsersTable() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM users");
            stmt.executeUpdate("INSERT INTO users (username, password, firstName, lastName) VALUES ('admin', 'reading_admin', 'Admin', 'Admin')");
        }
    }

    @Test
    void testCreateUser() {
        try {
            User user = userDao.createUser("testUser", "password", "First", "Last");
            assertNotNull(user, "User should be created");
            assertEquals("testUser", user.getUsername(), "Username should match");
        } catch (SQLException e) {
            fail("Failed to create user: " + e.getMessage());
        }
    }

    @Test
    void testGetUserByUsername() {
        try {
            userDao.createUser("testUser", "password", "First", "Last");
            User user = userDao.getUserByUsername("testUser");
            assertNotNull(user, "User should be found");
            assertEquals("First", user.getFirstName(), "First name should match");
        } catch (SQLException e) {
            fail("Failed to retrieve user: " + e.getMessage());
        }
    }

    @Test
    void testUpdateUser() {
        try {
            userDao.createUser("testUser", "password", "First", "Last");
            userDao.updateUser("testUser", "NewFirst", "NewLast", "newPassword");

            User updatedUser = userDao.getUserByUsername("testUser");
            assertNotNull(updatedUser, "Updated user should be found");
            assertEquals("NewFirst", updatedUser.getFirstName(), "First name should be updated");
        } catch (SQLException e) {
            fail("Failed to update user: " + e.getMessage());
        }
    }

    @Test
    void testDeleteUser() {
        try {
            userDao.createUser("testUser", "password", "First", "Last");
            userDao.deleteUser("testUser");

            User user = userDao.getUserByUsername("testUser");
            assertNull(user, "User should be deleted");
        } catch (SQLException e) {
            fail("Failed to delete user: " + e.getMessage());
        }
    }

    @Test
    void testGetAllUsers() {
        try {
            userDao.createUser("testUser1", "password1", "First1", "Last1");
            userDao.createUser("testUser2", "password2", "First2", "Last2");

            List<User> users = userDao.getAllUsers();
            assertEquals(2, users.size(), "There should be 2 users in the database");
        } catch (SQLException e) {
            fail("Failed to retrieve all users: " + e.getMessage());
        }
    }
}
