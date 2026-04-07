package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/shopping_cart_localization");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "shopping_user");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "mysecurepass");

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            try {
                java.sql.DatabaseMetaData md = conn.getMetaData();
                System.out.println("[DBConnection] Connected to DB URL=" + md.getURL() + " user=" + md.getUserName()
                        + " product=" + md.getDatabaseProductName() + " version=" + md.getDatabaseProductVersion());
            } catch (Exception metaEx) {
                System.out.println("[DBConnection] Connected (metadata not available): user=" + USER);
            }
            return conn;
        } catch (Exception e) {
            System.err.println("[DBConnection] Failed to connect to DB at " + URL);
            System.err.println("[DBConnection] Please ensure MySQL is running and credentials are set.\n" +
                    "You can set env vars DB_URL, DB_USER, DB_PASSWORD. Current user=" + USER);
            e.printStackTrace();
        }
        return null;
    }
}