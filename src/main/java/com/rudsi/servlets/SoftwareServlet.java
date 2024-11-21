package com.rudsi.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.rudsi.utils.DatabaseConnectionPool;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SoftwareServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("createSoftware.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String softwareName = request.getParameter("softwareName");
        String description = request.getParameter("description");
        String[] accessLevels = request.getParameterValues("accessLevels");

        if (softwareName == null || softwareName.isEmpty() || description == null || description.isEmpty()) {
            response.sendRedirect("createSoftware.jsp?message=Please+fill+all+fields");
            return;
        }

        String accessLevel = accessLevels[0];

        try (Connection connection = DatabaseConnectionPool.getConnection()) {
            String query = "INSERT INTO software (name, description, access_levels) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, softwareName);
                statement.setString(2, description);
                statement.setString(3, accessLevel);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    response.sendRedirect("createSoftware.jsp?message=Software+added+successfully");
                } else {
                    response.sendRedirect("createSoftware.jsp?message=Failed+to+add+software");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("createSoftware.jsp?message=Database+error");
        }
    }
}
