package com.app.dictionaryproject.Controller;

import com.app.dictionaryproject.service.DBRepository;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import javax.swing.*;
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
    public Label timer;
    public TextField inputWord;
    public  Label temp = new Label();
    public int index = 0;
    private int correctCount = 0;
    private int incorrectCount = 0;
    public ProgressBar progressBar;
    public double process = 0;
    public ScrollPane explain = new ScrollPane();
    public Button sound = new Button();
    public Button archiveWord = new Button();
    public static ArrayList<String> words_list = new ArrayList<>();
    public VBox vBox = new VBox();
    public LineChart<?, ?> chart ;
    private  List<Pair<Integer, Integer>> roundData = new ArrayList<>();

    private static final String TITLE = "Result";
    private static final String FONT_STYLE = "-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;";
    private static final String CENTER_ALIGNMENT = "-fx-alignment: center;";
    private static final String INSTRUCTION_TITLE = "Instructions";
    private static final String INSTRUCTION_FONT_STYLE = "-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: rgb(99, 122, 242);";

    private static final String mediaFile = "src/main/resources/data/sound.mp3";
    static Media media = new Media(new File(mediaFile).toURI().toString());
    static MediaPlayer mediaPlayer = new MediaPlayer(media);
    private final Timeline animation = new Timeline(new KeyFrame(Duration.seconds(1), e -> countDown()));

    private int minutes;
    private int seconds ;

    private void countDown() {
        if (seconds == 0) {
            minutes--;
            seconds = 59;
        } else {
            seconds--;
        }
        if (seconds <= 0 && minutes <= 0) {
            animation.pause();
            Platform.runLater(this::finishGame);
        }
        timer.setText(String.valueOf(seconds));

    }

    private void insertFromFile(String filepath) {
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
    private String getExplain(){
        String str = findExplain(words_list.get(index));
        int newlineIndex = str.indexOf('\n');
        return (newlineIndex != -1) ? str.substring(1, newlineIndex) : str;
    }
    public void increaseProgress() {
            String word = inputWord.getText();
            if (findExplain(word).equals(findExplain(words_list.get(index)))) {
                process += 0.5;
                correctCount++;
                playSoundCorrected();
                actionCorrect();
            } else {
                incorrectCount++;
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
            roundData.add(new Pair<>(correctCount, incorrectCount));
            updateChart();
            playSoundWin();
            finishGame();
        }
    }

    private void resetGame() {
        resetTime();
        process = 0;
        progressBar.setProgress(0);
        correctCount = 0;
        incorrectCount = 0;
    }



    public void initialize () {

        vBox.getChildren().get(2).setStyle("-fx-background-color: #8aaafa;" + "-fx-background-radius : 0px;");
        insertFromFile("src/main/resources/data/saveWord.txt");
        String result = getExplain();
        temp.setText(result);
        mediaPlayer.pause();
        resetTime();
    }


    private void resetTime() {
        minutes = 0;
        seconds = 5;
        timer.setText(String.valueOf(seconds));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }
    private void updateChart() {
        XYChart.Series series = new XYChart.Series();
        for (Pair roundData : roundData) {
            //series.getData().add(new XYChart.Data<>("Correct", roundData.getKey()));
            series.getData().add(new XYChart.Data<>("Incorrect", roundData.getValue()));
        }
        chart.getData().add(series);
    }


    public void playSound(MouseEvent event){
        mediaPlayer.setVolume(0.2);

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

    public void speech(ActionEvent event) throws IOException {
        ResultSearchController.textToSpeech(words_list.get(index));
    }

    public void switchToMain() {
        try {
            stopSoundWhenExit();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/MainScreen.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) inputWord.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    private void stopSoundWhenExit() {
        if(sound.getText().equals("On")) {
            sound.setText("Off");
            mediaPlayer.pause();
        }
    }

    private void showAlert(String headerText, String contentText, String backgroundColor) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(TITLE);

        Label headerLabel = new Label("\n" + headerText);
        headerLabel.setStyle(CENTER_ALIGNMENT +
                "-fx-font-size: 30px;" +
                "-fx-text-fill: white;");

        alert.getDialogPane().setHeader(headerLabel);

        Label contentLabel = new Label(contentText);
        contentLabel.setStyle(CENTER_ALIGNMENT + FONT_STYLE);

        alert.getDialogPane().setContent(contentLabel);
        alert.getDialogPane().setPrefWidth(450);
        alert.getDialogPane().setStyle(
                "-fx-font-family: Arial;" +
                        CENTER_ALIGNMENT +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: " + backgroundColor + ";"
        );

        alert.showAndWait();
    }

    public void actionCorrect() {
        showAlert("Congratulation", "Correct answer", "#32C446");
    }

    public void actionIncorrect() {
        showAlert("Wrong answer", "Correct answer:\n \t" + words_list.get(index), "#C93333");
    }

    public void showInstruction(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(INSTRUCTION_TITLE);

        Label headerLabel = new Label("\nFind Word");
        headerLabel.setStyle(CENTER_ALIGNMENT +
                "-fx-font-size: 20px;" +
                "-fx-text-fill: rgb(99, 122, 242);");

        alert.getDialogPane().setHeader(headerLabel);

        Label contentLabel = new Label("You fill up into the answer bar.\n" +
                "Press ENTER or click the check button to submit.");
        contentLabel.setStyle(CENTER_ALIGNMENT + INSTRUCTION_FONT_STYLE);

        alert.getDialogPane().setContent(contentLabel);
        alert.show();
    }



    public void limitTime() {

    }
    public void switchToGame(MouseEvent event) throws IOException {
        stopSoundWhenExit();
        SwitchScreen.switchToScene("/com/app/dictionaryproject/GameScreen.fxml",stage, event);
    }
    public void switchToEdit(MouseEvent event) throws IOException {
        stopSoundWhenExit();
        SwitchScreen.switchToScene("/com/app/dictionaryproject/EditScreen.fxml",stage, event);
    }
    public void  switchToMain(MouseEvent event) throws IOException {
        stopSoundWhenExit();
        SwitchScreen.switchToScene("/com/app/dictionaryproject/MainScreen.fxml",stage, event);

    }
    public void switchToAPI(MouseEvent event) throws IOException {
        stopSoundWhenExit();
        SwitchScreen.switchToScene("/com/app/dictionaryproject/APIScreen.fxml", stage, event);
    }
}
