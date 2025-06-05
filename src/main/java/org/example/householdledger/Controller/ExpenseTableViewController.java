package org.example.householdledger.Controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.householdledger.Model.expense;
import org.example.householdledger.Model.income;
import org.example.householdledger.Model.user;
import org.example.householdledger.Session.UserSession;
import org.example.householdledger.dao.expenseDAOlmpl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javafx.stage.FileChooser;
import java.io.File;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExpenseTableViewController implements Initializable
 {
    private expenseDAOlmpl expenseDAO = new expenseDAOlmpl();
    private int currentUserId;
@FXML private TableColumn<expense, String> ColumnName;
    @FXML private TableColumn<expense, String> ColumnAmount;
    @FXML private TableColumn<expense, String> ColumnDemo;
@FXML private Button addExpenseBtn;
@FXML private Button editExpenseBtn;
@FXML private Button deleteExpenseBtn;
@FXML private TableView TableViewExpense;
@FXML private Label expenseCountLabel;
     @FXML private TextField filterExpenseField;
     @FXML private Button exportExpenseBtn;

     private ObservableList<expense> masterExpenseList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    System.out.println("✅ Controller initialized");
    System.out.println("TableViewexpense is null? " + (TableViewExpense == null));
    user currentUser = UserSession.getCurrentUser();
    this.currentUserId = currentUser.getUserId();

    loadExpenseData(currentUserId);

    }


@FXML
void handleAddExpense() {
System.out.println("Add Expense Button Clicked");
    Dialog<expense> dialog = new Dialog<>();
    dialog.setTitle("Add New expense");
    dialog.setHeaderText("Enter details for new income record");

    // Set the button types
    ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

    // Create input fields
    TextField nameField = new TextField();
    nameField.setPromptText("expenseName");

    TextField amountField = new TextField();
    amountField.setPromptText("expenseAmount");

    TextField demoField = new TextField();
    demoField.setPromptText("expenseDemo");

    //DatePicker datePicker = new DatePicker(LocalDate.now());

    VBox content = new VBox(10, new Label("expenseName:"), nameField,
            new Label("expenseAmount:"), amountField,
            new Label("expenseDemo:"), demoField);

    dialog.getDialogPane().setContent(content);

    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == addButtonType) {
            try {
                String expenseName = nameField.getText();
                System.out.println("EXPENSEnAME + " + expenseName);
                float expenseAmount = Float.parseFloat(amountField.getText());
                System.out.println("expenseAmount + " + expenseAmount);
                String expenseDemo = demoField.getText();
                System.out.println("expenseDemo + " + expenseDemo);
               // LocalDate date = datePicker.getValue();
                 user currentUser = UserSession.getCurrentUser();
                int currentUserId = currentUser.getUserId();
                System.out.println("🧠 Inserting income for userId = " + currentUserId);

                expense newExpense = new expense(currentUserId, expenseName, expenseAmount, expenseDemo);
                System.out.println("newExpense + " + newExpense);
                // TableViewIncome.setItems(loadIncomeDataFromDatabase());
                TableViewExpense.refresh();
                return newExpense;

            } catch (Exception e) {
                showAlert("Invalid input", "Please enter valid values.");
                return null;
            }
        }
        return null;
    });

    dialog.showAndWait().ifPresent(newExpense -> {
        // Save to database
        expenseDAO.addExpense(newExpense.getUserId(), newExpense.getExpenseName(), newExpense.getExpenseAmount(), newExpense.getExpenseDemo());

        // Reload table
        loadExpenseData(currentUserId);
    });

    // TODO: Implement the logic for adding a new expense
}

@FXML
void handleEditExpense() {
    // TODO: Implement the logic for editing an existing expense
    expense selected = (expense) TableViewExpense.getSelectionModel().getSelectedItem();
    if (selected != null) {
        // Example edit dialog for name (you can add more fields later)
        TextInputDialog dialog = new TextInputDialog(selected.getExpenseName());
        dialog.setTitle("Edit Expense");
        dialog.setHeaderText("Editing income for: " + selected.getExpenseId());
        dialog.setContentText("New Name:");

        dialog.showAndWait().ifPresent(newName -> {
            selected.setExpenseName(newName);
            expenseDAO.updateIncome(selected); // ✅ update DB
            TableViewExpense.refresh(); // ✅ refresh table
        });

    } else {
        showAlert("No Selection", "Please select an income row to edit.");
    }
}

