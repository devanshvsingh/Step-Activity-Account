package Activity12;

public class FixedDepositAccount extends AbstractAccount {

    private int tenureMonths;
    private double interestRate;

    public FixedDepositAccount(String accountNumber, String name, int age,
                               double balance, String status, String pin,
                               int tenureMonths, double interestRate) {

        super(accountNumber, name, age,
              balance, "FIXED_DEPOSIT", status, pin);

        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
    }

    @Override
    public void processDebit(double amount)
            throws AccountException {

        throw new AccountException(
            "Premature withdrawal not allowed on Fixed Deposit"
        );
    }

    public double calculateMaturityAmount() {

        return balance +
               (balance * interestRate / 100);
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public double getInterestRate() {
        return interestRate;
    }
}