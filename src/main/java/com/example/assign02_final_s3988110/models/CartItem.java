package com.example.assign02_final_s3988110.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;


/**
 * model class representing a CartItem.
 * class encapsulates the details of an item in the shopping cart, including the book, quantity, and total price.
 */
public class CartItem {
    private final Book book;
    private final IntegerProperty quantity;
    private final DoubleProperty totalPrice;



    /**
     * Constructor initializing all fields of the CartItem class.
     *
     * @param book the book in the cart item
     * @param quantity the quantity of the book
     */
    public CartItem(Book book, int quantity) {
        this.book = book;
        this.quantity = new SimpleIntegerProperty(quantity);
        this.totalPrice = new SimpleDoubleProperty(book.getPrice() * quantity);
    }



    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
        this.totalPrice.set(this.book.getPrice() * quantity);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice.get();
    }

    public DoubleProperty totalPriceProperty() {
        return totalPrice;
    }


    /**
     * adds the specified quantity to the current quantity of the cart item.
     *
     * @param additionalQuantity the additional quantity to add
     */
    public void addQuantity(int additionalQuantity) {
        setQuantity(getQuantity() + additionalQuantity);
    }



    /**
     * removes the specified quantity from the current quantity of the cart item.
     * ensures the quantity does not go below zero.
     *
     * @param quantityToRemove the quantity to remove
     */
    public void removeQuantity(int quantityToRemove) {
        int newQuantity = getQuantity() - quantityToRemove;
        setQuantity(Math.max(newQuantity, 0));
    }



    /**
     * static factory method to create a new CartItem instance.
     *
     * @param book the book in the cart item
     * @param quantity the quantity of the book
     * @return a new CartItem instance
     */
    public static CartItem of(Book book, int quantity) {
        return new CartItem(book, quantity);
    }
}