@FXML
private void handleDeleteExpense() {

        expense selected = (expense) TableViewExpense.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Are you sure you want to delete:");
            alert.setContentText(selected.toTableString());

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    expenseDAO.deleteIncome(selected.getExpenseId()); // ✅ remove from DB
                    TableViewExpense.getItems().remove(selected);   // ✅ remove from UI
                    updateExpenseCountLabel(TableViewExpense.getItems().size());
                }
            });
        } else {
            showAlert("No Selection", "Please select an income row to delete.");

    }
    // TODO: Implement the logic for deleting an expense
}
     @FXML
     public void loadExpenseData(int userId) {
         try {
             List<expense> expenses = expenseDAO.getExpense(userId);
             if (expenses != null && !expenses.isEmpty()) {

                 ColumnName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getExpenseName()));
                 ColumnAmount.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.format("¥%.2f", cellData.getValue().getExpenseAmount())));
                 ColumnDemo.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getExpenseDemo()));

                 masterExpenseList.setAll(expenses); // ✅ Store in master list
                 TableViewExpense.setItems(masterExpenseList);

                 updateExpenseCountLabel(expenses.size());

                 setupFilter(); // 🔁 Setup filter logic

             } else {
                 masterExpenseList.clear();
                 TableViewExpense.setItems(masterExpenseList);
                 updateExpenseCountLabel(0);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
     private void setupFilter() {

         FilteredList<expense> filteredData = new FilteredList<>(masterExpenseList, p -> true);

         filterExpenseField.textProperty().addListener((observable, oldValue, newValue) -> {
             filteredData.setPredicate(exp -> {
                 if (newValue == null || newValue.isEmpty()) return true;

                 String lowerCaseFilter = newValue.toLowerCase();
                 return exp.getExpenseName().toLowerCase().contains(lowerCaseFilter)
                         || exp.getExpenseDemo().toLowerCase().contains(lowerCaseFilter);
             });
             updateExpenseCountLabel(filteredData.size());
         });

         SortedList<expense> sortedData = new SortedList<>(filteredData);
         sortedData.comparatorProperty().bind(TableViewExpense.comparatorProperty());
         TableViewExpense.setItems(sortedData);
     }
     @FXML
     private void handleExportToPDF() {
         List<expense> expenses = new ArrayList<>(TableViewExpense.getItems());

         if (expenses.isEmpty()) {
             showAlert("Nothing to Export", "There are no expenses to export.");
             return;
         }

         FileChooser fileChooser = new FileChooser();
         fileChooser.setTitle("Save Expense Report");
         fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
         fileChooser.setInitialFileName("expenses.pdf");
         File file = fileChooser.showSaveDialog(TableViewExpense.getScene().getWindow());

         if (file == null) {
             return; // User cancelled
         }

         Document document = new Document();

         try {
             PdfWriter.getInstance(document, new FileOutputStream(file));
             document.open();

             document.add(new Paragraph("Expense Report"));
             document.add(new Paragraph(" "));

             for (expense exp : expenses) {
                 String line = String.format("Name: %s | Amount: ¥%.2f | Demo: %s",
                         exp.getExpenseName(), exp.getExpenseAmount(), exp.getExpenseDemo());
                 document.add(new Paragraph(line));
             }

             document.close();
             showAlert("Success", "Expenses exported to:\n" + file.getAbsolutePath());

         } catch (FileNotFoundException | DocumentException e) {
             e.printStackTrace();
             showAlert("Error", "Failed to export PDF: " + e.getMessage());
         }
     }

     private void showAlert(String title, String content) {
         Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setTitle(title);
         alert.setHeaderText(null);
         alert.setContentText(content);
         alert.showAndWait();
     }

     private void updateExpenseCountLabel(int count) {
        if (expenseCountLabel != null) {
            expenseCountLabel.setText(count + " entries");
        }
    }

}