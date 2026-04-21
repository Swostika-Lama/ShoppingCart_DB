package dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TranslationDAO {

    private static final Logger logger = LoggerFactory.getLogger(TranslationDAO.class);
    private final Connection conn;

    public TranslationDAO(Connection conn) {
        this.conn = conn;
    }

    // ✅ FIXED: use languageId only
    public String findTranslation(String key, int languageId) {

        String sql = """
            SELECT t.translated_text
            FROM translations t
            JOIN content c ON t.content_id = c.id
            WHERE c.content_key = ?
            AND t.language_id = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, key);
            stmt.setInt(2, languageId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("translated_text");
                }
            }

        } catch (Exception e) {
            logger.error("Error fetching translation for key={}, languageId={}",
                    key, languageId, e);
        }

        return null;
    }
}