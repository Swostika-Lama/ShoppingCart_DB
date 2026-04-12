package test.util;

import db.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class TestDbHelper {

    private TestDbHelper() {}

    public static String prepareUniqueDb() {
        String dbName = "testdb_" + System.nanoTime();
        String url = "jdbc:h2:mem:" + dbName + ";MODE=MySQL;DB_CLOSE_DELAY=-1";
        System.setProperty("DB_URL", url);
        // Use empty user/password so DBConnection.createConnection will call DriverManager.getConnection(url)
        System.setProperty("DB_USER", "");
        System.setProperty("DB_PASSWORD", "");
        return dbName;
    }

    public static Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    public static void runSql(Connection conn, String... sqlStatements) throws SQLException {
        try (Statement st = conn.createStatement()) {
            for (String sql : sqlStatements) {
                st.execute(sql);
            }
        }
    }
}
