
package com.gdb.repository;

import com.gdb.db.ConnectionProvider;
import com.gdb.db.JdbcConnectionProvider;
import com.gdb.db.SchemaInitializer;

import java.io.*;
import java.util.Properties;

public class RepositoryFactory {

    private static AccountRepository accountRepository;
    private static TransactionRepository transactionRepository;
    private static ConnectionProvider connectionProvider;

    // Load configuration
    public static Properties loadPersistenceProperties() {

        Properties properties = new Properties();

        String[] paths = {
            "config/persistence.properties",
            "src/main/resources/config/persistence.properties",
            "bin/config/persistence.properties"
        };

        for (String path : paths) {

            File file = new File(path);

            if (file.isFile()) {

                try (InputStream input =
                         new FileInputStream(file)) {

                    properties.load(input);
                    return properties;

                } catch (IOException e) {
                    throw new RuntimeException(
                        "Unable to load persistence properties", e
                    );
                }
            }
        }

        return properties;
    }

    // Get persistence mode
    public static String getPersistenceMode() {

        return loadPersistenceProperties()
            .getProperty("persistence.mode", "memory")
            .trim()
            .toLowerCase();
    }

    // Get JDBC connection provider
    public static synchronized ConnectionProvider
            getConnectionProvider() {

        if (connectionProvider == null) {

            Properties properties =
                loadPersistenceProperties();

            String url = properties.getProperty(
                "persistence.db.url",
                "jdbc:sqlite:gdb.db"
            );

            String driver = properties.getProperty(
                "persistence.db.driver",
                "org.sqlite.JDBC"
            );

            connectionProvider =
                new JdbcConnectionProvider(url, driver);

            SchemaInitializer.initialize(connectionProvider);
        }

        return connectionProvider;
    }

    // Get account repository
    public static synchronized AccountRepository
            getAccountRepository() {

        if (accountRepository == null) {

            String mode = getPersistenceMode();

            if ("jdbc".equals(mode)) {

                accountRepository =
                    new JdbcAccountRepository(
                        getConnectionProvider()
                    );

            } else {

                accountRepository =
                    new InMemoryAccountRepository();
            }
        }

        return accountRepository;
    }

    // Get transaction repository
    public static synchronized TransactionRepository
            getTransactionRepository() {

        if (transactionRepository == null) {

            String mode = getPersistenceMode();

            if ("jdbc".equals(mode)) {

                transactionRepository =
                    new JdbcTransactionRepository(
                        getConnectionProvider()
                    );

            } else {

                transactionRepository =
                    new InMemoryTransactionRepository();
            }
        }

        return transactionRepository;
    }

    // Reset repository instances
    public static synchronized void reset() {

        if (connectionProvider != null) {
            connectionProvider.shutdown();
            connectionProvider = null;
        }

        accountRepository = null;
        transactionRepository = null;
    }
}
