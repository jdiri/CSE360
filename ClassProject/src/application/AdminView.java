package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import application.DataDesign.Seller;
import application.DataDesign.User;

import java.util.ArrayList;
import java.util.List;

import application.DataDesign.Buyer;
import application.DataDesign.Admin;

public class AdminView {
	private Group adminGroup;
	private Admin currentUser;
	private DataDesign.Bookstore bookstore = new DataDesign.Bookstore();
	private List<User> potentialSellers;
	private ObservableList<Long> potentialSellersID = FXCollections.observableArrayList();
	private ListView potentialSellerList;
	
	public AdminView() {
        adminGroup = new Group();
        initializeAdminView();
    }
	
	public Group getAdminGroup() {
		return adminGroup;
	}
	
	private void initializeAdminView() {
		Text adminTitle = createText("Admin Page", 100, 50);
		adminTitle.setStyle("-fx-font: 24 arial;");
		adminGroup.getChildren().add(adminTitle);
		
		potentialSellers = bookstore.getPendingSellers();
		
		potentialSellerList = new ListView();
		
		for (User seller: potentialSellers) {
			potentialSellersID.add(Long.parseLong(seller.getASUID()));
		}
		
		potentialSellerList.setItems(potentialSellersID);
		potentialSellerList.setLayoutX(350);
		adminGroup.getChildren().add(potentialSellerList);
		
		Button acceptButton = createButton("Accept User", 90, 50, 150, 150);
		acceptButton.setStyle("-fx-base: Green");
		Button rejectButton = createButton("Reject User", 90, 50, 150, 220);
		rejectButton.setStyle("-fx-base: Red");
		
		Button exitButton = createButton("X", 10, 10, 10, 10);
		exitButton.setStyle("-fx-base: Red");
		adminGroup.getChildren().addAll(acceptButton, rejectButton);
		
		acceptButton.setOnAction(event -> {
			handleAccept();
		});
		rejectButton.setOnAction(event -> {
			handleReject();
		});
		
		
		
	}
	
	public void setCurrentUser(Admin currentUser) {
    	this.currentUser = currentUser;
    }
	
	private void handleAccept() {
		String potentialID = potentialSellerList.getSelectionModel().getSelectedItem() + "";
		String tempID = "";

		for (User seller : potentialSellers) {
			tempID = seller.getASUID();
			if (tempID.equals(potentialID)) {
				bookstore.approveSeller(seller);
				break;
			}
		}
		potentialSellers = bookstore.getPendingSellers();
		potentialSellerList.getItems().clear();
		for (User seller: potentialSellers) {
			potentialSellersID.add(Long.parseLong(seller.getASUID()));
		}
		
		potentialSellerList.setItems(potentialSellersID);
	}
	
	private void handleReject() {
		String potentialID = potentialSellerList.getSelectionModel().getSelectedItem() + "";
		String tempID = "";

		for (User seller : potentialSellers) {
			tempID = seller.getASUID();
			if (tempID.equals(potentialID)) {
				bookstore.denySeller(seller);
				break;
			}
		}
		potentialSellers = bookstore.getPendingSellers();
		potentialSellerList.getItems().clear();
		for (User seller: potentialSellers) {
			potentialSellersID.add(Long.parseLong(seller.getASUID()));
		}
		
		potentialSellerList.setItems(potentialSellersID);
		
		
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
