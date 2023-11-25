package com.app.dictionaryproject.Controller;

import com.app.dictionaryproject.Models.Word;
import com.app.dictionaryproject.Models.WordShort;
import com.app.dictionaryproject.service.DBRepo;
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
import javafx.scene.layout.VBox;
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
    public DBRepo dbRepo = new DBRepo();
    public VBox vBox = new VBox();

    public void initialize() {
        vBox.getChildren().get(1).setStyle("-fx-background-color: #8aaafa;" + "-fx-background-radius : 0px;");
        editWordTab.setStyle("-fx-background-color: #8aaaff; " +
                "-fx-text-fill:white; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 10px;");

        addWordTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Apply styles when the tab is selected
                addWordTab.setStyle("-fx-background-color:white; " +
                        "-fx-text-fill: #8aaaff; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 10px;");
            } else {
                // Apply styles when the tab is not selected
                addWordTab.setStyle("-fx-background-color: #8aaaff; " +
                        "-fx-text-fill:white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 10px;");
            }
        });

        editWordTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Apply styles when the tab is selected
                editWordTab.setStyle("-fx-background-color:white; " +
                        "-fx-text-fill: #8aaaff; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 15px; " +
                        "-fx-background-radius: 10px;");
            } else {
                // Apply styles when the tab is not selected
                editWordTab.setStyle("-fx-background-color: #8aaaff; " +
                        "-fx-text-fill:white; " +
                        "-fx-font-size: 15px; " +
                        "-fx-background-radius: 10px;");
            }
        });
    }



    public void addWord() {
        String word = wordInput.getText();

        String phonetic = addPhonetic.getText();
        String type = addType.getText();
        String explain = addExplain.getText();
        String synonym = addSynonym.getText();
        String antonym = addAntonym.getText();
        String textScript = "Từ: " + word + "\n" + "Phát âm: " + phonetic + "\nLoại từ: " + type +
                            "\nNghĩa" + explain + "\nTừ đồng nghĩa:" + synonym + "\nTừ trái nghĩa" + antonym;
        String htmlCode = "<h2 class='nameWord'>" + word + "</h2>\n" +
                "<h3 class='pronounWord'>" + phonetic + "</h3>\n" +
                "<h4 class='typeWord'>" + type + "</h4>\n" +
                "<h5 class='meanWord'>" + explain + "</h5>\n";
//                "<h5 class='extraWord'>Từ đồng nghĩa: " + synonym + "</h5>\n" +
//                "<h5 class='extraWord'>Từ trái nghĩa: " + antonym + "</h5>";
        Word newWord = new Word(word,phonetic, type, explain, synonym, antonym);
        WordShort wordShort = new WordShort(word, textScript, htmlCode);
        dbRepo.insertWord(wordShort);
        database.insertWord(newWord);
        clearAddWordFields();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Word added successfully.");
        // Clear input fields after adding the word

    }
    public void setTextEditWord(MouseEvent event) {
        String word = wordInput.getText();
        if(!word.isEmpty()) {
            setWord(word);
        } else {
            clearEditWordFields();
        }
    }

    public void enterSaveWord(KeyEvent event){
            String word = wordInput.getText();
            if ( event.getCode() == KeyCode.ENTER && !word.isEmpty()) {
                setWord(word);
            } else if(word.isEmpty()){
                clearEditWordFields();
            }
    }

    private void setWord(String word) {
        editPhonetic.setText(database.searchWord(word).getPhonetic());
        editType.setText(database.searchWord(word).getWordType());
        editExplain.setText(database.searchWord(word).getDefinitionWord());
        editSynonym.setText(database.searchWord(word).getSynonym());
        editAntonym.setText(database.searchWord(word).getAntonym());
    }

    public void editWord() {

        String word     = wordInput.getText();
        String phonetic = editPhonetic.getText();
        String type     = editType.getText();
        String explain  = editExplain.getText();
        String synonym  = editSynonym.getText();
        String antonym  = editAntonym.getText();

        Word newWord = new Word(word, phonetic, type, explain, synonym, antonym);

        String textScript = "Từ: " + word + "\n" + "Phát âm: " + phonetic + "\nLoại từ: " + type +
                "\nNghĩa" + explain + "\nTừ đồng nghĩa:" + synonym + "\nTừ trái nghĩa" + antonym;
        String htmlCode = "<h2 class='nameWord'>" + word + "</h2>\n" +
                "<h3 class='pronounWord'>" + phonetic + "</h3>\n" +
                "<h4 class='typeWord'>" + type + "</h4>\n" +
                "<h5 class='meanWord'>" + explain + "</h5>\n";

        showAlert(Alert.AlertType.INFORMATION, "Success", "Word updated successfully.");
        database.updateWord(newWord);
        dbRepo.updateWord(new WordShort(word, textScript, htmlCode));
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
                    dbRepo.deleteWord(word);
                } else if (response == buttonTypeBackToHome) {
                    // Switch back to the home screen

                }
            });


            wordInput.clear();
        }
    }

    public void addWord(MouseEvent event) {
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
                    addWord();
                } else if (response == buttonTypeBackToHome) {
                    // Switch back to the home screen

                }
            });


            clearAddWordFields();
        }

    }

    public void update(MouseEvent event){
        editWord();
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void switchToGame(MouseEvent event) throws IOException {
        SwitchScreen.switchToScene("/com/app/dictionaryproject/GameScreen.fxml",stage, event);
    }
    public void switchToEdit(MouseEvent event) throws IOException {
        SwitchScreen.switchToScene("/com/app/dictionaryproject/EditScreen.fxml",stage, event);
    }
    public void  switchToMain(MouseEvent event) throws IOException {
        SwitchScreen.switchToScene("/com/app/dictionaryproject/MainScreen.fxml",stage, event);

    }
    public void switchToAPI(MouseEvent event) throws IOException {
        SwitchScreen.switchToScene("/com/app/dictionaryproject/APIScreen.fxml", stage, event);
    }
}
