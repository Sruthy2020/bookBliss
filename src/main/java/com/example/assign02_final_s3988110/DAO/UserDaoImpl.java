package com.example.assign02_final_s3988110.DAO;

import com.example.assign02_final_s3988110.models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * implementation of the UserDao interface for managing user-related operations.
 * this class provides methods for setting up the database, fetching users, creating users, updating user details, and more.
 */
public class UserDaoImpl implements UserDao {

	private final String TABLE_NAME = "users";
	public UserDaoImpl() {}

	/**
	 * sets up the table for user-related operations.
	 *
	 * @throws SQLException if there is an error accessing the database
	 */
	@Override
	public void setup() throws SQLException {
		try (Connection connection = Database.getConnection();
			 Statement stmt = connection.createStatement()) {
			String sql = "CREATE TABLE IF NOT EXISTS users ("
					+ "username VARCHAR(10) NOT NULL,"
					+ "password VARCHAR(8) NOT NULL,"
					+ "firstName VARCHAR(50) NOT NULL,"
					+ "lastName VARCHAR(50) NOT NULL,"
					+ "PRIMARY KEY (username))";
			stmt.executeUpdate(sql);
			String insertAdminSQL = "INSERT INTO users (username, password, firstName, lastName) "
					+ "SELECT 'admin', 'reading_admin', 'Admin', 'Admin' "
					+ "WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin')";
			stmt.executeUpdate(insertAdminSQL);
		}
	}



	/**
	 * retrieves a list of all users from the database, excluding the admin user.
	 *
	 * @return a list of all users
	 * @throws SQLException if there is an error accessing the database
	 */
	@Override
	public List<User> getAllUsers() throws SQLException {
		List<User> users = new ArrayList<>();
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username != 'admin'";
		try (Connection connection = Database.getConnection();
			 Statement stmt = connection.createStatement();
			 ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				User user = new User(
						rs.getString("username"),
						rs.getString("password"),
						rs.getString("firstName"),
						rs.getString("lastName"));
				users.add(user);
			}
		}
		return users;
	}



	/**
	 * retrieves a user by their username and password from the database.
	 *
	 * @param username the username of the user
	 * @param password the password of the user
	 * @return the user with the specified username and password, or null if not found
	 * @throws SQLException if there is an error accessing the database
	 */
	@Override
	public User getUser(String username, String password) throws SQLException {
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ? AND password = ?";
		try (Connection connection = Database.getConnection();
			 PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					User user = new User();
					user.setUsername(rs.getString("username"));
					user.setPassword(rs.getString("password"));
					user.setFirstName(rs.getString("firstName"));
					user.setLastName(rs.getString("lastName"));
					return user;
				}
				return null;
			}
		}
	}



	/**
	 * retrieves a user by their username from the database.
	 *
	 * @param username the username of the user
	 * @return the user with the specified username, or null if not found
	 * @throws SQLException if there is an error accessing the database
	 */
	@Override
	public User getUserByUsername(String username) throws SQLException {
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";
		try (Connection connection = Database.getConnection();
			 PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, username);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return new User(
							rs.getString("username"),
							rs.getString("password"),
							rs.getString("firstName"),
							rs.getString("lastName"));
				}
				return null;
			}
		}
	}



	/**
	 * updates the details of an existing user in the database.
	 *
	 * @param username the username of the user
	 * @param firstName the first name of the user
	 * @param lastName the last name of the user
	 * @param password the password of the user
	 * @throws SQLException if there is an error accessing the database
	 */
	public void updateUser(String username, String firstName, String lastName, String password) throws SQLException {
		String sql = "UPDATE " + TABLE_NAME + " SET firstName = ?, lastName = ?, password = ? WHERE username = ?";
		try (Connection connection = Database.getConnection();
			 PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, firstName);
			stmt.setString(2, lastName);
			stmt.setString(3, password);
			stmt.setString(4, username);

			stmt.executeUpdate();
		}
	}



	/**
	 * creates a new user in the database.
	 *
	 * @param username the username of the new user
	 * @param password the password of the new user
	 * @param firstName the first name of the new user
	 * @param lastName the last name of the new user
	 * @return the created user
	 * @throws SQLException if there is an error accessing the database
	 */
	@Override
	public User createUser(String username, String password, String firstName, String lastName) throws SQLException {
		String sql = "INSERT INTO " + TABLE_NAME + " (username, password, firstName, lastName) VALUES (?, ?, ?, ?)";
		try (Connection connection = Database.getConnection();
			 PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setString(3, firstName);
			stmt.setString(4, lastName);
			stmt.executeUpdate();
			return new User(username, password, firstName, lastName);
		}
	}



	/**
	 * deletes a user by their username from the database.
	 *
	 * @param username the username of the user to delete
	 * @throws SQLException if there is an error accessing the database
	 */
	@Override
	public void deleteUser(String username) throws SQLException {
		String sql = "DELETE FROM " + TABLE_NAME + " WHERE username = ?";
		try (Connection connection = Database.getConnection();
			 PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, username);
			stmt.executeUpdate();
		}
	}
}



