package com.example.assign02_final_s3988110.models;

/**
 * singleton class for managing the current user instance.
 * this class ensures only one instance of the user is created and provides global access to it.
 */
public class UserSingleton {

    private static UserSingleton instance;
    private User user;



    /**
     * private constructor to prevent instantiation.
     */
    private UserSingleton() {}



    /**
     * returns the single instance of the UserSingleton class.
     *
     * @return the single instance of the UserSingleton class
     */
    public static UserSingleton getInstance() {
        if (instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }



    /**
     * returns the current user.
     *
     * @return the current user
     */
    public User getUser() {
        return user;
    }



    /**
     * sets the current user.
     *
     * @param user the user to set as the current user
     */
    public void setUser(User user) {
        this.user = user;
    }
}
