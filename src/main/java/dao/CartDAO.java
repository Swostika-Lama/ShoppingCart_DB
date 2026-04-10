package dao;

import db.DBConnection;
import model.CartRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class CartDAO {

    private static final Logger logger = LoggerFactory.getLogger(CartDAO.class);

    public int insertCart(CartRecord cart) {
        String sql = "INSERT INTO cart_records (total_items, total_cost, language) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, cart.getTotalItems());
            stmt.setDouble(2, cart.getTotalCost());
            stmt.setString(3, cart.getLanguage());

            int rows = stmt.executeUpdate();
            logger.info("Inserted {} row(s) into cart_records", rows);

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    logger.info("Generated cart ID: {}", id);
                    return id;
                }
            }

        } catch (SQLException e) {
            logger.error("SQL error while inserting cart record: {}", cart, e);
        } catch (Exception e) {
            logger.error("Unexpected error while inserting cart record: {}", cart, e);
        }

        return -1;
    }
}
