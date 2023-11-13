package com.app.dictionaryproject.Controller;

import com.app.dictionaryproject.Models.Word;
import com.app.dictionaryproject.service.DBRepository;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class EditScreenController {
    @FXML
    public Scene scene;
    public Stage stage;
    public Parent root;
    public TextField wordInput = new TextField();

    public TabPane tabPane = new TabPane();
    public  Tab editWordTab = new Tab();
    public Tab addWordTab = new Tab();

    public  TextField addPhonetic = new TextField();
    public TextField addType = new TextField();
    public TextArea addExplain = new TextArea();
    public TextField addSynonym = new TextField();
    public TextField addAntonym = new TextField();

    //Edit word
    public  TextField editPhonetic = new TextField();
    public TextField editType = new TextField();
    public TextArea editExplain = new TextArea();
    public TextField editSynonym = new TextField();
    public TextField editAntonym = new TextField();

    public DBRepository database = new DBRepository();


    public void initialize() {

        editWordTab.setStyle("-fx-background-color: rgb(99, 122, 242); " +
                "-fx-text-fill:white; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 10px;");

        addWordTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Apply styles when the tab is selected
                addWordTab.setStyle("-fx-background-color:white; " +
                        "-fx-text-fill: rgb(99, 122, 242); " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 25px;");
            } else {
                // Apply styles when the tab is not selected
                addWordTab.setStyle("-fx-background-color: rgb(99, 122, 242); " +
                        "-fx-text-fill:white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 25px;");
            }
        });

        editWordTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Apply styles when the tab is selected
                editWordTab.setStyle("-fx-background-color:white; " +
                        "-fx-text-fill: rgb(99, 122, 242); " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 20px; " +
                        "-fx-background-radius: 10px;");
            } else {
                // Apply styles when the tab is not selected
                editWordTab.setStyle("-fx-background-color: rgb(99, 122, 242); " +
                        "-fx-text-fill:white; " +
                        "-fx-font-size: 20px; " +
                        "-fx-background-radius: 10px;");
            }
        });
    }


    public void  switchToMain(ActionEvent event) throws IOException {

        FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/MainScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(Loader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void addWord() {
        String word = wordInput.getText();
        String phonetic = addPhonetic.getText();
        String type = addType.getText();
        String explain = addExplain.getText();
        String synonym = addSynonym.getText();
        String antonym = addAntonym.getText();
        System.out.println(antonym);
        Word newWord = new Word(word,phonetic, type, explain, synonym, antonym);
        database.insertWord(newWord);
        clearAddWordFields();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Word added successfully.");
        // Clear input fields after adding the word

    }
    public void setTextEditWord(MouseEvent event) {
        String word = wordInput.getText();
        if(!word.isEmpty()) {
            editPhonetic.setText(database.searchWord(word).getPhonetic());
            editType.setText(database.searchWord(word).getWordType());
            editExplain.setText(database.searchWord(word).getDefinitionWord());
            editSynonym.setText(database.searchWord(word).getSynonym());
            editAntonym.setText(database.searchWord(word).getAntonym());
        }
    }

    public void enterSaveWord(KeyEvent event){
            String word = wordInput.getText();
            if ( event.getCode() == KeyCode.ENTER ) {
                editPhonetic.setText(database.searchWord(word).getPhonetic());
                editType.setText(database.searchWord(word).getWordType());
                editExplain.setText(database.searchWord(word).getDefinitionWord());
                editSynonym.setText(database.searchWord(word).getSynonym());
                editAntonym.setText(database.searchWord(word).getAntonym());
            }
    }

    public void editWord() {

        String word     = wordInput.getText();
        String phonetic = editPhonetic.getText();
        String type     = editType.getText();
        String explain  = editExplain.getText();
        String synonym  = editSynonym.getText();
        String antonym  = editAntonym.getText();

        Word newWord = new Word(word, phonetic, type, explain, synonym, antonym);
        database.updateWord(newWord);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Word updated successfully.");

        // Clear input fields after editing the word
        clearEditWordFields();
    }

    private void clearAddWordFields() {
        wordInput.clear();
        addPhonetic.clear();
        addType.clear();
        addExplain.clear();
        addSynonym.clear();
        addAntonym.clear();
    }

    private void clearEditWordFields() {
        wordInput.clear();
        editPhonetic.clear();
        editType.clear();
        editExplain.clear();
        editSynonym.clear();
        editAntonym.clear();
    }
    public void deleteWord(ActionEvent event){
        String word = wordInput.getText();
        if(!word.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");


            Label headerLabel = new Label("\nRemove word");
            headerLabel.setStyle("-fx-alignment: center;" +
                    "-fx-font-size: 30px;" +
                    "-fx-text-fill: white;"
            );

            alert.getDialogPane().setHeader(headerLabel);

            // Set the content text
            Label contentLabel = new Label("Do you want to delete \"" + word + "\"?");
            contentLabel.setStyle("-fx-alignment: center;" +
                    "-fx-font-size: 25px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: white ;"

            );

            alert.getDialogPane().setContent(contentLabel);

            alert.getDialogPane().setStyle(
                    "-fx-font-family: Arial;" +
                            "-fx-alignment: center;" +
                            "-fx-font-weight: bold;" + "-fx-background-color: linear-gradient(to top right, #ad84f0, #f196f4, #ad84f0);"


            );

            ButtonType buttonTypePlayAgain = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeBackToHome = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(buttonTypePlayAgain, buttonTypeBackToHome);

            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypePlayAgain) {
                    // Reset game state and start a new game
                    database.deleteWord(word);
                } else if (response == buttonTypeBackToHome) {
                    // Switch back to the home screen

                }
            });


            wordInput.clear();
        }
    }

    public void addWord(ActionEvent event) {
        addWord();
    }

    public void update(ActionEvent event){
        editWord();
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
