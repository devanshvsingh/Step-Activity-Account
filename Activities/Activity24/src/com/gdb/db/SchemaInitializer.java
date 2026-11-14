
package com.gdb.db;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

import com.gdb.exceptions.DataAccessException;

public final class SchemaInitializer {

    private SchemaInitializer() {
    }

    public static void initialize(ConnectionProvider provider) {

        String accounts =
            "CREATE TABLE IF NOT EXISTS accounts (" +
            "account_number INTEGER PRIMARY KEY, " +
            "customer_name TEXT NOT NULL, " +
            "age INTEGER NOT NULL, " +
            "balance REAL NOT NULL, " +
            "account_type TEXT NOT NULL, " +
            "status TEXT NOT NULL, " +
            "pin TEXT" +
            ")";

        String transactions =
            "CREATE TABLE IF NOT EXISTS transactions (" +
            "id TEXT PRIMARY KEY, " +
            "timestamp TEXT NOT NULL, " +
            "account_number INTEGER NOT NULL, " +
            "type TEXT NOT NULL, " +
            "amount REAL NOT NULL, " +
            "balance_after REAL NOT NULL, " +
            "status TEXT NOT NULL, " +
            "remarks TEXT, " +
            "from_account INTEGER NOT NULL DEFAULT 0, " +
            "to_account INTEGER NOT NULL DEFAULT 0" +
            ")";

        try (
            Connection conn = provider.getConnection();
            Statement stmt = conn.createStatement()
        ) {

            stmt.execute(accounts);
            stmt.execute(transactions);

        } catch (SQLException e) {

            throw new DataAccessException(
                "Unable to initialize database schema", e
            );
        }
    }
}
