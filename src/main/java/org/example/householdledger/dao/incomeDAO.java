package org.example.householdledger.dao;
import org.example.householdledger.Model.income;

import java.time.LocalDate;
import java.util.List;

public interface incomeDAO {

List<income> getIncome(int userId);
void getIncomeByDate(int userId,LocalDate date);
void getIncomeBySource(int userId,String name);
     void updateIncome(income income);
     void deleteIncome(int incomeId);
    public void addIncome(int userId, String name, float amount, String demo, LocalDate date);

    float getTotalIncomeByUserId(int userId);
}
