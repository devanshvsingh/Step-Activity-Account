package Activity15;

public class AccountRulesEngine {

    private static AccountRulesEngine instance;

    private AccountRulesPropertiesLoader savings;
    private AccountRulesPropertiesLoader current;
    private AccountRulesPropertiesLoader fixedDeposit;
    private AccountRulesPropertiesLoader salary;

    private AccountRulesEngine() {

        savings = new AccountRulesPropertiesLoader(
            "Activity15/config/rules/savings.properties"
        );

        current = new AccountRulesPropertiesLoader(
            "Activity15/config/rules/current.properties"
        );

        fixedDeposit = new AccountRulesPropertiesLoader(
            "Activity15/config/rules/fixeddeposit.properties"
        );

        salary = new AccountRulesPropertiesLoader(
            "Activity15/config/rules/salary.properties"
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

        String bucket = getBucket(tenureYears);

        if (accountType.equalsIgnoreCase("SAVINGS")) {

            return savings.getDouble(
                "min.balance." + bucket,
                10000.0
            );
        }

        return 0.0;
    }

    public double getInterestRate(
            String accountType,
            int tenureYears) {

        String bucket = getBucket(tenureYears);

        if (accountType.equalsIgnoreCase("SAVINGS")) {

            return savings.getDouble(
                "interest.rate." + bucket,
                2.70
            );
        }

        return 0.0;
    }

    public double getDailyTransferLimit(
            String accountType,
            int tenureYears) {

        String bucket = getBucket(tenureYears);

        AccountRulesPropertiesLoader loader = null;

        if (accountType.equalsIgnoreCase("SAVINGS")) {
            loader = savings;
        } else if (accountType.equalsIgnoreCase("CURRENT")) {
            loader = current;
        } else if (accountType.equalsIgnoreCase("FIXEDDEPOSIT")) {
            loader = fixedDeposit;
        } else if (accountType.equalsIgnoreCase("SALARY")) {
            loader = salary;
        }

        if (loader == null) {
            return 0.0;
        }

        return loader.getDouble(
            "daily.transfer.limit." + bucket,
            0.0
        );
    }
}