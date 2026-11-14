package com.gdb.tests;

import com.gdb.db.ConnectionProvider;
import com.gdb.db.JdbcConnectionProvider;
import com.gdb.db.SchemaInitializer;

import com.gdb.domain.AccountFactory;
import com.gdb.domain.IAccount;

import com.gdb.repository.JdbcAccountRepository;

import java.io.File;
import java.util.List;

public class TestJdbcAccountRepository {

    public static void main(String[] args) throws Exception {

        System.out.println("============================================================");

        System.out.println(
            "  ACTIVITY 23 - JDBC ACCOUNT REPOSITORY CRUD TESTS"
        );

        System.out.println("============================================================");


        String testDbUrl = "jdbc:sqlite:test_acc_gdb.db";

        File dbFile = new File("test_acc_gdb.db");

        if (dbFile.exists()) {
            dbFile.delete();
        }


        ConnectionProvider provider =
            new JdbcConnectionProvider(testDbUrl);

        try {

            SchemaInitializer.initialize(provider);

            JdbcAccountRepository repo =
                new JdbcAccountRepository(provider);


            // TEST 1: Save and FindById

            System.out.println("\n[TEST 1] Save & FindById:");

            IAccount acc1 = AccountFactory.createAccount(
                "SAVINGS",
                1001,
                "Rajesh Sharma",
                30,
                50000.0
            );

            acc1.setPin(1234);

            repo.save(acc1);

            IAccount found = repo.findById(1001);

            assert found != null;

            assert found.get_account_number() == 1001;

            assert "Rajesh Sharma".equals(
                found.get_customer_name()
            );

            assert found.get_balance() == 50000.0;

            System.out.println(
                "  Found: " + found.getAccountInfo()
            );

            System.out.println("  -> PASSED");


            // TEST 2: Update and FindAll

            System.out.println("\n[TEST 2] Update & FindAll:");

            IAccount acc2 = AccountFactory.createAccount(
                "CURRENT",
                1002,
                "Priya Patel",
                28,
                40000.0
            );

            repo.save(acc2);

            found.deposit(15000.0);

            repo.update(found);

            IAccount updated = repo.findById(1001);

            assert updated != null;

            assert updated.get_balance() == 65000.0;

            System.out.println(
                "  Updated balance: Rs. "
                + updated.get_balance()
                + " (Expected: 65000.0)"
            );


            List<IAccount> all = repo.findAll();

            assert all.size() == 2;

            System.out.println(
                "  Total accounts in DB: "
                + all.size()
                + " (Expected: 2)"
            );

            System.out.println("  -> PASSED");


            // TEST 3: Delete and Exists

            System.out.println("\n[TEST 3] Delete & Exists:");

            assert repo.exists(1002);

            repo.delete(1002);

            assert !repo.exists(1002);

            assert repo.findById(1002) == null;

            System.out.println(
                "  Account 1002 successfully deleted."
            );

            System.out.println("  -> PASSED");


            // TEST 4: Next Account Number

            System.out.println("\n[TEST 4] Next Account Number:");

            int nextNumber = repo.nextAccountNumber();

            assert nextNumber == 1002;

            System.out.println(
                "  Next account number: " + nextNumber
            );

            System.out.println("  -> PASSED");


            System.out.println("\n============================================================");

            System.out.println(
                "  ALL ACTIVITY 23 JDBC ACCOUNT REPOSITORY TESTS PASSED!"
            );

            System.out.println("============================================================");

        } finally {

            provider.shutdown();

            if (dbFile.exists()) {
                dbFile.delete();
            }
        }
    }
}