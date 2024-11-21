package com.rudsi; // This should match your package structure

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.rudsi.utils.HashPassword;

public class Main {

    private static final String URL = "jdbc:postgresql://localhost:5432/management";
    private static final String USER = "postgres";
    private static final String PASSWORD = "rudsi123###";

    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database successfully!");
            return connection;
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database!");
            e.printStackTrace();
            throw e;
        }
    }

    public static void createAdmin(Connection connection) throws SQLException {
        String insertQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            String password = HashPassword.hashPassword("manager");
            statement.setString(1, "Manager");
            statement.setString(2, password);
            statement.setString(3, "Manager");

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Manager user created successfully!");
            } else {
                System.out.println("Failed to create Manager user.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            Connection connection = getConnection();
            if (connection != null) {
                createAdmin(connection);
                System.out.println("Connection is successful!");
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
