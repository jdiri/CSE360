package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.Group;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Group loginGroup = new Group();
            BuyerView buyerView = new BuyerView();
            SellerView sellerView = new SellerView();

            Scene loginScene = new Scene(loginGroup, 400, 400);
            Scene buyerScene = new Scene(buyerView.getBuyerGroup(), 400, 400);
            Scene sellerScene = new Scene(sellerView.getSellerGroup(), 800, 400);

            // Login Page Setup
            TextField userIdField = createTextField("ASU ID", 200, 100, 120);
            TextField passwordField = createTextField("Password", 200, 100, 160);
            Button loginButton = createButton("Login", 80, 30, 150, 200);

            //add the elements
            loginGroup.getChildren().addAll(userIdField, passwordField, loginButton);
            
            //login to seller needs to be updated to hub
            loginButton.setOnAction(event -> {
                String userId = userIdField.getText();
                String password = passwordField.getText();

                if (userId.equals("123") && password.equals("p")) {
                    primaryStage.setScene(sellerScene);
                } else if (userId.equals("1234") && password.equals("wordpass")) {
                    primaryStage.setScene(buyerScene);
                }
                else {
                    showAlert("Login Failed", "Invalid User ID or Password");
                }
            });
            
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Application");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private void showAlert(String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private Button createButton(String text, int width, int height, int x, int y) {
        Button button = new Button(text);
        button.setMinWidth(width);
        button.setMinHeight(height);
        button.setLayoutX(x);
        button.setLayoutY(y);
        return button;
    }

    private TextField createTextField(String prompt, int width, int x, int y) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setMinWidth(width);
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        return textField;
    }
}
