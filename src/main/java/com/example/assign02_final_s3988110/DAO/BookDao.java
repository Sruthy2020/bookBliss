package com.example.assign02_final_s3988110.DAO;

import com.example.assign02_final_s3988110.models.Book;
import java.sql.SQLException;
import java.util.List;

/**
 * data Access Object (DAO) interface for managing book-related operations.
 * this interface provides methods for setting up the database, fetching books, updating book details, and more.
 */
public interface BookDao {
    /**
     * sets up the database for book-related operations.
     *
     * @throws SQLException if there is an error accessing the database
     */
    void setup() throws SQLException;



    /**
     * retrieves a list of all books from the database.
     *
     * @return a list of all books
     * @throws SQLException if there is an error accessing the database
     */
    List<Book> getAllBooks() throws SQLException;



    /**
     * retrieves a book by its title from the database.
     *
     * @param title the title of the book
     * @return the book with the specified title
     * @throws SQLException if there is an error accessing the database
     */
    Book getBookByTitle(String title) throws SQLException;



    /**
     * updates the details of a book in the database.
     *
     * @param book the book to update
     * @throws SQLException if there is an error accessing the database
     */
    void updateBook(Book book) throws SQLException;



    /**
     * deletes a book by its title from the database.
     *
     * @param title the title of the book
     * @throws SQLException if there is an error accessing the database
     */
    void deleteBook(String title) throws SQLException;



    /**
     * adds sample books to the database.
     *
     * @throws SQLException if there is an error accessing the database
     */
    void addSampleBooks() throws SQLException;



    /**
     * retrieves the top 5 popular books from the database.
     *
     * @return a list of the top 5 popular books
     * @throws SQLException if there is an error accessing the database
     */
    List<Book> getTop5PopularBooks() throws SQLException;



    /**
     * updates the stock of a book in the database.
     *
     * @param bookTitle the title of the book
     * @param newStock the new stock quantity of the book
     * @throws SQLException if there is an error accessing the database
     */
    void updateBookStock(String bookTitle, int newStock) throws SQLException;
}
