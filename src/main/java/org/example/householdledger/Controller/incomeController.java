package org.example.householdledger.Controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.householdledger.Model.expense;
import org.example.householdledger.Model.user;
import org.example.householdledger.dao.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.householdledger.Model.income;
import org.example.householdledger.Session.UserSession;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class incomeController implements Initializable {
    @FXML
    private Button loadIncomeTableBtn;

    @FXML
    private AnchorPane incomeTableContainer1;
    // FXML Controls
    @FXML private TableView<income> TableViewIncome;
    @FXML private TableView<income> TableViewExpense;
    @FXML private TableColumn<income, String> nameColumn;
    @FXML private TableColumn<income, Double> amountColumn;
    @FXML private TableColumn<income, LocalDate> dateColumn;
    @FXML private TableColumn<income, String> demoColumn;
    @FXML private ListView<income> incomeListView;
    @FXML private TextField searchField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Label incomeCountLabel;
    @FXML private Label expenseCountLabel;
    @FXML private Label totalIncomeLabel;
    @FXML private Label totalExpensesLabel;
    @FXML private Label netIncomeLabel;
    @FXML private Label savingsRateLabel;
    @FXML private Label incomeChangeLabel;
    @FXML private Label expenseChangeLabel;
    @FXML private Label netChangeLabel;
    @FXML private Label statusLabel;
    @FXML private Label lastUpdatedLabel;
    @FXML private Button addIncomeBtn;

    @FXML private ListView<expense> expenseListView;
    @FXML private AnchorPane AnchorPane;
@FXML private Label test;
    // Class variables
    private incomeDAOImpl incomeDAO;
    private int currentUserId;
    private ObservableList<income> allIncomes;
    private ObservableList<income> allExpenses; // Store all incomes for filtering
     // Store all incomes for filtering
private expenseDAOlmpl expenseDAO;
    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize DAOs
        incomeDAO = new incomeDAOImpl();
        expenseDAO = new expenseDAOlmpl();

        // Set up the custom cell factory for the ListView

        updateSummaryCards();
        // Setup search filter or other UI configs
        setupSearchFilter();

        // Get the current logged-in user
        user currentUser = UserSession.getCurrentUser();

        if (currentUser != null) {
            System.out.println("Current user: " + currentUser);
            currentUserId = currentUser.getUserId(); // Do NOT add +1 unless required
                        // Populate the ListView with this user’s expenses
           // loadIncomeData(currentUserId);           // Optionally for income table
        } else {
            updateStatus("No user session found");
        }

        System.out.println("TableViewIncome is null? " + (TableViewIncome == null));
    }


    // Method to load and display income data

    // Setup TableView columns
    private void setupIncomeTableView() {
      /*  if (incomeTableView == null) {
            System.err.println("incomeTableView is null - check FXML fx:id");
            return;
        }*/

        // Check if columns are null
        /*if (nameColumn == null || amountColumn == null || dateColumn == null ) {
            System.err.println("One or more table columns are null - check FXML fx:id attributes");
            return;
        }*/


// Format amount column with currency


// Format date column


        // Setup cell value factories

    }

    // Setup search functionality
    private void setupSearchFilter() {
        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterIncomeList();
            });
        }

        // Setup date picker listeners
        if (startDatePicker != null) {
            startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                filterIncomeList();
            });
        }

        if (endDatePicker != null) {
            endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                filterIncomeList();
            });
        }
    }

    // Filter income list based on search and date filters
    private void filterIncomeList() {
        if (allIncomes == null) return;

        ObservableList<income> filteredList = FXCollections.observableArrayList();
        String searchText = searchField != null ? searchField.getText().toLowerCase() : "";
        LocalDate startDate = startDatePicker != null ? startDatePicker.getValue() : null;
        LocalDate endDate = endDatePicker != null ? endDatePicker.getValue() : null;

        for (income inc : allIncomes) {
            boolean matchesSearch = searchText.isEmpty() ||
                    (inc.getName() != null && inc.getName().toLowerCase().contains(searchText)) ||
                    (inc.getDemo() != null && inc.getDemo().toLowerCase().contains(searchText));

            boolean matchesDateRange = true;
            if (startDate != null && inc.getDate() != null && inc.getDate().isBefore(startDate)) {
                matchesDateRange = false;
            }
            if (endDate != null && inc.getDate() != null && inc.getDate().isAfter(endDate)) {
                matchesDateRange = false;
            }

            if (matchesSearch && matchesDateRange) {
                filteredList.add(inc);
            }
        }

        TableViewIncome.setItems(filteredList);
        updateIncomeCountLabel(filteredList.size());
    }

    // Update helper methods
    private void updateIncomeCountLabel() {
        updateIncomeCountLabel(allIncomes != null ? allIncomes.size() : 0);
    }

    private void updateIncomeCountLabel(int count) {
        if (incomeCountLabel != null) {
            incomeCountLabel.setText(count + " entries");
        }
    }

    private void updateSummaryCards() {
        incomeDAOImpl dao1 = new incomeDAOImpl();
        expenseDAOlmpl dao = new expenseDAOlmpl();
        user currentUser = UserSession.getCurrentUser();
        int currentUserId1 = currentUser.getUserId();
        float totalIncome = dao1.getTotalIncomeByUserId(currentUserId1);
        float totalExpense = dao.getTotalExpenseByUserId(currentUserId1);

        float netIncome = totalIncome-totalExpense;
        float savingrate = (netIncome /totalIncome) *100;
        System.out.println("Total income: ¥" + totalIncome);
        System.out.println("Total Expense: ¥" + totalExpense);



        if (totalIncomeLabel != null) {
            totalIncomeLabel.setText("¥" + String.format("%.2f", totalIncome));
        }
        if (totalExpensesLabel != null) {
            totalExpensesLabel.setText("¥" + String.format("%.2f", totalExpense));
        }
        netIncomeLabel.setText("¥" + String.format("%.2f", netIncome));
        savingsRateLabel.setText(String.format("%.2f", savingrate));
        // You can add more summary calculations here
        // For expenses, net income, savings rate, etc.
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
        if (lastUpdatedLabel != null) {
            lastUpdatedLabel.setText("Last updated: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        }
        System.out.println("Status: " + message); // Also print to console for debugging
    }

    // FXML Event Handlers
    @FXML
    private void applyFilter() {
        filterIncomeList();
        updateStatus("Filter applied");
    }

    @FXML
    private void clearFilter() {
        if (searchField != null) searchField.clear();
        if (startDatePicker != null) startDatePicker.setValue(null);
        if (endDatePicker != null) endDatePicker.setValue(null);

        if (allIncomes != null) {
            TableViewIncome.setItems(allIncomes);
            updateIncomeCountLabel();
        }
        updateStatus("Filters cleared");
    }

    @FXML
    private void addIncome() {
        updateStatus("Add Income clicked - implement your add dialog here");
        // TODO: Implement add income dialog
        // After adding, call refreshIncomeList() to update the view
    }

    @FXML
    private void editSelectedIncome() {
        income selected = TableViewIncome.getSelectionModel().getSelectedItem();
        if (selected != null) {
            updateStatus("Edit Income clicked for: " + selected.getName());
            // TODO: Implement edit functionality
        } else {
            updateStatus("No income selected for editing");
        }
    }

    @FXML
    private void deleteSelectedIncome() {
        income selected = TableViewIncome.getSelectionModel().getSelectedItem();
        if (selected != null) {
            updateStatus("Delete Income clicked for: " + selected.getName());
            // TODO: Implement delete confirmation and logic
        } else {
            updateStatus("No income selected for deletion");
        }
    }

    @FXML
    private void refreshData() {
       // loadIncomeData(currentUserId);
        updateSummaryCards();
    }

    @FXML
    private void viewReports() {
        updateStatus("View Reports clicked - implement your reports view");
    }

    @FXML
    private void manageCategories() {
        updateStatus("Manage Categories clicked - implement category management");
    }

    @FXML
    private void exportData() {
        updateStatus("Export Data clicked - implement data export");
    }

    @FXML
    private void openSettings() {
        updateStatus("Settings clicked - implement settings dialog");
    }

    @FXML
    private void handleIncomeSelection() {
        income selectedIncome = TableViewIncome.getSelectionModel().getSelectedItem();
        if (selectedIncome != null) {
            updateStatus("Selected: " + selectedIncome.getName() + " - ¥" + selectedIncome.getAmount());
        }
    }

    // Method to refresh the list (useful for updates)
    public void refreshIncomeList() {
        if (currentUserId > 0) {
           // loadIncomeData(currentUserId);
        }
    }

    // Public method to set user ID and load data
    public void setUserId(int userId) {
        this.currentUserId = userId;
      //  loadIncomeData(userId);
    }
//==================================================expense part ======================================================================





    @FXML
    private void loadIncomeTable() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/householdledger/incomeTable.fxml"));

            // 🧠 Manually create controller instance and set it


            Parent TableViewIncome = loader.load();

            // Initialize with current user



            // Create a new scene and show it in a new window
            Stage stage = new Stage();
            Scene scene = new Scene(TableViewIncome);
            stage.setTitle("Income Table");
            stage.setScene(scene);
            stage.show();

            //loadIncomeTableBtn.setVisible(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

@FXML
void loadExpenseTable(){
try{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/householdledger/expenseTable.fxml"));

    // 🧠 Manually create controller instance and set it


    Parent TableViewExpense = loader.load();

    // Initialize with current user



    // Create a new scene and show it in a new window
    Stage stage = new Stage();
    Scene scene = new Scene(TableViewExpense);
        stage.setTitle("Expense Table");
    stage.setScene(scene);
    stage.show();

    //loadIncomeTableBtn.setVisible(false);

} catch (IOException e) {
        e.printStackTrace();
    }

}
}