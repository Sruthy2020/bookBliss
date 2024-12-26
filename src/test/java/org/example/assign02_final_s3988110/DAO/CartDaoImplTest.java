package org.example.assign02_final_s3988110.DAO;

import com.example.assign02_final_s3988110.DAO.BookDaoImpl;
import com.example.assign02_final_s3988110.DAO.CartDaoImpl;
import com.example.assign02_final_s3988110.models.Book;
import com.example.assign02_final_s3988110.models.CartItem;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartDaoImplTest {

    private CartDaoImpl cartDao;
    private BookDaoImpl bookDao;

    @BeforeEach
    void setUp() {
        cartDao = new CartDaoImpl();
        bookDao = new BookDaoImpl();
        try {
            bookDao.setup();  // Ensure books table and sample books are set up
            cartDao.setupCartTable();
            cartDao.clearCart("testUser");  // Clear the cart before each test
        } catch (SQLException e) {
            fail("Setup failed: " + e.getMessage());
        }
    }

    @Test
    void testSetupCartTable() {
        try {
            cartDao.setupCartTable();
            assertTrue(true, "Cart table setup completed successfully");
        } catch (SQLException e) {
            fail("Setup failed: " + e.getMessage());
        }
    }

    @Test
    void testAddOrUpdateCartItem() {
        try {
            Book book = bookDao.getBookByTitle("Absolute Java");
            CartItem cartItem = new CartItem(book, 1);
            cartDao.addOrUpdateCartItem("testUser", cartItem);
            List<CartItem> cartItems = cartDao.getCartItemsByUsername("testUser");
            assertEquals(1, cartItems.size(), "Cart should have one item");
            assertEquals("Absolute Java", cartItems.get(0).getBook().getTitle(), "Cart item should match");
        } catch (SQLException e) {
            fail("Failed to add or update cart item: " + e.getMessage());
        }
    }

    @Test
    void testGetCartItemsByUsername() {
        try {
            List<CartItem> cartItems = cartDao.getCartItemsByUsername("testUser");
            assertNotNull(cartItems, "Cart items should not be null");
            assertTrue(cartItems.isEmpty(), "Cart should be empty initially");

            Book book = bookDao.getBookByTitle("Absolute Java");
            CartItem cartItem = new CartItem(book, 1);
            cartDao.addOrUpdateCartItem("testUser", cartItem);

            cartItems = cartDao.getCartItemsByUsername("testUser");
            assertFalse(cartItems.isEmpty(), "Cart should have items after adding");
        } catch (SQLException e) {
            fail("Failed to retrieve cart items: " + e.getMessage());
        }
    }

    @Test
    void testClearCart() {
        try {
            Book book = bookDao.getBookByTitle("Absolute Java");
            CartItem cartItem = new CartItem(book, 1);
            cartDao.addOrUpdateCartItem("testUser", cartItem);

            cartDao.clearCart("testUser");
            List<CartItem> cartItems = cartDao.getCartItemsByUsername("testUser");
            assertTrue(cartItems.isEmpty(), "Cart should be cleared");
        } catch (SQLException e) {
            fail("Failed to clear cart: " + e.getMessage());
        }
    }

    @Test
    void testDeleteCartItem() {
        try {
            Book book = bookDao.getBookByTitle("Absolute Java");
            CartItem cartItem = new CartItem(book, 1);
            cartDao.addOrUpdateCartItem("testUser", cartItem);

            cartDao.deleteCartItem("testUser", "Absolute Java");
            List<CartItem> cartItems = cartDao.getCartItemsByUsername("testUser");
            assertTrue(cartItems.isEmpty(), "Cart item should be deleted");
        } catch (SQLException e) {
            fail("Failed to delete cart item: " + e.getMessage());
        }
    }
}
