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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.app.dictionaryproject.service.TranslateService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
    public ImageView imageView = new ImageView();
    TranslateService translateService = new TranslateService();
    public Button switchLanguage = new Button();
    public Label inputLanguage = new Label();
    public Label outputLanguage = new Label();
    public VBox vBox = new VBox();
    Image voiceIcon;
    Image waveIcon;


    String path = System.getProperty("user.dir")
            + "\\src\\main\\resources\\recorder\\talk.wav";
    RecorderService recorderService;
    private final String vi = "Vietnamese";
    private final String eng = "English";

    public void switchLang(MouseEvent event) {

        if (inputLanguage.getText().equals(vi)){
            inputLanguage.setText(eng);
            outputLanguage.setText(vi);
            imageView.setImage(voiceIcon);
        } else {
            inputLanguage.setText(vi);
            outputLanguage.setText(eng);
            imageView.setImage(null);
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



    public void translate(MouseEvent event) {
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

        vBox.getChildren().get(3).setStyle("-fx-background-color: #8aaafa;" + "-fx-background-radius : 0px;");

        InputStream voiceIconInput = getClass().getResourceAsStream("/Style/google-voice.png");
        InputStream waveIconInput = getClass().getResourceAsStream("/Style/wave-sound.png");
        voiceIcon = new Image(voiceIconInput);
        waveIcon = new Image(waveIconInput);
//        imageView.setImage(voiceIcon);
        AnimationTimer animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {

            }
        };
        recordButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (imageView.getImage() == null) {
                    return;
                }
                else if (imageView.getImage().equals(voiceIcon)) {
                    System.out.println("voicimage");
                    imageView.setImage(waveIcon);
                    animator.start();
//                    recordButton.setText("Stop");
                    recorderService = new RecorderService(path);
                    recorderService.startRecording();
                }
                else if (imageView.getImage().equals(waveIcon)){
                    recorderService.stopRecording();
//                    String abc ="hello";
//                    inputText.setText(abc);
//                    try {
//                        outputText.setText(translateService.getVietnameseText(abc));
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    recordButton.setText("Stop");
                    System.out.println("waveIcon");
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        String text = callTranslateAPI();
                        inputText.setText(text);
                        outputText.setText(translateService.getVietnameseText(text));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    animator.stop();
//                    recordButton.setText("Start");
                    imageView.setImage(voiceIcon);
                }
            }
        });
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
