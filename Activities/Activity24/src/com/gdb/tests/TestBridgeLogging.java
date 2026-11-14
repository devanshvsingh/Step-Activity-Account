
package com.gdb.tests;
import com.gdb.logging.SimulatedDatabase;
import com.gdb.logging.MemoryLogDestination;
import com.gdb.command.*;
import com.gdb.domain.*;
import com.gdb.logging.*;

import java.util.ArrayList;
import java.util.List;

public class TestBridgeLogging
{
    public static void main(String[] args)
    {
        System.out.println("============================================================");
        System.out.println(" ACTIVITY 18 — BRIDGE PATTERN (FILE + DB)");
        System.out.println("============================================================");

        try
        {
            // STEP 24: Instantiate all destinations
            FileLogDestination fileDestination =
                new FileLogDestination();

            SimulatedDatabase database =
                new SimulatedDatabase();

            DatabaseLogDestination databaseDestination =
                new DatabaseLogDestination(database);

            MemoryLogDestination memoryDestination =
                new MemoryLogDestination();

            TransactionLogger logger =
                new TransactionLogger(fileDestination);

            // Start each backend with an empty history.
            fileDestination.clear();
            databaseDestination.clear();
            memoryDestination.clear();

            // Create accounts.
            AbstractAccount acc1 = AccountFactory.createAccount(
                "SAVINGS", 1001, "Rajesh Sharma", 30, 15000.0, 0
            );

            AbstractAccount acc2 = AccountFactory.createAccount(
                "SAVINGS", 1002, "Priya Patel", 28, 10000.0, 0
            );

            acc1.setPin(1234);

            // Create and execute the commands exactly once.
            List<TransactionCommand> commands = new ArrayList<>();

            DepositCommand deposit =
                new DepositCommand(acc1, 5000.0);
            deposit.execute();
            commands.add(deposit);

            WithdrawCommand withdraw =
                new WithdrawCommand(acc1, 2000.0, 1234);
            withdraw.execute();
            commands.add(withdraw);

            TransferCommand transfer =
                new TransferCommand(acc1, acc2, 3000.0, 1234);
            transfer.execute();
            commands.add(transfer);

            // STEP 25: Log to FILE.
            logger.setDestination(fileDestination);

            for(TransactionCommand cmd : commands)
            {
                logger.log(cmd);
            }

            System.out.println(
                "[STEP 25] Logging to FILE destination..."
            );
            System.out.println(
                "  FILE log count: " + logger.readAll().size()
            );

            // STEP 26: Switch to DATABASE.
            logger.setDestination(databaseDestination);

            for(TransactionCommand cmd : commands)
            {
                logger.log(cmd);
            }

            System.out.println(
                "\n[STEP 26] Switched to DATABASE destination..."
            );
            System.out.println(
                "  DATABASE log count: " + logger.readAll().size()
            );

            // STEP 27: Switch to MEMORY.
            logger.setDestination(memoryDestination);

            for(TransactionCommand cmd : commands)
            {
                logger.log(cmd);
            }

            System.out.println(
                "\n[STEP 27] Switched to MEMORY destination..."
            );
            System.out.println(
                "  MEMORY log count: " + logger.readAll().size()
            );

            // STEP 28: Verify isolation.
            int fileCount = fileDestination.readAll().size();
            int databaseCount = databaseDestination.readAll().size();
            int memoryCount = memoryDestination.readAll().size();

            System.out.println(
                "\n[STEP 28] Verifying Data Isolation:"
            );

            System.out.println(
                "  FILE count: " + fileCount + " [EXPECTED: 3]"
            );

            System.out.println(
                "  DATABASE count: " + databaseCount
                    + " [EXPECTED: 3]"
            );

            System.out.println(
                "  MEMORY count: " + memoryCount
                    + " [EXPECTED: 3]"
            );

            // STEP 29: Final verification.
            if(fileCount == 3 &&
               databaseCount == 3 &&
               memoryCount == 3)
            {
                System.out.println(
                    "\n[STEP 29] All Bridge Pattern log backends "
                    + "verified successfully!"
                );
            }
            else
            {
                System.out.println(
                    "\n[STEP 29] Verification FAILED."
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
