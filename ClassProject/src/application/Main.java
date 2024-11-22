package application;
	
import java.text.DecimalFormat;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.*;
import javafx.scene.Group;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			DecimalFormat df = new DecimalFormat("####0.00");
			Group loginGroup = new Group();
			Group registerGroup = new Group();
			Group seller1Group = new Group();
			Group buyer1Group = new Group();
			Group admin1Group = new Group();
			
			Scene loginScene = new Scene(loginGroup, 400, 400);
			Scene registerScene = new Scene(registerGroup, 400, 400);
			Scene seller1Scene = new Scene(seller1Group, 800, 400);
			Scene buyer1Scene = new Scene(buyer1Group, 400, 400);
			Scene admin1Scene = new Scene(admin1Group, 400, 400);
			
			
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			DataDesign.Bookstore bookStore = new DataDesign.Bookstore();
			
			
			// Creating the login page functions
			
			Button loginButton = createButton("Log In", 75, 20, 150, 200);
			loginGroup.getChildren().add(loginButton);
			
			Button signupButton = createButton("Sign Up", 75, 20, 150, 250);
			signupButton.setOnAction(onClick -> primaryStage.setScene(registerScene));
			loginGroup.getChildren().add(signupButton);
			
			TextField loginUsernameField = createTextField("ASU ID", 50, 125, 130);
			loginGroup.getChildren().add(loginUsernameField);
			
			TextField loginPasswordField = createTextField("Password", 50, 125, 160);
			loginGroup.getChildren().add(loginPasswordField);
			
			loginButton.setOnAction(onClick -> {
				String username = loginUsernameField.getText();
				String password = loginPasswordField.getText();
				// FIXME
				// Do authentication based on the inputs
				primaryStage.setScene(seller1Scene); // Used for testing the other scenes, remove when auth is done/being done
				
				
				
			});
			
			
			// Creating the seller page functions and basic layout
			Text seller1Text1 = createText("Title:", 50, 50);
			seller1Group.getChildren().add(seller1Text1);
			
			Text seller1Text2 = createText("Pre Markup Price:", 50, 80);
			seller1Group.getChildren().add(seller1Text2);
			
			Text seller1Text3 = createText("Genre:", 50, 110);
			seller1Group.getChildren().add(seller1Text3);
			
			Text seller1Text4 = createText("Condition:", 50, 140);
			seller1Group.getChildren().add(seller1Text4);
			
			Text seller1Text5 = createText("Title: None", 400, 100);	
			seller1Text5.setStyle("-fx-font: 20 arial;");
			Text seller1Text6 = createText("Final Price: $0.00", 400, 140);		
			Text seller1Text7 = createText("Genre: None", 400, 170);
			Text seller1Text8 = createText("Condition: None", 400, 200);
			seller1Group.getChildren().addAll(seller1Text5, seller1Text6, seller1Text7, seller1Text8);
			
			TextField seller1Field1 = createTextField("Enter Title", 220, 85, 35);
			seller1Group.getChildren().add(seller1Field1);
			
			TextField seller1Field2 = createTextField("Enter Price", 50, 155, 65);
			seller1Group.getChildren().add(seller1Field2);
			
			String genres[] = {"Sci-fi", "Horror", "Fantasy", "Romance"}; //FIXME Can probably create a method to create a ComboBox to clear clutter
			ComboBox<String> seller1ComboBox1 = new ComboBox<>(FXCollections.observableArrayList(genres));
			seller1ComboBox1.setLayoutX(120);
			seller1ComboBox1.setLayoutY(95);
			seller1ComboBox1.setPromptText("SELECT");
			seller1Group.getChildren().add(seller1ComboBox1);
			
			String conditions[] = {"Used, Like New", "Moderately Used", "Heavily Used"}; //FIXME Can probably create a method to create a ComboBox to clear clutter
			ComboBox<String> seller1ComboBox2 = new ComboBox<>(FXCollections.observableArrayList(conditions));
			seller1ComboBox2.setLayoutX(120);
			seller1ComboBox2.setLayoutY(125);
			seller1ComboBox2.setPromptText("SELECT");
			seller1Group.getChildren().add(seller1ComboBox2);
			
			Button seller1Button2 = createButton("List Book", 50, 25, 540, 240);
			seller1Button2.setOnAction(OnClick -> {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("Action Not Available");
				errorAlert.setContentText("Enter Book Information First!");
				errorAlert.showAndWait();
			});
			seller1Group.getChildren().add(seller1Button2);
			
			
			Button seller1Button1 = createButton("Submit Book", 50, 25, 140, 180);
			seller1Button1.setOnAction(OnClick -> {
				String bookTitle = seller1Field1.getText();
				try {
					double bookPrePrice = Double.parseDouble(seller1Field2.getText());
					String bookCategory = seller1ComboBox1.getValue();
					String bookQuality = seller1ComboBox2.getValue();
					
					if (bookCategory == null) {
						Alert errorAlert = new Alert(AlertType.ERROR);
						errorAlert.setHeaderText("Input not valid");
						errorAlert.setContentText("Select a Category!");
						errorAlert.showAndWait();
					}
					else if (bookQuality == null) {
						Alert errorAlert = new Alert(AlertType.ERROR);
						errorAlert.setHeaderText("Input not valid");
						errorAlert.setContentText("Select a Quality!");
						errorAlert.showAndWait();
					}
					else {
						DataDesign.Book tempBook = new DataDesign.Book(bookTitle, bookPrePrice, bookCategory, bookQuality, 1, null);
						seller1Text5.setText("Title: " + bookTitle);
						double bookPrice = tempBook.getFinalPrice();
						
						seller1Text6.setText("Price: $" + df.format(bookPrice));
						seller1Text7.setText("Genre: " + bookCategory);
						seller1Text8.setText("Condition: " + bookQuality);
						
						
					}
					
					
				}
				catch (Exception e) {
					Alert errorAlert = new Alert(AlertType.ERROR);
					errorAlert.setHeaderText("Input not valid");
					errorAlert.setContentText("The Inputted Price is not a Number!");
					errorAlert.showAndWait();
				}
				
				
			});
			seller1Group.getChildren().add(seller1Button1);
			
			
			
			// For the start of the program
			primaryStage.setScene(loginScene);
			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static Button createButton(String text, int width, int height, int x, int y) {
		Button tempButton = new Button();
		
		tempButton.setText(text);
		tempButton.setMinWidth(width);
		tempButton.setMinHeight(height);
		tempButton.setLayoutX(x);
		tempButton.setLayoutY(y);

		return tempButton;
	}
	
	public static TextField createTextField(String prompt, int width, int x, int y) {
		TextField tempField = new TextField();
		
		tempField.setPromptText(prompt);
		tempField.setMinWidth(width);
		tempField.setLayoutX(x);
		tempField.setLayoutY(y);
		
		return tempField;
	}
	
	public static Text createText(String text, int x, int y) {
		Text tempText = new Text();
		
		tempText.setText(text);
		tempText.setLayoutX(x);
		tempText.setLayoutY(y);
		
		return tempText;
	}
	
	
}
