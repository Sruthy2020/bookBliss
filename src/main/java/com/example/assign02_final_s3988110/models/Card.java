package com.example.assign02_final_s3988110.models;


/**
 * model class representing a Card.
 * class encapsulates the details of a credit card including cardholder name, card number, CVV, and expiry date.
 */
public class Card {

    private String cardHolderName;
    private String cardNumber;
    private String cvv;
    private String expiryDate;



    /**
     * constructor initializing all fields of the Card class.
     *
     * @param cardHolderName the name of the cardholder
     * @param cardNumber the card number
     * @param cvv the card's CVV
     * @param expiryDate the card's expiry date
     */
    public Card(String cardHolderName, String cardNumber, String cvv, String expiryDate) {
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }



    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
