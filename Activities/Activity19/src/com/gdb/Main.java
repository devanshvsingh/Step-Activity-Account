
package com.gdb;

import com.gdb.domain.*;
import com.gdb.service.AccountService;
import com.gdb.logging.*;
import com.gdb.domain.Transaction;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("============================================================");
        System.out.println(" BANKING APPLICATION");
        System.out.println("============================================================");

        try
        {
            // STEP 20: Set up file logging
            FileLogDestination fileDestination =
                new FileLogDestination();

            TransactionLogger logger =
                new TransactionLogger(fileDestination);

            AccountService service =
                new AccountService(logger);

            // Create accounts
            IAccount acc1 = service.openAccount(
                "SAVINGS", "Navdeep", 20, 50000.0
            );

            IAccount acc2 = service.openAccount(
                "SAVINGS", "Rahul", 22, 30000.0
            );

            ((AbstractAccount) acc1).setPin(1234);

            System.out.println(
                "\nAccount created: "
                + acc1.get_account_number()
            );

            System.out.println(
                "Account created: "
                + acc2.get_account_number()
            );

            // STEP 21: Execute banking operations
            Transaction deposit = service.deposit(
                acc1.get_account_number(), 10000.0
            );

            System.out.println("\nDeposit: " + deposit);

            Transaction withdrawal = service.withdraw(
                acc1.get_account_number(), 5000.0, 1234
            );

            System.out.println("Withdrawal: " + withdrawal);

            Transaction transfer = service.transfer(
                acc1.get_account_number(),
                acc2.get_account_number(),
                2000.0,
                1234
            );

            System.out.println("Transfer: " + transfer);

            // Display final balances
            System.out.println("\nFinal Balances:");

            System.out.println(
                "Account #" + acc1.get_account_number()
                + ": Rs. " + acc1.get_balance()
            );

            System.out.println(
                "Account #" + acc2.get_account_number()
                + ": Rs. " + acc2.get_balance()
            );

            // Display saved transaction history
            System.out.println(
                "\nSaved Transaction History: "
                + service.getTransactionHistory().size()
                + " records"
            );
        }
        catch(Exception e)
        {
            System.out.println(
                "Application error: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}
