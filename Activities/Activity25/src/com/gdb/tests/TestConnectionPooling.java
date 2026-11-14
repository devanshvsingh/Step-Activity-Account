
package com.gdb.tests;

import com.gdb.db.*;
import com.gdb.domain.*;
import com.gdb.repository.*;

import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class TestConnectionPooling {

    public static void main(String[] args) throws Exception {

        System.out.println("=".repeat(60));
        System.out.println(
            "  ACTIVITY 25 - JDBC CONNECTION POOLING (HIKARICP)"
        );
        System.out.println("=".repeat(60));

        // STEP 1: Configure database properties

        Properties props = new Properties();

        props.setProperty(
            "persistence.db.url",
            "jdbc:sqlite:test_pool_gdb.db"
        );

        props.setProperty(
            "persistence.db.driver",
            "org.sqlite.JDBC"
        );

        props.setProperty(
            "persistence.pool.enabled",
            "true"
        );

        props.setProperty(
            "persistence.pool.size",
            "5"
        );

        props.setProperty(
            "persistence.pool.timeout",
            "10000"
        );

        File dbFile = new File("test_pool_gdb.db");

        if (dbFile.exists()) {
            dbFile.delete();
        }

        HikariConnectionProvider provider = null;

        try {

            // ==================================================
            // TEST 1: CONNECTION POOL INITIALIZATION
            // ==================================================

            System.out.println(
                "\n[TEST 1] HikariCP Connection Pool Initialization:"
            );

            provider = new HikariConnectionProvider(props);

            SchemaInitializer.initialize(provider);

            HikariDataSource dataSource =
                provider.getDataSource();

            System.out.println(
                "Pool Name: " +
                dataSource.getPoolName()
            );

            System.out.println(
                "Max Pool Size: " +
                dataSource.getMaximumPoolSize()
            );

            if (dataSource.isClosed()) {
                throw new AssertionError(
                    "Pool initialization failed"
                );
            }

            System.out.println("Pool initialized successfully.");
            System.out.println("-> PASSED");


            // ==================================================
            // TEST 2: CONNECTION BORROWING AND RETURNING
            // ==================================================

            System.out.println(
                "\n[TEST 2] Connection Borrowing & Returning:"
            );

            try (
                Connection conn = provider.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT 1")
            ) {

                if (rs.next()) {

                    System.out.println(
                        "Connection acquired: " +
                        (conn != null)
                    );

                    System.out.println(
                        "Query result: " +
                        rs.getInt(1)
                    );
                }
            }

            System.out.println(
                "Connection closed and returned to pool."
            );

            // Borrow another connection
            try (Connection conn = provider.getConnection()) {

                if (conn.isClosed()) {
                    throw new AssertionError(
                        "Connection unavailable"
                    );
                }

                System.out.println(
                    "Connection successfully borrowed again."
                );
            }

            System.out.println("-> PASSED");


            // ==================================================
            // TEST 3: PERFORMANCE BENCHMARK
            // ==================================================

            System.out.println(
                "\n[TEST 3] Pooled vs Unpooled Performance:"
            );

            int iterations = 50;

            // Unpooled benchmark
            long directStart = System.nanoTime();

            for (int i = 0; i < iterations; i++) {

                try (
                    Connection conn =
                        DriverManager.getConnection(
                            "jdbc:sqlite:test_pool_gdb.db"
                        );

                    Statement stmt = conn.createStatement();

                    ResultSet rs =
                        stmt.executeQuery("SELECT 1")
                ) {

                    if (!rs.next() || rs.getInt(1) != 1) {
                        throw new AssertionError(
                            "Direct query failed"
                        );
                    }
                }
            }

            long directEnd = System.nanoTime();

            // Pooled benchmark
            long poolStart = System.nanoTime();

            for (int i = 0; i < iterations; i++) {

                try (
                    Connection conn = provider.getConnection();

                    Statement stmt = conn.createStatement();

                    ResultSet rs =
                        stmt.executeQuery("SELECT 1")
                ) {

                    if (!rs.next() || rs.getInt(1) != 1) {
                        throw new AssertionError(
                            "Pooled query failed"
                        );
                    }
                }
            }

            long poolEnd = System.nanoTime();

            double directMs =
                (directEnd - directStart) / 1_000_000.0;

            double pooledMs =
                (poolEnd - poolStart) / 1_000_000.0;

            System.out.printf(
                "Unpooled Connections: %.2f ms%n",
                directMs
            );

            System.out.printf(
                "Pooled Connections: %.2f ms%n",
                pooledMs
            );

            System.out.printf(
                "Unpooled Average: %.3f ms/op%n",
                directMs / iterations
            );

            System.out.printf(
                "Pooled Average: %.3f ms/op%n",
                pooledMs / iterations
            );

            if (pooledMs > 0) {

                System.out.printf(
                    "Speedup Factor: %.2fx%n",
                    directMs / pooledMs
                );
            }

            System.out.println(
                "Both connection methods executed successfully."
            );

            System.out.println("-> PASSED");


            // ==================================================
            // TEST 4: REPOSITORY OPERATIONS OVER HIKARICP
            // ==================================================

            System.out.println(
                "\n[TEST 4] Repository Operations over HikariCP:"
            );

            JdbcAccountRepository accountRepository =
                new JdbcAccountRepository(provider);

            IAccount account = AccountFactory.createAccount(
                "SAVINGS",
                1001,
                "Navdeep",
                21,
                50000
            );

            accountRepository.save(account);

            System.out.println(
                "Saved Account #1001 via pooled repository."
            );

            IAccount retrieved =
                accountRepository.findById(1001);

            if (retrieved == null) {
                throw new AssertionError(
                    "Account retrieval failed"
                );
            }

            System.out.println(
                "Retrieved Account #1001: Balance Rs. " +
                retrieved.get_balance()
            );

            if (retrieved.get_balance() != 50000) {
                throw new AssertionError(
                    "Incorrect account balance"
                );
            }

            System.out.println("-> PASSED");


            // ==================================================
            // FINAL RESULT
            // ==================================================

            System.out.println("\n" + "=".repeat(60));

            System.out.println(
                "ALL ACTIVITY 25 CONNECTION POOLING TESTS PASSED!"
            );

            System.out.println("=".repeat(60));

        } finally {

            // Shutdown entire pool
            if (provider != null) {
                provider.shutdown();
                System.out.println(
                    "\nHikariCP connection pool closed."
                );
            }

            // Delete temporary test database
            if (dbFile.exists()) {
                dbFile.delete();
            }
        }
    }
}
