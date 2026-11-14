
package com.gdb.ui;

import com.gdb.domain.IAccount;
import com.gdb.domain.AbstractAccount;
import com.gdb.service.AccountService;

import java.util.List;
import java.util.Scanner;

public class AccountUI
{
    // STEP 1: Fields
    private AccountService service;
    private Scanner scanner;

    // STEP 2: Constructor
    public AccountUI(AccountService service)
    {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    // STEP 3: Start the interactive application
    public void start()
    {
        System.out.println("Booting Global Digital Bank...");

        while(true)
        {
            try
            {
                displayMainMenu();

                int choice = readInt("Enter your choice: ");

                switch(choice)
                {
                    case 1:
                        handleOpenAccount();
                        break;

                    case 2:
                        handleDeposit();
                        break;

                    case 3:
                        handleWithdraw();
                        break;

                    case 4:
                        handleTransfer();
                        break;

                    case 5:
                        handleCloseAccount();
                        break;

                    case 6:
                        handleViewAccount();
                        break;

                    case 7:
                        handleViewTransactions();
                        break;

                    case 8:
                        System.out.println("Thank you! Goodbye.");
                        return;

                    default:
                        System.out.println(
                            "Invalid choice. Please select 1-8."
                        );
                }
            }
            catch(Exception e)
            {
                System.out.println(
                    "ERROR: " + e.getMessage()
                );
            }

            System.out.println();
        }
    }

    // STEP 4: Display main menu
    private void displayMainMenu()
    {
        System.out.println("\n========================================");
        System.out.println("       GLOBAL DIGITAL BANK");
        System.out.println("========================================");
        System.out.println("1. Open Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Close Account");
        System.out.println("6. View Account Details");
        System.out.println("7. View Transaction History");
        System.out.println("8. Exit");
        System.out.println("========================================");
    }

    // STEP 5: Open Account
    private void handleOpenAccount()
    {
        System.out.println("\n--- Open Account ---");

        String type = readString(
            "Account Type (Savings/Current/FixedDeposit/Salary): "
        );

        String name = readString("Name: ");

        int age = readInt("Age: ");

        double balance = readDouble("Initial Balance: ");

        IAccount account = service.openAccount(
            type, name, age, balance
        );

        System.out.println(
            "SUCCESS: Account #" + account.get_account_number()
            + " | " + account.get_customer_name()
            + " | " + account.get_account_type()
            + " | Rs. " + account.get_balance()
            + " | " + account.get_status()
        );

        int pin;

        while(true)
        {
            pin = readInt("Set 4-digit PIN: ");

            if(pin >= 1000 && pin <= 9999)
            {
                break;
            }

            System.out.println(
                "Please enter a valid 4-digit PIN."
            );
        }

        ((AbstractAccount) account).setPin(pin);

        System.out.println("PIN set successfully.");
    }

    // STEP 6: Deposit
    private void handleDeposit() throws Exception
    {
        System.out.println("\n--- Deposit ---");

        int accountNumber = readInt("Account Number: ");

        double amount = readDouble("Amount to deposit: ");

        Object transaction = service.deposit(
            accountNumber, amount
        );

        IAccount account = service.getAccount(accountNumber);

        System.out.println(
            "SUCCESS: Deposited Rs. " + amount
            + " to #" + accountNumber
            + ". New balance: Rs. " + account.get_balance()
        );
    }

    // STEP 7: Withdraw
    private void handleWithdraw() throws Exception
    {
        System.out.println("\n--- Withdraw ---");

        int accountNumber = readInt("Account Number: ");

        double amount = readDouble("Amount to withdraw: ");

        int pin = readInt("Enter PIN: ");

        service.withdraw(accountNumber, amount, pin);

        IAccount account = service.getAccount(accountNumber);

        System.out.println(
            "SUCCESS: Withdrawn Rs. " + amount
            + " from #" + accountNumber
            + ". New balance: Rs. " + account.get_balance()
        );
    }

    // STEP 8: Transfer
    private void handleTransfer() throws Exception
    {
        System.out.println("\n--- Transfer ---");

        int fromAccount = readInt("Source Account Number: ");

        int toAccount = readInt("Destination Account Number: ");

        double amount = readDouble("Amount to transfer: ");

        int pin = readInt("Enter PIN: ");

        service.transfer(
            fromAccount, toAccount, amount, pin
        );

        IAccount account = service.getAccount(fromAccount);

        System.out.println(
            "SUCCESS: Transferred Rs. " + amount
            + " from #" + fromAccount
            + " to #" + toAccount
            + ". Remaining balance: Rs. "
            + account.get_balance()
        );
    }

    // STEP 9: Close Account
    private void handleCloseAccount() throws Exception
    {
        System.out.println("\n--- Close Account ---");

        int accountNumber = readInt("Account Number: ");

        int pin = readInt("Enter PIN: ");

        service.closeAccount(accountNumber, pin);

        System.out.println(
            "SUCCESS: Account #" + accountNumber
            + " has been closed."
        );
    }

    // STEP 10: View Account Details
    private void handleViewAccount() throws Exception
    {
        System.out.println("\n--- Account Details ---");

        int accountNumber = readInt("Account Number: ");

        IAccount account = service.getAccount(accountNumber);

        account.displayAccountInfo();
    }

    // STEP 11: View Transaction History
    private void handleViewTransactions()
    {
        System.out.println("\n--- Transaction History ---");

        List<?> history = service.getTransactionHistory();

        if(history.isEmpty())
        {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println(
            "Total Transactions: " + history.size()
        );

        for(int i = 0; i < history.size(); i++)
        {
            System.out.println(
                "[" + (i + 1) + "] " + history.get(i)
            );
        }
    }

    // STEP 12: Read integer input
    private int readInt(String prompt)
    {
        while(true)
        {
            System.out.print(prompt);

            try
            {
                return Integer.parseInt(
                    scanner.nextLine().trim()
                );
            }
            catch(NumberFormatException e)
            {
                System.out.println(
                    "Invalid input. Please enter an integer."
                );
            }
        }
    }

    // STEP 13: Read decimal input
    private double readDouble(String prompt)
    {
        while(true)
        {
            System.out.print(prompt);

            try
            {
                double value = Double.parseDouble(
                    scanner.nextLine().trim()
                );

                if(Double.isFinite(value))
                {
                    return value;
                }

                System.out.println(
                    "Please enter a valid finite number."
                );
            }
            catch(NumberFormatException e)
            {
                System.out.println(
                    "Invalid input. Please enter a number."
                );
            }
        }
    }

    // STEP 14: Read string input
    private String readString(String prompt)
    {
        while(true)
        {
            System.out.print(prompt);

            String value = scanner.nextLine().trim();

            if(!value.isEmpty())
            {
                return value;
            }

            System.out.println(
                "Input cannot be empty. Please try again."
            );
        }
    }
}
