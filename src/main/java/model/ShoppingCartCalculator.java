package model;

public class ShoppingCartCalculator {

    public static double calculateItemTotal(double price, int quantity) {
        if (price < 0 || quantity < 0) {
            throw new IllegalArgumentException("Please enter valid numeric values for price and quantity.");
        }
        return price * quantity;
    }
}