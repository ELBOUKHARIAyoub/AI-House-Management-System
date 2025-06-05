package org.example.householdledger.Model;



public class Budget {
    private int budgetId;
    private int userId;
    private String category;
    private double budget;     // total budget
    private double spent;
    private double progress;      // amount spent
    // remaining and progress can be calculated on the fly

    public Budget() {}

    public Budget(int userId, String category, double budget, double spent,double progress) {
        this.userId = userId;
        this.category = category;
        this.budget = budget;
        this.spent = spent;
        this.progress = progress;
    }

    public Budget(int userId, String category, double budget, double spent) {
        this.userId = userId;
        this.category = category;
        this.budget = budget;
        this.spent = spent;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public Budget(int budgetId, int userId, String category, double budget, double spent) {
        this.budgetId = budgetId;
        this.userId = userId;
        this.category = category;
        this.budget = budget;
        this.spent = spent;
    }

    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(double spent) {
        this.spent = spent;
    }

    public double getRemaining() {
        return budget - spent;
    }

    public double getProgress() {
        if (budget == 0) return 0;
        return (spent / budget) * 100;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "budgetId=" + budgetId +
                ", userId=" + userId +
                ", category='" + category + '\'' +
                ", budget=" + budget +
                ", spent=" + spent +
                ", remaining=" + getRemaining() +
                ", progress=" + getProgress() +
                '}';
    }
}
