package com.gdb.domain;

import com.gdb.exceptions.*;

public class FixedDepositAccount extends AbstractAccount
{
    private double interestRate;
    private int tenureMonths;

    public FixedDepositAccount(
        int account_number,
        String customer_name,
        int age,
        double balance)
    {
        super(account_number, customer_name, age, balance);

        this.interestRate = 6.5;
        this.tenureMonths = 12;
    }

    public FixedDepositAccount(
        int account_number,
        String customer_name,
        int age,
        double balance,
        double interestRate,
        int tenureMonths)
    {
        super(account_number, customer_name, age, balance);

        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
    }

    @Override
    public void withdraw(double amount, String pin)
        throws AccountException
    {
        throw new AccountException(
            "Withdrawal is not allowed from a Fixed Deposit account"
        );
    }

    public double getInterestRate()
    {
        return interestRate;
    }

    public int getTenureMonths()
    {
        return tenureMonths;
    }
}