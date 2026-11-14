package com.gdb.tests;

import com.gdb.db.ConnectionProvider;
import com.gdb.db.JdbcConnectionProvider;
import com.gdb.db.SchemaInitializer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestJdbcConnection {

    public static void main(String[] args) throws Exception {

        System.out.println("============================================================");

        System.out.println(
                "  ACTIVITY 22 - JDBC CONNECTION & SCHEMA INITIALIZATION");

        System.out.println("============================================================");


        String testDbUrl = "jdbc:sqlite:test_gdb.db";

        ConnectionProvider provider =
                new JdbcConnectionProvider(testDbUrl);


        // TEST 1: Database Connection

        System.out.println("\n[TEST 1] Database Connection Establishment:");

        try (
            Connection connection = provider.getConnection();

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT 1")
        ) {

            if (result.next()) {

                System.out.println("  Connected to: "
                        + connection.getMetaData().getURL());

                System.out.println("  Driver Name: "
                        + connection.getMetaData().getDriverName());

                System.out.println("  -> PASSED");

            } else {

                throw new RuntimeException(
                        "Connection test failed");
            }
        }


        // TEST 2: Schema Initialization

        System.out.println("\n[TEST 2] Schema DDL Execution:");

        SchemaInitializer.initialize(provider);

        System.out.println(
                "  Tables initialized successfully.");

        System.out.println("  -> PASSED");


        // TEST 3: Schema Verification

        System.out.println("\n[TEST 3] Schema Verification:");

        boolean accountsExists = false;

        boolean transactionsExists = false;

        try (
            Connection connection = provider.getConnection();

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery(
                    "SELECT name FROM sqlite_master "
                    + "WHERE type='table'")
        ) {

            while (result.next()) {

                String tableName = result.getString("name");

                if ("accounts".equals(tableName)) {

                    accountsExists = true;
                }

                if ("transactions".equals(tableName)) {

                    transactionsExists = true;
                }
            }
        }


        System.out.println(
                "  Table 'accounts' exists: " + accountsExists);

        System.out.println(
                "  Table 'transactions' exists: " + transactionsExists);


        if (!accountsExists || !transactionsExists) {

            throw new RuntimeException(
                    "Required database tables are missing");
        }

        System.out.println("  -> PASSED");


        provider.shutdown();


        System.out.println("\n============================================================");

        System.out.println(
                "  ALL ACTIVITY 22 JDBC TESTS PASSED!");

        System.out.println("============================================================");
    }
}