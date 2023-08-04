package com.example.petroleumsystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ReportConnection {
    public  static Connection connection;
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static Connection createConnection() throws SQLException, ClassNotFoundException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/yow" , "root" , "");
        return connection;
    }
}
