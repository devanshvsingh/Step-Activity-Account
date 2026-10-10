package com.gdb.tests;

import com.gdb.command.*;
import com.gdb.domain.*;
import com.gdb.logging.TransactionLog;

import com.gdb.command.DepositCommand;
import com.gdb.command.TransactionCommand;
import com.gdb.command.TransferCommand;
import com.gdb.command.WithdrawCommand;

import java.util.List;

public class TestCommandLogging {
    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("  ACTIVITY 17 — COMMAND PATTERN + FILE LOGGING");
        System.out.println("=".repeat(60));

        TransactionLog log = new TransactionLog();
        log.clear();

        // STEP 10: Create test accounts.
        
        IAccount acc1 = AccountFactory.createAccount(
            "SAVINGS", 1001, "John Doe", 25, 15000
        );

        acc1.setPin(1234);

        IAccount acc2 = AccountFactory.createAccount(
            "SAVINGS", 1002, "Jane Smith", 30, 10000
        );


        // STEP 11: Execute and log deposit.
        DepositCommand depCmd = new DepositCommand(acc1, 5000);
        depCmd.execute();
        log.log(depCmd);
        System.out.println("[STEP 11] Logged: " + depCmd.getTransaction());

        // STEP 12: Execute and log withdrawal.
        WithdrawCommand wthCmd = new WithdrawCommand(acc1, 2000, 1234);
        wthCmd.execute();
        log.log(wthCmd);
        System.out.println("[STEP 12] Logged: " + wthCmd.getTransaction());

        // STEP 13: Execute and log transfer.
        TransferCommand trfCmd = new TransferCommand(acc1, acc2, 3000, 1234);
        trfCmd.execute();
        log.log(trfCmd);
        System.out.println("[STEP 13] Logged: " + trfCmd.getTransaction());

        // STEP 14: Read all commands back.
        List<TransactionCommand> history = log.readAll();
        System.out.println("\n[STEP 14] Read " + history.size()
            + " commands from transaction log:");
        for (int i = 0; i < history.size(); i++) {
            System.out.println("  [" + (i + 1) + "] "
                + history.get(i).getTransaction());
        }

        // STEP 15: Verify persistence using a fresh log instance.
        TransactionLog freshLog = new TransactionLog();
        List<TransactionCommand> persisted = freshLog.readAll();
        if (persisted.size() == history.size() && persisted.size() == 3) {
            System.out.println("\n[STEP 15] Fresh reader verified "
                + persisted.size() + " persisted commands [PASS]");
        } else {
            System.out.println("\n[STEP 15] Persistence verification [FAIL]"
                + " (expected 3, found " + persisted.size() + ")");
            throw new IllegalStateException("Transaction log persistence check failed");
        }
    }
}
