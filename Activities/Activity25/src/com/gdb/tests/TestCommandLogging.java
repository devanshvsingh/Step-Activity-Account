
package com.gdb.tests;

import com.gdb.command.*;
import com.gdb.domain.*;
import com.gdb.logging.TransactionLog;

import java.util.List;

public class TestCommandLogging
{
    public static void main(String[] args)
    {
        System.out.println("============================================================");
        System.out.println(" ACTIVITY 17 — COMMAND PATTERN + FILE LOGGING");
        System.out.println("============================================================");

        try
        {
            TransactionLog log = new TransactionLog();

            // Start each test with a clean log.
            log.clear();

            // STEP 10: Create accounts.
            AbstractAccount acc1 = AccountFactory.createAccount(
                "SAVINGS", 1001, "Rajesh Sharma", 30, 15000.0, 0
            );

            AbstractAccount acc2 = AccountFactory.createAccount(
                "SAVINGS", 1002, "Priya Patel", 28, 10000.0, 0
            );

            acc1.setPin(1234);

            // STEP 11: Execute and log deposit.
            DepositCommand deposit =
                new DepositCommand(acc1, 5000.0);

            deposit.execute();
            log.log(deposit);

            System.out.println(
                "[STEP 11] Logged: " + deposit.getTransaction()
            );

            // STEP 12: Execute and log withdrawal.
            WithdrawCommand withdraw =
                new WithdrawCommand(acc1, 2000.0, 1234);

            withdraw.execute();
            log.log(withdraw);

            System.out.println(
                "[STEP 12] Logged: " + withdraw.getTransaction()
            );

            // STEP 13: Execute and log transfer.
            TransferCommand transfer =
                new TransferCommand(acc1, acc2, 3000.0, 1234);

            transfer.execute();
            log.log(transfer);

            System.out.println(
                "[STEP 13] Logged: " + transfer.getTransaction()
            );

            // STEP 14: Read all commands from the file.
            List<TransactionCommand> history = log.readAll();

            System.out.println(
                "\n[STEP 14] Read " + history.size()
                + " commands from transaction log:"
            );

            for(int i = 0; i < history.size(); i++)
            {
                System.out.println(
                    "  [" + (i + 1) + "] "
                    + history.get(i).getTransaction()
                );
            }

            // STEP 15: Verify persistence with a fresh logger.
            TransactionLog freshLog = new TransactionLog();

            List<TransactionCommand> persisted =
                freshLog.readAll();

            if(persisted.size() == 3)
            {
                System.out.println(
                    "\n[STEP 15] Fresh reader verified "
                    + persisted.size()
                    + " persisted commands [PASS]"
                );
            }
            else
            {
                System.out.println(
                    "\n[STEP 15] [FAIL] Expected 3 commands, found "
                    + persisted.size()
                );
            }
        }
        catch(Exception e)
        {
            System.out.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}