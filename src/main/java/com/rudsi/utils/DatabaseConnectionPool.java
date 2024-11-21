package com.rudsi.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnectionPool {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnectionPool.class.getName());
    private static HikariDataSource dataSource;

    static {
        initializeDataSource();
    }

    private static void initializeDataSource() {
        try {
            Properties properties = new Properties();

            // Load the properties file from the classpath
            try (InputStream input = DatabaseConnectionPool.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties")) {

                if (input == null) {
                    throw new RuntimeException("Failed to find 'db.properties' in the resources folder");
                }
                properties.load(input);
            }

            // Configure HikariCP
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("org.postgresql.Driver");
            config.setJdbcUrl(properties.getProperty("DB_URL"));
            config.setUsername(properties.getProperty("DB_USER"));
            config.setPassword(properties.getProperty("DB_PASSWORD"));

            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(30000);
            config.setConnectionTestQuery("SELECT 1");

            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");

            dataSource = new HikariDataSource(config);

            try (Connection conn = dataSource.getConnection()) {
                LOGGER.info("Successfully established initial database connection");
            }

        } catch (SQLException e) {
            String errorMessage = "Failed to establish database connection. Error: " + e.getMessage();
            LOGGER.severe(errorMessage);
            throw new ExceptionInInitializerError(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Failed to initialize HikariCP connection pool. Error: " + e.getMessage();
            LOGGER.severe(errorMessage);
            throw new ExceptionInInitializerError(errorMessage);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            LOGGER.severe("DataSource is null - attempting to reinitialize");
            initializeDataSource();
        }
        try {
            Connection conn = dataSource.getConnection();
            if (conn == null) {
                throw new SQLException("Unable to obtain connection from pool");
            }
            return conn;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to get connection from pool", e);
            throw e;
        }
    }

    public static void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            LOGGER.info("HikariCP Connection Pool Closed");
        }
    }

    public static boolean isDatabaseAccessible() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            LOGGER.severe("Database accessibility check failed: " + e.getMessage());
            return false;
        }
    }
}
