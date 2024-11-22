package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.Group;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Group loginGroup = new Group();
			Group registerGroup = new Group();
			Group seller1Group = new Group();
			Group buyer1Group = new Group();
			Group admin1Group = new Group();
			
			Scene loginScene = new Scene(loginGroup, 400, 400);
			Scene registerScene = new Scene(registerGroup, 400, 400);
			Scene seller1Scene = new Scene(seller1Group, 400, 400);
			Scene buyer1Scene = new Scene(buyer1Group, 400, 400);
			Scene admin1Scene = new Scene(admin1Group, 400, 400);
			
			
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			DataDesign.Bookstore test = new DataDesign.Bookstore();
			
			
			// Creating the login page functions
			
			Button loginButton = createButton("Log In", 75, 20, 150, 200);
			loginButton.setOnAction(onClick -> {
				
				
			});
			loginGroup.getChildren().add(loginButton);
			
			Button signupButton = createButton("Sign Up", 75, 20, 150, 250);
			signupButton.setOnAction(onClick -> primaryStage.setScene(registerScene));
			loginGroup.getChildren().add(signupButton);
			
			TextField loginUsernameField = createTextField("ASU ID", 50, 125, 130);
			loginGroup.getChildren().add(loginUsernameField);
			
			TextField loginPasswordField = createTextField("Password", 50, 125, 160);
			loginGroup.getChildren().add(loginPasswordField);
			
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
	
}
