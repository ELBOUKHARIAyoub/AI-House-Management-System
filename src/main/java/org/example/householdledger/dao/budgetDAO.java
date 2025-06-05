package org.example.householdledger.dao;

import org.example.householdledger.Model.Budget;
import java.util.List;

public interface budgetDAO {
    void addBudget(Budget budget);
    void updateBudget(Budget budget);
    void deleteBudget(int budgetId);
    List<Budget> getAllBudgetsForUser(int userId);

    double getTotalBudgetForUser(int userId);

    double getTotalSpentForUser(int userId);
}


