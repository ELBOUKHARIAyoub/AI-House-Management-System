package org.example.householdledger.Model;



import java.sql.*;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/housedb";
    private static final String USER = "root"; // your username
    private static final String PASSWORD = "admin"; // your password

    private static Connection connection;

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to DB", e);
        }
    }


    // Login method to check username and password
    public boolean login(String username, String password) {
        String sql = "SELECT * FROM user WHERE userName = ? AND userPassword = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            return rs.next();  // returns true if a matching record found

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("❌ Error checking login credentials: " + e.getMessage());
            return false;  // error means login failed
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("🔌 Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error closing connection: " + e.getMessage());
        }
    }
}
