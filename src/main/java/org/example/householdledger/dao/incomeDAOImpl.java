package org.example.householdledger.dao;

import org.example.householdledger.Model.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.householdledger.Model.income;


public class incomeDAOImpl extends DBConnection implements incomeDAO {



    @Override
    public List<income> getIncome(int userId) {
        String sql = "SELECT * FROM income WHERE userId = ?";
        List<income> incomes = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                income income = new income();
                income.setIncomeId(rs.getInt("incomeId"));
                income.setUserId(rs.getInt("userId"));

                income.setName(rs.getString("name"));
                income.setAmount(rs.getFloat("amount"));
                String comment = rs.getString("demo");
                String com = (comment != null) ? comment : "nothing";
                income.setDemo(com);
                //System.out.println(com);


                // Handle the date properly
                java.sql.Date sqlDate = rs.getDate("date");
                LocalDate localDate = (sqlDate != null) ? sqlDate.toLocalDate() : LocalDate.now();
                income.setDate(localDate);
                //  System.out.println(income);
                incomes.add(income);
            }

            return incomes; // Move this OUTSIDE the while loop

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getIncomeByDate(int userId, LocalDate date) {

    }

    @Override
    public void getIncomeBySource(int userId, String name) {

    }

    public void deleteIncome(int incomeId) {
        String sql = "DELETE FROM income WHERE incomeId = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, incomeId);
            int rows = stmt.executeUpdate();
            System.out.println("Deleted rows: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateIncome(income income) {
        String sql = "UPDATE income SET name = ?, amount = ?, demo = ?, date = ? WHERE incomeId = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, income.getName());
            stmt.setFloat(2, income.getAmount());
            stmt.setString(3, income.getDemo());
            stmt.setDate(4, java.sql.Date.valueOf(income.getDate()));
            stmt.setInt(5, income.getIncomeId());

            int rows = stmt.executeUpdate();
            System.out.println("Updated rows: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void addIncome(int userId, String name, float amount, String demo, LocalDate date) {
        String sql = "INSERT INTO income (userId, name, amount, demo, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, name);
            pstmt.setFloat(3, amount);
            pstmt.setString(4, demo);
            pstmt.setDate(5, java.sql.Date.valueOf(date));

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✅ Income inserted: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public float getTotalIncomeByUserId(int userId) {
        String sql = "SELECT SUM(amount) AS total FROM income WHERE userId = ?";
        float total = 0;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                total = rs.getFloat("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

}