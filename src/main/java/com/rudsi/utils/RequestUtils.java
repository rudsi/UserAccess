package com.rudsi.utils;

import com.rudsi.models.Request;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestUtils {

    public static List<Request> fetchPendingRequests() {
        List<Request> pendingRequests = new ArrayList<>();

        String query = "SELECT id, user_id, software_id, access_type, reason " +
                "FROM request " +
                "WHERE status = 'Pending'";

        try (Connection connection = DatabaseConnectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int requestId = resultSet.getInt("id");
                int userId = resultSet.getInt("user_id");
                int softwareId = resultSet.getInt("software_id");
                String accessType = resultSet.getString("access_type");
                String reason = resultSet.getString("reason");

                String employeeName = getEmployeeName(userId, connection);
                String softwareName = getSoftwareName(softwareId, connection);

                Request request = new Request();
                request.setId(requestId);
                request.setEmployeeName(employeeName);
                request.setSoftwareName(softwareName);
                request.setAccessType(accessType);
                request.setReason(reason);

                pendingRequests.add(request);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pendingRequests;
    }

    public static Request fetchRequestById(int requestId) {
        Request req = null;
        String query = "SELECT id, user_id, software_id, access_type, reason, status FROM request WHERE id = ?";

        try (Connection conn = DatabaseConnectionPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                req = new Request();
                req.setId(rs.getInt("id"));
                req.setUserId(rs.getInt("user_id"));
                req.setSoftwareId(rs.getInt("software_id"));
                req.setAccessType(rs.getString("access_type"));
                req.setReason(rs.getString("reason"));
                req.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return req;
    }

    public static void updateRequestStatus(Request req) {
        String updateQuery = "UPDATE request SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnectionPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            ps.setString(1, req.getStatus());
            ps.setInt(2, req.getRequestId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getEmployeeName(int userId, Connection connection) {
        String query = "SELECT username FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("username");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    private static String getSoftwareName(int softwareId, Connection connection) {
        String query = "SELECT name FROM software WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, softwareId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }
}
