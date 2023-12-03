package com.app.dictionaryproject.Controller;

import com.app.dictionaryproject.Models.Word;
import com.app.dictionaryproject.Models.WordShort;
import com.app.dictionaryproject.Repository.*;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditScreenController {
    @FXML
    public Scene scene;
    public Stage stage;
    public Parent root;
    @FXML
    public  TextField wordInput = new TextField();

    @FXML
    public  AnchorPane addWordPane = new AnchorPane();
    @FXML
    public AnchorPane editWordPane = new AnchorPane();
    @FXML

    public AnchorPane addNewWord = new AnchorPane();

    public DictDAO database = new Dict2DAOImpl();
    public DictDAO dbRepo = new Dict1DAOImpl();
    public VBox vBox = new VBox();
    public VBox currentTypeParentVBox = new VBox();

    public VBox extraWord = new VBox();

    public TextArea sym = new TextArea();
    public TextArea anto = new TextArea();
    public TextArea editArea = new TextArea();
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
        label.setPrefWidth(100);
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
        String customSynonym = sym.getText(); // You may modify this according to your actual input
        String customAntonym = anto.getText(); // You may modify this according to your actual input

        // Duyệt qua các phần tử con của VBox
        for (Node node : currentTypeParentVBox.getChildren()) {
            if (node instanceof HBox hBox) {

                // Duyệt qua các thành phần của HBox
                for (Node childNode : hBox.getChildren()) {
                    if (childNode instanceof TextArea textArea) {
                        // Lấy dữ liệu từ TextField
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

//        customHtmlCode += "<h5 class='extraWord'>Từ đồng nghĩa: " + customSynonym + "</h5>\n" +
//                "<h5 class='extraWord'>Từ trái nghĩa: " + customAntonym + "</h5>";

        System.out.println(customTextScript);
        System.out.println(customHtmlCode);
        WordShort wordShort = new WordShort(customWord, customTextScript, customHtmlCode);
        dbRepo.insertWord(wordShort);
        database.insertWord(new Word(customWord,customPhonetic,"","", customSynonym, customSynonym));
        clearAddWordFields();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Word added successfully.");
    }

    public void editCustomWord(MouseEvent event){
        String edited = editArea.getText();
        String htmlText = convertStructuredTextToHTML(edited);
        dbRepo.updateWord(new WordShort(wordInput.getText(), edited, htmlText));
        clearEditWordFields();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Word updated successfully.");
    }

    public static String convertStructuredTextToHTML(String structuredText) {
        String[] lines = structuredText.split("\n");

        // Initialize variables to store different parts of information

        StringBuilder htmlCode = new StringBuilder();
        for (String line : lines) {
            String[] parts = line.split(":", 2);
            if (parts.length == 2) {
                String label = parts[0].trim();
                String value = parts[1].trim();

                switch (label) {
                    case "Từ":
                        htmlCode.append("<h2 class='nameWord'>").append(value).append("</h2>\n");
                        break;
                    case "Phát âm":
                        htmlCode.append("<h3 class='pronounWord'>").append(value).append("</h3>\n");
                        break;
                    case "Loại":
                        htmlCode.append("<h4 class='typeWord'>").append(value).append("</h4>\n");
                        break;
                    case "Nghĩa":
                        htmlCode.append("<h5 class='meanWord'>").append(value).append("</h5>\n");
                        break;
                }
            }
        }

        // Build HTML code



        return htmlCode.toString();
    }




    public static String convertHTMLToStructuredText(String htmlCode) {
        Document document = Jsoup.parse(htmlCode);

        // Get word, phonetic, and type
        String word = document.select("h2.nameWord").text();
        String phonetic = document.select("h3.pronounWord").text();
        String type = document.select("h4.typeWord").text();

        // Get meanings and examples
        List<String> meanings = new ArrayList<>();
        Elements meanElements = document.select("h5.meanWord");
        for (Element meanElement : meanElements) {
            meanings.add(meanElement.text());
        }

        List<String> examples = new ArrayList<>();
        Elements exampleElements = document.select("h6.exampleWord");
        for (Element exampleElement : exampleElements) {
            String example = exampleElement.select("p").text();
            examples.add(example);
        }

        // Build structured text
        StringBuilder result = new StringBuilder();
        result.append("Từ: ").append(word).append("\n");
        result.append("Phát âm: ").append(phonetic).append("\n");
        result.append("Loại từ: ").append(type).append("\n");
        result.append("Nghĩa:\n");
        for (String meaning : meanings) {
            result.append("  - ").append(meaning).append("\n");
        }
        result.append("Ví dụ:\n");
        for (String example : examples) {
            result.append("  - ").append(example).append("\n");
        }

        return result.toString();
    }


    public void setTextEditWord(MouseEvent event) {
        String word = wordInput.getText();
        if(!word.isEmpty()) {
            getChoice(word);
        } else {
            clearEditWordFields();
        }
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
                if(((Word) database.searchWord(word)).getDefinitionWord().equals("not found")) {
                    wordInput.setEditable(false);
                   // addWordPane.setVisible(true);
                    addNewWord.setVisible(true);
                }
                else {
                    showAlert(Alert.AlertType.INFORMATION,"Warning", "Word is existed!");
                    clearAddWordFields();
                }

            } else if (response == buttonTypeEdit) {
                if(!((Word)database.searchWord(word)).getDefinitionWord().equals("not found")) {
                    wordInput.setEditable(false);
                    String text = ((WordShort)dbRepo.searchWord(word)).getTextDescription();

                    editArea.setText(text);
                    System.out.println(convertStructuredTextToHTML(editArea.getText()));
                    editWordPane.setVisible(true);

                } else {
                    showAlert(Alert.AlertType.INFORMATION,"Warning", "Word is not existed!");
                    clearEditWordFields();
                }
            }
        });
    }


    private void clearAddWordFields() {
        wordInput.clear();
        addWordPane.setVisible(false);
        addNewWord.setVisible(false);
        wordInput.setEditable(true);
    }

    private void clearEditWordFields() {
        wordInput.clear();
        editArea.clear();
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
                    "-fx-text-fill: #aaaaff;"
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
