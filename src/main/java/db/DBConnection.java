package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class DBConnection {

    private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);

    private DBConnection() { }

    private static final String URL = System.getenv().getOrDefault(
            "DB_URL",
            "jdbc:mysql://localhost:3306/shopping_cart_localization"
    );
    private static final String USER = System.getenv().getOrDefault("DB_USER", "shopping_user");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "mysecurepass");

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            logMetadata(conn);
            return conn;

        } catch (Exception e) {
            logger.error(
                    "Failed to connect to DB at {}. Ensure MySQL is running. Current user={}",
                    URL, USER, e
            );
            throw new IllegalStateException("Database connection failed", e);
        }
    }

    private static void logMetadata(Connection conn) {
        try {
            DatabaseMetaData md = conn.getMetaData();
            logger.info(
                    "Connected to DB: url={}, user={}, product={}, version={}",
                    md.getURL(),
                    md.getUserName(),
                    md.getDatabaseProductName(),
                    md.getDatabaseProductVersion()
            );
        } catch (Exception metaEx) {
            logger.warn("Connected, but metadata unavailable. user={}", USER, metaEx);
        }
    }
}
