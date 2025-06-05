package org.example.householdledger.dao;

import org.example.householdledger.Model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class expenseDAOlmpl extends DBConnection implements expenseDAO{
    @Override
    public List<expense> getExpense(int userId) {
        String sql = "SELECT * FROM expense WHERE userId = ?";
        List<expense> expenses = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                 expense expense = new expense();
                expense.setExpenseId(rs.getInt("expenseId"));
                expense.setUserId(rs.getInt("userId"));

                expense.setExpenseName(rs.getString("expenseName"));
                expense.setExpenseAmount(rs.getFloat("expenseAmount"));
                String comment = rs.getString("expenseDemo");
                String com  = (comment != null) ? comment : "nothing";
                expense.setExpenseDemo(com);
                System.out.println(com);

                System.out.println(expense);
                // Handle the date properly
               /* java.sql.Date sqlDate = rs.getDate("date");
                LocalDate localDate = (sqlDate != null) ? sqlDate.toLocalDate() : LocalDate.now();
                expense.setDate(localDate);
                  System.out.println(expense);*/
                expenses.add(expense);
            }

            return expenses; // Move this OUTSIDE the while loop

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteIncome(int expenseId) {
        String sql = "DELETE FROM expense WHERE expenseId = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, expenseId);
            int rows = stmt.executeUpdate();
            System.out.println("Deleted rows: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void addExpense(int userId, String expenseName, float expenseAmount, String expenseDemo) {
        String sql = "INSERT INTO expense (userId, expenseName, expenseAmount, expenseDemo) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, expenseName);
            pstmt.setFloat(3, expenseAmount);
            pstmt.setString(4, expenseDemo);


            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✅ Income inserted: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateIncome(expense expense) {
        String sql = "UPDATE expense SET expenseName = ?, expenseAmount = ?, expenseDemo = ?WHERE expenseId = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, expense.getExpenseName());
            stmt.setFloat(2, expense.getExpenseAmount());
            stmt.setString(3, expense.getExpenseDemo());

            stmt.setInt(4, expense.getExpenseId());

            int rows = stmt.executeUpdate();
            System.out.println("Updated rows: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public float getTotalExpenseByUserId(int userId) {
        String sql = "SELECT SUM(expenseAmount) AS total FROM expense WHERE userId = ?";
        float total1 = 0;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                total1 = rs.getFloat("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total1;
    }
}
