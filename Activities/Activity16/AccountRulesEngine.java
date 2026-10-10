package Activity16;

public class AccountRulesEngine {

    private static AccountRulesEngine instance;

    private AccountRulesPropertiesLoader savings;

    private AccountRulesEngine() {

        savings =
            new AccountRulesPropertiesLoader(
                "Activity16/config/rules/savings.properties"
            );
    }

    public static AccountRulesEngine getInstance() {

        if (instance == null) {
            instance = new AccountRulesEngine();
        }

        return instance;
    }

    private String getBucket(int tenureYears) {

        if (tenureYears >= 5) {
            return "privilege";
        }

        if (tenureYears >= 3) {
            return "premium";
        }

        if (tenureYears >= 1) {
            return "standard";
        }

        return "new";
    }

    public double getMinimumBalance(
            String accountType,
            int tenureYears) {

        String bucket =
            getBucket(tenureYears);

        if (accountType.equalsIgnoreCase("SAVINGS")) {

            return savings.getDouble(
                "min.balance." + bucket,
                10000
            );
        }

        return 0;
    }

    public double getInterestRate(
            String accountType,
            int tenureYears) {

        String bucket =
            getBucket(tenureYears);

        if (accountType.equalsIgnoreCase("SAVINGS")) {

            return savings.getDouble(
                "interest.rate." + bucket,
                2.70
            );
        }

        return 0;
    }
}