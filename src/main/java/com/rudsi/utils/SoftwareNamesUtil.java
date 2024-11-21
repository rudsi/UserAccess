package com.rudsi.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SoftwareNamesUtil {
    public static List<String> getSoftwareNames() throws SQLException {
        List<String> softwareNames = new ArrayList<>();
        try (Connection conn = DatabaseConnectionPool.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT name FROM software");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                softwareNames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching software names", e);
        }
        return softwareNames;
    }
}
