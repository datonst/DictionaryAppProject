package com.app.dictionaryproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {
    @Override

    public void start(Stage primaryStage) {
        Button btn = new Button("Click here!");
        Color blue = Color.rgb(99,122,242);
        Rectangle rect = new Rectangle(100,0,200,200);
        rect.setFill(blue);
        StackPane root = new StackPane();
        root.getChildren().add(rect);
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 1010, 720);
        primaryStage.setTitle("DictionaryAPP");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

