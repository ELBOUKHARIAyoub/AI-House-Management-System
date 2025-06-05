package org.example.householdledger.dao;

import org.example.householdledger.Model.user;

public interface UserDAO {
   public boolean checkUser(String username, String password);
   public void addUser(String username, String password);
   public double getBalanceById(int id);
   public void transferMoney(int id, double amount);
   public void popUp(int id, double amount);

   user getUserByUsername(String username);
}
