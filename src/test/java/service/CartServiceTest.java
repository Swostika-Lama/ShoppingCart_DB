package service;

import model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {

    @BeforeEach
    void setup() {
        String dbName = "testdb_" + System.nanoTime();
        System.setProperty("DB_URL", "jdbc:h2:mem:" + dbName + ";MODE=MySQL");
        System.setProperty("DB_USER", "sa");
        System.setProperty("DB_PASSWORD", "");
    }

    @Test
    void testSaveCart_callsDAOs() throws Exception {

        try (Connection conn = db.DBConnection.getConnection();
             Statement st = conn.createStatement()) {

            st.execute("""
                CREATE TABLE cart_records(
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    total_items INT,
                    total_cost DOUBLE,
                    language_id INT
                );
            """);

            st.execute("""
                CREATE TABLE cart_items(
                    cart_record_id INT,
                    item_number INT,
                    price DOUBLE,
                    quantity INT,
                    subtotal DOUBLE
                );
            """);
        }

        CartService svc = new CartService();

        CartItem item = new CartItem();
        item.setItemNumber(1);
        item.setPrice(10.0);
        item.setQuantity(2);
        item.setSubtotal(20.0);

        // FIXED: use languageId instead of "en"
        assertDoesNotThrow(() -> svc.saveCart(List.of(item), 1));
    }
}