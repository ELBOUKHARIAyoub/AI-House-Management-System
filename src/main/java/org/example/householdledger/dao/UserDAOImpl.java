package org.example.householdledger.dao;

import org.example.householdledger.Model.DBConnection;
import org.example.householdledger.Model.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAOImpl  implements UserDAO {

    @Override
    public boolean checkUser(String username, String password) {
        String sql = "SELECT * FROM user WHERE userName = ? AND userPassword = ?";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            System.out.println("Error in checkUser(): " + e.getMessage());
            e.printStackTrace(); // optional, but very helpful
            return false;
        }
    }


    @Override
    public void addUser(String username, String password) {

    }
    public user getUserByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM user WHERE userName = ? AND userPassword = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user u = new user();
                    u.setUserId(rs.getInt("userId"));  // ✅ Important!
                    u.setUserName(rs.getString("userName"));
                    u.setUserPassword(rs.getString("userPassword"));
                    return u;
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getUserByUsernameAndPassword: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public double getBalanceById(int id) {
        String sql = "SELECT balance FROM user WHERE userId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("balance");
                }

            }
        } catch (Exception e) {
            System.out.println("Error in getBalanceById: " + e.getMessage());}
        return 0;
    }

    @Override
    public void transferMoney(int id, double amount){
        String sql = "UPDATE user SET balance = balance + ? WHERE userId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2,id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error in transferMoney: " + e.getMessage());
        }
    }

    @Override
    public void popUp(int id, double amount){
        String sql = "UPDATE user SET balance = balance - ? WHERE userId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2,id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error in transferMoney: " + e.getMessage());
        }


    }
    @Override
    public user getUserByUsername(String username) {
        String sql = "SELECT * FROM user WHERE userName = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user u = new user();
                    u.setUserId(rs.getInt("userId"));
                    u.setUserName(rs.getString("userName"));
                    u.setUserPassword(rs.getString("userPassword"));
                    return u;
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getUserByUsername: " + e.getMessage());
        }
        return null;
    }

    }

