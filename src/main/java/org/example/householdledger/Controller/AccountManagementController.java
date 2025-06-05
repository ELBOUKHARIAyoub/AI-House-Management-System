package org.example.householdledger.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.example.householdledger.Model.user;
import org.example.householdledger.Session.UserSession;
import org.example.householdledger.dao.UserDAOImpl;

import java.sql.SQLOutput;

public class AccountManagementController {
@FXML Button withdrawButton;
@FXML Button PopUpButton;
    @FXML private StackPane popupPane;
    @FXML private TextField amountField;
    @FXML private TextField usernameField;
  //  @FXML private Button withdrawButton;
    @FXML private Button TransferButton;
   // @FXML private Button PopUpButton;
    @FXML private Label balanceLabel;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;

//@FXML
//Label balanceLabel;
  //  @FXML Button TransferButton;
private enum ActionType { WITHDRAW, TOPUP, TRANSFER }
    private ActionType currentAction;
    private final UserDAOImpl userDAO = new UserDAOImpl();

    //UserDAOImpl userDAO = new UserDAOImpl();
    @FXML
    public void initialize() {
        user currentUser = UserSession.getCurrentUser();
        int userID = currentUser.getUserId();
        double balance = userDAO.getBalanceById(userID);
        balanceLabel.setText(String.valueOf(balance));

        withdrawButton.setOnAction(e -> showPopup(ActionType.WITHDRAW));
        PopUpButton.setOnAction(e -> showPopup(ActionType.TOPUP));
        TransferButton.setOnAction(e -> showPopup(ActionType.TRANSFER));

        cancelButton.setOnAction(e -> popupPane.setVisible(false));
        confirmButton.setOnAction(e -> processAction());


    }
    private void showPopup(ActionType action) {
        currentAction = action;
        amountField.clear();
        usernameField.clear();

        usernameField.setVisible(action == ActionType.TRANSFER);
        usernameField.setManaged(action == ActionType.TRANSFER);

        popupPane.setVisible(true);
        popupPane.setManaged(true);
    }
    private void processAction() {
        try {
            int userId = UserSession.getCurrentUser().getUserId();
            double amount = Double.parseDouble(amountField.getText());

            switch (currentAction) {
                case WITHDRAW:
                    userDAO.popUp(userId, amount);
                    break;
                case TOPUP:
                    userDAO.transferMoney(userId, amount);
                    break;
                case TRANSFER:
                    String targetUsername = usernameField.getText();
                    user targetUser = userDAO.getUserByUsername(targetUsername);

                    if (targetUser != null) {
                        userDAO.popUp(userId, amount); // deduct from current
                        userDAO.transferMoney(targetUser.getUserId(), amount); // add to target
                    } else {
                        System.out.println("Target user not found");
                    }
                    break;
            }

            // Refresh balance
            double updatedBalance = userDAO.getBalanceById(userId);
            balanceLabel.setText(String.format("%.2f", updatedBalance));

        } catch (Exception e) {
            System.out.println("Error processing action: " + e.getMessage());
        } finally {
            popupPane.setVisible(false);
            popupPane.setManaged(false);
        }
    }


    @FXML
    private void showPopup() {
        popupPane.setVisible(true);
        popupPane.setManaged(true);
    }

    @FXML
    private void closePopup() {
        popupPane.setVisible(false);
        popupPane.setManaged(false);
    }

}
