package org.example.householdledger.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.householdledger.HelloApplication;
import org.example.householdledger.Model.*;


import javafx.scene.input.MouseEvent;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class signUpController {
@FXML
private TextField userName;
@FXML
private TextField userPassword;
DBConnection db = new DBConnection();
@FXML
void signUp() {
    String username = userName.getText();
    String password = userPassword.getText();
    if (username.isEmpty() || password.isEmpty()) {
        // Handle empty fields
        if (username.isEmpty()) {
            userName.clear();
            userName.setPromptText("Username required");
            userName.requestFocus();
        }
        if (password.isEmpty()) {
            userPassword.clear();
            userPassword.setPromptText("Password required");
            userPassword.requestFocus();
        }
        return;
    }
    user newUser = new user(username, password);

    try {
        Connection conn = db.getConnection(); // make sure it returns Connection

        String query = "INSERT INTO user (userName, userPassWord) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, newUser.getUserName());
        stmt.setString(2, newUser.getUserPassword());

        int result = stmt.executeUpdate();
        if (result > 0) {
            System.out.println("✅ User registered successfully!");
            // Optionally clear fields
            userName.clear();
            userPassword.clear();
        } else {
            System.out.println("❌ User registration failed.");
        }

        stmt.close();
        conn.close();

    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("❌ Database error: " + e.getMessage());
    }


}

@FXML
   void goToLogin(MouseEvent event){
    try {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 320);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
   }
}
