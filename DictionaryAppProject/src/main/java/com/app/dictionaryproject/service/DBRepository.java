package com.app.dictionaryproject.service;

import com.app.dictionaryproject.Models.Word;

import java.sql.*;
import java.util.ArrayList;

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
    public void insertWord(Word wordToInsert) {
        try {
            PreparedStatement psInsert =
                    connection.prepareStatement("INSERT INTO " +
                            "word_definition (word, phonetic, definitionWord) VALUES (?,?,?)");
            String word = wordToInsert.getWordTarget().strip();
            String phonetic = wordToInsert.getPhonetic().strip();
            String definitionWord = wordToInsert.getDefinitionWord().strip();
            psInsert.setString(1,word);
            psInsert.setString(2,phonetic);
            psInsert.setString(3,definitionWord);
            psInsert.executeUpdate();
        } catch (SQLException e) {
            System.out.println("KHONG HOPLE "+wordToInsert.getWordTarget());
            System.out.println("KHONG HOPLE "+wordToInsert.getPhonetic());
            System.out.println("KHONG HOPLE "+wordToInsert.getDefinitionWord());
            throw new RuntimeException(e);
        }
    }

    public Word searchWord(String wordToSearch) {
        String phonetic = "";
        String definitionWord = "";
        try {
            String sql = "SELECT * FROM word_definition WHERE word = ?" ;
            PreparedStatement psSearch = connection.prepareStatement(sql);
            psSearch.setString(1, wordToSearch);
            ResultSet resultSet = psSearch.executeQuery();
            while (resultSet.next()) {
                phonetic= resultSet.getNString("phonetic");
                definitionWord = resultSet.getNString("definitionWord");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Word(wordToSearch, phonetic, definitionWord);
    }

    public ArrayList<String> searchListWord(String wordToSearch) {
        ArrayList<String> listWord = new ArrayList<>();
        String word = "";
        try {
            String sql = "SELECT * FROM word_definition WHERE word like ?" ;
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
//     public static void main(String[] args) {
//        DBRepository db = new DBRepository();
//        Word temp = db.searchWord("water");
//        System.out.println(temp.getWordTarget());
//        System.out.println(temp.getPhonetic());
//        System.out.println(temp.getDefinitionWord());
//    }
//    }
}
