package org.example.householdledger.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.householdledger.HelloApplication;
import org.example.householdledger.Model.DBConnection;
import org.example.householdledger.Model.user;
import org.example.householdledger.Session.UserSession;
import org.example.householdledger.dao.UserDAO;
import org.example.householdledger.dao.UserDAOImpl;

import java.io.IOException;
import java.sql.Connection;

public class loginController {
    @FXML
    private TextField userName;
    @FXML
    private TextField userPassword;
    DBConnection db = new DBConnection();
    UserDAOImpl userDAO = new UserDAOImpl();
    @FXML
     public void goToSignUp(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SignUp.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("signUp");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void login(ActionEvent event) {
        String username = userName.getText();
        String password = userPassword.getText();

        user loggedUser = userDAO.getUserByUsernameAndPassword(username, password);

        if (loggedUser != null) {
            UserSession.init(loggedUser); // ✅ Set full user including userId
            System.out.println("Login successful!");
            System.out.println("Current user: " + loggedUser); // ✅ userId should NOT be 0

            try {
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("MainDashboard.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Main Dashboard");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            // Login failed
            System.out.println("Wrong username or password!");
            userName.clear();
            userPassword.clear();
            userName.requestFocus();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Wrong username or password!");
            alert.showAndWait();
        }
    }



    @FXML
    private void handleLogin(ActionEvent event) {
        // Assume login verification is successful
        System.out.println("Login successful!");

        try {
            // Load the MainDashboard.fxml
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("MainDashboard.fxml"));
            Parent root = loader.load();

            // Get the current stage from the ActionEvent
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Main Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void  getUsername() {
}}

