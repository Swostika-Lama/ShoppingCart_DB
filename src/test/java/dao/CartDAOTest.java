package dao;

import db.DBConnection;
import model.CartRecord;
import org.junit.jupiter.api.Test;
import test.util.TestDbHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CartDAOTest {

    @Test
    void testInsertCart_returnsGeneratedId_andPersists() throws Exception {

        TestDbHelper.prepareUniqueDb();

        // ✅ FIXED TABLE SCHEMA (language_id instead of language)
        try (Connection conn = TestDbHelper.getConnection();
             Statement st = conn.createStatement()) {

            st.execute("""
                CREATE TABLE cart_records(
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    total_items INT,
                    total_cost DOUBLE,
                    language_id INT
                );
            """);
        }

        CartRecord cart = new CartRecord();
        cart.setTotalItems(2);
        cart.setTotalCost(100.0);
        cart.setLanguageId(1); // ✅ FIXED

        CartDAO dao = new CartDAO();
        int id = dao.insertCart(cart);

        assertTrue(id > 0, "Expected generated id > 0");

        try (Connection conn = TestDbHelper.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT id, total_items, total_cost, language_id FROM cart_records WHERE id = " + id
             )) {

            assertTrue(rs.next());

            assertEquals(id, rs.getInt(1));
            assertEquals(2, rs.getInt(2));
            assertEquals(100.0, rs.getDouble(3));
            assertEquals(1, rs.getInt(4)); // language_id
        }
    }

    @Test
    void testInsertCart_returnsMinusOne_onSqlError() {

        TestDbHelper.prepareUniqueDb();

        CartRecord cart = new CartRecord();
        cart.setTotalItems(1);
        cart.setTotalCost(10.0);
        cart.setLanguageId(1); // ✅ FIXED

        CartDAO dao = new CartDAO();
        int id = dao.insertCart(cart);

        assertEquals(-1, id, "Expected -1 when insert fails due to missing table");
    }

    @Test
    void testInsertCart_handlesUnexpectedException() throws Exception {

        CartRecord cart = new CartRecord();
        cart.setTotalItems(1);
        cart.setTotalCost(10.0);
        cart.setLanguageId(1);
        try (var mocked = org.mockito.Mockito.mockStatic(DBConnection.class)) {

            Connection fakeConn = mock(Connection.class);

            when(fakeConn.prepareStatement(anyString(), anyInt()))
                    .thenThrow(new RuntimeException("boom"));

            mocked.when(DBConnection::getConnection).thenReturn(fakeConn);

            CartDAO dao = new CartDAO();
            int id = dao.insertCart(cart);

            assertEquals(-1, id);
        }
    }
}