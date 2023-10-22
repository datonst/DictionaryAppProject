package com.app.dictionaryproject.Controller;

//import com.app.dictionaryproject.Model.Word;
//import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Map;

public class GameScreenController {

    @FXML
    public TextField inputWord;
    public Label temp = new Label();
    public Scene scene;
    public Stage stage;
    public Parent root;

    public void switchToMain(ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/MainScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(Loader.load());
        stage.setScene(scene);
        stage.show();
    }
}
