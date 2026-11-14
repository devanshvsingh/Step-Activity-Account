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


    public static Properties loadPersistenceProperties() {

        Properties properties = new Properties();

        try (
            InputStream input =
                RepositoryFactory.class.getClassLoader()
                .getResourceAsStream("config/persistence.properties")
        ) {

            if (input != null) {

                properties.load(input);
                return properties;
            }

        } catch (IOException e) {

            throw new RuntimeException(e);
        }


        String[] paths = {
            "src/main/resources/config/persistence.properties",
            "config/persistence.properties",
            "bin/config/persistence.properties"
        };

        for (String path : paths) {

            File file = new File(path);

            if (file.exists()) {

                try (InputStream input =
                        new FileInputStream(file)) {

                    properties.load(input);
                    return properties;

                } catch (IOException e) {

                    throw new RuntimeException(e);
                }
            }
        }

        return properties;
    }


    public static String getPersistenceMode() {

        return loadPersistenceProperties()
                .getProperty("persistence.mode", "memory")
                .trim()
                .toLowerCase();
    }


    public static synchronized ConnectionProvider getConnectionProvider() {

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


    public static synchronized AccountRepository getAccountRepository() {

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


    public static synchronized TransactionRepository getTransactionRepository() {

        if (transactionRepository == null) {

            // JDBC transaction repository will be implemented in Activity 24.

            transactionRepository =
                new InMemoryTransactionRepository();
        }

        return transactionRepository;
    }


    public static synchronized void reset() {

        if (connectionProvider != null) {

            connectionProvider.shutdown();

            connectionProvider = null;
        }

        accountRepository = null;

        transactionRepository = null;
    }
}