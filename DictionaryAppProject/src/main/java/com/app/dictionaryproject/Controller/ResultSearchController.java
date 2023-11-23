package com.app.dictionaryproject.Controller;

import com.app.dictionaryproject.Controller.MainScreenController;
import com.app.dictionaryproject.Models.Word;
import com.app.dictionaryproject.Models.WordShort;
import com.app.dictionaryproject.service.DBRepo;
import com.app.dictionaryproject.service.DBRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ResultSearchController {
    @FXML
    public Scene scene;
    public Stage stage;
    public Parent root;


    @FXML
    public Label words = new Label();
    @FXML
    public Label phonetic = new Label();
    public Label type = new Label();
    public Label explain = new Label();
    public VBox vBox = new VBox();

    private static final String resultHTMLTemplate = """
                <html>
                <head>
                    <style>
                        html {
                            background-color: #fff;
                        }
                        body * {
                            margin: 0;
                        }
                        .nameWord {
                            font-family: Georgia, 'Times New Roman', Times, serif;
                            color: black;
                            font-size: 26px;
                            margin: 0 0 15 0;
                        }
                
                        .pronounWord {
                            font-size: 20px;
                            color: #333;
                            font-weight: 500;
                            margin: 0 0 15 0;
                        }
                
                        .typeWord {
                            font-family: Arial, Helvetica, sans-serif;
                            display: inline-block;
                            background-color: #a6a6a670;
                            color: #7d42f1;
                            padding: 8px;
                            border-radius: 8px;
                            margin: 15 0 15 10;
                            font-size: 18px;
                            font-weight: 800;
                        }
                
                        .meanWord {
                            position: relative;
                            font-family: Arial, Helvetica, sans-serif;
                            display: block;
                            color: #333;
                            margin: 0 0 15 50;
                            font-size: 14px;
                            font-weight: 700;
                        }
                
                        .meanWord::before {
                            position: absolute;
                            content: "";
                            left: -16;
                            border-left: solid 8px #2e6df6 ;
                            display: block;
                            height: 20px;
                        }
                        .extraWord {
                            position: relative;
                            color: #7d42f1;
                            margin: 0 0 0 0;
                            font-size: 16px;
                            font-weight: bold;
                            display: block;
                         
                        }
                        .extraWord p:first-child {
                            color:#7d42f8;
                            font-style: italic;
                            display: inline-block;
                        }
                        .extraWord p:last-child {
                            color: #1299B7;
                            font-style: normal;
                            margin: 0 0 0 10px;
                            display: inline-block;
                        }
                        .exampleWord {
                             position: relative;
                             font-size: 14px;
                             font-weight: 600;
                             font-style: italic;
                             margin: 0 0 15px 80px;
                        }
                        
                        .exampleWord {
                            position: relative;
                            font-size: 14px;
                            font-weight: 700;
                            font-family: Arial, Helvetica, sans-serif;
                            margin: 0 0 15 80;
                        }
                        
                        .exampleWord::before {
                            position: absolute;
                            content: "";
                            top: 6;
                            left: -24;
                            border-left: solid 14px #ff9c07 ;
                            display: block;
                            height: 8px;
                        }
                        
                        .exampleWord p:first-child {
                            color: #1299B7;
                            display: inline;
                            font-style: italic;
                        }
                
                        .exampleWord p:last-child {
                            color: #333;
                            display: inline;
                            font-weight: 400;
                        }
                
                    </style>
                </head>
                <body>
                </body>
                </html>
            """;
    public WebView webView = new WebView();
    public WebEngine webEngine = new WebEngine();
    @FXML
    public void initialize(WordShort currWordResult) {
        String text = currWordResult.getTextDescription();
        int newlineIndex = text.indexOf("\n");

        DBRepository dbRepository = new DBRepository();
        String temp = text.substring(4, newlineIndex);
        Word word = new Word("null","null","null","null","","" );
        if(!temp.isEmpty()) {
            word = dbRepository.searchWord(temp);
        }


        // Lấy dòng đầu tiên nếu tìm thấy
        if (newlineIndex != -1) {
            String firstLine = text.substring(3, newlineIndex);
            words.setText(firstLine);
        } else {
            System.out.println("Chuỗi không chứa dòng nào.");
        }
        if (currWordResult == null) {
            webView.getEngine().loadContent("");
            return;
        }

        String extra = "<h7 class = \"extraWord\">\n" +
                "   <p><em>Từ đồng nghĩa:</em>\n" +
                "   <p>" + word.getSynonym()+"</p>\n" +
                "</h7>\n" +
                "\n" +
                "<h7 class = \"extraWord\">\n" +
                "   <p><em>Từ trái nghĩa:</em> </p>\n" +
                "   <p>" + word.getAntonym() + "</p>\n" +
                "</h7>";
        String html = currWordResult.getHTMLDescription() + extra;
        String fullResult= resultHTMLTemplate.replace("<body>", "<body>" + html);

        webView.getEngine().loadContent(fullResult);
       //displayHTML("/index.html");
    }

    private void displayHTML( String filePath) {
        // Create a WebView and a WebEngine
        webEngine =  webView.getEngine();

        // Load the HTML file from the resources folder
        URL url = getClass().getResource(filePath);
        if (url != null) {
            webEngine.load(url.toExternalForm());
        } else {
            System.err.println("Could not find HTML file: " + filePath);
        }

    }
        public void updateResultView() {
            DBRepo dbRepo = new DBRepo();
            WordShort currWordResult = dbRepo.searchWord("");
        if (currWordResult == null) {
            webView.getEngine().loadContent("");
            return;
        }

        String fullResult= resultHTMLTemplate.replace("<body>", "<body>" + currWordResult.getHTMLDescription());
        webView.getEngine().loadContent(fullResult);
    }

    public static void textToSpeech(String textToSpeak) {
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
        String word = words.getText();
        //word = word.substring(0,word.indexOf("\t"));
        textToSpeech(word);
    }
    public void switchToGame(MouseEvent event) throws IOException {
        SwitchScreen.switchToScene("/com/app/dictionaryproject/GameScreen.fxml",stage, event);
    }
    public void switchToEdit(MouseEvent event) throws IOException {
        SwitchScreen.switchToScene("/com/app/dictionaryproject/EditScreen.fxml",stage, event);
    }
    public void  switchToMain(MouseEvent event) throws IOException {
        SwitchScreen.switchToScene("/com/app/dictionaryproject/MainScreen.fxml",stage, event);

    }
    public void switchToAPI(MouseEvent event) throws IOException {
        SwitchScreen.switchToScene("/com/app/dictionaryproject/APIScreen.fxml", stage, event);
    }

}
