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

import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GameScreenController {

    @FXML
    public Scene scene;
    public Stage stage;
    public Parent root;
    public TextField inputWord;
    public  Label temp = new Label();
    public int index = 0;
    public ProgressBar progressBar;
    public double process = 0;
    public ScrollPane explain = new ScrollPane();
    public Button sound = new Button();
    public Button archiveWord = new Button();
    public static ArrayList<String> words_list = new ArrayList<>();
    String mediaFile = "src/main/resources/data/sound.mp3";
    Media media = new Media(new File(mediaFile).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    private void  insertFromFile(String filepath) {
        Path path = Path.of(filepath);

        try {
            List<String> lines = Files.readAllLines(path);

            words_list.addAll(lines);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }
    }

    private String findExplain(String word) {
        DBRepository search = new DBRepository();
        return search.searchWord(word).getDefinitionWord();
    }


    @FXML
    public void handleKeyPress(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            increaseProgress();
        }
    }
//
//    public void showArchive(MouseEvent event) {
//        temp.setVisible(!temp.isVisible());
//    }

    /**
     * This method is responsible for advancing the progress in a game or learning application.
     * It compares the user's input word with the explanation of the current word in a list.
     * If the input matches the explanation, the progress is increased, and the user is rewarded.
     * If not, the user is informed of the incorrect input.
     * The method also updates the user interface and checks for game completion.
     */
    public void increaseProgress() {
            String word = inputWord.getText();
            if (findExplain(word).equals(findExplain(words_list.get(index)))) {
                process += 0.5;
                playSoundCorrected();
                actionCorrect();
            } else {
                playSoundIncorrected();
                actionIncorrect();
            }

            if (index < words_list.size()) {
                index = index + 1;
            } else {
                index = 0;
            }
            temp.setText(getExplain());
            inputWord.deleteText(0, word.length());
            progressBar.setProgress(process);
        if(process > 0.99){
            playSoundWin();
            finishGame();
        }
    }

    private void resetGame() {
        process = 0;
        progressBar.setProgress(0);
    }

    private String getExplain(){
        String str = findExplain(words_list.get(index));
        int newlineIndex = str.indexOf('\n');
        return (newlineIndex != -1) ? str.substring(1, newlineIndex) : str;
    }
    public void initialize () {

        insertFromFile("src/main/resources/data/saveWord.txt");
        String result = getExplain();
        temp.setText(result);
        //explain.setContent(temp);
       // Platform.runLater(() -> playSound("src/main/resources/data/sound.wav"));

    }

    public void playSound(MouseEvent event){
        mediaPlayer.setVolume(0.2);
        mediaPlayer.setOnPlaying(() -> System.out.println("Playing"));
        mediaPlayer.setOnPaused(() -> System.out.println("Paused"));
        mediaPlayer.setOnStopped(() -> System.out.println("Stopped"));
        mediaPlayer.setOnEndOfMedia(() -> System.out.println("End of Media"));
        mediaPlayer.setOnError(() -> System.out.println("Error occurred"));

        if(sound.getText().equals("Off")) {
            sound.setText("On");

            mediaPlayer.play();
        } else {
            sound.setText("Off");
            mediaPlayer.pause();

        }
    }

    private void playSoundCorrected(){
        String mediaFileCorrect = "src/main/resources/data/fish.wav";
        Media mediaCorrect = new Media(new File(mediaFileCorrect).toURI().toString());
        MediaPlayer mediaPlayerCorrect = new MediaPlayer(mediaCorrect);
        mediaPlayerCorrect.setVolume(0.2);
        mediaPlayerCorrect.play();

    }

    private void playSoundIncorrected() {
        String mediaFileInCorrect = "src/main/resources/data/wrong.wav";
        Media mediaInCorrect = new Media(new File(mediaFileInCorrect).toURI().toString());
        MediaPlayer mediaPlayerInCorrect = new MediaPlayer(mediaInCorrect);
        mediaPlayerInCorrect.play();
    }

    private void playSoundWin(){
        String mediaFileWin = "src/main/resources/data/win.wav";
        Media mediaWin = new Media(new File(mediaFileWin).toURI().toString());
        MediaPlayer mediaPlayerWin = new MediaPlayer(mediaWin);
        mediaPlayerWin.setVolume(0.5);
        mediaPlayerWin.play();
    }
    private void finishGame() {
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

    private void textToSpeech(String textToSpeak) {
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
        alert.getDialogPane().setPrefWidth(450);
        alert.getDialogPane().setStyle(
                        "-fx-alignment: center;"+
                        "-fx-font-weight: bold;"+
                        "-fx-background-color: #32C446;"

        );


        alert.showAndWait();

    }
    public void actionIncorrect() {
        String name = " ";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Result");

        Label headerLabel = new Label("\nWrong answer" );
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
        alert.getDialogPane().setPrefWidth(450);
        alert.getDialogPane().setStyle(
                        "-fx-font-family: Arial;"+
                        "-fx-alignment: center;"+
                        "-fx-font-weight: bold;"+
                        "-fx-background-color: #C93333;"

        );
        alert.show();
    }

    public void showInstruction(MouseEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        Label headerLabel = new Label("\nFind Word");
        headerLabel.setStyle("-fx-alignment: center;" +
                             "-fx-font-size: 20px;" +
                             "-fx-text-fill: rgb(99, 122, 242);"
        );
        alert.getDialogPane().setHeader(headerLabel);
        Label contentLabel = new Label("You fill up  into answer bar.\n" +
                "Press ENTER or click check button to submit.");
        contentLabel.setStyle("-fx-alignment: center;"+
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: rgb(99, 122, 242) ;"

        );

        alert.getDialogPane().setContent(contentLabel);
        alert.show();
    }
    private void switchToScene(String fxmlPath, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAPI(ActionEvent event) throws IOException {
        switchToScene("/com/app/dictionaryproject/APIScreen.fxml", event);
    }

    public void switchToEdit(ActionEvent event) throws IOException {
        switchToScene("/com/app/dictionaryproject/EditScreen.fxml", event);
    }
    public void switchToMain(ActionEvent event) throws IOException {
        switchToScene("/com/app/dictionaryproject/MainScreen.fxml", event);
    }
}
