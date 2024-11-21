package com.rudsi.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.rudsi.utils.DatabaseConnectionPool;
import com.rudsi.utils.HashPassword;

public class SignUpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("signup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.sendRedirect("signup.jsp?error=empty_fields");
            return;
        }

        if (role == null || role.trim().isEmpty()) {
            role = "Employee";
        }

        if (doesUsernameExist(username)) {
            response.sendRedirect("signup.jsp?error=username_already_exists");
            return;
        }

        String hashedPassword = HashPassword.hashPassword(password);
        if (hashedPassword == null) {
            response.sendRedirect("signup.jsp?error=hashing_failed");
            return;
        }

        try (Connection connection = DatabaseConnectionPool.getConnection()) {
            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, hashedPassword);
                statement.setString(3, role);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    response.sendRedirect("login.jsp?message=signup_success");
                } else {
                    response.sendRedirect("signup.jsp?error=registration_failed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("signup.jsp?error=database_error");
        }
    }

    private boolean doesUsernameExist(String username) {
        try (Connection connection = DatabaseConnectionPool.getConnection()) {
            String query = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
