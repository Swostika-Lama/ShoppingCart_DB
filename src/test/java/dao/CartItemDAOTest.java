package dao;

import model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.util.TestDbHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class CartItemDAOTest {

    @BeforeEach
    void setup() {
        TestDbHelper.prepareUniqueDb();
    }

    @Test
    void testInsertItem_executesWithoutException() throws Exception {
        try (Connection conn = TestDbHelper.getConnection(); Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE cart_items(cart_record_id INT, item_number INT, price DOUBLE, quantity INT, subtotal DOUBLE);");
        }

        CartItem item = new CartItem();
        item.setCartRecordId(1);
        item.setItemNumber(1);
        item.setPrice(10.0);
        item.setQuantity(2);
        item.setSubtotal(20.0);

        CartItemDAO dao = new CartItemDAO();
        assertDoesNotThrow(() -> dao.insertItem(item));

        // verify persisted
        try (Connection conn = TestDbHelper.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT cart_record_id, item_number, price, quantity, subtotal FROM cart_items")) {
            assertTrue(rs.next(), "Expected a row in cart_items table");
            assertEquals(1, rs.getInt(1));
            assertEquals(1, rs.getInt(2));
            assertEquals(10.0, rs.getDouble(3));
            assertEquals(2, rs.getInt(4));
            assertEquals(20.0, rs.getDouble(5));
            assertFalse(rs.next(), "Only one row expected");
        }
    }
}
