package com.app.dictionaryproject.Controller;

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
import java.awt.Toolkit;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GameScreenController {

    @FXML
    public TextField inputWord;
    public Label temp = new Label();
    public Scene scene;
    public Stage stage;
    public Parent root;
    public int index = 0;
    public ProgressBar progressBar;
    public double process = 0;
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
                process += 0.5;
                actionCorrect();
            } else {
                actionIncorrect();
            }

            if (index < words_list.size()) {
                index = index + 1;
            } else {
                index = 0;
            }
            temp.setText(findExplain(words_list.get(index)));
            inputWord.deleteText(0, word.length());
            progressBar.setProgress(process);
        if(process > 0.99){

            finishGame();
        }
    }

    public void resetGame() {
        process = 0;
        progressBar.setProgress(0);
    }


    public void initialize () {
        insertFromFile("src/main/resources/data/saveWord.txt");
        temp.setText(findExplain(words_list.get(index)));
        explain.setContent(temp);

    }




    public void finishGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");


        Label headerLabel = new Label("\nWell done");
        headerLabel.setStyle("-fx-alignment: center;" +
                "-fx-font-size: 30px;" +
                "-fx-text-fill: white;"
        );

        alert.getDialogPane().setHeader(headerLabel);

        // Set the content text
        Label contentLabel = new Label("Do you want to play again?");
        contentLabel.setStyle("-fx-alignment: center;"+
                "-fx-font-size: 25px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;"

        );

        alert.getDialogPane().setContent(contentLabel);

        alert.getDialogPane().setStyle(
                        "-fx-font-family: Arial;"+
                        "-fx-alignment: center;" +
                        "-fx-font-weight: bold;" + "-fx-background-color: linear-gradient(to top right, #ad84f0, #f196f4, #ad84f0);"


        );

        ButtonType buttonTypePlayAgain = new ButtonType("Play Again", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeBackToHome = new ButtonType("Back to Home", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonTypePlayAgain, buttonTypeBackToHome);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypePlayAgain) {
                // Reset game state and start a new game
                resetGame();
            } else if (response == buttonTypeBackToHome) {
                // Switch back to the home screen
                switchToMain();
            }
        });
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
        textToSpeech(words_list.get(index));
    }

    public void switchToMain(ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/MainScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(Loader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/MainScreen.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) inputWord.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    public void actionCorrect() {
        String name = " ";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Result");


        // Play correct sound
        Toolkit.getDefaultToolkit().beep();
        Label headerLabel = new Label("\nCongratulation");
        headerLabel.setStyle("-fx-alignment: center;" +
                "-fx-font-size: 30px;" +
                "-fx-text-fill: white;"
        );

        alert.getDialogPane().setHeader(headerLabel);

        // Set the content text
        Label contentLabel = new Label("Correct answer");
        contentLabel.setStyle("-fx-alignment: center;"+
                "-fx-font-size: 25px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;"

        );

        alert.getDialogPane().setContent(contentLabel);
        alert.getDialogPane().setPrefWidth(300);
        alert.getDialogPane().setStyle(
                "-fx-alignment: center;"+
                        "-fx-font-weight: bold;"+
                        "-fx-background-color: linear-gradient(to top right, #ad84f0, #f196f4, #ad84f0);"

        );


        alert.showAndWait();

    }
    public void actionIncorrect() {
        String name = " ";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Result");

        Label headerLabel = new Label("\nWrong answer");
        headerLabel.setStyle("-fx-alignment: center;" +
                "-fx-font-size: 30px;" +
                "-fx-text-fill: white;"
        );

        alert.getDialogPane().setHeader(headerLabel);

        Label contentLabel = new Label("Correct answer:\n \t" + words_list.get(index));
        contentLabel.setStyle("-fx-alignment: center;"+
                "-fx-font-size: 25px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;"

        );

        alert.getDialogPane().setContent(contentLabel);
        alert.getDialogPane().setPrefWidth(300);
        alert.getDialogPane().setStyle(
                "-fx-font-family: Arial;"+
                        "-fx-alignment: center;"+
                        "-fx-font-weight: bold;"+
                        "-fx-background-color: linear-gradient(to top right, #ad84f0, #f196f4, #ad84f0);"

        );
        alert.show();
    }
}
