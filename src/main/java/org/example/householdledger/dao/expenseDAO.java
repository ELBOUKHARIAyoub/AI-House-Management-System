package org.example.householdledger.dao;

import org.example.householdledger.Model.expense;
import org.example.householdledger.Model.income;

import java.util.List;

public interface expenseDAO {
    List<expense> getExpense(int userId);

    void addExpense(int userId, String expenseName, float expenseAmount, String expenseDemo);

    float getTotalExpenseByUserId(int userId);
}
