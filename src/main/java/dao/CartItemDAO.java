package dao;

import db.DBConnection;
import model.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CartItemDAO {

    public void insertItem(CartItem item) {
        String sql = "INSERT INTO cart_items (cart_record_id, item_number, price, quantity, subtotal) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getCartRecordId());
            stmt.setInt(2, item.getItemNumber());
            stmt.setDouble(3, item.getPrice());
            stmt.setInt(4, item.getQuantity());
            stmt.setDouble(5, item.getSubtotal());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}