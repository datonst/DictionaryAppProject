package com.app.dictionaryproject.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public Connection getConnection() {
        String databaseName ="dictionaryapp"; // điền tên scheme trong database
        String port = ""; // điền cổng mà mình có vào đây
        String databaseUser = "root"; // điền tên user
        String databasePassword = ""; // điền password
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
