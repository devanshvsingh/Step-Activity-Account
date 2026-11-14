package com.gdb.tests;

import com.gdb.domain.*;

public class TestAccountRulesEngine
{
    public static void main(String[] args)
    {
        System.out.println(
            "=== Activity 13.2: Dynamic Account Rules Test ==="
        );

        IAccount account = AccountFactory.createAccount(
            "SAVINGS",
            4001,
            "Amit Kumar",
            28,
            10000,
            4
        );

        SavingsAccount savings =
            (SavingsAccount) account;

        System.out.println(
            "Created Savings Account (Tenure: "
            + savings.getTenureYears()
            + " yrs):"
        );

        System.out.println(
            " -> Min Balance: Rs "
            + savings.getMinBalance()
            + " (Dynamically fetched)"
        );

        System.out.println(
            " -> Interest Rate: "
            + savings.getInterestRate()
            + "% (Dynamically fetched)"
        );

        if(savings.getMinBalance() == 5000.0 &&
           savings.getInterestRate() == 3.5)
        {
            System.out.println(
                "Dynamic rule integration verified!"
            );
        }
        else
        {
            System.out.println(
                "Dynamic rule integration failed!"
            );
        }
    }
}