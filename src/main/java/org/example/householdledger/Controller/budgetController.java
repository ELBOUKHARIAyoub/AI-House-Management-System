package org.example.householdledger.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import org.example.householdledger.Model.user;
import org.example.householdledger.Session.UserSession;
import org.example.householdledger.dao.*;

import java.io.IOException;


public class budgetController {
    @FXML
    private Button loadBudgetDataBtn;
    user currentUser= UserSession.getCurrentUser();
    @FXML
    private Label totalBudgetLabel;
    @FXML
    private Label totalSpentLabel;
    @FXML
    private Label remainingBudgetLabel;
    @FXML Label progressLabel;
    @FXML
    private ProgressBar budgetProgressBar;
    private budgetDAO budgetDAOlmpl = new budgetDAOImpl();
     //budgetDAOlmpl expenseDAOlmpl;

    @FXML public void initialize() {
        user currentUser = UserSession.getCurrentUser();
        budgetProgressBar.setProgress(0.5);

    }

    @FXML public void handleBudgetButton(){
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/householdledger/budgetTable.fxml"));

        // 🧠 Manually create controller instance and set it


        Parent TableViewIncome = loader.load();

        // Initialize with current user



        // Create a new scene and show it in a new window
        Stage stage = new Stage();
        Scene scene = new Scene(TableViewIncome);
        stage.setTitle("budget Table");
        stage.setScene(scene);
        stage.show();

        //loadIncomeTableBtn.setVisible(false);

    } catch (IOException e) {
        e.printStackTrace();
    }
    }


   @FXML public void refreshData(ActionEvent event) {

       int currentUserId = currentUser.getUserId();
       double totalBudget = budgetDAOlmpl.getTotalBudgetForUser(currentUserId);
       double totalSpend = budgetDAOlmpl.getTotalSpentForUser(currentUserId);
       double remaining = totalBudget - totalSpend;
       System.out.println("aa" + totalBudget);
       System.out.println("aa" + totalSpend);
       System.out.println("aa" + remaining);
       System.out.println("aa" + currentUser);
       System.out.println("aa" + currentUserId);
       double progress = 0;
       totalBudgetLabel.setText("¥" + totalBudget);
       totalSpentLabel.setText("¥" + totalSpend);
       remainingBudgetLabel.setText("¥" + remaining);
       if (totalBudget > 0) {
           progress = (totalSpend / totalBudget); // This gives a value between 0.0 and 1.0
           budgetProgressBar.setProgress(progress);
           System.out.println("progress" + progress);

       }
       double progressPercentage = budgetProgressBar.getProgress() * 100;
       progressLabel.setText(String.format("%.0f%%", progressPercentage));

   }
}
