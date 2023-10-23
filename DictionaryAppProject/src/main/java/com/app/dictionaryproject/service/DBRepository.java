package com.app.dictionaryproject.service;

import com.app.dictionaryproject.Models.Word;

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
    public void insertWord(Word wordToInsert) {
        try {
            PreparedStatement psInsert =
                    connection.prepareStatement("INSERT INTO " +
                            "dict (word, pronunciation, wordtype, meaning, synonym, antonym) VALUES (?,?,?,?,?.?)");
            String word = wordToInsert.getWord_target().strip();
            String phonetic = wordToInsert.getPhonetic().strip();
            String wordType = wordToInsert.getWordType().strip();
            String synonym = wordToInsert.getSynonym().strip();
            String antonym = wordToInsert.getAntonym().strip();
            String definitionWord = wordToInsert.getDefinitionWord().strip();
            psInsert.setString(2,word);
            psInsert.setString(3,phonetic);
            psInsert.setString(4,wordType);
            psInsert.setString(5,definitionWord);
            psInsert.setString(6,synonym);
            psInsert.setString(7,antonym);
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
        String phonetic = "";
        String wordType = "";
        String synonym = "";
        String antonym = "";
        String definitionWord = "";
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (word.isEmpty()){
            return null;
        };
        return new Word(word, phonetic,wordType, synonym, antonym, definitionWord);
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
     public static void main(String[] args) {
        DBRepository db = new DBRepository();
        String tmp;
         Scanner sc = new Scanner(System.in);
         tmp = sc.nextLine();
        Word temp = db.searchWord(tmp);
        System.out.println(temp.getWord_target());
        System.out.println(temp.getPhonetic());
        System.out.println(temp.getWordType().replace("\n ", ","));
        System.out.println(temp.getAntonym());
         System.out.println(temp.getSynonym());
        System.out.println(temp.getDefinitionWord());
    }
}
