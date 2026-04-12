package model;

public class CartItem {
    private int cartRecordId;
    private int itemNumber;
    private double price;
    private int quantity;
    private double subtotal;

    public int getCartRecordId() { return cartRecordId; }
    public void setCartRecordId(int cartRecordId) { this.cartRecordId = cartRecordId; }

    public int getItemNumber() { return itemNumber; }
    public void setItemNumber(int itemNumber) { this.itemNumber = itemNumber; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}