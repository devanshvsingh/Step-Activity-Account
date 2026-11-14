
package com.gdb.ui;

import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.service.AccountService;

import java.util.*;

public class AccountUI {

    private final AccountService service;
    private final Scanner scanner;

    public AccountUI(AccountService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void start() {

        while (true) {

            displayMainMenu();

            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {

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
                            "Invalid choice. Please enter 1-8."
                        );
                }

            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    // Display main menu
    private void displayMainMenu() {

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

    // Open account
    private void handleOpenAccount() throws Exception {

        System.out.println("\n--- Open Account ---");

        String type = readString(
            "Account Type (Savings/Current/FixedDeposit/Salary): "
        );

        String name = readString("Name: ");

        int age = readInt("Age: ");

        double amount = readDouble("Initial Balance: ");

        IAccount account = service.openAccount(
            type, name, age, amount
        );

        System.out.println(
            "SUCCESS: " + account.getAccountInfo()
        );

        int pin = readInt("Set 4-digit PIN: ");

        account.setPin(pin);

        System.out.println("PIN set successfully.");
    }

    // Deposit
    private void handleDeposit() throws Exception {

        System.out.println("\n--- Deposit ---");

        int accNo = readInt("Account Number: ");

        double amount = readDouble("Amount to deposit: ");

        Transaction txn = service.deposit(accNo, amount);

        System.out.println(
            "SUCCESS: Deposited Rs. " + amount +
            " to #" + accNo +
            ". New balance: Rs. " + txn.getBalanceAfter()
        );
    }

    // Withdraw
    private void handleWithdraw() throws Exception {

        System.out.println("\n--- Withdraw ---");

        int accNo = readInt("Account Number: ");

        double amount = readDouble("Amount to withdraw: ");

        int pin = readInt("PIN: ");

        Transaction txn = service.withdraw(
            accNo, amount, pin
        );

        System.out.println(
            "SUCCESS: Withdrew Rs. " + amount +
            " from #" + accNo +
            ". New balance: Rs. " + txn.getBalanceAfter()
        );
    }

    // Transfer
    private void handleTransfer() throws Exception {

        System.out.println("\n--- Transfer ---");

        int fromAcc = readInt("From Account: ");

        int toAcc = readInt("To Account: ");

        double amount = readDouble("Amount: ");

        int pin = readInt("PIN: ");

        service.transfer(fromAcc, toAcc, amount, pin);

        System.out.println(
            "SUCCESS: Transferred Rs. " + amount +
            " from #" + fromAcc +
            " to #" + toAcc
        );
    }

    // Close account
    private void handleCloseAccount() throws Exception {

        System.out.println("\n--- Close Account ---");

        int accNo = readInt("Account Number: ");

        int pin = readInt("PIN: ");

        service.closeAccount(accNo, pin);

        System.out.println(
            "SUCCESS: Account #" + accNo +
            " has been closed."
        );
    }

    // View account details
    private void handleViewAccount() {

        System.out.println("\n--- View Account Details ---");

        int accNo = readInt("Account Number: ");

        IAccount account = service.getAccount(accNo);

        if (account == null) {

            System.out.println(
                "ERROR: Account not found: " + accNo
            );

        } else {

            account.displayAccountInfo();
        }
    }

    // View transaction history
    private void handleViewTransactions() {

        System.out.println("\n--- Transaction History ---");

        List<Transaction> history =
            service.getTransactionHistory();

        if (history.isEmpty()) {

            System.out.println("No transactions logged.");

        } else {

            for (int i = 0; i < history.size(); i++) {

                System.out.println(
                    "  [" + (i + 1) + "] " + history.get(i)
                );
            }
        }
    }

    // Read integer input
    private int readInt(String prompt) {

        while (true) {

            System.out.print(prompt);

            try {

                return Integer.parseInt(
                    scanner.nextLine().trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                    "Invalid integer number. Please try again."
                );
            }
        }
    }

    // Read decimal input
    private double readDouble(String prompt) {

        while (true) {

            System.out.print(prompt);

            try {

                double value = Double.parseDouble(
                    scanner.nextLine().trim()
                );

                if (!Double.isFinite(value)) {
                    System.out.println("Please enter a valid amount.");
                    continue;
                }

                return value;

            } catch (NumberFormatException e) {

                System.out.println(
                    "Invalid decimal number. Please try again."
                );
            }
        }
    }

    // Read string input
    private String readString(String prompt) {

        while (true) {

            System.out.print(prompt);

            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("Input cannot be empty.");
        }
    }
}
