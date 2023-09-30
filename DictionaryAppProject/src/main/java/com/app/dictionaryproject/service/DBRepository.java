package com.app.dictionaryproject.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    public void insertWord(String word, String phonetic, String definitionWord) {
        try {
            PreparedStatement psInsert =
                    connection.prepareStatement("INSERT INTO " +
                            "word_definition (word, phonetic, definitionWord) VALUES (?,?,?)");
            word =word.strip();
            phonetic = phonetic.strip();
            definitionWord =definitionWord.strip();
            psInsert.setString(1,word);
            psInsert.setString(2,phonetic);
            psInsert.setString(3,definitionWord);
            psInsert.executeUpdate();
        } catch (SQLException e) {
            System.out.println("KHONG HOPLE "+word);
            System.out.println("KHONG HOPLE "+phonetic);
            System.out.println("KHONG HOPLE "+definitionWord);
            throw new RuntimeException(e);
        }
    }


}
