package com.app.dictionaryproject.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import com.app.dictionaryproject.service.TranslateService;

import java.io.IOException;

public class APIScreenController {
    @FXML
    public Scene scene;
    public Stage stage;
    public Parent root;

    public Button back = new Button();
    public TextArea inputText = new TextArea();
    public TextArea outputText = new TextArea();
    TranslateService translateService = new TranslateService();
    public Button switchLanguage = new Button();
    public Label inputLanguage = new Label();
    public Label outputLanguage = new Label();
    String vi = "Vietnamese";
    String eng = "English";
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
}
