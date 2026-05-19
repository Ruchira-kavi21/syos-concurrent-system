package server.infrastructure;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/syos_db";
    private static final String USER = "root";
    private static final String PASSWORD = "ruchira2005";

    private DatabaseConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = java.sql.DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully.");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database.");
        }
    }
    public static DatabaseConnection getInstance(){
        if (instance == null){
            instance = new DatabaseConnection();
        }else {
            try {
                if (instance.getConnection().isClosed()){
                    instance = new DatabaseConnection();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return instance;
    }
    public Connection getConnection(){
        return connection;
    }
}
