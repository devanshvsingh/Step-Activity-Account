package Activity15;

public class SavingsAccount extends Account {

    private int tenureYears;
    private double minBalance;
    private double interestRate;

    public SavingsAccount(int accountNumber, String name, int age,
                           double balance, int tenureYears) {

        super(accountNumber, name, age, balance, "SAVINGS");

        this.tenureYears = tenureYears;

        AccountRulesEngine engine =
            AccountRulesEngine.getInstance();

        this.minBalance =
            engine.getMinimumBalance("SAVINGS", tenureYears);

        this.interestRate =
            engine.getInterestRate("SAVINGS", tenureYears);
    }

    @Override
    public void withdraw(double amount, int pin)
            throws AccountException {

        if (!verifyPin(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }

        if (amount <= 0) {
            throw new InvalidAmountException(
                "Withdrawal amount must be positive"
            );
        }

        if (balance - amount < minBalance) {
            throw new MinimumBalanceViolationException(
                "Minimum balance must be maintained"
            );
        }

        balance -= amount;
    }

    @Override
    public int getTenureYears() {
        return tenureYears;
    }

    public double getMinBalance() {
        return minBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }
}