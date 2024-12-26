package com.example.assign02_final_s3988110.DAO;

import com.example.assign02_final_s3988110.models.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * implementation of the BookDao interface for managing book-related operations.
 * this class provides methods for setting up the database, fetching books, updating book details, and more.
 */
public class BookDaoImpl implements BookDao {
    private final String TABLE_NAME = "books";

    public BookDaoImpl() {}

    /**
     * sets up the table if it does not exist.
     *
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public void setup() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + "title VARCHAR(100) NOT NULL,"
                    + "author VARCHAR(100) NOT NULL,"
                    + "physicalCopies INT NOT NULL,"
                    + "price DOUBLE NOT NULL,"
                    + "soldCopies INT NOT NULL,"
                    + "PRIMARY KEY (title))";
            stmt.executeUpdate(sql);
            addSampleBooks();
            System.out.println("Table '" + TABLE_NAME + "' has been created or already exists.");
        } catch (SQLException e) {
            System.err.println("Error during table creation: ");
            e.printStackTrace();
        }
    }



    /**
     * inserts sample books into the database.
     *
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public void addSampleBooks() throws SQLException {
        String sql = "INSERT OR IGNORE INTO " + TABLE_NAME + " (title, author, physicalCopies, price, soldCopies) VALUES "
                + "('Absolute Java', 'Savitch', 10, 50, 142),"
                + "('JAVA: How to Program', 'Deitel and Deitel', 100, 70, 475),"
                + "('Computing Concepts with JAVA 8 Essentials', 'Horstman', 500, 89, 60),"
                + "('Java Software Solutions', 'Lewis and Loftus', 500, 99, 12),"
                + "('Java Program Design', 'Cohoon and Davidson', 2, 29, 86),"
                + "('Clean Code', 'Robert Martin', 100, 45, 300),"
                + "('Gray Hat C#', 'Brandon Perry', 300, 68, 178),"
                + "('Python Basics', 'David Amos', 1000, 49, 79),"
                + "('Bayesian Statistics The Fun Way', 'Will Kurt', 600, 42, 155)";
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement()) {
            System.out.println("Inserting sample books...");
            stmt.executeUpdate(sql);
            System.out.println("Sample books inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error during inserting sample books: ");
            e.printStackTrace();
        }
    }



    /**
     * retrieves a list of all books from the database.
     *
     * @return a list of all books
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("physicalCopies"),
                        rs.getDouble("price"),
                        rs.getInt("soldCopies")
                );
                books.add(book);
            }
        }
        return books;
    }



    /**
     * retrieves a book by its title from the database.
     *
     * @param title the title of the book
     * @return the book with the specified title, or null if not found
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public Book getBookByTitle(String title) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE title = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getInt("physicalCopies"),
                            rs.getDouble("price"),
                            rs.getInt("soldCopies")
                    );
                }
            }
        }
        return null;
    }



    /**
     * updates the details of an existing book in the database.
     *
     * @param book the book to update
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public void updateBook(Book book) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET author = ?, physicalCopies = ?, price = ?, soldCopies = ? WHERE title = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getAuthor());
            stmt.setInt(2, book.getPhysicalCopies());
            stmt.setDouble(3, book.getPrice());
            stmt.setInt(4, book.getSoldCopies());
            stmt.setString(5, book.getTitle());
            stmt.executeUpdate();
            System.out.println("Book '" + book.getTitle() + "' updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating book: ");
            e.printStackTrace();
        }
    }



    /**
     * deletes a book by its title from the database.
     *
     * @param title the title of the book
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public void deleteBook(String title) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE title = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.executeUpdate();
            System.out.println("Book '" + title + "' deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting book: ");
            e.printStackTrace();
        }
    }



    /**
     * retrieves the top 5 popular books from the database based on sold copies.
     *
     * @return a list of the top 5 popular books
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public List<Book> getTop5PopularBooks() throws SQLException {
        List<Book> topBooks = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY soldCopies DESC LIMIT 5";
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("physicalCopies"),
                        rs.getDouble("price"),
                        rs.getInt("soldCopies")
                );
                topBooks.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching top 5 popular books: ");
            e.printStackTrace();
        }
        return topBooks;
    }



    /**
     * updates the stock of a book in the database.
     *
     * @param bookTitle the title of the book
     * @param newStock the new stock quantity of the book
     * @throws SQLException if there is an error accessing the database
     */
    @Override
    public void updateBookStock(String bookTitle, int newStock) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET physicalCopies = ? WHERE title = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newStock);
            stmt.setString(2, bookTitle);
            stmt.executeUpdate();
            System.out.println("Stock updated successfully for book: " + bookTitle);
        } catch (SQLException e) {
            System.err.println("Error updating stock: ");
            e.printStackTrace();
        }
    }
}
