package org.example.householdledger.Model;

public class user {
    private int userId;
    private String userName;
    private String userPassword;
    private double balance;
    //==================================================


    public user(int userId, String userName, double balance) {
        this.userId = userId;
        this.userName = userName;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public user( String userName, String userPassword) {

        this.userName = userName;
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "user{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }

    public user() {
    }
}
