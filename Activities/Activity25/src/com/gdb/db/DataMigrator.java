
package com.gdb.db;

import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;

import com.gdb.repository.AccountRepository;
import com.gdb.repository.TransactionRepository;

import java.util.List;

public class DataMigrator {

    private DataMigrator() {
    }

    // Migration statistics
    public static class MigrationResult {

        private final int accountsMigrated;
        private final int transactionsMigrated;

        public MigrationResult(
            int accountsMigrated,
            int transactionsMigrated
        ) {

            this.accountsMigrated = accountsMigrated;
            this.transactionsMigrated = transactionsMigrated;
        }

        public int getAccountsMigrated() {
            return accountsMigrated;
        }

        public int getTransactionsMigrated() {
            return transactionsMigrated;
        }

        @Override
        public String toString() {

            return "Migration Summary:\n" +
                "Accounts migrated: " + accountsMigrated + "\n" +
                "Transactions migrated: " + transactionsMigrated;
        }
    }

    // Migrate accounts and transactions
    public static MigrationResult migrate(
        AccountRepository srcAcc,
        AccountRepository destAcc,
        TransactionRepository srcTxn,
        TransactionRepository destTxn
    ) {

        if (srcAcc == null || destAcc == null ||
            srcTxn == null || destTxn == null) {

            throw new IllegalArgumentException(
                "Repositories cannot be null"
            );
        }

        int accountCount = 0;
        int transactionCount = 0;

        // Migrate accounts
        List<IAccount> accounts = srcAcc.findAll();

        for (IAccount account : accounts) {

            if (account != null) {
                destAcc.save(account);
                accountCount++;
            }
        }

        // Migrate transactions
        List<Transaction> transactions = srcTxn.findAll();

        for (Transaction transaction : transactions) {

            if (transaction != null) {
                destTxn.save(transaction);
                transactionCount++;
            }
        }

        return new MigrationResult(
            accountCount,
            transactionCount
        );
    }
}
