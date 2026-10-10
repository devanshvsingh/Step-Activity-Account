package Activity15;

public class CurrentAccount extends Account {

    private double overdraftLimit;

    public CurrentAccount(int accountNumber, String name, int age,
                          double balance, double overdraftLimit) {

        super(accountNumber, name, age, balance, "CURRENT");

        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public boolean canWithdraw(double amount) {

        return amount <= balance + overdraftLimit;
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

        if (amount > balance + overdraftLimit) {
            throw new InsufficientBalanceException(
                "Overdraft limit exceeded"
            );
        }

        balance -= amount;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }
}