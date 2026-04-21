package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class LocalizationServiceTest {

    @BeforeEach
    void setup() {
        System.setProperty("DB_URL", "jdbc:h2:mem:testdb_localization;MODE=MySQL;DB_CLOSE_DELAY=-1");
        System.setProperty("DB_USER", "sa");
        System.setProperty("DB_PASSWORD", "");
    }

    @Test
    void testGet_returnsFromDBAndCaches() throws Exception {

        try (Connection conn = db.DBConnection.getConnection();
             Statement st = conn.createStatement()) {

            st.execute("CREATE TABLE content(id INT AUTO_INCREMENT PRIMARY KEY, content_key VARCHAR(100));");
            st.execute("CREATE TABLE languages(id INT AUTO_INCREMENT PRIMARY KEY, code VARCHAR(10), country VARCHAR(10));");
            st.execute("CREATE TABLE translations(id INT AUTO_INCREMENT PRIMARY KEY, content_id INT, language_id INT, translated_text VARCHAR(200));");

            st.execute("INSERT INTO content(content_key) VALUES('app.title');");
            st.execute("INSERT INTO languages(id, code, country) VALUES(1,'en','US');");
            st.execute("INSERT INTO translations(content_id, language_id, translated_text) VALUES(1,1,'My App');");
        }

        try (Connection conn = db.DBConnection.getConnection()) {

            LocalizationService ls = new LocalizationService(conn);

            // FIXED: use languageId instead of Locale
            String text = ls.get("app.title", 1);
            assertEquals("My App", text);

            String text2 = ls.get("app.title", 1);
            assertEquals("My App", text2);
        }
    }
}