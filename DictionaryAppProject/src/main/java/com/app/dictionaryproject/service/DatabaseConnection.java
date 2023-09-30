package com.app.dictionaryproject.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public Connection getConnection() {
        String databaseName ="dictionaryapp"; // điền tên scheme
        String port = "3309";
        String databaseUser = "root";
        String databasePassword = "son1863824";
        String url="jdbc:mysql://localhost:"+port+"/"+databaseName;
        System.out.println(url);
        Connection databaseLink;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try {
                databaseLink = DriverManager.getConnection(url,databaseUser,databasePassword);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return databaseLink;
    }

}
