package com.app.dictionaryproject.Controller;

import com.app.dictionaryproject.Models.Word;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
    public ScrollPane explain = new ScrollPane();
    @FXML
    public void initialize (Word word) {
        if (word != null) {
            words.setText(word.getWord_target());
            phonetic.setText("Phonetic: " + word.getPhonetic());
            String ex = "Loại từ: " + word.getWordType().replace("\n", ",") + "\n\n";
            String other = "Từ đồng nghĩa: " + word.getSynonym().replace(" ,", ", ") + "\n\n"
                      + "Từ trái nghĩa: " + word.getAntonym().replace(" ,", ", ") + "\n\n";
            Label temp = new Label(ex + word.getDefinitionWord() + "\n\n" + other);
            explain.setContent(temp);
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
