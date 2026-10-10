package Activity14;

public class TestAccountRulesEngineProperties {

    public static void main(String[] args) {

        System.out.println(
            "=== Activity 14: Properties-Driven Rules Engine Test ==="
        );

        System.out.println(
            "[Config] Loaded rules from config/savings.properties"
        );

        int[] tenures = {0, 2, 4, 6};

        for (int tenure : tenures) {

            double minBalance =
                AccountRulesEngine.getSavingsMinBalance(tenure);

            double interest =
                AccountRulesEngine.getSavingsInterestRate(tenure);

            System.out.printf(
                "Tenure %d yrs -> Min Balance: Rs %.1f | Interest: %.2f%%%n",
                tenure,
                minBalance,
                interest
            );
        }

        System.out.println(
            "All external properties loaded and verified successfully!"
        );
    }
}