package db;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class DBConnectionTest {

    @Test
    void testGetConnection_UsingH2() throws Exception {

        // Fresh DB each run
        System.setProperty("DB_URL", "jdbc:h2:mem:testdb;MODE=MySQL");
        System.setProperty("DB_USER", "sa");
        System.setProperty("DB_PASSWORD", "");

        Connection conn = DBConnection.getConnection();
        assertNotNull(conn);
        assertFalse(conn.isClosed());

        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE test_table(id INT PRIMARY KEY, name VARCHAR(50));");
            st.execute("INSERT INTO test_table VALUES(1, 'Hello');");

            ResultSet rs = st.executeQuery("SELECT name FROM test_table WHERE id = 1");
            assertTrue(rs.next());
            assertEquals("Hello", rs.getString(1));
        }
    }

}

