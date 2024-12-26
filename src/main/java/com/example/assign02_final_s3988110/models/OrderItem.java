package com.example.assign02_final_s3988110.models;

import javafx.beans.property.*;

/**
 * model class representing an OrderItem.
 * this class encapsulates the details of an item in an order, including the order number, book title, quantity, price per unit, and total price.
 */
public class OrderItem {
    private StringProperty orderNumber;
    private final StringProperty bookTitle;
    private final IntegerProperty quantity;
    private final DoubleProperty pricePerUnit;
    private final DoubleProperty totalPrice;
    private StringProperty date;




    /**
     * constructor initializing all fields of the OrderItem class.
     *
     * @param orderNumber the order number
     * @param bookTitle the title of the book
     * @param quantity the quantity of the book
     * @param pricePerUnit the price per unit of the book
     */
    public OrderItem(String orderNumber, String bookTitle, int quantity, double pricePerUnit) {
        this.orderNumber = new SimpleStringProperty(orderNumber);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.pricePerUnit = new SimpleDoubleProperty(pricePerUnit);
        this.totalPrice = new SimpleDoubleProperty(pricePerUnit * quantity);
    }



    /**
     * constructor initializing selected fields of the OrderItem class.
     *
     * @param bookTitle the title of the book
     * @param quantity the quantity of the book
     * @param pricePerUnit the price per unit of the book
     */
    public OrderItem(String bookTitle, int quantity, double pricePerUnit) {
        this(null, bookTitle, quantity, pricePerUnit);
    }



    public StringProperty orderNumberProperty() {
        if (orderNumber == null) {
            orderNumber = new SimpleStringProperty(this, "orderNumber");
        }
        return orderNumber;
    }

    public DoubleProperty orderTotalPriceProperty() {
        return totalPrice;
    }

    public void setOrderTotalPrice(double totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public double getOrderTotalPrice() {
        return totalPrice.get();
    }

    public String getOrderNumber() {
        return orderNumberProperty().get();
    }

    public void setOrderNumber(String orderNumber) {
        orderNumberProperty().set(orderNumber);
    }

    public StringProperty bookTitleProperty() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle.set(bookTitle);
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
        this.totalPrice.set(pricePerUnit.get() * quantity);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public DoubleProperty pricePerUnitProperty() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit.set(pricePerUnit);
    }

    public double getPricePerUnit() {
        return pricePerUnit.get();
    }

    public DoubleProperty totalPriceProperty() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public double getTotalPrice() {
        return totalPrice.get();
    }

    public StringProperty dateProperty() {
        if (date == null) {
            date = new SimpleStringProperty(this, "date");
        }
        return date;
    }

    public String getDate() {
        return dateProperty().get();
    }

    public void setDate(String date) {
        dateProperty().set(date);
    }
}
