
package com.gdb.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionProvider implements ConnectionProvider {

    private final String url;

    public JdbcConnectionProvider(String url) {
        this.url = url;

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                "SQLite JDBC driver not found", e
            );
        }
    }

    public JdbcConnectionProvider(String url, String driver) {
        this.url = url;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                "JDBC driver not found: " + driver, e
            );
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    @Override
    public void shutdown() {
        // SQLite connections are closed individually.
    }

    @Override
    public String getProviderName() {
        return "SQLite JDBC";
    }
}
