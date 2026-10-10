package Activity13_2;

public class SavingsAccount extends AbstractAccount {

    private int tenureYears;
    private double minBalance;
    private double interestRate;

    public SavingsAccount(
            String accountNumber,
            String name,
            int age,
            double balance,
            String status,
            String pin,
            int tenureYears) {

        super(
            accountNumber,
            name,
            age,
            balance,
            "SAVINGS",
            status,
            pin
        );

        this.tenureYears = tenureYears;

        this.minBalance =
            AccountRulesEngine.getSavingsMinBalance(
                tenureYears
            );

        this.interestRate =
            AccountRulesEngine.getSavingsInterestRate(
                tenureYears
            );
    }

    @Override
    public void processDebit(double amount)
            throws AccountException {

        if (balance - amount < minBalance) {

            throw new MinimumBalanceViolationException(
                "Cannot breach minimum balance of Rs "
                + minBalance
            );
        }

        balance -= amount;
    }

    public void applyInterest() {

        double interest =
            balance * interestRate / 100.0;

        balance += interest;
    }

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