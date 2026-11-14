
package com.gdb.domain;

import com.gdb.exceptions.*;
import java.time.LocalDateTime;
import java.io.Serializable;

public abstract class AbstractAccount implements IAccount,Serializable
{
    protected int account_number;
    protected String customer_name;
    protected int age;
    protected double balance;
    protected String status;
    protected String pin;

    // Activity 15: Daily transfer tracking
    protected double dailyTransferTotal = 0.0;
    protected LocalDateTime lastTransferDate = LocalDateTime.now();

    public AbstractAccount(
        int account_number,
        String customer_name,
        int age,
        double balance)
    {
        this.account_number = account_number;
        this.customer_name = customer_name;
        this.age = age;
        this.balance = balance;
        this.status = "ACTIVE";
        this.pin = null;
    }

    // Activity 15: Account status and PIN checks
    public boolean isActive()
    {
        return "ACTIVE".equalsIgnoreCase(status);
    }

    public boolean verifyPin(String suppliedPin)
    {
        return isValidPin(suppliedPin);
    }

    public boolean canWithdraw(double amount)
    {
        return amount > 0 && amount <= balance;
    }

    // Default tenure; SavingsAccount overrides this.
    public int getTenureYears()
    {
        return 0;
    }

    public double getDailyTransferTotal()
    {
        resetDailyTransferIfNeeded();
        return dailyTransferTotal;
    }

    public LocalDateTime getLastTransferDate()
    {
        return lastTransferDate;
    }

    public double getDailyTransferLimit()
    {
        return AccountRulesEngine.getDailyTransferLimit(
            get_account_type(), getTenureYears()
        );
    }

    public double getRemainingDailyTransferLimit()
    {
        resetDailyTransferIfNeeded();

        return Math.max(
            0.0,
            getDailyTransferLimit() - dailyTransferTotal
        );
    }

    public boolean canTransfer(double amount)
    {
        resetDailyTransferIfNeeded();

        return amount > 0 &&
            dailyTransferTotal + amount <= getDailyTransferLimit();
    }

    public void updateDailyTransferTotal(double amount)
    {
        resetDailyTransferIfNeeded();

        dailyTransferTotal += amount;
        lastTransferDate = LocalDateTime.now();
    }

    public void resetDailyTransferIfNeeded()
    {
        if(lastTransferDate == null ||
           !lastTransferDate.toLocalDate().equals(
               LocalDateTime.now().toLocalDate()))
        {
            dailyTransferTotal = 0.0;
            lastTransferDate = LocalDateTime.now();
        }
    }

    @Override
    public int get_account_number()
    {
        return account_number;
    }

    @Override
    public String get_customer_name()
    {
        return customer_name;
    }

    @Override
    public double get_balance()
    {
        return balance;
    }

    @Override
    public String get_status()
    {
        return status;
    }

    @Override
    public void deposit(double amount) throws InvalidAmountException
    {
        if(amount <= 0)
        {
            throw new InvalidAmountException(
                "Deposit amount must be greater than zero"
            );
        }

        balance += amount;
    }

    public void setPin(int pin)
    {
        this.pin = String.valueOf(pin);
    }

    public void setPin(String pin)
    {
        this.pin = pin;
    }

    @Override
    public void withdraw(double amount, String pin)
        throws AccountException
    {
        if(amount <= 0)
        {
            throw new InvalidAmountException(
                "Withdrawal amount must be greater than zero"
            );
        }

        if(!isValidPin(pin))
        {
            throw new InvalidPinException("Invalid PIN");
        }

        if(amount > balance)
        {
            throw new InsufficientBalanceException(
                "Insufficient balance"
            );
        }

        processDebit(amount);
    }

    public void withdraw(double amount, int pin)
        throws AccountException
    {
        withdraw(amount, String.valueOf(pin));
    }

    protected void processDebit(double amount)
        throws AccountException
    {
        balance -= amount;
    }

    protected boolean isValidPin(String pin)
    {
        return this.pin != null && this.pin.equals(pin);
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String get_account_type()
    {
        return getClass().getSimpleName();
    }

    @Override
    public void displayAccountInfo()
    {
        System.out.println("Account Number: " + account_number);
        System.out.println("Customer Name: " + customer_name);
        System.out.println("Age: " + age);
        System.out.println("Balance: Rs " + balance);
        System.out.println("Account Type: " + get_account_type());
        System.out.println("Status: " + status);
    }

    // Activity 16 - STEP 7: Deposit and return a transaction record
    public Transaction depositWithTransaction(double amount)
        throws InvalidAmountException
    {
        deposit(amount);

        return new Transaction(
            Transaction.generateId(),
            java.time.LocalDateTime.now(),
            get_account_number(),
            TransactionType.DEPOSIT,
            amount,
            get_balance(),
            "SUCCESS",
            "Deposit of Rs. " + amount,
            0,
            get_account_number()
        );
    }

// Activity 16 - STEP 8: Withdraw and return a transaction record
public Transaction withdrawWithTransaction(double amount, int pin)
    throws AccountException
{
    withdraw(amount, pin);

    return new Transaction(
        Transaction.generateId(),
        java.time.LocalDateTime.now(),
        get_account_number(),
        TransactionType.WITHDRAW,
        amount,
        get_balance(),
        "SUCCESS",
        "Withdrawal of Rs. " + amount,
        get_account_number(),
        0
    );
}
}