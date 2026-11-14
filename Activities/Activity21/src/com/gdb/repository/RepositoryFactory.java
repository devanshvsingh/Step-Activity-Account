package com.gdb.repository;

import java.io.*;
import java.util.Properties;

public class RepositoryFactory {

    private static AccountRepository accountRepository;
    private static TransactionRepository transactionRepository;

    private static String getPersistenceMode() {

        Properties properties = new Properties();

        try (InputStream input =
                RepositoryFactory.class.getClassLoader()
                .getResourceAsStream("config/persistence.properties")) {

            if (input != null) {
                properties.load(input);
            } else {
                try (InputStream file = new FileInputStream(
                        "src/main/resources/config/persistence.properties")) {
                    properties.load(file);
                }
            }

        } catch (IOException e) {
            return "memory";
        }

        return properties.getProperty("persistence.mode", "memory")
                .trim().toLowerCase();
    }

    public static synchronized AccountRepository getAccountRepository() {

        String mode = getPersistenceMode();

        if (!mode.equals("memory")) {
            throw new UnsupportedOperationException(
                    "Only memory persistence is currently implemented");
        }

        if (accountRepository == null) {
            accountRepository = new InMemoryAccountRepository();
        }

        return accountRepository;
    }

    public static synchronized TransactionRepository getTransactionRepository() {

        String mode = getPersistenceMode();

        if (!mode.equals("memory")) {
            throw new UnsupportedOperationException(
                    "Only memory persistence is currently implemented");
        }

        if (transactionRepository == null) {
            transactionRepository = new InMemoryTransactionRepository();
        }

        return transactionRepository;
    }
}