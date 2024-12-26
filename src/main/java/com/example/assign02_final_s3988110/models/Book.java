package com.example.assign02_final_s3988110.models;

import javafx.beans.property.*;



/**
 * model class representing a Book.
 * this class encapsulates the details of a book including title, author, physical copies, price, and sold copies.
 */
public class Book {

    private final StringProperty title;
    private final StringProperty author;
    private final IntegerProperty physicalCopies;
    private DoubleProperty price;
    private DoubleProperty totalPrice;
    private IntegerProperty soldCopies;



    /**
     * Constructor initializing all fields of the Book class.
     *
     * @param title the title of the book
     * @param author the author of the book
     * @param physicalCopies the number of physical copies available
     * @param price the price of the book
     * @param soldCopies the number of copies sold
     */
    public Book(String title, String author, int physicalCopies, double price, int soldCopies) {
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.physicalCopies = new SimpleIntegerProperty(physicalCopies);
        this.price = new SimpleDoubleProperty(price);
        this.soldCopies = new SimpleIntegerProperty(soldCopies);
        this.totalPrice = new SimpleDoubleProperty(price * physicalCopies);
    }



    /**
     * Constructor initializing selected fields of the Book class.
     *
     * @param title the title of the book
     * @param author the author of the book
     * @param physicalCopies the number of physical copies available
     */
    public Book(String title, String author, int physicalCopies) {
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.physicalCopies = new SimpleIntegerProperty(physicalCopies);
    }



    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public int getPhysicalCopies() {
        return physicalCopies.get();
    }

    public IntegerProperty physicalCopiesProperty() {
        return physicalCopies;
    }

    public void setPhysicalCopies(int physicalCopies) {
        this.physicalCopies.set(physicalCopies);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public int getSoldCopies() {
        return soldCopies.get();
    }

    public IntegerProperty soldCopiesProperty() {
        return soldCopies;
    }

    public void setSoldCopies(int soldCopies) {
        this.soldCopies.set(soldCopies);
    }

    public double getTotalPrice() {
        return totalPrice.get();
    }

    public DoubleProperty totalPriceProperty() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice.set(totalPrice);
    }



    @Override
    public String toString() {
        return "Book{" +
                "title='" + title.get() + '\'' +
                ", author='" + author.get() + '\'' +
                ", physicalCopies=" + physicalCopies.get() +
                ", price=" + (price != null ? price.get() : "N/A") +
                ", soldCopies=" + (soldCopies != null ? soldCopies.get() : "N/A") +
                ", totalPrice=" + (totalPrice != null ? totalPrice.get() : "N/A") +
                '}';
    }
}
