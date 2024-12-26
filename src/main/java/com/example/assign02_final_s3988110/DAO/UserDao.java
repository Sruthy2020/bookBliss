package com.example.assign02_final_s3988110.DAO;

import com.example.assign02_final_s3988110.models.User;

import java.sql.SQLException;
import java.util.List;

/**
 * A data access object (DAO) is a pattern that provides an abstract interface 
 * to a database or other persistence mechanism. 
 * the DAO maps application calls to the persistence layer and provides some specific data operations 
 * without exposing details of the database. 
 */
public interface UserDao{

	/**
	 * sets up the table for user-related operations.
	 *
	 * @throws SQLException if there is an error accessing the database
	 */
	void setup() throws SQLException;



	/**
	 * retrieves a list of all users from the database.
	 *
	 * @return a list of all users
	 * @throws SQLException if there is an error accessing the database
	 */
    List<User> getAllUsers() throws SQLException;



	/**
	 * Retrieves a user by their username and password from the database.
	 *
	 * @param username the username of the user
	 * @param password the password of the user
	 * @return the user with the specified username and password
	 * @throws SQLException if there is an error accessing the database
	 */
	User getUser(String username, String password) throws SQLException;



	/**
	 * Creates a new user in the database.
	 *
	 * @param username the username of the new user
	 * @param password the password of the new user
	 * @param firstName the first name of the new user
	 * @param lastName the last name of the new user
	 * @return the created user
	 * @throws SQLException if there is an error accessing the database
	 */
    User createUser(String username, String password, String firstName, String lastName) throws SQLException;



	/**
	 * Retrieves a user by their username from the database.
	 *
	 * @param username the username of the user
	 * @return the user with the specified username
	 * @throws SQLException if there is an error accessing the database
	 */
    User getUserByUsername(String username) throws SQLException;



	/**
	 * deletes a user by their username from the database.
	 *
	 * @param username the username of the user
	 * @throws SQLException if there is an error accessing the database
	 */
	void deleteUser(String username) throws SQLException;
//	public void dropUsersTable() throws SQLException;
}
