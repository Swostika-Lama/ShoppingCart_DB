package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.util.TestDbHelper;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class TranslationDAOBranchTest {

    @BeforeEach
    void setupDb() {
        TestDbHelper.prepareUniqueDb();
    }

    @Test
    void testFindTranslation_returnsNullWhenLanguageMissing() throws Exception {

        try (Connection conn = TestDbHelper.getConnection();
             Statement st = conn.createStatement()) {

            st.execute("""
                CREATE TABLE content(
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    content_key VARCHAR(255)
                );
            """);

            st.execute("""
                CREATE TABLE translations(
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    content_id INT,
                    language_id INT,
                    translated_text VARCHAR(255)
                );
            """);

            // Only English exists (language_id = 1)
            st.execute("INSERT INTO content(content_key) VALUES('greeting');");
            st.execute("INSERT INTO translations(content_id, language_id, translated_text) VALUES(1, 1, 'Hello');");
        }

        try (Connection conn = TestDbHelper.getConnection()) {

            TranslationDAO dao = new TranslationDAO(conn);

            // Request missing language (e.g. Finnish = 2)
            String res = dao.findTranslation("greeting", 2);

            assertNull(res, "Expected null when requested language is missing");
        }
    }
}