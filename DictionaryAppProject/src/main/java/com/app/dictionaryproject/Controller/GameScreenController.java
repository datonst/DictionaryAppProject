package com.app.dictionaryproject.Controller;

//import com.app.dictionaryproject.Model.Word;
//import javafx.animation.PauseTransition;
import com.app.dictionaryproject.DictionaryCmd.WordCmd;
import com.app.dictionaryproject.service.DBRepository;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameScreenController {

    @FXML
    public TextField inputWord;
    public Label temp = new Label();
    public Scene scene;
    public Stage stage;
    public Parent root;
    public int index = 0;
    public ProgressBar progressBar;
    public double process;
    public ScrollPane explain = new ScrollPane();
    String[] array = {"hello", "apple", "orange", "window", "car", "mountain", "anxiety", "ant","mouse", "cat"};
    String[] arrayExplain = {"xin chao", "qua tao", "qua cam", "cua so", "o to", "ngon nui", "lo lang", "con kien", "con chuot", "con meo"};
    public static ArrayList<String> words_list = new ArrayList<>();

    public void  insertFromFile(String filepath) {
        Path path = Path.of(filepath);
        try {
            List<String> lines = Files.readAllLines(path);
            words_list.addAll(lines);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }
    }

    public String findExplain(String word) {
        DBRepository search = new DBRepository();
        return search.searchWord(word).getDefinitionWord();
    }


    @FXML
    public void handleKeyPress(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            increaseProgress();
        }
    }


    public void increaseProgress() throws IOException {
        //DBRepository search = new DBRepository();

        String word = inputWord.getText();
        if (findExplain(word).equals(findExplain(words_list.get(index)))) {
            process += 0.1;
            actionCorrect();
        } else {
            actionIncorrect();
        }

        if(index < words_list.size()) {
            index = index + 1;
        } else {
            index = 0;
        }
        temp.setText(findExplain(words_list.get(index)));
        inputWord.deleteText(0,word.length());
        progressBar.setProgress(process);
    }
    public void initialize () {
        insertFromFile("src/main/resources/data/saveWord.txt");
        temp.setText(findExplain(words_list.get(index)));
        explain.setContent(temp);
    }
    public void actionCorrect() {
        String name = " ";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Result");

        Label headerLabel = new Label("   Congratulation!!!!");
        headerLabel.setStyle("-fx-alignment: center;");
        headerLabel.setStyle("-fx-font-size: 30px;");
        headerLabel.setPrefHeight(100);
        alert.getDialogPane().setHeader(headerLabel);

        // Set the content text
        Label contentLabel = new Label("Correct answer!!");
        contentLabel.setStyle("-fx-alignment: center;");
        contentLabel.setStyle("-fx-font-size: 30px;");
        contentLabel.setPrefHeight(100);
        alert.getDialogPane().setContent(contentLabel);
        alert.show();

    }
    public void actionIncorrect() {
        String name = " ";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Result");
        Label headerLabel = new Label("   Wrong answer");
        headerLabel.setStyle("-fx-alignment: center;");
        headerLabel.setStyle("-fx-font-size: 30px;");
        headerLabel.setPrefHeight(100);
        alert.getDialogPane().setHeader(headerLabel);

        Label contentLabel = new Label("Correct answer: " + words_list.get(index));
        contentLabel.setStyle("-fx-alignment: center;");
        contentLabel.setStyle("-fx-font-size: 30px;");
        contentLabel.setPrefHeight(100);
        alert.getDialogPane().setContent(contentLabel);
        alert.show();
    }
    public void switchToMain(ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/MainScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(Loader.load());
        stage.setScene(scene);
        stage.show();
    }
}
