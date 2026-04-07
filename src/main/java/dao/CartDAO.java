package dao;

import db.DBConnection;
import model.CartRecord;

import java.sql.*;

public class CartDAO {

    public int insertCart(CartRecord cart) {
        String sql = "INSERT INTO cart_records (total_items, total_cost, language) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, cart.getTotalItems());
            stmt.setDouble(2, cart.getTotalCost());
            stmt.setString(3, cart.getLanguage());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}