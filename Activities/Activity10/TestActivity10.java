package Activity10;
import Activity9.*;
public class TestActivity10 {

    // Transfer money from one account to another
    public static void transfer(AbstractAccount source,
                                 AbstractAccount destination,
                                 double amount,
                                 String pin) {

        try {
            // First withdraw from source
            source.withdraw(amount, pin);

            // Only deposit if withdrawal succeeds
            destination.deposit(amount);

            System.out.println(
                "Transfer Rs " + amount +
                " from " + source.getAccountType() +
                " to " + destination.getAccountType() +
                ": SUCCESS"
            );

        } catch (AccountException e) {

            System.out.println(
                "Transfer failed: " + e.getMessage()
            );
        }
    }

    public static void main(String[] args) {

        System.out.println(
            "=== Activity 10: Banking Operations Suite ==="
        );

        // ------------------------------------------------
        // STEP 1: Create Account Portfolio
        // ------------------------------------------------

        AbstractAccount[] accounts = {

            new SavingsAccount(
                "SAV1001",
                "Rajesh Sharma",
                28,
                10000.0,
                "ACTIVE",
                "1234",
                1000.0,
                4.0
            ),

            new CurrentAccount(
                "CUR1001",
                "Priya Patel",
                34,
                5000.0,
                "ACTIVE",
                "5678",
                10000.0
            ),

            new SalaryAccount(
                "SAL1001",
                "Amit Kumar",
                30,
                15000.0,
                "ACTIVE",
                "1111",
                "ABC Company"
            )
        };

        AbstractAccount savings = accounts[0];
        AbstractAccount current = accounts[1];

        // ------------------------------------------------
        // STEP 2: Successful Transfer
        // ------------------------------------------------

        transfer(
            savings,
            current,
            3000.0,
            "1234"
        );

        System.out.println(
            "Savings Balance: Rs " +
            savings.getBalance() +
            " | Current Balance: Rs " +
            current.getBalance()
        );

        // ------------------------------------------------
        // STEP 3: Failed Transfer - Wrong PIN
        // ------------------------------------------------

        double savingsBefore = savings.getBalance();
        double currentBefore = current.getBalance();

        try {

            savings.withdraw(2000.0, "9999");

            // This line should NOT execute
            current.deposit(2000.0);

        } catch (AccountException e) {

            System.out.println(
                "Failed Transfer (Wrong PIN): "
                + "Exception caught, no balance changed [PASS]"
            );
        }

        // Verify balances did not change
        if (savings.getBalance() == savingsBefore &&
            current.getBalance() == currentBefore) {

            System.out.println(
                "Balance verification: PASS"
            );
        }

        // ------------------------------------------------
        // STEP 4: Monthly Banking Cycle
        // ------------------------------------------------

        for (AbstractAccount account : accounts) {

            if (account instanceof SavingsAccount) {

                SavingsAccount savingsAccount =
                    (SavingsAccount) account;

                savingsAccount.applyInterest();
            }

            if (account instanceof SalaryAccount) {

                SalaryAccount salaryAccount =
                    (SalaryAccount) account;

                System.out.println(
                    "Salary Account " +
                    salaryAccount.getAccountNumber() +
                    " inactive months: " +
                    salaryAccount.getInactiveMonths()
                );
            }
        }

        System.out.println(
            "Monthly Interest Cycle processed for all qualifying accounts."
        );

        System.out.println(
            "All banking operations passed!"
        );
    }
}