package com.app.dictionaryproject.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DictFactory {
    Connection getConnection(String databaseUser, String databasePassword, String url) {
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
    public Connection getDictDAO(String type)  {
        String databaseName;
        String port;
        String databaseUser ;
        String databasePassword;
        String url;
        if (type.equalsIgnoreCase("dict1")){
            databaseName ="dictionaryDPeng"; // điền tên scheme
            port = "3309";
            databaseUser = "root";
            databasePassword = "12345678";
            url="jdbc:mysql://localhost:"+ port + "/" + databaseName;
            return getConnection(databaseUser,databasePassword,url);
        } else {
            databaseName ="dictionaryapp"; // điền tên scheme
            port = "3309";
            databaseUser = "root";
            databasePassword = "12345678";
            url="jdbc:mysql://localhost:"+ port + "/"+ databaseName;
            return getConnection(databaseUser,databasePassword,url);
        }
    }
}
