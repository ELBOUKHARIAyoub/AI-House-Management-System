package org.example.householdledger.Controller;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.example.householdledger.Model.income;
import org.example.householdledger.dao.incomeDAOImpl;
import org.example.householdledger.Session.UserSession;

import java.time.LocalDate;

public class IncomeListCell extends ListCell<income> {
    private HBox displayContent;
    private HBox editContent;
    private Label displayLabel;
    private TextField amountField;
    private TextField nameField;
    private DatePicker datePicker;
    private TextField demoField;
    private Button saveButton;
    private Button cancelButton;
    private Button editButton;
    private Button deleteButton;

    private boolean isEditing = false;
    private incomeDAOImpl incomeDAO;

    public IncomeListCell() {
        this.incomeDAO = new incomeDAOImpl();
        createDisplayContent();
        createEditContent();
    }

    private void createDisplayContent() {
        displayLabel = new Label();
        displayLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #2c3e50;");

        editButton = new Button("✏");
        editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 2 6; -fx-background-radius: 3;");

        deleteButton = new Button("🗑");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 2 6; -fx-background-radius: 3;");

        Region spacer = new Region();

        displayContent = new HBox(10);
        displayContent.setAlignment(Pos.CENTER_LEFT);
        displayContent.getChildren().addAll(displayLabel, spacer, editButton, deleteButton);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        editButton.setOnAction(e -> startEdit());
        deleteButton.setOnAction(e -> deleteItem());
    }

    private void createEditContent() {
        amountField = new TextField();
        amountField.setPromptText("Amount");
        amountField.setPrefWidth(80);
        amountField.setStyle("-fx-font-size: 11px;");

        nameField = new TextField();
        nameField.setPromptText("Source (e.g., Salary)");
        nameField.setPrefWidth(120);
        nameField.setStyle("-fx-font-size: 11px;");

        datePicker = new DatePicker(LocalDate.now());
        datePicker.setPrefWidth(120);
        datePicker.setStyle("-fx-font-size: 11px;");

        demoField = new TextField();
        demoField.setPromptText("Notes");
        demoField.setPrefWidth(100);
        demoField.setStyle("-fx-font-size: 11px;");

        saveButton = new Button("✓");
        saveButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 4 8; -fx-background-radius: 3;");

        cancelButton = new Button("✗");
        cancelButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 4 8; -fx-background-radius: 3;");

        editContent = new HBox(5);
        editContent.setAlignment(Pos.CENTER_LEFT);
        editContent.getChildren().addAll(amountField, nameField, datePicker, demoField, saveButton, cancelButton);

        saveButton.setOnAction(e -> saveItem());
        cancelButton.setOnAction(e -> cancelEdit());

        // Enter key saves, Escape cancels
        amountField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) saveItem();
            else if (e.getCode() == KeyCode.ESCAPE) cancelEdit();
        });
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) saveItem();
            else if (e.getCode() == KeyCode.ESCAPE) cancelEdit();
        });
    }

    @Override
    protected void updateItem(income item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
            return;
        }

        if (isEditing) {
            populateEditFields(item);
            setGraphic(editContent);
        } else {
            if (item.getName() == null || item.getName().isEmpty()) {
                // This is the "Add New" row
                displayLabel.setText("+ Click to add new income...");
                displayLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #27ae60; -fx-font-style: italic;");
                editButton.setVisible(false);
                deleteButton.setVisible(false);

                // Make the whole row clickable for new items
                setOnMouseClicked(e -> {
                    if (e.getClickCount() == 1) {
                        startEdit();
                    }
                });
            } else {
                displayLabel.setText(String.format("¥%.2f - %s (%s)",
                        item.getAmount(), item.getName(), item.getDate().toString()));
                displayLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #2c3e50;");
                editButton.setVisible(true);
                deleteButton.setVisible(true);
                setOnMouseClicked(null);
            }
            setGraphic(displayContent);
        }
    }

    private void populateEditFields(income item) {
        if (item.getName() != null && !item.getName().isEmpty()) {
            amountField.setText(String.valueOf(item.getAmount()));
            nameField.setText(item.getName());
            datePicker.setValue(item.getDate());
            demoField.setText(item.getDemo() != null ? item.getDemo() : "");
        } else {
            // New item - clear fields
            amountField.clear();
            nameField.clear();
            datePicker.setValue(LocalDate.now());
            demoField.clear();
        }
    }

    public void startEdit() {
        isEditing = true;
        updateItem(getItem(), false);
        nameField.requestFocus();
    }

    public void cancelEdit() {
        isEditing = false;
        updateItem(getItem(), false);
    }

    private void saveItem() {
        if (!validateInput()) return;

        try {
            float amount = Float.parseFloat(amountField.getText());
            String name = nameField.getText().trim();
            LocalDate date = datePicker.getValue();
            String demo = demoField.getText().trim();

            income currentItem = getItem();

            if (currentItem.getName() == null || currentItem.getName().isEmpty()) {
                // Adding new income
                 int userId = UserSession.getCurrentUserId();
                incomeDAO.addIncome(userId, name, amount, demo, date);

                // Create new income object and update the item
                income newIncome = new income(userId, name, amount, demo, date);

                // Add to ListView
                getListView().getItems().set(getIndex(), newIncome);

                // Add a new "Add New" row if this was the last item
                if (getIndex() == getListView().getItems().size() - 1) {
                    getListView().getItems().add(new income()); // Empty income for "Add New" row
                }

                showSuccessMessage("Income added successfully!");
            } else {
                // Updating existing income (you'll need to implement updateIncome in DAO)
                currentItem.setAmount(amount);
                currentItem.setName(name);
                currentItem.setDate(date);
                currentItem.setDemo(demo);

                showSuccessMessage("Income updated successfully!");
            }

            isEditing = false;
            updateItem(getItem(), false);

        } catch (Exception e) {
            showErrorMessage("Error saving income: " + e.getMessage());
        }
    }

    private void deleteItem() {
        income item = getItem();
        if (item != null && item.getName() != null && !item.getName().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Income");
            alert.setHeaderText("Delete this income entry?");
            alert.setContentText(String.format("¥%.2f - %s", item.getAmount(), item.getName()));

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    getListView().getItems().remove(item);
                    // Here you would also delete from database
                    // incomeDAO.deleteIncome(item.getIncomeId());
                    showSuccessMessage("Income deleted successfully!");
                }
            });
        }
    }

    private boolean validateInput() {
        if (amountField.getText().trim().isEmpty()) {
            showErrorMessage("Please enter an amount");
            amountField.requestFocus();
            return false;
        }

        try {
            float amount = Float.parseFloat(amountField.getText());
            if (amount <= 0) {
                showErrorMessage("Amount must be greater than 0");
                amountField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Please enter a valid number for amount");
            amountField.requestFocus();
            return false;
        }

        if (nameField.getText().trim().isEmpty()) {
            showErrorMessage("Please enter an income source");
            nameField.requestFocus();
            return false;
        }

        if (datePicker.getValue() == null) {
            showErrorMessage("Please select a date");
            datePicker.requestFocus();
            return false;
        }

        return true;
    }

    private void showSuccessMessage(String message) {
        // You can implement a toast notification or status bar update here
        System.out.println("SUCCESS: " + message);
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
