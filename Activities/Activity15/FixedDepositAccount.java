package Activity15;

public class FixedDepositAccount extends Account {

    private int tenureMonths;
    private double interestRate;

    public FixedDepositAccount(int accountNumber, String name, int age,
                               double balance, int tenureMonths,
                               double interestRate) {

        super(accountNumber, name, age, balance, "FIXEDDEPOSIT");

        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(double amount, int pin)
            throws AccountException {

        throw new AccountException(
            "Premature withdrawal not allowed"
        );
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public double getInterestRate() {
        return interestRate;
    }
}