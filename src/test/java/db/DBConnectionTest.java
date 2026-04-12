package db;

import org.junit.jupiter.api.Test;
import test.util.TestDbHelper;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DBConnectionTest {

    private String invokeGetConfig(String key, String def) throws Exception {
        var m = DBConnection.class.getDeclaredMethod("getConfig", String.class, String.class);
        m.setAccessible(true);
        return (String) m.invoke(null, key, def);
    }

    private Connection invokeCreate(String url, String user, String pass) throws Exception {
        var m = DBConnection.class.getDeclaredMethod("createConnection", String.class, String.class, String.class);
        m.setAccessible(true);
        return (Connection) m.invoke(null, url, user, pass);
    }

    @Test
    void testGetConnection_UsingH2() throws Exception {
        TestDbHelper.prepareUniqueDb();

        try (Connection conn = DBConnection.getConnection()) {
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

    @Test
    void testGetConnection_throwsWhenInvalidUrl() {
        System.setProperty("DB_URL", "jdbc:invalid:url");
        System.setProperty("DB_USER", "");
        System.setProperty("DB_PASSWORD", "");

        assertThrows(IllegalStateException.class, DBConnection::getConnection);
    }

    @Test
    void testGetConfig_defaultValue() throws Exception {
        System.clearProperty("NON_EXISTENT_KEY");
        String result = invokeGetConfig("NON_EXISTENT_KEY", "fallback");
        assertEquals("fallback", result);
    }

    @Test
    void testGetConfig_envVariable() throws Exception {
        String result = invokeGetConfig("PATH", "fallback");
        assertNotEquals("fallback", result);
    }

    @Test
    void testCreateConnection_noUserNoPassword() throws Exception {
        Connection conn = invokeCreate("jdbc:h2:mem:test_noauth", null, null);
        assertNotNull(conn);
    }

    @Test
    void testLogMetadata_exception() throws Exception {
        Connection mockConn = mock(Connection.class);
        when(mockConn.getMetaData()).thenThrow(new SQLException("fail"));

        var m = DBConnection.class.getDeclaredMethod("logMetadata", Connection.class);
        m.setAccessible(true);

        assertDoesNotThrow(() -> m.invoke(null, mockConn));
    }
    @Test
    void testLogMetadata_success() throws Exception {
        Connection mockConn = mock(Connection.class);
        DatabaseMetaData md = mock(DatabaseMetaData.class);

        when(mockConn.getMetaData()).thenReturn(md);
        when(md.getURL()).thenReturn("jdbc:h2:mem:test");
        when(md.getUserName()).thenReturn("sa");
        when(md.getDatabaseProductName()).thenReturn("H2");
        when(md.getDatabaseProductVersion()).thenReturn("2.2.224");

        var m = DBConnection.class.getDeclaredMethod("logMetadata", Connection.class);
        m.setAccessible(true);

        assertDoesNotThrow(() -> m.invoke(null, mockConn));
    }
    @Test
    void testGetConnection_userBlank_passwordPresent() throws Exception {
        System.setProperty("DB_URL", "jdbc:h2:mem:test_blank_user");
        System.setProperty("DB_USER", "");
        System.setProperty("DB_PASSWORD", "secret");

        try (Connection conn = DBConnection.getConnection()) {
            assertNotNull(conn);
            assertFalse(conn.isClosed());
        }
    }
    @Test
    void testGetConnection_noCredentials() throws Exception {
        System.setProperty("DB_URL", "jdbc:h2:mem:test_nocreds");
        System.clearProperty("DB_USER");
        System.clearProperty("DB_PASSWORD");

        try (Connection conn = DBConnection.getConnection()) {
            assertNotNull(conn);
        }
    }
    @Test
    void testGetConfig_envOverridesDefault() throws Exception {
        // System property should override env and default
        System.setProperty("TEST_KEY", "sysValue");

        String result = invokeGetConfig("TEST_KEY", "fallback");

        assertEquals("sysValue", result);

        System.clearProperty("TEST_KEY");
    }





}
