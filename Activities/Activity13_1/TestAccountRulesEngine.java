package Activity13_1;

public class TestAccountRulesEngine {

    public static void main(String[] args) {

        System.out.println(
            "=== Activity 13.1: Hardcoded Rules Engine Test ==="
        );

        int[] tenures = {0, 2, 4, 6};

        for (int tenure : tenures) {

            double minBalance =
                AccountRulesEngine.getSavingsMinBalance(tenure);

            double interest =
                AccountRulesEngine.getSavingsInterestRate(tenure);

            System.out.println(
                "Tenure " + tenure +
                " yrs -> Min Balance: Rs " + minBalance +
                " | Interest: " + interest + "%"
            );
        }

        System.out.println(
            "Rules Engine lookup completed successfully!"
        );
    }
}