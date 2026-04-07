package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TranslationDAO {

    private final Connection conn;

    public TranslationDAO(Connection conn) {
        this.conn = conn;
    }

    public String findTranslation(String key, String lang, String country) {
        try {
            String sql = """
                SELECT t.translated_text
                FROM translations t
                JOIN content c ON t.content_id = c.id
                JOIN languages l ON t.language_id = l.id
                WHERE c.content_key = ? 
                AND l.code = ? 
                AND l.country = ?
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, key);
            stmt.setString(2, lang);
            stmt.setString(3, country);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("translated_text");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // fallback handled in LocalizationService
    }
}
