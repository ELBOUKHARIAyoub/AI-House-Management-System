package org.example.householdledger.dao;

import org.example.householdledger.Model.Budget;
import org.example.householdledger.Model.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class budgetDAOImpl extends DBConnection implements budgetDAO {



    @Override
    public void addBudget(Budget budget) {
        String sql = "INSERT INTO budget (userId, category, budgetAmount, spentAmount,progress) VALUES (?, ?, ?, ?,?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, budget.getUserId());
            stmt.setString(2, budget.getCategory());
            stmt.setDouble(3, budget.getBudget());
            stmt.setDouble(4, budget.getSpent());
            stmt.setDouble(5, (budget.getSpent()*100)/ budget.getBudget());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error adding budget: " + e.getMessage());
        }
    }

    @Override
    public void updateBudget(Budget budget) {
        String sql = "UPDATE budget SET category = ?, budgetAmount = ?, spentAmount = ?, progress = ? WHERE budgetId = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, budget.getCategory());
            stmt.setDouble(2, budget.getBudget());
            stmt.setDouble(3, budget.getSpent());
            stmt.setDouble(4,(budget.getSpent()*100)/ budget.getBudget());
            stmt.setInt(5, budget.getBudgetId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error updating budget: " + e.getMessage());
        }
    }

    @Override
    public void deleteBudget(int budgetId) {
        String sql = "DELETE FROM budget WHERE budgetId = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, budgetId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error deleting budget: " + e.getMessage());
        }
    }

    @Override
    public List<Budget> getAllBudgetsForUser(int userId) {
        List<Budget> list = new ArrayList<>();
        String sql = "SELECT * FROM budget WHERE userId = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Budget budget = new Budget();
                budget.setBudgetId(rs.getInt("budgetId"));
                budget.setUserId(rs.getInt("userId"));
                budget.setCategory(rs.getString("category"));
                budget.setBudget(rs.getDouble("budgetAmount"));
                budget.setSpent(rs.getDouble("spentAmount"));
                budget.setProgress(rs.getDouble("progress"));
                list.add(budget);
                System.out.println(list);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error loading budgets: " + e.getMessage());
        }
        return list;
    }
    @Override
    public double getTotalBudgetForUser(int userId) {
        double totalBudget = 0;
        String sql = "SELECT SUM(budgetAmount) AS totalBudget FROM budget WHERE userId = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalBudget = rs.getDouble("totalBudget");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error calculating total budget: " + e.getMessage());
        }
        return totalBudget;
    }

    @Override
    public double getTotalSpentForUser(int userId) {
        double totalSpent = 0;
        String sql = "SELECT SUM(spentAmount) AS totalSpent FROM budget WHERE userId = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalSpent = rs.getDouble("totalSpent");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error calculating total spent: " + e.getMessage());
        }
        return totalSpent;
    }

}
