package Activity5_6_7;

public class TestAccountExceptions {

    public static void displayAccount(Account account) {

        System.out.println(
                "Account #" + account.getAccountNumber()
                + " | " + account.getName()
                + " (" + account.getAge() + " yrs)"
                + " | " + account.getAccountType()
                + " | " + account.getBalance()
                + " | " + account.getStatus()
                + " | PIN: " + (account.hasPin() ? "Yes" : "No")
        );
    }


    public static void main(String[] args) {

        System.out.println(">>> 1. Valid Account Creation");

        Account account1 = null;

        try {

            account1 = new Account(
                    1001,
                    "John Doe",
                    25,
                    1000.0,
                    "Savings"
            );

            System.out.println("Account created successfully");
            displayAccount(account1);

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        System.out.println();
        System.out.println(">>> 2. Invalid Age");

        try {

            Account account2 = new Account(
                    1002,
                    "Young Kid",
                    16,
                    500.0,
                    "Savings"
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        System.out.println();
        System.out.println(">>> 3. Invalid Account Type");

        try {

            Account account3 = new Account(
                    1003,
                    "Test User",
                    25,
                    1000.0,
                    "Invalid"
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        System.out.println();
        System.out.println(">>> 4. Minimum Balance on Creation");

        try {

            Account account4 = new Account(
                    1004,
                    "Bob Wilson",
                    25,
                    300.0,
                    "Savings"
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        System.out.println();
        System.out.println(">>> 5. Valid Deposit and Withdrawal");

        Account account5 = null;

        try {

            account5 = new Account(
                    1005,
                    "Alice Brown",
                    30,
                    1500.0,
                    "Savings"
            );

            account5.setPin(1234);

            System.out.println("Setting PIN: SUCCESS");

            account5.deposit(500.0);

            System.out.println(
                    "Depositing 500.0: SUCCESS"
            );

            System.out.println(
                    "Balance: " + account5.getBalance()
            );

            account5.withdraw(200.0, 1234);

            System.out.println(
                    "Withdrawing 200.0: SUCCESS"
            );

            System.out.println(
                    "Balance: " + account5.getBalance()
            );

        } catch (AccountException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        System.out.println();
        System.out.println(">>> 6. Invalid Deposit");

        try {

            account5.deposit(-100.0);

        } catch (AccountException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        System.out.println();
        System.out.println(">>> 7. Insufficient Balance");

        try {

            Account account7 = new Account(
                    1007,
                    "Charlie Green",
                    35,
                    1000.0,
                    "Savings"
            );

            account7.setPin(1234);

            System.out.println(
                    "Trying to withdraw 2000.0"
            );

            account7.withdraw(2000.0, 1234);

        } catch (AccountException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        System.out.println();
        System.out.println(">>> 8. Minimum Balance Violation");

        try {

            Account account8 = new Account(
                    1008,
                    "Diana Prince",
                    28,
                    1000.0,
                    "Savings"
            );

            account8.setPin(1234);

            System.out.println(
                    "Trying to withdraw 600.0"
            );

            account8.withdraw(600.0, 1234);

        } catch (AccountException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        System.out.println();
        System.out.println(">>> 9. Inactive Account Operations");

        Account account9 = null;

        try {

            account9 = new Account(
                    1009,
                    "Eve Wilson",
                    32,
                    2000.0,
                    "Current"
            );

            account9.closeAccount();

            System.out.println(
                    "Closing account: SUCCESS"
            );

            account9.deposit(100.0);

        } catch (AccountException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );

        } catch (IllegalStateException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        try {

            account9.reopenAccount();

            System.out.println(
                    "Reopening account: SUCCESS"
            );

            account9.deposit(100.0);

            System.out.println(
                    "Deposit after reopening: SUCCESS"
            );

        } catch (AccountException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );

        } catch (IllegalStateException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        System.out.println();
        System.out.println(">>> 10. PIN Verification");

        Account account10 = null;

        try {

            account10 = new Account(
                    1010,
                    "Frank Miller",
                    40,
                    1500.0,
                    "Savings"
            );

            account10.setPin(1234);

            System.out.println(
                    "Setting PIN 1234: SUCCESS"
            );

            account10.withdraw(200.0, 1234);

            System.out.println(
                    "Withdrawal with correct PIN: SUCCESS"
            );

            account10.withdraw(100.0, 9999);

        } catch (AccountException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        System.out.println();
        System.out.println("Testing account without PIN");

        try {

            Account accountWithoutPin = new Account(
                    1011,
                    "No PIN User",
                    25,
                    1000.0,
                    "Savings"
            );

            accountWithoutPin.withdraw(100.0, 1234);

        } catch (AccountException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        System.out.println();
        System.out.println(">>> 11. All Accounts Summary");

        if (account1 != null) {
            displayAccount(account1);
        }

        if (account5 != null) {
            displayAccount(account5);
        }

        if (account9 != null) {
            displayAccount(account9);
        }

        if (account10 != null) {
            displayAccount(account10);
        }
        System.out.println("=== Activity 7: Account Subclasses Test ===");

SavingsAccount savings = new SavingsAccount(
        101,
        "Aditya",
        20,
        10000.0
);

System.out.println("Savings Account Created: Balance Rs "
        + savings.getBalance()
        + " | Min Balance: Rs "
        + savings.getMinBalance());


CurrentAccount current = new CurrentAccount(
        102,
        "Aditya",
        20,
        10000.0
);

System.out.println("Current Account Created: Overdraft Limit Rs "
        + current.getOverdraftLimit());


FixedDepositAccount fd = new FixedDepositAccount(
        103,
        "Aditya",
        20,
        10000.0
);

System.out.println("Fixed Deposit Created: Tenure "
        + fd.getTenureMonths()
        + " months | Interest: "
        + fd.getInterestRate()
        + "%");


SalaryAccount salary = new SalaryAccount(
        104,
        "Aditya",
        20,
        10000.0,
        "Infosys"
);

System.out.println("Salary Account Created: Employer "
        + salary.getEmployerName());

System.out.println("All subclasses instantiated successfully!");
        System.out.println();
        System.out.println(">>> TEST COMPLETED");
    }
}