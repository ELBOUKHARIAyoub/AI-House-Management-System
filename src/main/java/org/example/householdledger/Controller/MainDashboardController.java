package org.example.householdledger.Controller;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.example.householdledger.Model.user;
import org.example.householdledger.Session.UserSession;

public class MainDashboardController {

    @FXML
    private TextField HelloName;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TabPane mainTabPane;

    @FXML
    public void initialize() {
        user currentUser = UserSession.getCurrentUser();
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getUserName() + "!");
        }

        // Use Platform.runLater to ensure the scene is available
        Platform.runLater(() -> {
            Scene scene = mainTabPane.getScene();
            if (scene != null) {
                //scene.getStylesheets().add(getClass().getResource("css/tabpane-styles.css").toExternalForm());

                // Add smooth entrance animations
                addEntranceAnimations();

                // Add tab switching animations
                addTabSwitchAnimations();
            }
        });
    }

    private void addEntranceAnimations() {
        if (mainTabPane != null) {
            // Fade in the main content
            FadeTransition fadeIn = new FadeTransition(Duration.millis(800), mainTabPane);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        }

        if (welcomeLabel != null && welcomeLabel.getParent() != null) {
            // Slide in welcome header
            TranslateTransition slideDown = new TranslateTransition(Duration.millis(600));
            slideDown.setNode(welcomeLabel.getParent());
            slideDown.setFromY(-50);
            slideDown.setToY(0);

            FadeTransition welcomeFade = new FadeTransition(Duration.millis(600));
            welcomeFade.setNode(welcomeLabel.getParent());
            welcomeFade.setFromValue(0.0);
            welcomeFade.setToValue(1.0);

            ParallelTransition welcomeAnimation = new ParallelTransition(slideDown, welcomeFade);
            welcomeAnimation.setDelay(Duration.millis(200));
            welcomeAnimation.play();
        }
    }

    private void addTabSwitchAnimations() {
        if (mainTabPane != null) {
            mainTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
                if (newTab != null && newTab.getContent() != null) {
                    Node content = newTab.getContent();

                    // Smooth fade transition for tab content
                    FadeTransition fade = new FadeTransition(Duration.millis(300), content);
                    fade.setFromValue(0.7);
                    fade.setToValue(1.0);

                    // Subtle scale animation
                    ScaleTransition scale = new ScaleTransition(Duration.millis(300), content);
                    scale.setFromX(0.98);
                    scale.setFromY(0.98);
                    scale.setToX(1.0);
                    scale.setToY(1.0);

                    ParallelTransition transition = new ParallelTransition(fade, scale);
                    transition.play();
                }
            });
        }
    }

    // Add hover effects for better interactivity
    private void addHoverEffects(Node node) {
        if (node != null) {
            node.setOnMouseEntered(e -> {
                ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), node);
                scaleUp.setToX(1.05);
                scaleUp.setToY(1.05);
                scaleUp.play();
            });

            node.setOnMouseExited(e -> {
                ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), node);
                scaleDown.setToX(1.0);
                scaleDown.setToY(1.0);
                scaleDown.play();
            });
        }
    }

    // Optional: Method to add hover effects to specific nodes
    public void setupHoverEffects() {
        // You can call this method to add hover effects to specific elements
        // For example, if you have buttons or other interactive elements
        Platform.runLater(() -> {
            // Add hover effects to any specific nodes you want
            // addHoverEffects(someButton);
            // addHoverEffects(someOtherNode);
        });
    }
}