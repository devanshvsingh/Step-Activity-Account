package com.gdb.domain;

import com.gdb.exceptions.*;

public class SalaryAccount extends AbstractAccount
{
    private String employer;
    private int inactiveMonths;

    public SalaryAccount(
        int account_number,
        String customer_name,
        int age,
        double balance,
        String employer,
        int inactiveMonths)
    {
        super(account_number, customer_name, age, balance);

        this.employer = employer;
        this.inactiveMonths = inactiveMonths;
    }

    public SalaryAccount(
        int account_number,
        String customer_name,
        int age,
        double balance)
    {
        this(
            account_number,
            customer_name,
            age,
            balance,
            "Unknown",
            0
        );
    }

    public String getEmployer()
    {
        return employer;
    }

    public int getInactiveMonths()
    {
        return inactiveMonths;
    }

    public void setInactiveMonths(int inactiveMonths)
    {
        this.inactiveMonths = inactiveMonths;
    }

    public void creditSalary(double amount)
        throws InvalidAmountException
    {
        deposit(amount);
        inactiveMonths = 0;
    }
}