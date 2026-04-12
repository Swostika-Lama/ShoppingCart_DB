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
        try (Connection conn = TestDbHelper.getConnection(); Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE languages(id INT AUTO_INCREMENT PRIMARY KEY, code VARCHAR(10), country VARCHAR(10));");
            st.execute("CREATE TABLE content(id INT AUTO_INCREMENT PRIMARY KEY, content_key VARCHAR(255));");
            st.execute("CREATE TABLE translations(id INT AUTO_INCREMENT PRIMARY KEY, content_id INT, language_id INT, translated_text VARCHAR(255));");

            // insert content and a french language/translation only
            st.execute("INSERT INTO content(content_key) VALUES('greeting');");
            st.execute("INSERT INTO languages(code, country) VALUES('fr', 'FR');");
            st.execute("INSERT INTO translations(content_id, language_id, translated_text) VALUES(1, 1, 'Bonjour');");
        }

        try (Connection conn = TestDbHelper.getConnection()) {
            TranslationDAO dao = new TranslationDAO(conn);
            String res = dao.findTranslation("greeting", "en", "US");
            assertNull(res, "Expected null when requested language is missing");
        }
    }
}

