package com.example.assign02_final_s3988110.utils;

import java.util.regex.Pattern;

/**
 * utility class for validating user-related information.
 * class provides methods to validate usernames, passwords, and check if passwords match.
 */
public class ValidationUtil {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{4,}$");



    /**
     * validates the username.
     *
     * @param username the username to validate
     * @return true if the username is valid, false otherwise
     */
    public static boolean isUsernameValid(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }



    /**
     * validates the password based on specific criteria.
     *
     * @param password the password to validate
     * @return true if the password is valid, false otherwise
     */
    public static boolean isPasswordValid(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }



    /**
     * checks if the password and confirm password match.
     *
     * @param password the password
     * @param confirmPassword the confirm password
     * @return true if the passwords match, false otherwise
     */
    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }
}
