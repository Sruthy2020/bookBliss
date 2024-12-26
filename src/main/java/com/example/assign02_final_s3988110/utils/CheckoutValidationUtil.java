package com.example.assign02_final_s3988110.utils;

import java.time.LocalDateTime;

/**
 * utility class for validating checkout-related information.
 * this class provides methods to validate card number, expiry date, and CVV.
 */
public class CheckoutValidationUtil {


    /**
     * validates the card number.
     *
     * @param cardNumber the card number to validate
     * @return true if the card number is valid, false otherwise
     */
    public static boolean validateCardNumber(String cardNumber) {
        return cardNumber.matches("\\d{16}");
    }



    /**
     * validates the expiry date.
     *
     * @param expiryDate the expiry date to validate in the format MM/YY
     * @return true if the expiry date is valid and in the future, false otherwise
     */
    public static boolean validateExpiryDate(String expiryDate) {
        try {
            String[] parts = expiryDate.split("/");
            if (parts.length != 2) return false;
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt("20" + parts[1]);
            if (month < 1 || month > 12) return false;
            LocalDateTime expiry = LocalDateTime.of(year, month, 1, 0, 0);
            return expiry.isAfter(LocalDateTime.now());
        } catch (Exception e) {
            return false;
        }
    }



    /**
     * validates the CVV.
     *
     * @param cvv the CVV to validate
     * @return true if the CVV is valid, false otherwise
     */
    public static boolean validateCVV(String cvv) {
        return cvv.matches("\\d{3}");
    }
}
