package com.rudsi.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.rudsi.utils.DatabaseConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String softwareName = request.getParameter("softwareName");
        String accessType = request.getParameter("accessType");
        String reason = request.getParameter("reason");

        String username = (String) request.getSession().getAttribute("username");

        if (username == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in.");
            return;
        }

        Integer userId = null;

        String getUserIdQuery = "SELECT id FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(getUserIdQuery)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    userId = resultSet.getInt("id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error fetching user ID", e);
        }

        if (userId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user.");
            return;
        }

        Integer softwareId = null;
        String getSoftwareIdQuery = "SELECT id FROM software WHERE name = ?";

        try (Connection connection = DatabaseConnectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(getSoftwareIdQuery)) {

            preparedStatement.setString(1, softwareName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    softwareId = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error fetching software ID", e);
        }

        if (softwareId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid software name.");
            return;
        }

        String insertQuery = "INSERT INTO request (user_id, software_id, access_type, reason, status) VALUES (?, ?, ?, ?, 'Pending')";

        try (Connection connection = DatabaseConnectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, softwareId);
            preparedStatement.setString(3, accessType);
            preparedStatement.setString(4, reason);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                request.setAttribute("successMessage", "Request successfully sent!");
            } else {
                request.setAttribute("errorMessage", "Failed to submit request.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error inserting request into database", e);
        }
        request.getRequestDispatcher("requestAccess.jsp").forward(request, response);
    }
}
