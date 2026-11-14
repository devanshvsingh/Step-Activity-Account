package com.gdb.domain;

public class AccountRulesEngine
{
    private static final String SAVINGS_FILE =
        "src/main/resources/config/rules/savings.properties";

    private static final String CURRENT_FILE =
        "src/main/resources/config/rules/current.properties";

    private static final String FD_FILE =
        "src/main/resources/config/rules/fixeddeposit.properties";

    private static final String SALARY_FILE =
        "src/main/resources/config/rules/salary.properties";

    private static AccountRulesPropertiesLoader
        load(String filePath)
    {
        return new AccountRulesPropertiesLoader(filePath);
    }

    private static String getSavingsTier(int tenureYears)
    {
        if(tenureYears >= 5)
        {
            return "privilege";
        }
        else if(tenureYears >= 3)
        {
            return "premium";
        }
        else if(tenureYears >= 1)
        {
            return "standard";
        }
        else
        {
            return "new";
        }
    }

    public static double getSavingsMinBalance(
        int tenureYears)
    {
        AccountRulesPropertiesLoader loader =
            load(SAVINGS_FILE);

        String tier = getSavingsTier(tenureYears);

        return loader.getDouble(
            "savings.min_balance." + tier,
            10000.0
        );
    }

    public static double getSavingsInterestRate(
        int tenureYears)
    {
        AccountRulesPropertiesLoader loader =
            load(SAVINGS_FILE);

        String tier = getSavingsTier(tenureYears);

        return loader.getDouble(
            "savings.interest_rate." + tier,
            2.70
        );
    }

    public static double getCurrentOverdraftLimit(
        double monthlyTurnover)
    {
        AccountRulesPropertiesLoader loader =
            load(CURRENT_FILE);

        double multiplier =
            loader.getDouble(
                "current.overdraft.multiplier",
                2.5
            );

        double minimum =
            loader.getDouble(
                "current.overdraft.minimum",
                25000.0
            );

        return Math.max(
            minimum,
            monthlyTurnover * multiplier
        );
    }

    public static double getFDInterestRate(
        int months)
    {
        AccountRulesPropertiesLoader loader =
            load(FD_FILE);

        if(months >= 12)
        {
            return loader.getDouble(
                "fixeddeposit.interest_rate.12",
                6.5
            );
        }
        else if(months >= 6)
        {
            return loader.getDouble(
                "fixeddeposit.interest_rate.6",
                6.0
            );
        }
        else if(months >= 3)
        {
            return loader.getDouble(
                "fixeddeposit.interest_rate.3",
                5.5
            );
        }

        return 5.0;
    }

    public static double getSalaryMinimumBalance()
    {
        AccountRulesPropertiesLoader loader =
            load(SALARY_FILE);

        return loader.getDouble(
            "salary.minimum.balance",
            0.0
        );
    }

    public static int getSalaryInactiveMonths()
    {
        AccountRulesPropertiesLoader loader =
            load(SALARY_FILE);

        return (int) loader.getDouble(
            "salary.inactive.months",
            3
        );
    }
private static String getTenureTier(int tenureYears)
{
     if(tenureYears >= 5)
     {
        return "privilege";
     }
     else if(tenureYears >= 3)
     {
       return "premium";
     }
        else if(tenureYears >= 1)
        {
            return "standard";
        }

        return "new";
    }
    // Activity 15: Fetch the daily transfer limit
public static double getDailyTransferLimit(String accountType, int tenureYears)
{
    if(accountType == null)
    {
       return 0.0;
    }
    String type = accountType.toLowerCase();
    if(type.contains("savings"))
    {
        type = "savings";
    }
    else if(type.contains("current"))
    {
        type = "current";
    }
    else if(type.contains("fixeddeposit"))
    {
        type = "fixeddeposit";
    }
    else if(type.contains("salary"))
    {
        type = "salary";
    }
    else
    {
        return 0.0;
    }

    String tier = getTenureTier(tenureYears);
    String file;

    switch(type)
    {
        case "savings":
            file = SAVINGS_FILE;
          break;

        case "current":
            file = CURRENT_FILE;
            break;

        case "fixeddeposit":
            file = FD_FILE;
            break;

        case "salary":
            file = SALARY_FILE;
            break;

        default:
            return 0.0;
    }

    AccountRulesPropertiesLoader loader = load(file);

    String key = type + ".daily.transfer.limit." + tier;

    return loader.getDouble(key, 0.0);
}
}
