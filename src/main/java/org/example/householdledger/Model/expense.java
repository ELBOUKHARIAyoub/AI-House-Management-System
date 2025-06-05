package org.example.householdledger.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class expense {
private int expenseId;
    private int userId;
    private String expenseName;
private float expenseAmount;
    private String expenseDemo;

//                expense newExpense = new expense(currentUserId, expenseName, expenseAmount, expenseDemo);


    public expense(int userId, String expenseName, float expenseAmount, String expenseDemo) {
        this.userId = userId;
        this.expenseName = expenseName;
        this.expenseAmount = expenseAmount;
        this.expenseDemo = expenseDemo;

    }



    @Override
    public String toString() {
        return String.format("ID: %d|Name: %s | Amount: ¥%.2f | Demo: %s | ",
                userId, expenseName, expenseAmount, expenseDemo );
    }


    public expense() {
    }



    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public float getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(float expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseDemo() {
        return expenseDemo;
    }

    public void setExpenseDemo(String expenseDemo) {
        this.expenseDemo = expenseDemo;
    }
    public String toTableString() {
        return String.format("%s | ¥%.2f | %s ",
                expenseName,
                expenseAmount,
                expenseDemo != null ? expenseDemo : "N/A");
    }
}
