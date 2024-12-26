package com.example.assign02_final_s3988110.models;

import javafx.beans.property.*;
import java.util.List;

/**
 * model class representing an Order.
 * class encapsulates the details of an order including order number, date, total price, order items, and user.
 */
public class Order {

    private final StringProperty orderNumber;
    private final StringProperty date;
    private final DoubleProperty totalPrice;
    private List<OrderItem> orderItems;
    private User user;



    /**
     * Constructor initializing all fields of the Order class.
     *
     * @param orderNumber the order number
     * @param date the order date
     * @param totalPrice the total price of the order
     * @param orderItems the list of order items
     * @param user the user who placed the order
     */
    public Order(String orderNumber, String date, double totalPrice, List<OrderItem> orderItems, User user) {
        this.orderNumber = new SimpleStringProperty(orderNumber);
        this.date = new SimpleStringProperty(date);
        this.totalPrice = new SimpleDoubleProperty(totalPrice);
        this.orderItems = orderItems;
        this.user = user;
    }



    public StringProperty orderNumberProperty() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber.set(orderNumber);
    }

    public String getOrderNumber() {
        return orderNumber.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getDate() {
        return date.get();
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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    /**
     * calculates the total price of the order based on the order items.
     *
     * @return the total price of the order
     */
    public double calculateTotalPrice() {
        return orderItems.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }
}





