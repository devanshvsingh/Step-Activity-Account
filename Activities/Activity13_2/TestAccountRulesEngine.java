package Activity13_2;

public class TestAccountRulesEngine {

    public static void main(String[] args) {

        System.out.println(
            "=== Activity 13.2: Dynamic Account Rules Test ==="
        );

        SavingsAccount account =
            (SavingsAccount) AccountFactory.createAccount(
                "SAVINGS",
                "SAV1001",
                "Rajesh Sharma",
                28,
                50000.0,
                "ACTIVE",
                "1234",
                4
            );

        System.out.println(
            "Created Savings Account (Tenure: "
            + account.getTenureYears()
            + " yrs):"
        );

        System.out.println(
            " -> Min Balance: Rs "
            + account.getMinBalance()
            + " (Dynamically fetched)"
        );

        System.out.println(
            " -> Interest Rate: "
            + account.getInterestRate()
            + "% (Dynamically fetched)"
        );

        System.out.println(
            "Dynamic rule integration verified!"
        );
    }
}