package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    @Test
    void testSettersAndGetters() {
        CartItem item = new CartItem();

        item.setCartRecordId(10);
        item.setItemNumber(3);
        item.setPrice(5.50);
        item.setQuantity(4);
        item.setSubtotal(22.00);

        assertEquals(10, item.getCartRecordId());
        assertEquals(3, item.getItemNumber());
        assertEquals(5.50, item.getPrice());
        assertEquals(4, item.getQuantity());
        assertEquals(22.00, item.getSubtotal());
    }

    @Test
    void testDefaultValues() {
        CartItem item = new CartItem();

        assertEquals(0, item.getCartRecordId());
        assertEquals(0, item.getItemNumber());
        assertEquals(0.0, item.getPrice());
        assertEquals(0, item.getQuantity());
        assertEquals(0.0, item.getSubtotal());
    }
}
