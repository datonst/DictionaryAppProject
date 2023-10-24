package com.app.dictionaryproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class MainScreen extends Application {
    private static final int WIDTH = 736;
    private static final int HEIGHT = 650;
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainScreen.class.getResource("MainScreen.fxml"));
        String iconPath = "D:\\DictionaryAppProject\\DictionaryAppProject\\src\\main\\resources\\Style\\icon.png";
        Image icon = new Image(iconPath);
        primaryStage.getIcons().add(icon);
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        primaryStage.setTitle("");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
//  Color blue = Color.rgb(99,122,242); đây là màu nền chủ đạo
