package Activity15;

public class AccountFactory {

    public static IAccount createAccount(
            String type,
            int accountNumber,
            String name,
            int age,
            double balance) {

        if (type == null) {
            throw new IllegalArgumentException(
                "Account type cannot be null"
            );
        }

        switch (type.toUpperCase()) {

            case "SAVINGS":
                return new SavingsAccount(
                    accountNumber,
                    name,
                    age,
                    balance,
                    0
                );

            case "CURRENT":
                return new CurrentAccount(
                    accountNumber,
                    name,
                    age,
                    balance,
                    25000
                );

            case "FIXEDDEPOSIT":
            case "FIXED_DEPOSIT":
                return new FixedDepositAccount(
                    accountNumber,
                    name,
                    age,
                    balance,
                    12,
                    6.5
                );

            default:
                throw new IllegalArgumentException(
                    "Unknown account type: " + type
                );
        }
    }

    public static IAccount createAccount(
            String type,
            int accountNumber,
            String name,
            int age,
            double balance,
            int tenureYears) {

        if (type.equalsIgnoreCase("SAVINGS")) {

            return new SavingsAccount(
                accountNumber,
                name,
                age,
                balance,
                tenureYears
            );
        }

        return createAccount(
            type,
            accountNumber,
            name,
            age,
            balance
        );
    }
}