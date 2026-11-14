
package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.service.TransferService;
import com.gdb.exceptions.*;

public class TestTransfer
{
    public static void main(String[] args)
    {
        System.out.println("============================================================");
        System.out.println(" ACTIVITY 15 — TRANSFER WITH DAILY LIMITS");
        System.out.println("============================================================");

        try
        {
            // STEP 9: Create two savings accounts
            AbstractAccount acc1 = AccountFactory.createAccount(
                "SAVINGS",
                1001,
                "Rajesh Sharma",
                30,
                100000.0,
                0
            );

            AbstractAccount acc2 = AccountFactory.createAccount(
                "SAVINGS",
                1002,
                "Priya Patel",
                28,
                20000.0,
                0
            );

            acc1.setPin("1234");

            System.out.println(
                "[STEP 9] Account #1001 | Rajesh Sharma | "
                + "Savings | Rs. " + acc1.get_balance()
                + " | " + acc1.get_status()
            );

            System.out.println(
                "[STEP 9] Account #1002 | Priya Patel | "
                + "Savings | Rs. " + acc2.get_balance()
                + " | " + acc2.get_status()
            );

            TransferService service = new TransferService();

            // STEP 10: Transfer Rs. 5,000
            service.transfer(acc1, acc2, 5000.0, "1234");

            System.out.println(
                "[STEP 10] Transfer Rs. 5,000: SUCCESS"
                + " | acc1 = Rs. " + acc1.get_balance()
                + " | acc2 = Rs. " + acc2.get_balance()
            );

            // STEP 11: Test insufficient balance
            try
            {
                service.transfer(acc1, acc2, 100000.0, "1234");
            }
            catch(InsufficientBalanceException e)
            {
                System.out.println(
                    "[STEP 11] Caught InsufficientBalanceException: "
                    + e.getMessage()
                );
            }

            // STEP 12: Test daily transfer limit
            System.out.println(
                "[STEP 12] Daily limit for acc1: Rs. "
                + acc1.getDailyTransferLimit()
            );

            int count = 0;

            while(true)
            {
                try
                {
                    service.transfer(
                        acc1, acc2, 20000.0, "1234"
                    );

                    count++;

                    System.out.println(
                        "  Transfer #" + count
                        + " of Rs. 20,000: SUCCESS"
                        + " | used today = Rs. "
                        + acc1.getDailyTransferTotal()
                    );
                }
                catch(AccountException e)
                {
                    System.out.println(
                        "[STEP 12] Caught AccountException: "
                        + e.getMessage()
                    );

                    break;
                }
            }

            // STEP 13: Display daily usage and remaining limit
            System.out.println(
                "[STEP 13] Used today: Rs. "
                + acc1.getDailyTransferTotal()
                + " | Remaining: Rs. "
                + acc1.getRemainingDailyTransferLimit()
            );
        }
        catch(Exception e)
        {
            System.out.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
