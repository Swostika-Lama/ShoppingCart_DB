package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartRecordTest {

    @Test
    void testSettersAndGetters() {
        CartRecord record = new CartRecord();

        record.setTotalItems(5);
        record.setTotalCost(19.99);
        record.setLanguage("en");

        assertEquals(5, record.getTotalItems());
        assertEquals(19.99, record.getTotalCost());
        assertEquals("en", record.getLanguage());
    }

    @Test
    void testDefaultValues() {
        CartRecord record = new CartRecord();

        assertEquals(0, record.getTotalItems());
        assertEquals(0.0, record.getTotalCost());
        assertNull(record.getLanguage());
    }
}
