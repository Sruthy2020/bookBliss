package com.example.assign02_final_s3988110.utils;

import com.example.assign02_final_s3988110.models.Book;
import com.example.assign02_final_s3988110.DAO.BookDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

/**
 * utility class for managing book-related operations.
 * this class provides methods for fetching popular books from the database.
 */
public class BookUtil {
    private final BookDaoImpl bookDao = new BookDaoImpl();



    /**
     *the top 5 popular books from the database.
     *
     * @return an observable list of the top 5 popular books
     * @throws SQLException if there is an error accessing the database
     */
    public ObservableList<Book> getTop5PopularBooks() throws SQLException {
        List<Book> topBooks = bookDao.getTop5PopularBooks();
        return FXCollections.observableArrayList(topBooks);
    }
}
