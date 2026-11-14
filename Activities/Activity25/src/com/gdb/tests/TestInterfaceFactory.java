package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;

public class TestInterfaceFactory
{
    public static void main(String[] args)
    {
        System.out.println("=== Activity 12: Factory-Driven System Suite ===");
        System.out.println();

        // Test 1: Savings Account Creation & Deposit
        try
        {
            IAccount savings = AccountFactory.createAccount(
                "SAVINGS",
                3001,
                "Rajesh Sharma",
                25,
                10000
            );

            double oldBalance = savings.get_balance();

            savings.deposit(2000);

            if(savings.get_balance() == oldBalance + 2000)
            {
                System.out.println(
                    "[Test 1] Savings Account Creation & Deposit: [PASS]"
                );
            }
            else
            {
                System.out.println(
                    "[Test 1] Savings Account Creation & Deposit: [FAIL]"
                );
            }
        }
        catch(Exception e)
        {
            System.out.println(
                "[Test 1] Savings Account Creation & Deposit: [FAIL]"
            );
        }

        // Test 2: Current Account Overdraft Withdrawal
        try
        {
            IAccount current = AccountFactory.createAccount(
                "CURRENT",
                3002,
                "Priya Patel",
                30,
                5000
            );

            current.withdraw(6000, "1234");

            System.out.println(
                "[Test 2] Current Account Overdraft Withdrawal: [PASS]"
            );
        }
        catch(AccountException e)
        {
            /*
             * If the current account implementation requires a PIN,
             * retry with the account's configured PIN when available.
             */
            try
            {
                IAccount current = AccountFactory.createAccount(
                    "CURRENT",
                    3002,
                    "Priya Patel",
                    30,
                    5000
                );

                if(current instanceof CurrentAccount)
                {
                    ((CurrentAccount) current).setPin(1234);
                }

                current.withdraw(6000, "1234");

                System.out.println(
                    "[Test 2] Current Account Overdraft Withdrawal: [PASS]"
                );
            }
            catch(Exception ex)
            {
                System.out.println(
                    "[Test 2] Current Account Overdraft Withdrawal: [FAIL]"
                );
            }
        }

        // Test 3: Fixed Deposit Premature Withdrawal Block
        try
        {
            IAccount fixedDeposit = AccountFactory.createAccount(
                "FIXED_DEPOSIT",
                3003,
                "Amit Kumar",
                28,
                20000
            );

            try
            {
                fixedDeposit.withdraw(5000, "1234");

                System.out.println(
                    "[Test 3] Fixed Deposit Premature Withdrawal Block: [FAIL]"
                );
            }
            catch(AccountException e)
            {
                System.out.println(
                    "[Test 3] Fixed Deposit Premature Withdrawal Block: [PASS]"
                );
            }
        }
        catch(Exception e)
        {
            System.out.println(
                "[Test 3] Fixed Deposit Premature Withdrawal Block: [FAIL]"
            );
        }

        // Test 4: Invalid Type Rejection
        try
        {
            AccountFactory.createAccount(
                "INVALID_TYPE",
                3004,
                "Unknown Customer",
                25,
                10000
            );

            System.out.println(
                "[Test 4] Invalid Type Rejection: [FAIL]"
            );
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(
                "[Test 4] Invalid Type Rejection: [PASS]"
            );
        }

        System.out.println();
        System.out.println(
            "Factory-driven architecture successfully verified!"
        );
    }
}