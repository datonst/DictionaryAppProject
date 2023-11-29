package com.app.dictionaryproject.Controller;

import com.app.dictionaryproject.Models.Word;
import com.app.dictionaryproject.Models.WordShort;
import com.app.dictionaryproject.service.DBRepo;
import com.app.dictionaryproject.service.DBRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
//import org.kordamp.ikonli.fontawesome.FontAwesomeIcon;
//import org.kordamp.ikonli.javafx.FontIcon;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditScreenController {
    @FXML
    public Scene scene;
    public Stage stage;
    public Parent root;
    public TextField wordInput = new TextField();

    public AnchorPane addWordPane = new AnchorPane();
    public AnchorPane editWordPane = new AnchorPane();
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
    public VBox currentTypeParentVBox = new VBox();


    public void addExplain(MouseEvent event) {

        // Create an HBox for Word Type
         createLabeledTextArea("Type:", 30, currentTypeParentVBox);
        // Create an HBox for Explain
        createLabeledTextArea("Explain:", 60 ,currentTypeParentVBox);
        //currentTypeParentVBox.getChildren().addAll(phoneticHBox,wordTypeHBox,explainHBox);
    }

    private void createLabeledTextArea(String labelText,double height, VBox parentVBox) {
        HBox hBox = new HBox();
        //hBox.getStylesheets().add("EditWord.css");
        // Create a Label
        Label label = new Label();
        label.setPrefWidth(80);
        label.setPrefHeight(20);
        label.setText(labelText);
        label.getStyleClass().add("attribute");
        // Create a TextArea
        TextArea textArea = new TextArea();
        textArea.setPrefHeight(height);
        textArea.setPrefWidth(520);
        textArea.getStyleClass().add("word");

        // Add Label and TextArea to HBox
        hBox.getChildren().addAll(label, textArea);
        hBox.setSpacing(10);

        // Add HBox to the parent VBox
        parentVBox.getChildren().add(hBox);

    }
    public void initialize() {
        vBox.getChildren().get(1).setStyle("-fx-background-color: #8aaafa;" + "-fx-background-radius : 0px;");
        createLabeledTextArea("Phonetic:",30, currentTypeParentVBox);
        // Create an HBox for Word Type
        createLabeledTextArea("Type:", 30, currentTypeParentVBox);
        // Create an HBox for Explain
        createLabeledTextArea("Explain:", 60 ,currentTypeParentVBox);
    }

    public void addCustomWord(MouseEvent event) {
        String customWord = wordInput.getText();
        String customPhonetic = "";
        List<String> customTypes = new ArrayList<>();
        List<String> customExplains = new ArrayList<>();
        String customSynonym = ""; // You may modify this according to your actual input
        String customAntonym = ""; // You may modify this according to your actual input

        // Duyệt qua các phần tử con của VBox
        for (Node node : currentTypeParentVBox.getChildren()) {
            if (node instanceof HBox) {
                HBox hBox = (HBox) node;

                // Duyệt qua các thành phần của HBox
                for (Node childNode : hBox.getChildren()) {
                    if (childNode instanceof TextArea) {
                        // Lấy dữ liệu từ TextField
                        TextArea textArea = (TextArea) childNode;
                        String labelText = ((Label) hBox.getChildren().get(0)).getText();
                        switch (labelText) {
                            case "Phonetic:":
                                customPhonetic = textArea.getText();
                                break;
                            case "Type:":
                                customTypes.add(textArea.getText());
                                break;
                            case "Explain:":
                                customExplains.add(textArea.getText());
                                break;
                        }
                    }
                }
            }
        }

        StringBuilder customTextScriptBuilder = new StringBuilder();
        customTextScriptBuilder.append("Từ: ").append(customWord).append("\n");
        customTextScriptBuilder.append("Phát âm: ").append(customPhonetic).append("\n");

        // Thêm mỗi loại và giải thích vào customTextScript
        for (int i = 0; i < Math.max(customTypes.size(), customExplains.size()); i++) {
            if (i < customTypes.size()) {
                customTextScriptBuilder.append("Loại từ: ").append(customTypes.get(i)).append("\n");
            }
            if (i < customExplains.size()) {
                customTextScriptBuilder.append("Nghĩa: ").append(customExplains.get(i)).append("\n");
            }
        }

        customTextScriptBuilder
                .append("Từ đồng nghĩa: ").append(customSynonym).append("\n")
                .append("Từ trái nghĩa: ").append(customAntonym);

        String customTextScript = customTextScriptBuilder.toString();

        String customHtmlCode = "<h2 class='nameWord'>" + customWord + "</h2>\n" +
                "<h3 class='pronounWord'>" + customPhonetic + "</h3>\n";

        // Thêm mỗi loại và giải thích vào HTML
        for (int i = 0; i < Math.max(customTypes.size(), customExplains.size()); i++) {
            if (i < customTypes.size()) {
                customHtmlCode += "<h4 class='typeWord'>" + customTypes.get(i) + "</h4>\n";
            }
            if (i < customExplains.size()) {
                customHtmlCode += "<h5 class='meanWord'>" + customExplains.get(i) + "</h5>\n";
            }
        }

        customHtmlCode += "<h5 class='extraWord'>Từ đồng nghĩa: " + customSynonym + "</h5>\n" +
                "<h5 class='extraWord'>Từ trái nghĩa: " + customAntonym + "</h5>";

        System.out.println(customTextScript);
        System.out.println(customHtmlCode);
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
            getChoice(word);
        } else {
            clearEditWordFields();
        }
    }

    public void enterSaveWord(KeyEvent event){
            String word = wordInput.getText();
            if ( event.getCode() == KeyCode.ENTER && !word.isEmpty()) {
                getChoice(word);
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
    public void getChoice(String word) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");


        Label headerLabel = new Label("What's your choice?");

        alert.getDialogPane().setHeader(headerLabel);

        // Set the content text
        Label contentLabel = new Label("Let's choose one option");


        alert.getDialogPane().setContent(contentLabel);
        alert.getDialogPane().setStyle("-fx-alignment: center");

        ButtonType buttonTypeAdd = new ButtonType("Add new word", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeEdit = new ButtonType("Repair word", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonTypeAdd, buttonTypeEdit);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeAdd) {
                if(database.searchWord(word).getDefinitionWord().equals("not found")) {
                    wordInput.setEditable(false);
                    addWordPane.setVisible(true);
                }
                else {
                    showAlert(Alert.AlertType.INFORMATION,"Warning", "Word is existed!");
                    clearAddWordFields();
                }

            } else if (response == buttonTypeEdit) {
                if(!database.searchWord(word).getDefinitionWord().equals("not found")) {
                    wordInput.setEditable(false);
                    editWordPane.setVisible(true);
                    setWord(word);
                } else {
                    showAlert(Alert.AlertType.INFORMATION,"Warning", "Word is not existed!");
                    clearEditWordFields();
                }
            }
        });
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
        addWordPane.setVisible(false);
        wordInput.setEditable(true);
    }

    private void clearEditWordFields() {
        wordInput.clear();
        editPhonetic.clear();
        editType.clear();
        editExplain.clear();
        editSynonym.clear();
        editAntonym.clear();
        editWordPane.setVisible(false);
        wordInput.setEditable(true);
    }
    public void deleteWord(ActionEvent event){
        String word = wordInput.getText();
        if(!word.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");


            Label headerLabel = new Label("\nRemove word");
            headerLabel.setStyle("-fx-alignment: center;" +
                    "-fx-font-size: 30px;" +
                    "-fx-text-fill: black;"
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
                            "-fx-font-weight: bold;"


            );

            ButtonType buttonTypePlayAgain = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeBackToHome = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(buttonTypePlayAgain, buttonTypeBackToHome);

            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypePlayAgain) {
                    database.deleteWord(word);
                    dbRepo.deleteWord(word);
                } else if (response == buttonTypeBackToHome) {

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


            Label headerLabel = new Label("\nAre you want to add a new word?");
            headerLabel.setStyle("-fx-alignment: center;" +
                    "-fx-font-size: 30px;" +
                    "-fx-text-fill: black;"
            );

            alert.getDialogPane().setHeader(headerLabel);


            alert.getDialogPane().setStyle(
                    "-fx-font-family: Arial;" +
                            "-fx-alignment: center;" +
                            "-fx-font-weight: bold;"


            );

            ButtonType buttonTypePlayAgain = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeBackToHome = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(buttonTypePlayAgain, buttonTypeBackToHome);

            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypePlayAgain) {
                    addWord();
                } else if (response == buttonTypeBackToHome) {
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
