package com.gdb.domain;

import com.gdb.exceptions.*;

public class CurrentAccount extends AbstractAccount
{
    private double minimumBalance;

    public CurrentAccount(
        int account_number,
        String customer_name,
        int age,
        double balance)
    {
        super(account_number, customer_name, age, balance);

        this.minimumBalance = 0.0;
    }

    public CurrentAccount(
        int account_number,
        String customer_name,
        int age,
        double balance,
        double minimumBalance)
    {
        super(account_number, customer_name, age, balance);

        this.minimumBalance = minimumBalance;
    }

    @Override
    protected void processDebit(double amount)
        throws MinimumBalanceViolationException
    {
        if(balance - amount < minimumBalance)
        {
            throw new MinimumBalanceViolationException(
                "Cannot withdraw. Minimum balance of Rs "
                + minimumBalance + " must be maintained"
            );
        }

        balance -= amount;
    }

    public double getMinimumBalance()
    {
        return minimumBalance;
    }
}