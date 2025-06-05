package org.example.householdledger.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class income {
private int incomeId;
private int userId;
private String name;
private float amount;

private String demo;
    private LocalDate date;
    public income() {
    }

    public income( int userId, String name, float amount, String demo, LocalDate date) {

        this.userId = userId;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.demo = demo;
    }



    public int getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(int incomeId) {
        this.incomeId = incomeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    @Override
    public String toString() {
        return String.format("%s - ¥%.2f - %s - %s", name, amount, demo, date);
    }


    public String toTableString() {
        return String.format("%s | ¥%.2f | %s | %s",
                name,
                amount,
                demo != null ? demo : "N/A",
                date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "N/A");
    }
}
