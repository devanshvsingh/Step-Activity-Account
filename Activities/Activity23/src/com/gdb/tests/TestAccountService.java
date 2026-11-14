
package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.service.AccountService;
import com.gdb.logging.*;
import com.gdb.exceptions.*;

import java.util.List;

public class TestAccountService
{
    public static void main(String[] args)
    {
        System.out.println("============================================================");
        System.out.println(" ACTIVITY 19 — ACCOUNT SERVICE DEMO");
        System.out.println("============================================================");

        try
        {
            // STEP 12: Wire the dependencies
            MemoryLogDestination memory =
                new MemoryLogDestination();

            TransactionLogger logger =
                new TransactionLogger(memory);

            AccountService service =
                new AccountService(logger);

            // STEP 13: Open two accounts
            IAccount acc1 = service.openAccount(
                "SAVINGS", "John Doe", 25, 15000.0
            );

            IAccount acc2 = service.openAccount(
                "SAVINGS", "Jane Smith", 30, 10000.0
            );

            ((AbstractAccount) acc1).setPin(1234);
            ((AbstractAccount) acc2).setPin(5678);

            System.out.println(
                "[STEP 13] Opened: Account #"
                + acc1.get_account_number()
                + " | John Doe (25 yrs, Tenure: 0 yrs)"
                + " | Savings | Rs. "
                + acc1.get_balance() + " | Active"
            );

            System.out.println(
                "[STEP 13] Opened: Account #"
                + acc2.get_account_number()
                + " | Jane Smith (30 yrs, Tenure: 0 yrs)"
                + " | Savings | Rs. "
                + acc2.get_balance() + " | Active"
            );

            // STEP 14: Deposit
            Transaction deposit = service.deposit(
                acc1.get_account_number(), 5000.0
            );

            System.out.println(
                "\n[STEP 14] Deposit: " + deposit
            );

            // STEP 15: Withdrawal
            Transaction withdrawal = service.withdraw(
                acc1.get_account_number(), 2000.0, 1234
            );

            System.out.println(
                "[STEP 15] Withdrawal: " + withdrawal
            );

            // STEP 16: Transfer
            Transaction transfer = service.transfer(
                acc1.get_account_number(),
                acc2.get_account_number(),
                1000.0,
                1234
            );

            System.out.println(
                "[STEP 16] Transfer: " + transfer
            );

            // STEP 17: Display final balances
            System.out.println("\n[STEP 17] Final Balances:");

            System.out.println(
                "  John (Account #" + acc1.get_account_number()
                + "): Rs. " + acc1.get_balance()
            );

            System.out.println(
                "  Jane (Account #" + acc2.get_account_number()
                + "): Rs. " + acc2.get_balance()
            );

            // STEP 18: Display transaction history
            List<Transaction> history =
                service.getTransactionHistory();

            System.out.println(
                "\n[STEP 18] Transaction History ("
                + history.size() + " records):"
            );

            for(int i = 0; i < history.size(); i++)
            {
                System.out.println(
                    "  [" + (i + 1) + "] " + history.get(i)
                );
            }

            // STEP 19: Exception handling
            System.out.println(
                "\n[STEP 19] Error Handling Checks:"
            );

            // Test missing account
            try
            {
                service.deposit(9999, 1000.0);
            }
            catch(AccountException e)
            {
                System.out.println(
                    "  Deposit to missing account caught: "
                    + e.getMessage() + " [PASS]"
                );
            }

            // Test incorrect PIN
            try
            {
                service.withdraw(
                    acc1.get_account_number(), 1000.0, 9999
                );
            }
            catch(InvalidPinException e)
            {
                System.out.println(
                    "  Withdrawal with wrong PIN caught: "
                    + e.getMessage() + " [PASS]"
                );
            }

            // Additional lifecycle check: close account
            service.closeAccount(
                acc2.get_account_number(), 5678
            );

            System.out.println(
                "\nAccount #" + acc2.get_account_number()
                + " closed successfully."
            );
        }
        catch(Exception e)
        {
            System.out.println(
                "Test failed: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}
