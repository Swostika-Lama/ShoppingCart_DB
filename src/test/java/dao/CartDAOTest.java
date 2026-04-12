package dao;

import model.CartRecord;
import org.junit.jupiter.api.Test;
import test.util.TestDbHelper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


import static org.junit.jupiter.api.Assertions.*;

class CartDAOTest {

    @Test
    void testInsertCart_returnsGeneratedId_andPersists() throws Exception {
        TestDbHelper.prepareUniqueDb();

        try (Connection conn = TestDbHelper.getConnection(); Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE cart_records(id INT AUTO_INCREMENT PRIMARY KEY, total_items INT, total_cost DOUBLE, language VARCHAR(10));");
        }

        CartRecord cart = new CartRecord();
        cart.setTotalItems(2);
        cart.setTotalCost(100.0);
        cart.setLanguage("en");

        CartDAO dao = new CartDAO();
        int id = dao.insertCart(cart);

        // id should be positive (H2 auto_increment starts at 1)
        assertTrue(id > 0, "Expected generated id > 0");

        try (Connection conn = TestDbHelper.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT id, total_items, total_cost, language FROM cart_records WHERE id = " + id)) {
            assertTrue(rs.next());
            assertEquals(id, rs.getInt(1));
            assertEquals(2, rs.getInt(2));
            assertEquals(100.0, rs.getDouble(3));
            assertEquals("en", rs.getString(4));
        }
    }

    @Test
    void testInsertCart_returnsMinusOne_onSqlError() throws Exception {
        TestDbHelper.prepareUniqueDb();

        // Do NOT create cart_records table so insert will fail and method returns -1
        CartRecord cart = new CartRecord();
        cart.setTotalItems(1);
        cart.setTotalCost(10.0);
        cart.setLanguage("en");

        CartDAO dao = new CartDAO();
        int id = dao.insertCart(cart);

        assertEquals(-1, id, "Expected -1 when insert fails due to missing table");
    }
}

