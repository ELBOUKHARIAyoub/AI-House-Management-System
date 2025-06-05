package org.example.householdledger.Controller;
import org.example.householdledger.Model.*;

import java.sql.*;

public class UserController {
DBConnection db = new DBConnection();

public user getUserByID(int id){
    String query = "SELECT * FROM user WHERE id = ?";
    user user = null;
    try (Connection conn = db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setInt(1, id); // Set ID

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            user = new user();
            user.setUserId(rs.getInt("userId"));
            user.setUserName(rs.getString("userName"));
            user.setUserPassword(rs.getString("userPassword"));
            // add other fields here
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return user;
}
    public user getUserByUserName(String userName){
        String query = "SELECT * FROM user WHERE userName = ?";
        user user = null;
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userName); // Set ID

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new user();
                user.setUserId(rs.getInt("userId"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                // add other fields here
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

}

