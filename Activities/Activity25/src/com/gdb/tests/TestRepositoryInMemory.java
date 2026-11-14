
package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.repository.*;
import com.gdb.service.AccountService;
import com.gdb.logging.*;

import java.time.LocalDateTime;
import java.util.List;

public class TestRepositoryInMemory {

    public static void main(String[] args) throws Exception {

        System.out.println("=".repeat(60));
        System.out.println("  ACTIVITY 21 — REPOSITORY PATTERN (IN-MEMORY)");
        System.out.println("=".repeat(60));

        AccountRepository accountRepo =
                new InMemoryAccountRepository();

        TransactionRepository txnRepo =
                new InMemoryTransactionRepository();

        TransactionLogger logger =
                new TransactionLogger(new MemoryLogDestination());

        AccountService service =
                new AccountService(accountRepo, txnRepo, logger);

        // TEST 1: Account Repository CRUD

        System.out.println("\n[TEST 1] Account Repository CRUD:");

        IAccount account1 = AccountFactory.createAccount(
                "SAVINGS", 1001, "Rajesh Sharma", 30, 50000, 0
        );

        accountRepo.save(account1);

        assert accountRepo.exists(1001);
        assert accountRepo.findById(1001) != null;

        System.out.println("  Saved: " + account1.getAccountInfo());
        System.out.println("  Found: " +
                accountRepo.findById(1001).getAccountInfo());

        account1.deposit(15000);
        accountRepo.update(account1);

        assert accountRepo.findById(1001).get_balance() == 65000;

        System.out.println("  Updated balance: Rs. " +
                accountRepo.findById(1001).get_balance());

        IAccount account2 = AccountFactory.createAccount(
                "CURRENT", 1002, "Rahul Sharma", 35, 30000, 0
        );

        accountRepo.save(account2);

        List<IAccount> allAccounts = accountRepo.findAll();

        assert allAccounts.size() == 2;

        System.out.println("  Total accounts in repository: " +
                allAccounts.size());

        allAccounts.clear();
        assert accountRepo.findAll().size() == 2;

        accountRepo.delete(1002);
        assert !accountRepo.exists(1002);

        assert accountRepo.nextAccountNumber() == 1001;
        assert accountRepo.nextAccountNumber() == 1002;

        System.out.println("  -> PASSED");

        // TEST 2: Transaction Repository Operations

        System.out.println("\n[TEST 2] Transaction Repository Operations:");

        Transaction txn1 = new Transaction(
                Transaction.generateId(),
                LocalDateTime.now(),
                1001,
                TransactionType.DEPOSIT,
                5000,
                70000,
                "SUCCESS",
                "Cash deposit",
                0,
                0
        );

        Transaction txn2 = new Transaction(
                Transaction.generateId(),
                LocalDateTime.now(),
                1001,
                TransactionType.TRANSFER,
                2000,
                68000,
                "SUCCESS",
                "Transfer to account 1002",
                1001,
                1002
        );

        txnRepo.save(txn1);
        txnRepo.save(txn2);

        assert txnRepo.findAll().size() == 2;
        assert txnRepo.findByAccount(1001).size() == 2;
        assert txnRepo.findByAccount(1002).size() == 1;

        System.out.println("  Saved 2 transaction records.");
        System.out.println("  Found transactions for #1001: " +
                txnRepo.findByAccount(1001).size());

        txnRepo.clear();
        assert txnRepo.findAll().isEmpty();

        System.out.println("  -> PASSED");

        // TEST 3: Service Integration

        System.out.println("\n[TEST 3] Service Integration with Repositories:");

        IAccount john = service.openAccount(
                "SAVINGS", "John Doe", 25, 20000
        );

        john.setPin(1234);

        IAccount jane = service.openAccount(
                "SAVINGS", "Jane Smith", 28, 10000
        );

        jane.setPin(5678);

        System.out.println("  Opened account #" +
                john.get_account_number() + " via AccountService.");

        Transaction deposit = service.deposit(
                john.get_account_number(), 5000
        );

        assert deposit != null;
        assert john.get_balance() == 25000;

        System.out.println("  Deposit executed. New Balance: Rs. " +
                john.get_balance());

        Transaction withdrawal = service.withdraw(
                john.get_account_number(), 2000, 1234
        );

        assert withdrawal != null;
        assert john.get_balance() == 23000;

        Transaction transfer = service.transfer(
                john.get_account_number(),
                jane.get_account_number(),
                1000,
                1234
        );

        assert transfer != null;
        assert john.get_balance() == 22000;
        assert jane.get_balance() == 11000;

        assert accountRepo.findAll().size() == 3;
        assert txnRepo.findAll().size() == 3;
        assert txnRepo.findByAccount(john.get_account_number()).size() == 3;

        System.out.println("  Withdrawal executed.");
        System.out.println("  Transfer executed.");
        System.out.println("  Repository transaction records: " +
                txnRepo.findAll().size());

        System.out.println("  -> PASSED");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  ALL ACTIVITY 21 IN-MEMORY REPOSITORY TESTS PASSED!");
        System.out.println("=".repeat(60));
    }
}
