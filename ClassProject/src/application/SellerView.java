package application;

import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

import java.text.DecimalFormat;

public class SellerView {
    private Group sellerGroup;
    private Text sellerTitleText;
    private Text sellerPriceText;
    private Text sellerGenreText;
    private Text sellerConditionText;
    private TextField titleField;
    private TextField priceField;
    private ComboBox<String> genreComboBox;
    private ComboBox<String> conditionComboBox;
    private DecimalFormat df;

    public SellerView() {
        df = new DecimalFormat("####0.00");
        sellerGroup = new Group();
        initializeSellerView();
    }

    private void initializeSellerView() {
        sellerGroup.getChildren().add(createText("Title:", 50, 50));
        sellerGroup.getChildren().add(createText("Pre Markup Price:", 50, 80));
        sellerGroup.getChildren().add(createText("Genre:", 50, 110));
        sellerGroup.getChildren().add(createText("Condition:", 50, 140));

        sellerTitleText = createText("Title: None", 400, 100);
        sellerPriceText = createText("Final Price: $0.00", 400, 140);
        sellerGenreText = createText("Genre: None", 400, 170);
        sellerConditionText = createText("Condition: None", 400, 200);
        sellerGroup.getChildren().addAll(sellerTitleText, sellerPriceText, sellerGenreText, sellerConditionText);

        titleField = createTextField("Enter Title", 220, 85, 35);
        priceField = createTextField("Enter Price", 50, 155, 65);
        sellerGroup.getChildren().addAll(titleField, priceField);

        genreComboBox = new ComboBox<>(FXCollections.observableArrayList("Sci-fi", "Horror", "Fantasy", "Romance"));
        genreComboBox.setLayoutX(120);
        genreComboBox.setLayoutY(95);
        genreComboBox.setPromptText("SELECT");
        sellerGroup.getChildren().add(genreComboBox);

        conditionComboBox = new ComboBox<>(FXCollections.observableArrayList("Used Like New", "Moderately Used", "Heavily Used"));
        conditionComboBox.setLayoutX(120);
        conditionComboBox.setLayoutY(125);
        conditionComboBox.setPromptText("SELECT");
        sellerGroup.getChildren().add(conditionComboBox);

        Button submitButton = createButton("Submit Book", 50, 25, 140, 180);
        submitButton.setOnAction(event -> handleSubmit());
        sellerGroup.getChildren().add(submitButton);

        Button listButton = createButton("List Book", 50, 25, 540, 240);
        listButton.setOnAction(event -> showAlert("Action Not Available", "Enter Book Information First!"));
        sellerGroup.getChildren().add(listButton);
    }

    private void handleSubmit() {
        String bookTitle = titleField.getText();
        try {
            double bookPrePrice = Double.parseDouble(priceField.getText());
            String bookCategory = genreComboBox.getValue();
            String bookQuality = conditionComboBox.getValue();

            if (bookCategory == null) {
                showAlert("Input not valid", "Select a Category!");
            } else if (bookQuality == null) {
                showAlert("Input not valid", "Select a Quality!");
            } else {
                sellerTitleText.setText("Title: " + bookTitle);
				DataDesign.Book tempBook = new DataDesign.Book(bookTitle, bookPrePrice, bookCategory, bookQuality, 1, null);
                double finalPrice = tempBook.getFinalPrice(); // Assuming some markup logic
                sellerPriceText.setText("Price: $" + df.format(finalPrice));
                sellerGenreText.setText("Genre: " + bookCategory);
                System.out.print(bookCategory);
                sellerConditionText.setText("Condition: " + bookQuality);
            }
        } catch (Exception e) {
            showAlert("Input not valid", "The Inputted Price is not a Number!");
        }
    }

    private void showAlert(String header, String content) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }

    public Group getSellerGroup() {
        return sellerGroup;
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
