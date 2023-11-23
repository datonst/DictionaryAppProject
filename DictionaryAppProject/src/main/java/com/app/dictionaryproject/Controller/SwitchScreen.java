package com.app.dictionaryproject.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class SwitchScreen {
    @FXML
    public static void switchToScene(String fxmlPath, Stage stage, MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(SwitchScreen.class.getResource(fxmlPath));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }
}
