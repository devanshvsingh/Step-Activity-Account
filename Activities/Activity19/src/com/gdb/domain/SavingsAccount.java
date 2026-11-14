package com.gdb.domain;

import com.gdb.exceptions.*;

public class SavingsAccount extends AbstractAccount
{
    private double interestRate;
    private double minBalance;
    private int tenureYears;

    public SavingsAccount(
        int account_number,
        String customer_name,
        int age,
        double balance,
        int tenureYears)
    {
        super(account_number, customer_name, age, balance);

        this.tenureYears = tenureYears;

        this.minBalance =
            AccountRulesEngine.getSavingsMinBalance(tenureYears);

        this.interestRate =
            AccountRulesEngine.getSavingsInterestRate(tenureYears);
    }

    // Kept for compatibility with previous activities
    public SavingsAccount(
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
            0
        );
    }

    @Override
    protected void processDebit(double amount)
        throws MinimumBalanceViolationException
    {
        if(balance - amount < minBalance)
        {
            throw new MinimumBalanceViolationException(
                "Cannot withdraw. Minimum balance of Rs "
                + minBalance
                + " must be maintained"
            );
        }

        balance -= amount;
    }

    public void applyInterest()
        throws InvalidAmountException, InactiveAccountException
    {
        if(!"ACTIVE".equalsIgnoreCase(status))
        {
            throw new InactiveAccountException(
                "Account is inactive"
            );
        }

        double interestAmount =
            (interestRate * balance) / 100.0;

        deposit(interestAmount);
    }

    public double getMinBalance()
    {
        return minBalance;
    }

    public double getInterestRate()
    {
        return interestRate;
    }
    @Override
    public int getTenureYears()
    {
        return tenureYears;
    }
}