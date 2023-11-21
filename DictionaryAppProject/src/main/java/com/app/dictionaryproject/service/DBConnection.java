package com.app.dictionaryproject.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public Connection getConnection() {
        String databaseName ="dictionaryDPeng"; // điền tên scheme
        String port = "3306";
        String databaseUser = "root";

        String databasePassword = "04032004";

        String url="jdbc:mysql://localhost:"+ port + "/"+databaseName;
        //System.out.println(url);
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
