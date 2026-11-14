package com.gdb.domain;

public class AccountFactory
{
    public static AbstractAccount createAccount(
        String type,
        int accountNumber,
        String customerName,
        int age,
        double balance)
    {
        return createAccount(
            type,
            accountNumber,
            customerName,
            age,
            balance,
            0
        );
    }

    public static AbstractAccount createAccount(
        String type,
        int accountNumber,
        String customerName,
        int age,
        double balance,
        int tenureYears)
    {
        if(type == null)
        {
            throw new IllegalArgumentException(
                "Account type cannot be null"
            );
        }

        switch(type.toUpperCase())
        {
            case "SAVINGS":
                return new SavingsAccount(
                    accountNumber,
                    customerName,
                    age,
                    balance,
                    tenureYears
                );

            case "CURRENT":
                return new CurrentAccount(
                    accountNumber,
                    customerName,
                    age,
                    balance
                );

            case "FIXED_DEPOSIT":
            case "FD":
                return new FixedDepositAccount(
                    accountNumber,
                    customerName,
                    age,
                    balance
                );

            case "SALARY":
                return new SalaryAccount(
                    accountNumber,
                    customerName,
                    age,
                    balance,
                    "Company",
                    0
                );

            default:
                throw new IllegalArgumentException(
                    "Unknown account type: " + type
                );
        }
    }

    public static AbstractAccount createAccount(
        String type,
        int accountNumber,
        String customerName,
        int age,
        double balance,
        String employer,
        int inactiveMonths)
    {
        if(type == null)
        {
            throw new IllegalArgumentException(
                "Account type cannot be null"
            );
        }

        if(type.equalsIgnoreCase("SALARY"))
        {
            return new SalaryAccount(
                accountNumber,
                customerName,
                age,
                balance,
                employer,
                inactiveMonths
            );
        }

        return createAccount(
            type,
            accountNumber,
            customerName,
            age,
            balance,
            0
        );
    }

    public static AbstractAccount createAccount(
        String type,
        int accountNumber,
        String customerName,
        int age,
        double balance,
        double interestRate,
        int tenureMonths)
    {
        if(type == null)
        {
            throw new IllegalArgumentException(
                "Account type cannot be null"
            );
        }

        if(type.equalsIgnoreCase("FIXED_DEPOSIT") ||
           type.equalsIgnoreCase("FD"))
        {
            return new FixedDepositAccount(
                accountNumber,
                customerName,
                age,
                balance,
                interestRate,
                tenureMonths
            );
        }

        return createAccount(
            type,
            accountNumber,
            customerName,
            age,
            balance,
            0
        );
    }
}