package dao;

import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class TranslationDAOTest {

    private static Connection conn;
    private TranslationDAO dao;

    @BeforeAll
    static void setupDatabase() throws Exception {
        conn = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        try (Statement st = conn.createStatement()) {

            st.execute("""
                CREATE TABLE languages (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    code VARCHAR(10),
                    country VARCHAR(10)
                );
            """);

            st.execute("""
                CREATE TABLE content (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    content_key VARCHAR(255)
                );
            """);

            st.execute("""
                CREATE TABLE translations (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    content_id INT,
                    language_id INT,
                    translated_text VARCHAR(255)
                );
            """);

            st.execute("INSERT INTO languages (code, country) VALUES ('en', 'US');");
            st.execute("INSERT INTO content (content_key) VALUES ('greeting');");
            st.execute("""
                INSERT INTO translations (content_id, language_id, translated_text)
                VALUES (1, 1, 'Hello');
            """);
        }
    }

    @BeforeEach
    void initDAO() {
        dao = new TranslationDAO(conn);
    }

    @Test
    void testFindTranslationReturnsCorrectValue() {
        String result = dao.findTranslation("greeting", "en", "US");
        assertEquals("Hello", result);
    }

    @Test
    void testFindTranslationReturnsNullWhenNotFound() {
        String result = dao.findTranslation("missing.key", "en", "US");
        assertNull(result);
    }
}
