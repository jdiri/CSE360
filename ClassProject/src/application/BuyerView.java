package application;

import java.text.DecimalFormat;

import application.DataDesign.Book;
import application.DataDesign.Bookstore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class BuyerView {
	private Group buyerGroup;
	private ComboBox<String> categoryComboBox;
    private ComboBox<String> conditionComboBox;
    private ListView<String> bookListView;
    private TextField searchTextField;
	private DecimalFormat df;
	
	public BuyerView() {
        df = new DecimalFormat("####0.00");
        buyerGroup = new Group();
        initializeBuyerView();
    }
	
	private void initializeBuyerView() {
		buyerGroup.getChildren().add(createText("Select Category:", 50, 50));
        buyerGroup.getChildren().add(createText("Select Condition:", 50, 100));
        
        //Set up category cbox
        categoryComboBox = new ComboBox<>(FXCollections.observableArrayList("Sci-fi", "Horror", "Fantasy", "Romance"));
        categoryComboBox.setLayoutX(150);
        categoryComboBox.setLayoutY(35);
        categoryComboBox.setPromptText("Choose Category");
        buyerGroup.getChildren().add(categoryComboBox);
        
        //Set up condition cbox
        conditionComboBox = new ComboBox<>(FXCollections.observableArrayList("Used, Like New", "Moderately Used", "Heavily Used"));
        conditionComboBox.setLayoutX(150);
        conditionComboBox.setLayoutY(85);
        conditionComboBox.setPromptText("Choose Condition");
        buyerGroup.getChildren().add(conditionComboBox);
        
        //Set up Book List
        bookListView = new ListView<>();
        bookListView.setLayoutX(350);
        bookListView.setLayoutY(30);
        bookListView.setPrefSize(260, 550);
        buyerGroup.getChildren().add(bookListView);
        
        //Set up search field 
        searchTextField = new TextField();
        searchTextField.setPromptText("Search by Book Title...");
        searchTextField.setLayoutX(150);
        searchTextField.setLayoutY(135);
        buyerGroup.getChildren().add(searchTextField);

        //Set up Search Button
        Button searchButton = createButton("Search Books", 120, 30, 50, 380);
        searchButton.setOnAction(event -> searchBooks());
        buyerGroup.getChildren().add(searchButton);

	}
	
	private void searchBooks() {
		String searchQuery = searchTextField.getText().toLowerCase();
        String selectedCategory = categoryComboBox.getValue();
        String selectedCondition = conditionComboBox.getValue();
        
        if (searchQuery.isEmpty() && selectedCategory == null || selectedCondition == null) {
            showAlert("Selection Missing", "Please select both a category and a condition.");
        } else {
        	Bookstore bookstore = Bookstore.getInstance();
            ObservableList<String> filteredBooks = FXCollections.observableArrayList();

            for (Book book : bookstore.getBooksByFilter(selectedCategory, selectedCondition)) {
                // If title is provided, match the title with the search query
                if (searchQuery.isEmpty() || book.getTitle().toLowerCase().contains(searchQuery)) {
                    filteredBooks.add(formatBookDisplay(book));
                }
            }

            if (filteredBooks.isEmpty()) {
                showAlert("No Books Found", "No books match your criteria.");
            } else {
                bookListView.setItems(filteredBooks);
            }
        }
    }
	
	private String formatBookDisplay(Book book) {
		return String.format("%s - $%.2f - Qty: %d", book.getTitle(), book.getPreMarkupPrice(), book.getQuantity());
    }
	
	public Group getBuyerGroup() {
        return buyerGroup;
    }
	
	private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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

    private Text createText(String text, int x, int y) {
        Text tempText = new Text();
        tempText.setText(text);
        tempText.setLayoutX(x);
        tempText.setLayoutY(y);
        return tempText;
    }
}
