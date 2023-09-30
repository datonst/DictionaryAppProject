package com.app.dictionaryproject;

import com.app.dictionaryproject.service.DBRepository;
import com.app.dictionaryproject.service.TextToSQL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DBRepository dbRepository = new DBRepository();
        dbRepository.insertWord("hello","hi","xinchao");
        TextToSQL textToSQL = new TextToSQL();
        textToSQL.convert(dbRepository);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}