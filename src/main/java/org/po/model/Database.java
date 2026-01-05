package org.po.model;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class Database {

    Connection connection;

    public boolean initializeConnection() throws SQLException {
        connection = null;
        Dotenv dotenv = Dotenv.load();

        String url = dotenv.get("DB_URL");
        String username = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASS");

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(
                    url,username,password
            );
            System.out.println("Connected to database successfully");

        }
        catch (Exception e){
            System.out.println("Error in initializing connection");
            System.out.println(e.getMessage());
            return false;
        }

        return connection.isValid(2);
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

   //update interface
    public String executeUpdate(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        int resultSet = statement.executeUpdate(query);

        if (resultSet > 0) {
            return "good";
        }
        else{
            return "bad";
        }

    }

    public Connection getConnection() {
        return this.connection;
    }

}
