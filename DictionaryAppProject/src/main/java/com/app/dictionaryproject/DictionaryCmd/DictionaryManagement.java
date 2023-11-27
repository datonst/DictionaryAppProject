package com.app.dictionaryproject.DictionaryCmd;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class DictionaryManagement extends Dictionary {
    Scanner sc = new Scanner(System.in);

    //phương thức thêm từ bằng commandline
    void insertFromCommandline() {
        boolean added = false;
        System.out.println("Enter the number of words you want to insert: ");
        int quantity = 0;
        try {
            quantity = sc.nextInt();
        } catch (Exception e) {
            System.out.println("error");
            return;
        }
        sc.nextLine(); // đọc bỏ dòng trống

        for(int i = 0; i < quantity; i++) {
            System.out.println("Enter word: ");
            String word_target = sc.nextLine();
            word_target = formatWord(word_target);

            System.out.println("Enter word's meaning: ");
            String word_explain = sc.nextLine();
            word_explain = formatWord(word_explain);

            WordCmd newWord = new WordCmd();
            newWord.setWord_target(word_target);
            newWord.addWord_explain(word_explain);
            if(addWord(newWord)) {
                added = true;
            }
            System.out.println("added successfully!\n");
        }

        if(added) {
            sortDictionary();// nếu thêm được từ thì sắp xếp
        }
    }

    //phương thức nhập từ file
    public void  insertFromFile(String filepath) {
        Path path = Path.of(filepath);
        try {
            List<String> word_list = Files.readAllLines(path);

            // Populate the dictionary with words and meanings
            for(String word_data : word_list){
                String[] wordFromFile = word_data.split("\t");
                WordCmd newWord = new WordCmd();
//				 wordFromFile[0] = wordFromFile[0].replaceAll(" ", "");
//				 wordFromFile[1] = wordFromFile[1].substring(1);
                newWord.setWord_target(formatWord(wordFromFile[0]));
                newWord.addWord_explain(formatWord(wordFromFile[1]));
                addWord(newWord);
            }
            sortDictionary();
        } catch (IOException e) {
            e.printStackTrace();


        }
    }

    //phương thức xuất ra file
    public void exportToFile(String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (int i = 0 ; i < size ; i++) {
                for (String explain : words[i].getWord_explains()) {
                    writer.write(words[i].getWord_target());
                    writer.write("\t");
                    writer.write(explain);
                    writer.newLine();
                }
            }
            writer.close();
            System.out.println("Data has been exported to the file " + fileName);
        } catch (IOException e) {
            System.out.println("File path not found.");
            e.printStackTrace();
        }
    }


    //phương thức tra cứu
    void dictionaryLookup() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the word you want to look up: ");
        String word = sc.nextLine();
        int index = binarySearch(word);
        if(index == -1) {
            System.out.println("Word not found in the dictionary");
        }else {
            System.out.println("English             | Vietnamese          ");
            words[index].printWord(false);
        }
        System.out.println("Do you want to listen the pronunciation of this word? (y/n)");
        String choosen = scanner.nextLine();
        if(choosen.equals("y")){
            textToSpeech(word);
        }
    }
    public static void textToSpeech(String textToSpeak) {
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
    //phương thức sửa từ
    void fixWord() {
        System.out.println("Enter the word you want to edit: ");
        String wordToEdit = sc.nextLine();
        int index = binarySearch(wordToEdit);
        if(index == -1) {
            System.out.println("Word not found in the dictionary");
            return;
        }else {
            System.out.println("English             | Vietnamese          ");
            words[index].printWord(false);
        }
        while(true) {
            System.out.println("\nWhat you want to edit: ");
            System.out.println("0. Exit");
            System.out.println("1. Edit word");
            System.out.println("2. Remove explain");
            System.out.println("3. Add explain");
            System.out.println("Your action: ");
            String selection = sc.nextLine();
            switch (selection) {
                case "1": {
                    System.out.println("Enter new word: ");
                    String newWord = sc.nextLine();
                    words[index].setWord_target(newWord);
                    sortDictionary();
                    break;
                }
                case "2": {
                    System.out.println("Enter the explain you want to remove: ");
                    String explain = sc.nextLine();
                    words[index].removeWord_explain(formatWord(explain));
                    break;
                }
                case "3": {
                    System.out.println("Enter the explain you want to add: ");
                    String explain = sc.nextLine();
                    words[index].addWord_explain(formatWord(explain));
                    break;
                }
                case "0" :{
                    System.out.println("Exiting");
                    return;
                }
                default:
                    System.out.println("Action not supported.");
                    return;
            }
            System.out.println("English             | Vietnamese          ");
            words[index].printWord(false);
        }
    }

    //phương thức xóa từ
    void removeWord() {
        System.out.println("Enter the word you want to remove: ");
        String wordToRemove = sc.nextLine();
        int indexToRemove = binarySearch(wordToRemove);

        if(indexToRemove == -1) {
            System.out.println("Word not found in the dictionary");
        }else {
            for(int i = indexToRemove; i < size - 1; i++) {
                words[i] = words[i + 1];
            }
            words[size - 1] = null;
            size--;
            System.out.println("Word removed successfully");
        }
    }
    // phương thức chuẩn hóa đầu vào
    private String formatWord(String input) {
        if(input.isEmpty()) {
            return input;
        }else {
            input = input.substring(0, 1).toUpperCase() + 	input.substring(1).toLowerCase()	;
        }
        return input;
    }
    //game
    public int randomIndex() {
        Random random = new Random();
        return random.nextInt(size);
    }

    public boolean findMeaning(String word, int index){
        if (words[index].containsWord_explain(word)){
            return true;
        }
        return false;
    }
}
