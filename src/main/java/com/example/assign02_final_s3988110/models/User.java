package com.example.assign02_final_s3988110.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * model class representing a User.
 * this class encapsulates the details of a user including first name, last name, username, and password.
 */
public class User {
    private StringProperty firstName;
    private StringProperty lastName;
    private final StringProperty username;
    private StringProperty password;




    /**
     * constructor initializing all fields of the User class.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     */
    public User(String username, String password, String firstName, String lastName) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
    }



    /**
     * constructor initializing the username and password fields of the User class.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    public User(String username, String password) {
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
    }



    /**
     * constructor initializing the username, first name, and last name fields of the User class.
     *
     * @param username the username of the user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     */
    public User(String username, String firstName, String lastName) {
        this.username = new SimpleStringProperty(username);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
    }



    /**
     * dfault constructor initializing all fields of the User class.
     */
    public User() {
        this.username = new SimpleStringProperty();
        this.firstName = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
    }



    /**
     * constructor initializing the username field of the User class.
     *
     * @param username the username of the user
     */
    public User(String username) {
        this.username = new SimpleStringProperty(username);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getPassword() {
        return password.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }



    /**
     * authenticates the user based on the provided username and password.
     *
     * @param inputUsername the input username
     * @param inputPassword the input password
     * @return true if the username and password match, false otherwise
     */
    public boolean authenticate(String inputUsername, String inputPassword) {
        return this.username.get().equals(inputUsername) && this.password.get().equals(inputPassword);
    }
}
