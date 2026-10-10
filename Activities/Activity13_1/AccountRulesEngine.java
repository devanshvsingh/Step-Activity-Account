package Activity13_1;

import java.util.HashMap;
import java.util.Map;

public class AccountRulesEngine {

    private static final Map<String, Double> minBalances = new HashMap<>();
    private static final Map<String, Double> interestRates = new HashMap<>();

    static {

        minBalances.put("NEW", 10000.0);
        minBalances.put("STANDARD", 7500.0);
        minBalances.put("PREMIUM", 5000.0);
        minBalances.put("PRIVILEGE", 2500.0);

        interestRates.put("NEW", 2.70);
        interestRates.put("STANDARD", 3.00);
        interestRates.put("PREMIUM", 3.50);
        interestRates.put("PRIVILEGE", 4.00);
    }

    public static String getSavingsBucket(int tenureYears) {

        if (tenureYears >= 5) {
            return "PRIVILEGE";
        }

        if (tenureYears >= 3) {
            return "PREMIUM";
        }

        if (tenureYears >= 1) {
            return "STANDARD";
        }

        return "NEW";
    }

    public static double getSavingsMinBalance(int tenureYears) {

        String bucket = getSavingsBucket(tenureYears);

        return minBalances.get(bucket);
    }

    public static double getSavingsInterestRate(int tenureYears) {

        String bucket = getSavingsBucket(tenureYears);

        return interestRates.get(bucket);
    }

    public static double getCurrentOverdraftLimit(double monthlyTurnover) {

        return Math.max(25000.0, monthlyTurnover * 2.5);
    }

    public static double getFDInterestRate(int months) {

        if (months >= 36) {
            return 7.50;
        }

        if (months >= 12) {
            return 6.50;
        }

        return 5.00;
    }
}