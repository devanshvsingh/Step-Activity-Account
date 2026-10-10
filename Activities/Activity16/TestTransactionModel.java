package Activity16;

public class TestTransactionModel {

    public static void main(String[] args)
            throws Exception {

        System.out.println(
            "============================================================"
        );

        System.out.println(
            "  ACTIVITY 16 — TRANSACTION MODEL TEST"
        );

        System.out.println(
            "============================================================"
        );

        // STEP 10
        SavingsAccount acc1 =
            (SavingsAccount) AccountFactory.createAccount(
                "SAVINGS",
                1001,
                "Rajesh Sharma",
                30,
                50000,
                0
            );

        acc1.setPin(1234);

        Transaction deposit =
            acc1.depositWithTransaction(5000);

        System.out.println(
            "[STEP 10] Deposit Transaction: "
            + deposit
        );

        // STEP 11
        Transaction withdrawal =
            acc1.withdrawWithTransaction(
                2000,
                1234
            );

        System.out.println(
            "[STEP 11] Withdrawal Transaction: "
            + withdrawal
        );

        // STEP 12
        SavingsAccount acc2 =
            (SavingsAccount) AccountFactory.createAccount(
                "SAVINGS",
                1002,
                "Priya Patel",
                28,
                20000,
                0
            );

        TransferService service =
            new TransferService();

        Transaction transfer =
            service.transferWithTransaction(
                acc1,
                acc2,
                1000,
                1234
            );

        System.out.println(
            "[STEP 12] Transfer Transaction: "
            + transfer
        );

        // STEP 13
        acc1.deposit(1000);

        System.out.println(
            "[STEP 13] Legacy Deposit +1000: "
            + "Account #" + acc1.getAccountNumber()
            + " | " + acc1.getName()
            + " (" + acc1.getAge() + " yrs, Tenure: "
            + acc1.getTenureYears()
            + " yrs) | "
            + acc1.getAccountType()
            + " | Rs. "
            + acc1.getBalance()
            + " | "
            + acc1.getStatus()
        );
    }
}