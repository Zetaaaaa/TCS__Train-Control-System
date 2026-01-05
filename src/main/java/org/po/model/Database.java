package org.po.model;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class Database {

    Connection connection;

    public void initializeConnection()  {
        connection = null;
        Dotenv dotenv = Dotenv.load();

        String url = dotenv.get("DB_URL");
        String username = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASS");

        System.out.println(username + " " + password);
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(
                    url,username,password
            );
            System.out.println("Connected to database successfully");

//            //Testing functionality
//            ResultSet queryResult = executeQuery(connection,"select * from test");
//
//            while(queryResult.next()){
//                System.out.println(queryResult.getString(1) +" "+queryResult.getString(2));
//            }
        }
        catch (Exception e){
            System.out.println("Error in initializing connection");
            System.out.println(e.getMessage());
        }
    }

    //sql interface
    public ResultSet executeQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet != null) {
            return resultSet;
        }
        else {
            throw new SQLException("No results found");
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    //For testing
    public static void main(String[] args) {
        Database database = new Database();
        database.initializeConnection();
    }



}
