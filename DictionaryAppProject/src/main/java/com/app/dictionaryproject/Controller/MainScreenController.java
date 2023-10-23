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

import java.io.IOException;

import java.util.*;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;


public class MainScreenController {
    @FXML
    public TextField Height;
    @FXML
    public ListView<String> listWord;
    public AnchorPane mainPane = new AnchorPane();
    
    @FXML public Button editWord = new Button();

    @FXML
    public Button game = new Button();
    @FXML
    public Button API = new Button();
    @FXML
    public Button searchButton = new Button();

    //Các biến để chuyển đổi qua lịa các màn hình
    public Scene scene;
    public Stage stage;
    public Parent root;

    String[] array = {"hello", "apple", "orange", "window", "car", "mountain", "anxiety", "ant"};

    // chuyển khi chọn nút tìm kiếm
    public void Submit(ActionEvent event) {
        String name = Height.getText();

//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Name");
//        alert.setContentText("Your name: " + name);
//        alert.show();
//
//        String selectedValue = listWord.getSelectionModel().getSelectedItem();
        if (!Objects.equals(name, "")) {

        if (!name.isEmpty()) {

            // Handle the selected item here
            try {
                FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/ResultSearch.fxml"));
                root = Loader.load();


                ResultSearchController controller = Loader.getController();
                DBRepository search = new DBRepository();
                if (search.searchWord(name) != null) {
                    controller.initialize(search.searchWord(name));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    // xử lý trường hợp không tìm thấy từ trong từ điển
                }

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
        } else {
            // xử lý trường hợp không nhập gì vào text Field
        }


    }

    public void SubmitEnter(KeyEvent event) {
        String name = Height.getText();

        if (!name.isEmpty() && event.getCode() == KeyCode.ENTER ) {
            // Handle the selected item here
            try {
                FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/ResultSearch.fxml"));
                root = Loader.load();
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
    public void actionQA(ActionEvent event) {
        String name = Height.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Name");
        alert.setContentText("Your name: " + name);
        alert.show();
    }

    // show list word
    public void showSearch() {
        // Convert array to ObservableList
        DBRepository wordListSQL = new DBRepository();
        ArrayList<String> listSearch = new ArrayList<>();
        String startWord = Height.getText();
        ArrayList<String> listWordFound = wordListSQL.searchListWord(startWord);
        BooleanBinding isInputEmpty = new BooleanBinding() {
            {
                super.bind(Height.textProperty());
            }

            @Override
            protected boolean computeValue() {
                String text = Height.getText();
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

        Height.textProperty().addListener((observable, oldValue, newValue) -> showSearch());

        listWord.setOnMouseClicked(event -> {
            String selectedValue = listWord.getSelectionModel().getSelectedItem();
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


        //Lưu các từ đã tìm vào file
//    ------------------------------------------------
//        listWord.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            // Handle selection change here
////            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/data/saveWord.txt", true))) {
////                // Append the new word to the file
////                writer.write(newValue);
////                writer.newLine();
////            } catch (IOException e) {
////                System.out.println("Error: " + e.getMessage());
////            }
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Items");
//            alert.setHeaderText("Result search: ");
//            alert.setContentText("Item name: " + newValue);
//            alert.show();
//
//        });
    }


    public void handleKeyPress(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
           // Submit(event);
        }
    }

    // chuyển sang các option khác
    public void switchToGame(ActionEvent event) throws IOException {

        FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/GameScreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(Loader.load());
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void handleMouseEntered(MouseEvent event) {
        // Xử lý sự kiện khi chuột đi vào
        game.setStyle("-fx-background-color:white; " +
                "-fx-text-fill: rgb(99, 122, 242); " +
                "-fx-padding: 10px 15px; " +
                "-fx-text-transform: uppercase; " +
                "-fx-letter-spacing: 4px; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-effect: dropshadow(three-pass-box, #03e9f4, 10, 0, 0, 0);" +
                "-fx-background-radius: 25px;");
    }

    @FXML
    public void handleMouseExit(MouseEvent event) {
        // Xử lý sự kiện khi chuột đi vào
        game.setStyle("-fx-background-color: rgb(99, 122, 242); " +
                "-fx-text-fill:white; " +
                "-fx-padding: 10px 15px; " +
                "-fx-text-transform: uppercase; " +
                "-fx-letter-spacing: 4px; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-effect: dropshadow(three-pass-box, #03e9f4, 10, 0, 0, 0);" +
                "-fx-background-radius: 25px;");
    }
    public void switchToEdit(ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/EditScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(Loader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleMouseEnteredEdit(MouseEvent event) {
        // Xử lý sự kiện khi chuột đi vào
        editWord.setStyle("-fx-background-color:white; " +
                "-fx-text-fill: rgb(99, 122, 242); " +
                "-fx-padding: 10px 15px; " +
                "-fx-text-transform: uppercase; " +
                "-fx-letter-spacing: 4px; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-effect: dropshadow(three-pass-box, #03e9f4, 10, 0, 0, 0);" +
                "-fx-background-radius: 25px;");
    }

    @FXML
    public void handleMouseExitEdit(MouseEvent event) {
        // Xử lý sự kiện khi chuột đi vào
        editWord.setStyle("-fx-background-color: rgb(99, 122, 242); " +
                "-fx-text-fill:white; " +
                "-fx-padding: 10px 15px; " +
                "-fx-text-transform: uppercase; " +
                "-fx-letter-spacing: 4px; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-effect: dropshadow(three-pass-box, #03e9f4, 10, 0, 0, 0);" +
                "-fx-background-radius: 25px;");
    }
    public void switchToAPI(ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("/com/app/dictionaryproject/APIScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(Loader.load());
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void handleMouseEnteredAPI(MouseEvent event) {
        // Xử lý sự kiện khi chuột đi vào
        API.setStyle("-fx-background-color:white; " +
                "-fx-text-fill: rgb(99, 122, 242); " +
                "-fx-padding: 10px 15px; " +
                "-fx-text-transform: uppercase; " +
                "-fx-letter-spacing: 4px; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-effect: dropshadow(three-pass-box, #03e9f4, 10, 0, 0, 0);" +
                "-fx-background-radius: 25px;");
    }

    @FXML
    public void handleMouseExitAPI(MouseEvent event) {
        // Xử lý sự kiện khi chuột đi vào
        API.setStyle("-fx-background-color: rgb(99, 122, 242); " +
                "-fx-text-fill:white; " +
                "-fx-padding: 10px 15px; " +
                "-fx-text-transform: uppercase; " +
                "-fx-letter-spacing: 4px; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-effect: dropshadow(three-pass-box, #03e9f4, 10, 0, 0, 0);" +
                "-fx-background-radius: 25px;");
    }

}
