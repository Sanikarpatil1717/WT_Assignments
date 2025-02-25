package com.carmart.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/carmart";
    private static final String USER = "postgres";
    private static final String PASSWORD = "tiger";
    
    private static Connection connection;

    // Private constructor to prevent instantiation
    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            synchronized (DBConnection.class) { // Thread safety
                if (connection == null || connection.isClosed()) {
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                }
            }
        }
        return connection;
    }
}
