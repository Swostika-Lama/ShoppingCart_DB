package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartCalculatorTest {

    @Test
    void testCalculateItemTotal_ValidInput() {
        double result = ShoppingCartCalculator.calculateItemTotal(10.0, 3);
        assertEquals(30.0, result);
    }

    @Test
    void testCalculateItemTotal_ZeroValues() {
        double result = ShoppingCartCalculator.calculateItemTotal(0.0, 5);
        assertEquals(0.0, result);
    }

    @Test
    void testCalculateItemTotal_NegativePrice() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ShoppingCartCalculator.calculateItemTotal(-10.0, 2);
        });

        assertEquals("Please enter valid numeric values for price and quantity.", exception.getMessage());
    }

    @Test
    void testCalculateItemTotal_NegativeQuantity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ShoppingCartCalculator.calculateItemTotal(10.0, -2);
        });

        assertEquals("Please enter valid numeric values for price and quantity.", exception.getMessage());
    }
}