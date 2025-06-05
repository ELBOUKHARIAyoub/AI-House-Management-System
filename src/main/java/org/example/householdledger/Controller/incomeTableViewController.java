package org.example.householdledger.Controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.example.householdledger.Model.income;
import org.example.householdledger.Model.user;
import org.example.householdledger.Session.UserSession;
import org.example.householdledger.dao.incomeDAOImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.TableColumn;

public class incomeTableViewController implements Initializable {

    @FXML
    private Label incomeCountLabel;

    private incomeDAOImpl incomeDAO = new incomeDAOImpl();
    private int currentUserId;

    @FXML private TableView<income> TableViewIncome;
    @FXML private TableColumn<income, String> ColumnName;
    @FXML private TableColumn<income, String> ColumnAmount;
    @FXML private TableColumn<income, String> ColumnDate;
    @FXML private TableColumn<income, String> ColumnDemo;
    @FXML private Button editIncomeBtn;
    @FXML private Button deleteIncomeBtn;
    @FXML private TextField filterNameField;
    @FXML private DatePicker filterDatePicker;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("✅ Controller initialized");
        System.out.println("TableViewIncome is null? " + (TableViewIncome == null));
        user currentUser = UserSession.getCurrentUser();
        this.currentUserId = currentUser.getUserId();  // 👈 properly assign to the field

        System.out.println("✅ Current user ID: " + currentUserId);
        loadIncomeData(currentUserId);
        // Basic setup only, no user ID-specific logic here
    }

    // ✅ Custom init method you can call manually
    public void initData(int userId) {


    }
    @FXML
    private void handleApplyFilter() {
        String nameFilter = filterNameField.getText().toLowerCase();
        LocalDate dateFilter = filterDatePicker.getValue();

        ObservableList<income> allIncomes = TableViewIncome.getItems();
        ObservableList<income> filtered = FXCollections.observableArrayList();

        for (income inc : incomeDAO.getIncome(currentUserId)) {
            boolean nameMatches = (nameFilter.isEmpty() || inc.getName().toLowerCase().contains(nameFilter));
            boolean dateMatches = (dateFilter == null || dateFilter.equals(inc.getDate()));

            if (nameMatches && dateMatches) {
                filtered.add(inc);
            }
        }

        TableViewIncome.setItems(filtered);
        updateIncomeCountLabel(filtered.size());
    }
    @FXML
    private void handleExportToPdf() {
        try {
            List<income> incomes = TableViewIncome.getItems();
            if (incomes.isEmpty()) {
                showAlert("No Data", "No income data available to export.");
                return;
            }

            // Let user choose file location and name
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF");
            fileChooser.setInitialFileName("Income_Report_" + LocalDate.now() + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            File selectedFile = fileChooser.showSaveDialog(TableViewIncome.getScene().getWindow());

            if (selectedFile == null) {
                return; // User cancelled
            }

            String filePath = selectedFile.getAbsolutePath();

            // Generate PDF
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("Income Report - " + LocalDate.now() + "\n\n"));
            PdfPTable table = new PdfPTable(4);
            table.setWidths(new int[]{3, 2, 4, 3});
            table.setSpacingBefore(10);

            // Table headers
            table.addCell("Source");
            table.addCell("Amount");
            table.addCell("Comment");
            table.addCell("Date");

            // Table data
            for (income inc : incomes) {
                table.addCell(inc.getName());
                table.addCell("¥" + inc.getAmount());
                table.addCell(inc.getDemo());
                table.addCell(inc.getDate().toString());
            }

            document.add(table);
            document.close();

            showAlert("Exported", "PDF successfully exported to:\n" + filePath);
            openPdf(filePath); // Optional: Open after export

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not export PDF.\n" + e.getMessage());
        }
    }

    private void openPdf(String filePath) {
        try {
            java.awt.Desktop.getDesktop().open(new java.io.File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Open Failed", "Could not open the PDF automatically.");
        }
    }


    @FXML
    private void handleClearFilter() {
        filterNameField.clear();
        filterDatePicker.setValue(null);
        loadIncomeData(currentUserId);
    }


    public void loadIncomeData(int userId) {
        try {
            List<income> incomes = incomeDAO.getIncome(userId);
            if (incomes != null && !incomes.isEmpty()) {

                ColumnName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
                ColumnAmount.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.format("¥%.2f", cellData.getValue().getAmount())));
                ColumnDemo.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDemo()));
                ColumnDate.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                        cellData.getValue().getDate() != null
                                ? cellData.getValue().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                : "N/A"));


                ObservableList<income> observableList = FXCollections.observableArrayList(incomes);
                TableViewIncome.setItems(observableList);
                updateIncomeCountLabel(incomes.size());
            } else {
                updateIncomeCountLabel(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateIncomeCountLabel(int count) {
        if (incomeCountLabel != null) {
            incomeCountLabel.setText(count + " entries");
        }
    }


    @FXML
    private void handleDeleteIncome() {
        income selected = TableViewIncome.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Are you sure you want to delete:");
            alert.setContentText(selected.toTableString());

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    incomeDAO.deleteIncome(selected.getIncomeId()); // ✅ remove from DB
                    TableViewIncome.getItems().remove(selected);   // ✅ remove from UI
                    updateIncomeCountLabel(TableViewIncome.getItems().size());
                }
            });
        } else {
            showAlert("No Selection", "Please select an income row to delete.");
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void handleEditIncome() {
        income selected = TableViewIncome.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Example edit dialog for name (you can add more fields later)
            TextInputDialog dialog = new TextInputDialog(selected.getName());
            dialog.setTitle("Edit Income");
            dialog.setHeaderText("Editing income for: " + selected.getDate());
            dialog.setContentText("New Name:");

            dialog.showAndWait().ifPresent(newName -> {
                selected.setName(newName);
                incomeDAO.updateIncome(selected); // ✅ update DB
                TableViewIncome.refresh(); // ✅ refresh table
            });

        } else {
            showAlert("No Selection", "Please select an income row to edit.");
        }
    }
    @FXML
    private void handleAddIncome() {
        Dialog<income> dialog = new Dialog<>();
        dialog.setTitle("Add New Income");
        dialog.setHeaderText("Enter details for new income record");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create input fields
        TextField nameField = new TextField();
        nameField.setPromptText("Source");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        TextField demoField = new TextField();
        demoField.setPromptText("Comment");

        DatePicker datePicker = new DatePicker(LocalDate.now());

        VBox content = new VBox(10, new Label("Source:"), nameField,
                new Label("Amount:"), amountField,
                new Label("Comment:"), demoField,
                new Label("Date:"), datePicker);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String name = nameField.getText();
                    float amount = Float.parseFloat(amountField.getText());
                    String demo = demoField.getText();
                    LocalDate date = datePicker.getValue();
                   // user currentUser = UserSession.getCurrentUser();
                    //int currentUserId = currentUser.getUserId();
                    System.out.println("🧠 Inserting income for userId = " + currentUserId);

                    income newIncome = new income(currentUserId, name, amount, demo, date);
                   // TableViewIncome.setItems(loadIncomeDataFromDatabase());
                    TableViewIncome.refresh();
                    return newIncome;

                } catch (Exception e) {
                    showAlert("Invalid input", "Please enter valid values.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newIncome -> {
            // Save to database
            incomeDAO.addIncome(newIncome.getUserId(), newIncome.getName(), newIncome.getAmount(), newIncome.getDemo(), newIncome.getDate());

            // Reload table
            loadIncomeData(currentUserId);
        });
    }


}


