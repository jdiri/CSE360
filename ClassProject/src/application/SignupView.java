package application;

import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

import java.util.List;

import application.DataDesign.User;

public class SignupView {
	private Group SignupGroup;
	private DataDesign.Bookstore bookstore;
	private String userID;
	private String password;
	private String userType;
	private TextField userIdField;
	private TextField passwordField;
	private ComboBox<String> roleSelection;
	private List<User> users;
	
	
	public SignupView() {
		SignupGroup = new Group();
		userID = "";
		password = "";
		userType = "";
		
        initializeSignupView();
    }
	
	private void initializeSignupView() {
		bookstore = new DataDesign.Bookstore();
		Text signupTitle = createText("Sign Up", 145, 70);
		signupTitle.setStyle("-fx-font: 24 arial;");
		userIdField = createTextField("ASU ID", 200, 100, 120);
        passwordField = createTextField("Password", 200, 100, 160);
        roleSelection = new ComboBox<>(FXCollections.observableArrayList("Seller", "Buyer"));
        roleSelection.setPromptText("Select Role");
        roleSelection.setLayoutX(140);
        roleSelection.setLayoutY(200);
        Button signupButton = createButton("Sign Up", 80, 30, 150, 240);
        
        signupButton.setOnAction(event -> {
        	handleSignup();
        });
        
        SignupGroup.getChildren().addAll(signupTitle, userIdField, passwordField, roleSelection, signupButton);
		
		
        
        
	}
	
	private void handleSignup() {
		userID = userIdField.getText();
		try {
			long tempID = Long.parseLong(userID);
			
			if (tempID < 1000000000 || tempID > 9999999999L) {
				showAlert("Invalid Input", "Invalid User ID!");
				return;
			}
			
			password = passwordField.getText();
			userType = (String)roleSelection.getValue();
			
			users = bookstore.getUsers();
			
			for (User user : users) {
				if (Long.parseLong(user.getASUID()) == tempID) {
					showAlert("Invalid Input", "User ID already Used!");
					return;
				}
			}
			
			
			bookstore.registerUser(userID, password, userType);
		}
		catch (Exception e) {
			showAlert("Invalid Input", "Invalid User ID!");
		}
	}
	
	public Group getSignupGroup() {
        return SignupGroup;
    }
	
	private void showAlert(String header, String content) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }
	
	private Button createButton(String text, int width, int height, int x, int y) {
        Button tempButton = new Button();
        tempButton.setText(text);
        tempButton.setMinWidth(width);
        tempButton.setMinHeight(height);
        tempButton.setLayoutX(x);
        tempButton.setLayoutY(y);
        return tempButton;
    }

    private TextField createTextField(String prompt, int width, int x, int y) {
        TextField tempField = new TextField();
        tempField.setPromptText(prompt);
        tempField.setMinWidth(width);
        tempField.setLayoutX(x);
        tempField.setLayoutY(y);
        return tempField;
    }

    private Text createText(String text, int x, int y) {
        Text tempText = new Text();
        tempText.setText(text);
        tempText.setLayoutX(x);
        tempText.setLayoutY(y);
        return tempText;
    }
}
