package com.example.assign02_final_s3988110.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:application.db";

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(DB_URL);
    }
}
