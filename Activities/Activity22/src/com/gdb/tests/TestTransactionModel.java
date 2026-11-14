
package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.service.TransferService;

public class TestTransactionModel
{
    public static void main(String[] args)
    {
        System.out.println("============================================================");
        System.out.println(" ACTIVITY 16 — TRANSACTION MODEL TEST");
        System.out.println("============================================================");

        try
        {
            // STEP 10: Create accounts and test deposit
            AbstractAccount acc1 = AccountFactory.createAccount(
                "SAVINGS", 1001, "Rajesh Sharma", 30, 50000.0, 0
            );

            AbstractAccount acc2 = AccountFactory.createAccount(
                "SAVINGS", 1002, "Priya Patel", 28, 20000.0, 0
            );

            acc1.setPin(1234);

            Transaction depositTxn =
                acc1.depositWithTransaction(5000.0);

            System.out.println(
                "[STEP 10] Deposit Transaction: " + depositTxn
            );

            // STEP 11: Test withdrawal
            Transaction withdrawTxn =
                acc1.withdrawWithTransaction(2000.0, 1234);

            System.out.println(
                "[STEP 11] Withdrawal Transaction: " + withdrawTxn
            );

            // STEP 12: Test transfer
            TransferService service = new TransferService();

            Transaction transferTxn =
                service.transferWithTransaction(
                    acc1, acc2, 1000.0, 1234
                );

            System.out.println(
                "[STEP 12] Transfer Transaction: " + transferTxn
            );

            // STEP 13: Verify legacy deposit still works
            acc1.deposit(1000.0);

            System.out.println(
                "[STEP 13] Legacy Deposit +1000: "
                + "Account #" + acc1.get_account_number()
                + " | " + acc1.get_customer_name()
                + " | Savings | Rs. " + acc1.get_balance()
                + " | " + acc1.get_status()
            );
        }
        catch(Exception e)
        {
            System.out.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
