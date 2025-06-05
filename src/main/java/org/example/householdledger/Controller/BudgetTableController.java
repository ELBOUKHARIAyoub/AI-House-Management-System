package org.example.householdledger.Controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.example.householdledger.Model.Budget;
import org.example.householdledger.Model.user;
import org.example.householdledger.Session.UserSession;
import org.example.householdledger.dao.budgetDAO;
import org.example.householdledger.dao.budgetDAOImpl;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BudgetTableController implements Initializable {

    @FXML
    private TableView<Budget> budgetCategoriesTable;
    @FXML private TextField filterField;
   // @FXML private TableView<Budget> budgetCategoriesTable;

   /* @FXML private TableColumn<Budget, String> categoryCol;
    @FXML private TableColumn<Budget, Double> budgetCol;
    @FXML private TableColumn<Budget, Double> spentCol;
    @FXML private TableColumn<Budget, Double> remainingCol;
    @FXML private TableColumn<Budget, Double> progressCol;*/
   private ObservableList<Budget> masterBudgetList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Budget, String> categoryCol;

    @FXML
    private TableColumn<Budget, String> budgetCol;

    @FXML
    private TableColumn<Budget, String> spentCol;

    @FXML
    private TableColumn<Budget, String> progressCol;


    private final budgetDAO budgetDAO = new budgetDAOImpl();
    private final ObservableList<Budget> budgetList = FXCollections.observableArrayList();
    private int currentUserId ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user currentUser = UserSession.getCurrentUser();
        if (currentUser == null) {
            System.err.println("⚠️ No user logged in.");
            return;
        }
        this.currentUserId = currentUser.getUserId();

        // Set up columns
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        budgetCol.setCellValueFactory(new PropertyValueFactory<>("budget"));
        spentCol.setCellValueFactory(new PropertyValueFactory<>("spent"));

        progressCol.setCellValueFactory(new PropertyValueFactory<>("progress"));

        budgetCategoriesTable.setItems(budgetList);
        loadBudgetData(currentUserId);
      //  setupFilterBudget();
    }

    private void loadBudgetData(int userId) {

try{
    List<Budget> budgets = budgetDAO.getAllBudgetsForUser(userId);

    if (budgets != null && !budgets.isEmpty()) {
        // Assuming these columns are defined in your controller and linked via FXML
        categoryCol.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getCategory())
        );

        budgetCol.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(String.format("¥%.2f", cellData.getValue().getBudget()))
        );

        spentCol.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(String.format("¥%.2f", cellData.getValue().getSpent()))
        );

        progressCol.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(String.format("%.1f%%", cellData.getValue().getProgress())));
                //budgetCategoriesTable.getItems().setAll(budgets);
        masterBudgetList.setAll(budgets);
        budgetCategoriesTable.setItems(masterBudgetList);
        setupFilterBudget();
    }else {
        masterBudgetList.clear();
        budgetCategoriesTable.setItems(masterBudgetList);
          //  updateExpenseCountLabel(0);
        }
        }catch (Exception e) {
    e.printStackTrace();
}

        // Set the data to the TableView

    }


    @FXML
    void addNewBudget() {

            System.out.println("➕ Add Budget Button Clicked");

            Dialog<Budget> dialog = new Dialog<>();
            dialog.setTitle("Add New Budget");
            dialog.setHeaderText("Enter details for the new budget record");

            // Set button types
            ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            // Input fields
            TextField categoryField = new TextField();
            categoryField.setPromptText("Category");

            TextField amountField = new TextField();
            amountField.setPromptText("Total Budget Amount");

            TextField spentField = new TextField();
            spentField.setPromptText("Spent Amount");

            // Layout
            VBox content = new VBox(10,
                    new Label("Category:"), categoryField,
                    new Label("Budget Amount:"), amountField,
                    new Label("Spent Amount:"), spentField
            );
            dialog.getDialogPane().setContent(content);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    try {
                        String category = categoryField.getText().trim();
                        double budgetAmount = Double.parseDouble(amountField.getText().trim());
                        double spentAmount = Double.parseDouble(spentField.getText().trim());

                        if (category.isEmpty() || budgetAmount <= 0 || spentAmount < 0) {
                            showAlert("Invalid Input", "Please fill all fields with valid values.");
                            return null;
                        }

                        // Get current user
                        user currentUser = UserSession.getCurrentUser();
                        int currentUserId = currentUser.getUserId();

                        // Create Budget object
                        Budget newBudget = new Budget(currentUserId, category, budgetAmount, spentAmount);

                        // Insert into DB
                        budgetDAO.addBudget(newBudget);
                        System.out.println("✅ New Budget added: " + newBudget);

                        // Refresh Table
                        loadBudgetData(currentUserId);
                        return newBudget;

                    } catch (NumberFormatException e) {
                        showAlert("Invalid Input", "Please enter valid numbers for budget and spent.");
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlert("Error", "An unexpected error occurred while adding the budget.");
                    }
                }
                return null;
            });

            dialog.showAndWait();
        }

        private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void deleteSelectedBudget(ActionEvent event) {
        Budget selected = budgetCategoriesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            budgetDAO.deleteBudget(selected.getBudgetId());
            loadBudgetData(currentUserId);
        } else {
            showAlert("⚠️ Please select a budget to delete.");
        }
    }

    public void updateSelectedBudget(ActionEvent event) {
        Budget selected = budgetCategoriesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Dialog<Budget> dialog = new Dialog<>();
            dialog.setTitle("Update Budget");
            dialog.setHeaderText("Edit the selected budget record");

            // Set button types
            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            // Pre-filled input fields
            TextField categoryField = new TextField(selected.getCategory());
            TextField amountField = new TextField(String.valueOf(selected.getBudget()));
            TextField spentField = new TextField(String.valueOf(selected.getSpent()));

            VBox content = new VBox(10,
                    new Label("Category:"), categoryField,
                    new Label("Budget Amount:"), amountField,
                    new Label("Spent Amount:"), spentField
            );
            dialog.getDialogPane().setContent(content);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    try {
                        String category = categoryField.getText().trim();
                        double budgetAmount = Double.parseDouble(amountField.getText().trim());
                        double spentAmount = Double.parseDouble(spentField.getText().trim());

                        if (category.isEmpty() || budgetAmount <= 0 || spentAmount < 0) {
                            showAlert("Invalid Input", "Please enter valid values.");
                            return null;
                        }

                        // Update fields
                        selected.setCategory(category);
                        selected.setBudget(budgetAmount);
                        selected.setSpent(spentAmount);
                        selected.setProgress((spentAmount * 100.0) / budgetAmount);

                        budgetDAO.updateBudget(selected);
                        System.out.println("✅ Budget updated: " + selected);
                        loadBudgetData(currentUserId);

                    } catch (NumberFormatException e) {
                        showAlert("Invalid Input", "Please enter valid numbers.");
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlert("Error", "Unexpected error during update.");
                    }
                }
                return null;
            });

            dialog.showAndWait();
        } else {
            showAlert("⚠️ Please select a budget to update.");
        }
    }


    @FXML
    public void exportBudgetToPDF(ActionEvent event) {
        List<Budget> budgets = new ArrayList<>(budgetCategoriesTable.getItems());

        if (budgets.isEmpty()) {
            showAlert("Nothing to Export", "There are no budgets to export.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Budget Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("budgets.pdf");

        File file = fileChooser.showSaveDialog(budgetCategoriesTable.getScene().getWindow());
        if (file == null) {
            return; // User cancelled
        }

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            document.add(new Paragraph("Budget Report"));
            document.add(new Paragraph(" "));

            for (Budget b : budgets) {
                String line = String.format(
                        "Category: %s | Budget: ¥%.2f | Spent: ¥%.2f | Remaining: ¥%.2f | Progress: %.2f%%",
                        b.getCategory(), b.getBudget(), b.getSpent(), b.getRemaining(), b.getProgress()
                );
                document.add(new Paragraph(line));
            }

            document.close();
            showAlert("Success", "Budgets exported to:\n" + file.getAbsolutePath());

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to export PDF: " + e.getMessage());
        }
    }

        private void setupFilterBudget() {
            FilteredList<Budget> filteredData = new FilteredList<>(masterBudgetList, p -> true);

            filterField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(budget -> {
                    if (newValue == null || newValue.isEmpty()) return true;

                    String lowerCaseFilter = newValue.toLowerCase();
                    return budget.getCategory().toLowerCase().contains(lowerCaseFilter);
                });

                // Optional: Update a label to show how many match
                // example: updateBudgetCountLabel(filteredData.size());
            });

            SortedList<Budget> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(budgetCategoriesTable.comparatorProperty());
            budgetCategoriesTable.setItems(sortedData);
        }


        private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
