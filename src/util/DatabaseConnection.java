package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection Utility Class provides a method to establish a connection
 * to the database.
 * 
 * @author Kisal
 * Date: 01 January 2025
 */
public class DatabaseConnection {
    // Database URL, username, and password constants
    private static final String URL = "jdbc:mysql://localhost:3306/geometrydb"; // Replace 'geometrydb' with your database name if different.
    private static final String USER = "root"; // Replace with your database username.
    private static final String PASSWORD = ""; // Replace with your database password.

    /**
     * Establishes and returns a connection to the MySQL database.
     * 
     * @return Connection object if successful, or null if a connection cannot be established.
     */
    public static Connection getConnection() {
        Connection connection = null; // Initialize connection object.
        try {
            // Attempt to establish a connection using the DriverManager.
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // Log an error message and print the stack trace if the connection fails.
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection; // Return the connection (null if failed).
    }
}
