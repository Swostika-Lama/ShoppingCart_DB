package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartRecordTest {

    @Test
    void testSettersAndGetters() {
        CartRecord store = new CartRecord();

        store.setTotalItems(5);
        store.setTotalCost(19.99);
        store.setLanguage("en");

        assertEquals(5, store.getTotalItems());
        assertEquals(19.99, store.getTotalCost());
        assertEquals("en", store.getLanguage());
    }

    @Test
    void testDefaultValues() {
        CartRecord store = new CartRecord();

        assertEquals(0, store.getTotalItems());
        assertEquals(0.0, store.getTotalCost());
        assertNull(store.getLanguage());
    }
}
