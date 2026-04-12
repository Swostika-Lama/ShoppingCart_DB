package dao;

import model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class CartItemDAOTest {

    @BeforeEach
    void setup() {
        String dbName = "testdb_" + System.nanoTime();
        System.setProperty("DB_URL", "jdbc:h2:mem:" + dbName + ";MODE=MySQL");
        System.setProperty("DB_USER", "sa");
        System.setProperty("DB_PASSWORD", "");
    }

    @Test
    void testInsertItem_executesWithoutException() throws Exception {
        try (Connection conn = db.DBConnection.getConnection(); Statement st = conn.createStatement()) {
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
    }
}
