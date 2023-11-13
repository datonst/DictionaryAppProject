package com.app.dictionaryproject.Controller;



//import com.app.dictionaryproject.Controller.ResultSearchController;
import com.app.dictionaryproject.Models.Word;
import com.app.dictionaryproject.service.DBRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;


public class MainScreenController {
    @FXML
    public TextField  wordSearch;
    @FXML
    public ListView<String> listWord;
    public ListView<String> listWordArchive = new ListView<>();
    public ArrayList<String> words_list = new ArrayList<>();
    public AnchorPane archivePane = new AnchorPane();
    public AnchorPane mainPane = new AnchorPane();
    
    @FXML public Button editWord = new Button();

    @FXML
    public Button game = new Button();
    @FXML
    public Button API = new Button();
    public Button pratice = new Button();
    @FXML
    public Button searchButton = new Button();
    public Button archiveWord = new Button();

    private static Set<String> checkUnique = new HashSet<>();
    //Các biến để chuyển đổi qua lịa các màn hình
    public Scene scene;
    public Stage stage;
    public Parent root;

    public void actionQA(ActionEvent event) {
        String name =  wordSearch.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Name");
        alert.setContentText("Your name: " + name);
        alert.show();
    }

    public void showArchive(MouseEvent event) {
        archivePane.setVisible(!archivePane.isVisible());
    }
    private void  insertFromFile(String filePath) {
        Path path = Path.of(filePath);

        try {
            List<String> lines = Files.readAllLines(path);

            words_list.addAll(lines);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }
    }

    public void showArchive() {
        // Convert array to ObservableList
        insertFromFile("src/main/resources/data/saveWord.txt");
        listWordArchive.getItems().setAll(words_list);
    }

    // chuyển khi chọn nút tìm kiếm
    public void Submit(ActionEvent event) {
        String name = wordSearch.getText().trim();
            if (!name.isEmpty()) {

                // Handle the selected item here
                try {
                    FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/ResultSearch.fxml"));
                    root = Loader.load();


                    ResultSearchController controller = Loader.getController();
                    DBRepository search = new DBRepository();
                    controller.initialize(search.searchWord(name));

                    // Lấy từ khi search
//                ResultSearchController controller = Loader.getController();
//                controller.initialize(new Word(name,"/122/", "nvv"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


    }
    public void SubmitEnter(KeyEvent event) {
        String name = wordSearch.getText().trim();

        if (!name.isEmpty() && event.getCode() == KeyCode.ENTER ) {
            // Handle the selected item here
            try {
                FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/ResultSearch.fxml"));
                root = Loader.load();

                ResultSearchController controller = Loader.getController();
                DBRepository search = new DBRepository();
                ArrayList<String> listWordFound = search.searchListWord(name);
                if(search.searchWord(name).getDefinitionWord().equals("not found") && !listWordFound.isEmpty()) {
                    controller.initialize(search.searchWord(listWordFound.get(0)));
                    addSaveWord(listWordFound.get(0));
                } else {
                    controller.initialize(search.searchWord(name));
                }
                if(!search.searchWord(name).getDefinitionWord().equals("not found")){
                    addSaveWord(name);
                }
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }

    // show list word
    public void showSearch() {
        // Convert array to ObservableList
        DBRepository wordListSQL = new DBRepository();
        ArrayList<String> listSearch = new ArrayList<>();
        String startWord =  wordSearch.getText().trim();
        ArrayList<String> listWordFound = wordListSQL.searchListWord(startWord);
        BooleanBinding isInputEmpty = new BooleanBinding() {
            {
                super.bind( wordSearch.textProperty());
            }

            @Override
            protected boolean computeValue() {
                String text =  wordSearch.getText();
                return text == null || text.trim().isEmpty();
            }
        };
        listWord.visibleProperty().bind(Bindings.not(isInputEmpty));

        listWord.getItems().setAll(listWordFound);
        if (startWord.isBlank()) {
            listWord.getItems().setAll(listSearch);

        }

    }

    //chuyển screen khi chọn từ trong listword
    public void initialize() {
        showArchive();
        wordSearch.textProperty().addListener((observable, oldValue, newValue) -> showSearch());

        returnExplain(listWord);

        returnExplain(listWordArchive);

        //Lưu các từ đã tìm vào file
//    ------------------------------------------------
        listWord.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Handle selection change here
            addSaveWord(newValue);

        });
    }

    private void returnExplain(ListView<String> list) {
        list.setOnMouseClicked(event -> {
            String selectedValue = list.getSelectionModel().getSelectedItem();
            if (selectedValue != null) {
                // Handle the selected item here
                try {
                    FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/ResultSearch.fxml"));
                    root = Loader.load();
                    ResultSearchController controller = Loader.getController();
                    DBRepository search = new DBRepository();
                    controller.initialize(search.searchWord(selectedValue));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void addSaveWord(String newValue) {


        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/data/saveWord.txt", true))) {
            // Append the new word to the file
            if(!checkUnique.contains(newValue)) {
                writer.write(newValue);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        checkUnique.add(newValue);
    }


    // chuyển sang các option khác
    public void switchToGame(MouseEvent event) throws IOException {

        FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/GameScreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(Loader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleMouseEnteredGame(MouseEvent event) {
        // Xử lý sự kiện khi chuột đi vào
        handleMouseEntered(game);
    }

    @FXML
    public void handleMouseExitGame(MouseEvent event) {
        // Xử lý sự kiện khi chuột đi vào
        handleMouseExit(game);
    }
    public void switchToEdit(MouseEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/EditScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(Loader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleMouseEnteredEdit(MouseEvent event) {
        // Xử lý sự kiện khi chuột đi vào
        handleMouseEntered((editWord));
    }

    @FXML
    public void handleMouseExitEdit(MouseEvent event) {
        // Xử lý sự kiện khi chuột đi vào
        handleMouseExit(editWord);
    }
    public void switchToAPI(MouseEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/APIScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(Loader.load());
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void handleMouseEnteredAPI(MouseEvent event) {
        // Xử lý sự kiện khi chuột đi vào
        handleMouseEntered(API);
    }

    @FXML
    public void handleMouseExitAPI(MouseEvent event) {
        // Xử lý sự kiện khi chuột đi vào
        handleMouseExit(API);
    }

    public  void handleMouseEnteredPractice(MouseEvent event) {
        handleMouseEntered(pratice);
    }

    public void handleMouseExitPractice(MouseEvent event){
        handleMouseExit(pratice);
    }
    @FXML
    public void handleMouseExit(Button button) {
        // Xử lý sự kiện khi chuột đi vào
        button.setStyle("-fx-background-color: rgb(99, 122, 242); " +
                "-fx-text-fill:white; " );
    }
    private void handleMouseEntered(Button button){
        button.setStyle("-fx-background-color:white; " +
                "-fx-text-fill: rgb(99, 122, 242); "
                );
    }

}
