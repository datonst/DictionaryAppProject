package com.app.dictionaryproject.service;

import com.app.dictionaryproject.Models.Word;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DBRepository {

    DatabaseConnection databaseConnection ;
    Connection connection;
    public DBRepository() {
        databaseConnection = new DatabaseConnection();
        connection = databaseConnection.getConnection();
        try {
            PreparedStatement psCreateTable =
                    connection.prepareStatement("CREATE TABLE IF NOT EXISTS word_definition " +
                            "(id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "word VARCHAR(255) NOT NULL, phonetic VARCHAR(255),definitionWord VARCHAR(15000));"
                    );
            psCreateTable.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void deleteWord(String wordToDelete) {
        try  {
            String sql = "DELETE FROM dict WHERE word = ?";
            try (PreparedStatement psDelete = connection.prepareStatement(sql)) {
                psDelete.setString(1, wordToDelete);
                psDelete.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertWord(Word wordToInsert) {
        try {
            PreparedStatement psInsert =
                    connection.prepareStatement("INSERT INTO " +
                            "dict (word, pronunciation, wordtype, meaning, synonym, antonym) VALUES (?,?,?,?,?,?)");
            String word = wordToInsert.getWord_target().strip();
            String phonetic = wordToInsert.getPhonetic().strip();
            String wordType = wordToInsert.getWordType().strip();
            String synonym = wordToInsert.getSynonym().strip();
            String antonym = wordToInsert.getAntonym().strip();
            String definitionWord = wordToInsert.getDefinitionWord().strip();
            psInsert.setString(1,word);
            psInsert.setString(2,phonetic);
            psInsert.setString(3,wordType);
            psInsert.setString(4,definitionWord);
            psInsert.setString(5,synonym);
            psInsert.setString(6,antonym);
            psInsert.executeUpdate();
        } catch (SQLException e) {
            System.out.println("KHONG HOPLE "+wordToInsert.getWord_target());
            System.out.println("KHONG HOPLE "+wordToInsert.getPhonetic());
            System.out.println("KHONG HOPLE "+wordToInsert.getWordType());
            System.out.println("KHONG HOPLE "+wordToInsert.getDefinitionWord());
            System.out.println("KHONG HOPLE "+wordToInsert.getSynonym());
            System.out.println("KHONG HOPLE "+wordToInsert.getAntonym());

            throw new RuntimeException(e);
        }
    }

    public  Word searchWord(String wordToSearch) {
        String word = "";
        String phonetic = "/null/";
        String wordType = "N/A";
        String synonym = "N/A";
        String antonym = "N/A";
        String definitionWord = "not found";
        try {
            String sql = "SELECT * FROM dict WHERE word = ?" ;
            PreparedStatement psSearch = connection.prepareStatement(sql);
            psSearch.setString(1, wordToSearch);
            ResultSet resultSet = psSearch.executeQuery();
            while (resultSet.next()) {
                word = resultSet.getNString("word");
                phonetic= resultSet.getNString("pronunciation");
                wordType = resultSet.getNString("wordtype");
                synonym = resultSet.getNString("synonym");
                antonym = resultSet.getNString("antonym");
                definitionWord = resultSet.getNString("meaning");
            }
            // Show a success alert

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (word.isEmpty()){
            return new Word(wordToSearch, phonetic,wordType, synonym, antonym, definitionWord);
        }
        return new Word(word, phonetic,wordType, synonym, antonym, definitionWord);
    }

    public void updateWord(Word wordToUpdate) {
        try {
            PreparedStatement psUpdate =
                    connection.prepareStatement("UPDATE dict SET pronunciation=?, wordtype=?, meaning=?, synonym=?, antonym=? WHERE word=?");

            String phonetic = wordToUpdate.getPhonetic().strip();
            String wordType = wordToUpdate.getWordType().strip();
            String synonym = wordToUpdate.getSynonym().strip();
            String antonym = wordToUpdate.getAntonym().strip();
            String definitionWord = wordToUpdate.getDefinitionWord().strip();
            String word = wordToUpdate.getWord_target().strip();

            psUpdate.setString(1, phonetic);
            psUpdate.setString(2, wordType);
            psUpdate.setString(3, definitionWord);
            psUpdate.setString(4, synonym);
            psUpdate.setString(5, antonym);
            psUpdate.setString(6, word);

            int rowsAffected = psUpdate.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Word not found in the database: " + wordToUpdate.getWord_target());
                // Handle the case where the word is not found in the database
            } else {
                System.out.println("Word updated successfully: " + wordToUpdate.getWord_target());
            }

        } catch (SQLException e) {
            // Handle database update errors
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> searchListWord(String wordToSearch) {
        ArrayList<String> listWord = new ArrayList<>();
        String word = "";
        try {
            String sql = "SELECT word FROM dict WHERE word like ?" ;
            PreparedStatement psSearch = connection.prepareStatement(sql);
            psSearch.setString(1, wordToSearch + "%");
            ResultSet resultSet = psSearch.executeQuery();
            while (resultSet.next()) {
                word = resultSet.getNString("word");
                listWord.add(word);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listWord;
    }

}
