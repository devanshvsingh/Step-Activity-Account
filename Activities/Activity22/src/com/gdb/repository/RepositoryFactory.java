package com.gdb.repository;

import com.gdb.db.ConnectionProvider;
import com.gdb.db.JdbcConnectionProvider;
import com.gdb.db.SchemaInitializer;

import java.io.*;
import java.util.Properties;

/**
 * Factory creating repositories based on persistence configuration.
 */
public class RepositoryFactory {

    private static AccountRepository accountRepositoryInstance;

    private static TransactionRepository transactionRepositoryInstance;

    private static ConnectionProvider connectionProviderInstance;

    public static Properties loadPersistenceProperties() {

        Properties props = new Properties();

        String[] paths = {
            "config/persistence.properties",
            "src/main/resources/config/persistence.properties",
            "bin/config/persistence.properties"
        };

        for (String path : paths) {

            File file = new File(path);

            if (file.exists()) {

                try (InputStream input =
                        new FileInputStream(file)) {

                    props.load(input);

                    return props;

                } catch (IOException e) {

                    throw new RuntimeException(
                            "Unable to load persistence properties", e);
                }
            }
        }

        try (InputStream input =
                RepositoryFactory.class.getClassLoader()
                .getResourceAsStream("config/persistence.properties")) {

            if (input != null) {
                props.load(input);
            }

        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        return props;
    }

    public static String getPersistenceMode() {

        return loadPersistenceProperties()
                .getProperty("persistence.mode", "memory")
                .trim()
                .toLowerCase();
    }

    /**
     * Activity 22: Initialize JDBC connection provider.
     */
    public static synchronized ConnectionProvider getConnectionProvider() {

        if (connectionProviderInstance == null) {

            Properties props = loadPersistenceProperties();

            String url = props.getProperty(
                    "persistence.db.url",
                    "jdbc:sqlite:gdb.db");

            String driver = props.getProperty(
                    "persistence.db.driver",
                    "org.sqlite.JDBC");

            connectionProviderInstance =
                    new JdbcConnectionProvider(url, driver);

            SchemaInitializer.initialize(
                    connectionProviderInstance);
        }

        return connectionProviderInstance;
    }

    public static synchronized AccountRepository getAccountRepository() {

        if (accountRepositoryInstance == null) {

            String mode = getPersistenceMode();

            if ("memory".equals(mode)) {

                accountRepositoryInstance =
                        new InMemoryAccountRepository();

            } else if ("jdbc".equals(mode)) {

                getConnectionProvider();

                throw new UnsupportedOperationException(
                        "JdbcAccountRepository will be implemented "
                        + "in Activity 23.");

            } else {

                accountRepositoryInstance =
                        new InMemoryAccountRepository();
            }
        }

        return accountRepositoryInstance;
    }

    public static synchronized TransactionRepository getTransactionRepository() {

        if (transactionRepositoryInstance == null) {

            String mode = getPersistenceMode();

            if ("memory".equals(mode)) {

                transactionRepositoryInstance =
                        new InMemoryTransactionRepository();

            } else if ("jdbc".equals(mode)) {

                getConnectionProvider();

                throw new UnsupportedOperationException(
                        "JdbcTransactionRepository will be implemented "
                        + "in Activity 24.");

            } else {

                transactionRepositoryInstance =
                        new InMemoryTransactionRepository();
            }
        }

        return transactionRepositoryInstance;
    }

    public static synchronized void reset() {

        if (connectionProviderInstance != null) {

            connectionProviderInstance.shutdown();

            connectionProviderInstance = null;
        }

        accountRepositoryInstance = null;

        transactionRepositoryInstance = null;
    }
}