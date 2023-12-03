package com.app.dictionaryproject.Repository;


import com.app.dictionaryproject.Models.WordShort;
import com.app.dictionaryproject.WordForm.WordRoot;

import java.sql.*;
import java.util.ArrayList;

public class Dict1DAOImpl implements DictDAO {
    Connection connection;
    public Dict1DAOImpl()  {
        DictFactory dictFactory = new DictFactory();
        connection = dictFactory.getDictDAO("dict1");
    }


    @Override
    public void insertWord(WordRoot wordRoot) {
        try {
            WordShort wordToInsert = (WordShort) wordRoot;
            PreparedStatement psInsert =
                    connection.prepareStatement("INSERT INTO " +
                            "dictionary (word, textDescription, htmlDescription) VALUES (?,?,?)");

            // Thiết lập giá trị cho các tham số
            psInsert.setString(1, wordToInsert.getWord());
            psInsert.setString(2, wordToInsert.getTextDescription());
            psInsert.setString(3, wordToInsert.getHTMLDescription());

            // Thực hiện câu lệnh SQL chèn dữ liệu vào cơ sở dữ liệu
            psInsert.executeUpdate();

            // Đóng PreparedStatement sau khi sử dụng
            psInsert.close();

        } catch (SQLException e) {
            // Xử lý ngoại lệ SQL
            throw new RuntimeException(e);
        }
    }


    @Override
    public  WordShort searchWord(String wordToSearch) {
        String word = "This word does not have meaning";
        String text = "This word does not have meaning";
        String html = "This word does not have meaning";
        try {
            String sql = "SELECT * FROM dictionary WHERE word = ?" ;
            PreparedStatement psSearch = connection.prepareStatement(sql);
            psSearch.setString(1, wordToSearch);
            ResultSet resultSet = psSearch.executeQuery();
            while (resultSet.next()) {
                word = resultSet.getNString("word");
                text= resultSet.getNString("textDescription");
                html = resultSet.getNString("htmlDescription");

            }
            // Show a success alert

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (word.isEmpty()){
            return new WordShort(word, text, html);
        }
        return new WordShort(word, text, html);
    }

    @Override
    public ArrayList<String> searchListWord(String wordToSearch) {
        ArrayList<String> listWord = new ArrayList<>();
        String word = "";
        try {
            String sql = "SELECT word FROM dictionary WHERE word like ?" ;
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

    @Override
    public void deleteWord(String wordToDelete) {
        try  {
            String sql = "DELETE FROM dictionary WHERE word = ?";
            try (PreparedStatement psDelete = connection.prepareStatement(sql)) {
                psDelete.setString(1, wordToDelete);
                psDelete.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateWord(WordRoot wordRoot) {
        try {
            WordShort wordToUpdate = (WordShort) wordRoot;
            PreparedStatement psUpdate =
                    connection.prepareStatement("UPDATE dictionary SET textDescription=?, htmlDescription=? WHERE word=?");

            String word = wordToUpdate.getWord().strip();
            String textScript = wordToUpdate.getTextDescription();
            String htmlScript = wordToUpdate.getHTMLDescription();

            psUpdate.setString(1, textScript);
            psUpdate.setString(2, htmlScript);
            psUpdate.setString(3, word);
            psUpdate.executeUpdate();
        } catch (SQLException e) {
            // Handle database update errors
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existWord(String word) {
        String sql = "SELECT word from dictionary WHERE word= ?" ;
        PreparedStatement psSearch = null;
        try {
            psSearch = connection.prepareStatement(sql);
            psSearch.setString(1, word);
            ResultSet resultSet = psSearch.executeQuery();
            String check = null;
            while(resultSet.next()) {
                check = resultSet.getString(1);
            }
            return check != null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
