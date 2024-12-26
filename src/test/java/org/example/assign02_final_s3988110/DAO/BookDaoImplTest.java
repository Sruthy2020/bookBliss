package org.example.assign02_final_s3988110.DAO;

import com.example.assign02_final_s3988110.DAO.BookDaoImpl;
import com.example.assign02_final_s3988110.models.Book;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookDaoImplTest {

    private BookDaoImpl bookDao;

    @BeforeEach
    void setUp() {
        bookDao = new BookDaoImpl();
        try {
            bookDao.setup();
        } catch (SQLException e) {
            fail("Setup failed: " + e.getMessage());
        }
    }

    @Test
    void testAddSampleBooks() {
        try {
            List<Book> books = bookDao.getAllBooks();
            assertFalse(books.isEmpty(), "Books should be added to the database");
        } catch (SQLException e) {
            fail("Failed to retrieve books: " + e.getMessage());
        }
    }

    @Test
    void testGetBookByTitle() {
        try {
            Book book = bookDao.getBookByTitle("Absolute Java");
            assertNotNull(book, "Book should be found");
            assertEquals("Savitch", book.getAuthor(), "Author should match");
        } catch (SQLException e) {
            fail("Failed to retrieve book: " + e.getMessage());
        }
    }

    @Test
    void testUpdateBook() {
        try {
            Book book = bookDao.getBookByTitle("Absolute Java");
            assertNotNull(book, "Book should be found");

            book.setPhysicalCopies(20);
            bookDao.updateBook(book);

            Book updatedBook = bookDao.getBookByTitle("Absolute Java");
            assertNotNull(updatedBook, "Updated book should be found");
            assertEquals(20, updatedBook.getPhysicalCopies(), "Physical copies should be updated");

        } catch (SQLException e) {
            fail("Failed to update book: " + e.getMessage());
        }
    }

    @Test
    void testDeleteBook() {
        try {
            bookDao.deleteBook("Absolute Java");
            Book book = bookDao.getBookByTitle("Absolute Java");
            assertNull(book, "Book should be deleted");
        } catch (SQLException e) {
            fail("Failed to delete book: " + e.getMessage());
        }
    }

    @Test
    void testGetTop5PopularBooks() {
        try {
            List<Book> topBooks = bookDao.getTop5PopularBooks();
            assertEquals(5, topBooks.size(), "Should return 5 books");
        } catch (SQLException e) {
            fail("Failed to retrieve top 5 books: " + e.getMessage());
        }
    }
}


