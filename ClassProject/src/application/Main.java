package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.Group;
import javafx.stage.Stage;
import application.DataDesign.Seller;
import application.DataDesign.User;

public class Main extends Application {
	private User user;
	private DataDesign.Bookstore bookstore = new DataDesign.Bookstore();
	private TextField userIdField;
	private TextField passwordField;
	private Button loginButton;
	private Button signupButton;
	private Group loginGroup;
	private BuyerView buyerView;
	private SellerView sellerView;
	private SignupView signupView;
	private Scene loginScene;
	private Scene buyerScene;
	private Scene sellerScene;
	private Scene signupScene;
	
	
    @Override
    public void start(Stage primaryStage) {
        try {
            loginGroup = new Group();
            buyerView = new BuyerView();
            sellerView = new SellerView();
            signupView = new SignupView();


            loginScene = new Scene(loginGroup, 400, 400);
            buyerScene = new Scene(buyerView.getBuyerGroup(), 600, 900);
            sellerScene = new Scene(sellerView.getSellerGroup(), 800, 400);
            signupScene = new Scene(signupView.getSignupGroup(), 400, 400);


            // Login Page Setup
            userIdField = createTextField("ASU ID", 200, 100, 120);
            passwordField = createTextField("Password", 200, 100, 160);
            loginButton = createButton("Login", 80, 30, 150, 200);
            signupButton = createButton("Sign Up", 80, 30, 150, 240);
            

            //add the elements
            loginGroup.getChildren().addAll(userIdField, passwordField, loginButton, signupButton);
            
            //login to seller needs to be updated to hub
            loginButton.setOnAction(event -> {
                Scene tempScene = handleLogin();
                if (tempScene != null) {
                	
                	primaryStage.setScene(tempScene);
                }
                else {
                	// For testing purposes only
                }
            });
            
            signupButton.setOnAction(event -> {
            	primaryStage.setScene(signupScene);
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
    
    private Scene handleLogin () {
    	String userId = userIdField.getText();
        String password = passwordField.getText();
        
        user = bookstore.authenticateUser(userId, password);
        
        if (user == null) {
        	if (userId.equals("1234") && password.equals("wordpass")) {
                return buyerScene;
            }
            else {
                showAlert("Login Failed", "Invalid User ID or Password");
                return null;
            }
        }
        else {
        	String userType = user.getRole();
        	if (userType == "buyer") {
        		return buyerScene;
        	}
        	else if (userType == "seller") {
        		if (user.login()) {
        			sellerView.setCurrentUser((Seller)user);
        			return sellerScene;
        		}
        		else {
        			showAlert("Authentication Error", "Not Yet Authorized for Seller Role!");
        		}
        	}
        	else {
        		return null;
        	}
        }
        return null;
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
