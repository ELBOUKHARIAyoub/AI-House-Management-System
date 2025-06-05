package org.example.householdledger.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.example.householdledger.Model.user;
import org.example.householdledger.Session.UserSession;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController1 implements Initializable {

    @FXML
    private ListView<HBox> chatListView;

    @FXML
    private TextField inputField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user currentUser = UserSession.getCurrentUser();
        int userID = currentUser.getUserId();
    }

    // Called when Send button is clicked
    @FXML
    private void onSend() {
        String userInput = inputField.getText().trim();
        if (userInput.isEmpty()) return;

        // Display user's message
        addMessage(userInput, true);
        inputField.clear();

        // Run AI logic in a background thread
        new Thread(() -> {
            try {
                String sql = DeepSeekService.askDeepSeek(userInput);
                System.out.println("Generated SQL:\n" + sql);

                user currentUser = UserSession.getCurrentUser();
                int userId = currentUser.getUserId();

                String result;

                // Check for placeholder usage
                if (sql.contains("?")) {
                    result = DeepSeekService.executeSQL(sql, userId);
                } else {
                    result = DeepSeekService.executeSQL(sql);
                }

                // Generate AI answer based on SQL result
                String response = DeepSeekService.generateAnswer(userInput, result);

                // Optional: include the SQL in the chat response
                String finalOutput = "🧠 SQL:\n" + sql + "\n\n📊 Answer:\n" + response;

                Platform.runLater(() -> addMessage(finalOutput, false));

            } catch (Exception e) {
                Platform.runLater(() -> addMessage("⚠️ Error: " + e.getMessage(), false));
            }
        }).start();
    }

    // Add a chat bubble to the list view
    private void addMessage(String message, boolean isUser) {
        Label label = new Label((isUser ? "🧍 " : "") + message);
        label.setWrapText(true);
        label.setMaxWidth(400);
        label.setStyle(
                "-fx-background-color: " + (isUser ? "#3498db" : "#95a5a6") + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-size: 14px;"
        );

        HBox bubble = new HBox(label);
        bubble.setPadding(new Insets(5));
        bubble.setSpacing(10);

        if (isUser) {
            bubble.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            bubble.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }

        Platform.runLater(() -> {
            chatListView.getItems().add(bubble);
            chatListView.scrollTo(chatListView.getItems().size() - 1);
        });
    }
}
