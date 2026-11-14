
package com.gdb.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.gdb.exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class HikariConnectionProvider implements ConnectionProvider {

    private final HikariDataSource dataSource;

    // Constructor: Initialize HikariCP connection pool
    public HikariConnectionProvider(Properties config) {

        try {

            HikariConfig hikariConfig = new HikariConfig();

            // Database URL
            String jdbcUrl = config.getProperty(
                "persistence.db.url",
                "jdbc:sqlite:gdb.db"
            );

            // JDBC driver
            String driver = config.getProperty(
                "persistence.db.driver",
                "org.sqlite.JDBC"
            );

            // Pool size
            int poolSize = Integer.parseInt(
                config.getProperty(
                    "persistence.pool.size",
                    "10"
                )
            );

            // Connection timeout
            long timeout = Long.parseLong(
                config.getProperty(
                    "persistence.pool.timeout",
                    "30000"
                )
            );

            // Configure HikariCP
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setDriverClassName(driver);
            hikariConfig.setMaximumPoolSize(poolSize);
            hikariConfig.setConnectionTimeout(timeout);

            hikariConfig.setPoolName("GDB-HikariPool");

            // Validate connections
            hikariConfig.setConnectionTestQuery("SELECT 1");

            // Initialize connection pool
            this.dataSource = new HikariDataSource(hikariConfig);

        } catch (Exception e) {

            throw new DataAccessException(
                "Failed to initialize HikariCP connection pool", e
            );
        }
    }

    // Get a connection from the pool
    @Override
    public Connection getConnection() throws SQLException {

        return dataSource.getConnection();
    }

    // Shut down the connection pool
    @Override
    public void shutdown() {

        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    // Provider information
    @Override
    public String getProviderName() {

        return "HikariConnectionProvider [Pool size: " +
            dataSource.getMaximumPoolSize() + "]";
    }

    // Access HikariDataSource
    public HikariDataSource getDataSource() {

        return dataSource;
    }
}