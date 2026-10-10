package Activity8;

public class TestActivity8 {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("       ACTIVITY 8: POLYMORPHISM");
        System.out.println("======================================");

        System.out.println("\n>>> 1. Savings Account");

        SavingsAccount savings =
                new SavingsAccount(
                        201,
                        "Aditya",
                        20,
                        10000.0
                );

        savings.setPin(1234);

        System.out.println(
                "Initial Balance: Rs "
                + savings.getBalance()
        );

        System.out.println(
                "Minimum Balance: Rs "
                + savings.getMinBalance()
        );

        try {

            savings.withdraw(5000.0, 1234);

            System.out.println(
                    "Withdrawal of Rs 5000: SUCCESS"
            );

            System.out.println(
                    "Remaining Balance: Rs "
                    + savings.getBalance()
            );

        } catch (AccountException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        System.out.println(
                "\n>>> 2. Savings Minimum Balance Test"
        );

        try {

            savings.withdraw(4500.0, 1234);

            System.out.println(
                    "Withdrawal: SUCCESS"
            );

        } catch (AccountException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        System.out.println("\n>>> 3. Current Account");

        CurrentAccount current =
                new CurrentAccount(
                        202,
                        "Aditya",
                        20,
                        10000.0
                );

        current.setPin(1234);

        System.out.println(
                "Initial Balance: Rs "
                + current.getBalance()
        );

        System.out.println(
                "Overdraft Limit: Rs "
                + current.getOverdraftLimit()
        );

        try {

            current.withdraw(30000.0, 1234);

            System.out.println(
                    "Withdrawal of Rs 30000: SUCCESS"
            );

            System.out.println(
                    "Balance after withdrawal: Rs "
                    + current.getBalance()
            );

        } catch (AccountException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }

        System.out.println(
                "\n>>> 4. Current Account Overdraft Test"
        );

        try {

            current.withdraw(6000.0, 1234);

            System.out.println(
                    "Withdrawal: SUCCESS"
            );

        } catch (AccountException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }

        System.out.println("\n>>> 5. Fixed Deposit Account");

        FixedDepositAccount fd =
                new FixedDepositAccount(
                        203,
                        "Aditya",
                        20,
                        10000.0
                );

        fd.setPin(1234);

        System.out.println(
                "Balance: Rs "
                + fd.getBalance()
        );

        System.out.println(
                "Tenure: "
                + fd.getTenureMonths()
                + " months"
        );

        System.out.println(
                "Interest Rate: "
                + fd.getInterestRate()
                + "%"
        );

        System.out.println(
                "Maturity Date: "
                + fd.getMaturityDate()
        );

        try {

            fd.withdraw(1000.0, 1234);

            System.out.println(
                    "Withdrawal: SUCCESS"
            );

        } catch (AccountException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        System.out.println(
                "\n>>> 6. Dynamic Method Dispatch"
        );

        Account account;

        account = savings;

        System.out.println(
                "\nAccount reference -> SavingsAccount"
        );

        try {

            account.withdraw(1000.0, 1234);

            System.out.println(
                    "SavingsAccount.withdraw() executed"
            );

        } catch (AccountException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        account = current;

        System.out.println(
                "\nAccount reference -> CurrentAccount"
        );

        try {

            account.withdraw(1000.0, 1234);

            System.out.println(
                    "CurrentAccount.withdraw() executed"
            );

        } catch (AccountException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        account = fd;

        System.out.println(
                "\nAccount reference -> FixedDepositAccount"
        );

        try {

            account.withdraw(1000.0, 1234);

            System.out.println(
                    "FixedDepositAccount.withdraw() executed"
            );

        } catch (AccountException e) {

            System.out.println(
                    "Exception: " + e.getMessage()
            );
        }


        System.out.println(
                "\n======================================"
        );

        System.out.println(
                "        ACTIVITY 8 COMPLETED"
        );

        System.out.println(
                "======================================"
        );
    }
}