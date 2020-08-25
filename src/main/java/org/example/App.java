package org.example;

import org.example.windows.EntryWindow;
import org.example.windows.RecordWindow;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class App {

    public static Connection connection;

    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("AccessInfo"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection(properties.getProperty("address"), properties.getProperty("user"), properties.getProperty("password"));
        //start window
        new EntryWindow();
        //new RecordWindow();
    }
}
