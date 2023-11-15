package com.app.dictionaryproject.Controller;

import com.app.dictionaryproject.Models.Word;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.LightBase;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;

public class ResultSearchController {
    @FXML
    public Scene scene;
    public Stage stage;
    public Parent root;

    public void  switchToMain(ActionEvent event) throws IOException {

        FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/MainScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(Loader.load());
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public Label words = new Label();
    @FXML
    public Label phonetic = new Label();
    public Label type = new Label();
    public Label explain = new Label();
    public Label synonym = new Label();
    public Label antonym = new Label();
    public VBox vBox = new VBox();
    public  ScrollPane scrollPane = new ScrollPane();
    @FXML
    public void initialize(Word word) {
        if (word != null) {
            words.setText(word.getWord_target());
            phonetic.setText("Phonetic: " + word.getPhonetic());
            String ex = "Loại từ: " + word.getWordType().replace("\n", " ");
            String sym = "Từ đồng nghĩa: " + word.getSynonym().replace(" ,", ", ");
            String anto = "Từ trái nghĩa: " + word.getAntonym().replace(" ,", ", ");
            type.setText(ex);
            type.getStyleClass().add("word");

            synonym.setText(sym);
            antonym.setText(anto);

            // Use Label with wrapText set to true for multiline text
            explain.setText("Define: " + word.getDefinitionWord());
            explain.setWrapText(true);

            // Clear previous content and add new labels
            vBox.getChildren().clear();
            vBox.getChildren().addAll(type,  explain, synonym, antonym);
            vBox.setSpacing(10);
            scrollPane.setContent(vBox);

        }
    }

    public void textToSpeech(String textToSpeak) {
        // thay đổi theo từng máy của mọi người
        String command = "cscript.exe /nologo  " + System.getProperty("user.dir") + "\\src\\main\\resources\\data\\TTSAPI.vbs \"" + textToSpeak + "\"";

        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                //System.out.println("Text spoken successfully.");
            } else {
                System.err.println("Error occurred while speaking the text.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void speech(ActionEvent event) throws IOException {
        String word = words.getText();
        //word = word.substring(0,word.indexOf("\t"));
        textToSpeech(word);
    }
}
