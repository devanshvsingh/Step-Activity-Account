
package com.gdb.tests;

import com.gdb.db.*;
import com.gdb.domain.*;
import com.gdb.exceptions.DataAccessException;
import com.gdb.repository.*;

import java.io.File;
import java.time.LocalDateTime;

public class TestJdbcTransactionRepository {

    public static void main(String[] args) {

        File file = new File("test_txn_gdb.db");

        if (file.exists()) {
            file.delete();
        }

        ConnectionProvider provider =
            new JdbcConnectionProvider(
                "jdbc:sqlite:test_txn_gdb.db"
            );

        SchemaInitializer.initialize(provider);

        JdbcTransactionRepository transactions =
            new JdbcTransactionRepository(provider);

        JdbcAccountRepository accounts =
            new JdbcAccountRepository(provider);

        try {

            // TEST 1: Save and query transactions

            System.out.println(
                "\n[TEST 1] Transaction Save & Query"
            );

            Transaction t1 = new Transaction(
                Transaction.generateId(),
                LocalDateTime.now(),
                1001,
                TransactionType.DEPOSIT,
                5000,
                5000,
                "SUCCESS",
                "Initial deposit",
                0,
                1001
            );

            Transaction t2 = new Transaction(
                Transaction.generateId(),
                LocalDateTime.now(),
                1001,
                TransactionType.WITHDRAW,
                1000,
                4000,
                "SUCCESS",
                "Cash withdrawal",
                1001,
                0
            );

            transactions.save(t1);
            transactions.save(t2);

            if (transactions.findAll().size() != 2 ||
                transactions.findByAccount(1001).size() != 2) {

                throw new AssertionError(
                    "Transaction save/query failed"
                );
            }

            System.out.println(
                "Saved 2 transaction records."
            );

            System.out.println(
                "Found transactions for Account #1001: " +
                transactions.findByAccount(1001).size()
            );

            System.out.println("-> PASSED");


            // TEST 2: Atomic transfer

            System.out.println(
                "\n[TEST 2] Atomic Transfer Execution"
            );

            accounts.save(
                new SavingsAccount(
                    1001, "Sender", 25, 50000, 1
                )
            );

            accounts.save(
                new SavingsAccount(
                    1002, "Receiver", 25, 30000, 1
                )
            );

            System.out.println(
                "Initial Sender Balance: Rs. " +
                accounts.findById(1001).get_balance()
            );

            System.out.println(
                "Initial Receiver Balance: Rs. " +
                accounts.findById(1002).get_balance()
            );

            System.out.println(
                "Executing transfer of Rs. 10000..."
            );

            transactions.transfer(1001, 1002, 10000);

            double senderBalance =
                accounts.findById(1001).get_balance();

            double receiverBalance =
                accounts.findById(1002).get_balance();

            if (senderBalance != 40000 ||
                receiverBalance != 40000) {

                throw new AssertionError(
                    "Transfer balances incorrect"
                );
            }

            System.out.println(
                "Updated Sender Balance: Rs. " +
                senderBalance
            );

            System.out.println(
                "Updated Receiver Balance: Rs. " +
                receiverBalance
            );

            System.out.println(
                "Transfer audit record saved."
            );

            System.out.println("-> PASSED");


            // TEST 3: Rollback on insufficient balance

            System.out.println(
                "\n[TEST 3] Atomic Rollback"
            );

            try {

                transactions.transfer(
                    1001, 1002, 999999
                );

                throw new AssertionError(
                    "Expected insufficient balance exception"
                );

            } catch (DataAccessException expected) {

                System.out.println(
                    "Caught expected exception: " +
                    expected.getMessage()
                );
            }

            senderBalance =
                accounts.findById(1001).get_balance();

            receiverBalance =
                accounts.findById(1002).get_balance();

            if (senderBalance != 40000 ||
                receiverBalance != 40000) {

                throw new AssertionError(
                    "Rollback failed"
                );
            }

            System.out.println(
                "Sender Balance unchanged: Rs. " +
                senderBalance
            );

            System.out.println(
                "Receiver Balance unchanged: Rs. " +
                receiverBalance
            );

            System.out.println("-> PASSED");


            // TEST 4: Data migration

            System.out.println(
                "\n[TEST 4] Data Migration from Memory to JDBC"
            );

            AccountRepository memoryAccounts =
                new InMemoryAccountRepository();

            TransactionRepository memoryTransactions =
                new InMemoryTransactionRepository();

            // Add 3 accounts
            memoryAccounts.save(
                new SavingsAccount(
                    2001, "Customer One", 25, 10000, 1
                )
            );

            memoryAccounts.save(
                new SavingsAccount(
                    2002, "Customer Two", 30, 20000, 1
                )
            );

            memoryAccounts.save(
                new SavingsAccount(
                    2003, "Customer Three", 28, 30000, 1
                )
            );

            // Add 5 transactions
            for (int i = 0; i < 5; i++) {

                Transaction t = new Transaction(
                    Transaction.generateId(),
                    LocalDateTime.now().plusSeconds(i),
                    2001,
                    TransactionType.DEPOSIT,
                    1000,
                    10000 + (i * 1000),
                    "SUCCESS",
                    "Migration transaction " + (i + 1),
                    0,
                    2001
                );

                memoryTransactions.save(t);
            }

            DataMigrator.MigrationResult result =
                DataMigrator.migrate(
                    memoryAccounts,
                    accounts,
                    memoryTransactions,
                    transactions
                );

            if (result.getAccountsMigrated() != 3 ||
                result.getTransactionsMigrated() != 5) {

                throw new AssertionError(
                    "Data migration failed"
                );
            }

            if (accounts.findById(2001) == null ||
                accounts.findById(2002) == null ||
                accounts.findById(2003) == null) {

                throw new AssertionError(
                    "Migrated accounts not found"
                );
            }

            System.out.println(
                "Migrated " +
                result.getAccountsMigrated() +
                " accounts and " +
                result.getTransactionsMigrated() +
                " transaction records."
            );

            System.out.println(
                "Verified migrated accounts in SQLite."
            );

            System.out.println("-> PASSED");


            System.out.println(
                "\n=============================================="
            );

            System.out.println(
                "ALL ACTIVITY 24 JDBC TRANSACTION TESTS PASSED!"
            );

            System.out.println(
                "=============================================="
            );

        } finally {

            provider.shutdown();
        }
    }
}
