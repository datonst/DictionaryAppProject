package com.app.dictionaryproject.Controller;

import com.app.dictionaryproject.service.RecorderService;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import com.app.dictionaryproject.service.TranslateService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class APIScreenController implements Initializable {
    @FXML
    public Scene scene;
    public Stage stage;
    public Parent root;

    @FXML
    public Button recordButton;

    public Button back = new Button();
    public TextArea inputText = new TextArea();
    public TextArea outputText = new TextArea();
    TranslateService translateService = new TranslateService();
    public Button switchLanguage = new Button();
    public Label inputLanguage = new Label();
    public Label outputLanguage = new Label();
    String vi = "Vietnamese";
    String eng = "English";

    AnimationTimer animator = new AnimationTimer() {
        @Override
        public void handle(long arg0) {

        }
    };
    String path = System.getProperty("user.dir")
            + "\\src\\main\\resources\\recorder\\talk.wav";
    RecorderService recorderService;
    public void switchLang(ActionEvent event) {
        if (inputLanguage.getText().equals(vi)){
            inputLanguage.setText(eng);
            outputLanguage.setText(vi);
        } else {
            inputLanguage.setText(vi);
            outputLanguage.setText(eng);
        }
    }
    public void translateViToEng(ActionEvent event) {
        String text = inputText.getText();
        try {
//            outputText.setText(translateService.getVietnameseText(text));
            System.out.println(translateService.getVietnameseText(text));
            outputText.setText("translated");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void translateEngToVi() {

    }

    public void  switchToMain(ActionEvent event) throws IOException {

        FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/MainScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(Loader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void translate(ActionEvent event) {
        String text = inputText.getText();
        try {
            if(inputLanguage.getText().equals(eng)) {
                outputText.setText(translateService.getVietnameseText(text));
            } else {
                outputText.setText(translateService.getEnglishText(text));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void TextToSpeech(String textToSpeak) {
        // thay đổi theo từng máy của mọi người
        String command = "cscript.exe /nologo  "
                + System.getProperty("user.dir")
                + "\\src\\main\\resources\\data\\TTSAPI.vbs \"" + textToSpeak + "\"";

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
    public void speakerTextOutput(ActionEvent event){
        TextToSpeech(outputText.getText());
    }
    public void speakerTextInput(ActionEvent event){
        TextToSpeech(inputText.getText());
    }


    public void voiceToText(ActionEvent actionEvent) {

    }

    public String callTranslateAPI() throws IOException {
        return translateService.getSpeechToText(path,"en");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recordButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (recordButton.getText().equals("Start")) {
                    animator.start();
                    recordButton.setText("Stop");

                    recorderService = new RecorderService(path);
                    recorderService.startRecording();
                }
                else {
                    recorderService.stopRecording();
//                    try {
//                        String text = callTranslateAPI();
//                        outputText.setText(text);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
                    animator.stop();
                    recordButton.setText("Start");
                }
            }
        });
    };
}
