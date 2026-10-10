package Activity3_4;

public class TestAccount {
    public static void displayAccount(Account account) {

        System.out.println(
                "Account #" + account.getAccountNumber()
                        + " | " + account.getName()
                        + " (" + account.getAge() + " yrs)"
                        + " | " + account.getAccountType()
                        + " | " + account.getBalance()
                        + " | " + account.getStatus()
                        + " | PIN: " + (account.hasPin() ? "Yes" : "No"));
    }

    public static void main(String[] args) {
        System.out.println(">>> 1. Creating Valid Account");

        Account account1 = new Account(
                1001,
                "Aditya",
                19,
                1000.0,
                "Savings");
        displayAccount((account1));
        System.out.println(">>> 2. Creating Invalid Account(under 18)");
        System.out.println("Creating account with age 16");

        Account account2 = new Account(
                1002,
                "Young Kid",
                16,
                500.0,
                "Savings");
        System.out.println(
                "Age auto-corrected to: " + account2.getAge());
        displayAccount(account2);
        System.out.println(">>> 3. Creating Invalid Account");
        System.out.println("Creating account with type \"Invalid\"");
        Account account3 = new Account(
                1003,
                "Test User",
                25,
                500.0,
                "Invalid"
        );
        System.out.println(
                "Account type defaulted to: "
                + account3.getAccountType()
        );

        displayAccount(account3);
         System.out.println(
                ">>> 4: Minimum Balance Enforcement on Creation"
        );

        System.out.println(
                "Creating Savings account with 300 (below minimum)"
        );

        Account account4 = new Account(
                1004,
                "Bob Wilson",
                25,
                300.0,
                "Savings"
        );

        System.out.println(
                "Balance auto-corrected to minimum: "
                + account4.getBalance()
        );

        displayAccount(account4);
         System.out.println(
                ">>> 5:  Withdrawal with Minimum Balance"
        );
       Account account5 = new Account(
                1005,
                "Alice Brown",
                30,
                1200.0,
                "Current"
        );
         account5.setPin(1234);

        System.out.print("Initial: ");
        displayAccount(account5);

        double withdrawAmount = 200.0;

        boolean withdrawResult =
                account5.withdraw(withdrawAmount, 1234);

        if (withdrawResult) {

            System.out.println(
                    "Withdrawing " + withdrawAmount
                    + ": SUCCESS"
            );

            System.out.println(
                    "New balance: " + account5.getBalance()
            );
        }
        System.out.print("After withdrawal: ");
        displayAccount(account5);
        withdrawAmount = 900.0;

        withdrawResult =
                account5.withdraw(withdrawAmount, 1234);

        if (!withdrawResult) {

            System.out.println(
                    "Withdrawing " + withdrawAmount
                    + " (would leave -100): "
                    + "FAILED (Minimum balance violation)"
            );
        }
        System.out.println(
                "Current balance: " + account5.getBalance()
        );
        System.out.println(
                ">>> Test 6: Account Status Management"
        );

        Account account6 = new Account(
                1006,
                "Charlie Green",
                35,
                2000.0,
                "Savings"
        );

        System.out.print("Initial: ");
        displayAccount(account6);


        boolean closeResult = account6.closeAccount();

        if (closeResult) {
            System.out.println("Closing account: SUCCESS");
        }

        System.out.print("After close: ");
        displayAccount(account6);


        boolean depositResult = account6.deposit(500.0);

        if (!depositResult) {

            System.out.println(
                    "Depositing 500.0 to closed account: "
                    + "FAILED (Account inactive)"
            );
        }


        boolean reopenResult = account6.reopenAccount();

        if (reopenResult) {
            System.out.println("Reopening account: SUCCESS");
        }

        System.out.print("After reopen: ");
        displayAccount(account6);
        System.out.println(
                ">>> Test 7: PIN Protection"
        );

        Account account7 = new Account(
                1007,
                "Diana Prince",
                28,
                1500.0,
                "Savings"
        );


        boolean pinResult = account7.setPin(1234);

        if (pinResult) {
            System.out.println(
                    "Setting PIN 1234: SUCCESS"
            );
        }


        withdrawAmount = 200.0;

        withdrawResult =
                account7.withdraw(withdrawAmount, 1234);

        if (withdrawResult) {

            System.out.println(
                    "Withdrawing 200.0 with correct PIN (1234): "
                    + "SUCCESS"
            );

            System.out.println(
                    "New balance: " + account7.getBalance()
            );
        }


        withdrawResult =
                account7.withdraw(100.0, 9999);

        if (!withdrawResult) {

            System.out.println(
                    "Withdrawing 100.0 with incorrect PIN (9999): "
                    + "FAILED (Incorrect PIN)"
            );
        }


        // Account without PIN
        Account accountWithoutPin = new Account(
                1008,
                "No PIN User",
                25,
                1000.0,
                "Savings"
        );

        withdrawResult =
                accountWithoutPin.withdraw(100.0, 1234);

        if (!withdrawResult) {

            System.out.println(
                    "Withdrawing 100.0 with PIN not set: "
                    + "FAILED (PIN not set)"
            );
        }
        System.out.println(
                ">>> Test 8: All Accounts Summary"
        );

        displayAccount(account1);
        displayAccount(account2);
        displayAccount(account3);
        displayAccount(account4);
        displayAccount(account5);
        displayAccount(account6);
        displayAccount(account7);
    }
}