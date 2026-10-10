package Activity8;

public class CurrentAccount extends Account {

    private double overdraftLimit = 25000.0;

    public CurrentAccount(int accountNumber, String name,
                          int age, double initialBalance) {

        super(accountNumber, name, age,
              initialBalance, "Current");
    }

    @Override
    public void withdraw(double amount, int pin)
            throws InvalidAmountException,
            InsufficientBalanceException,
            InactiveAccountException,
            InvalidPinException,
            MinimumBalanceViolationException {

        if (getStatus().equalsIgnoreCase("Inactive")) {
            throw new InactiveAccountException(
                    "Account is inactive");
        }

        if (!hasPin()) {
            throw new InvalidPinException(
                    "PIN not set");
        }

        if (!verifyPin(pin)) {
            throw new InvalidPinException(
                    "Incorrect PIN");
        }

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Withdrawal amount must be positive");
        }

        if (amount > getBalance() + overdraftLimit) {
            throw new InsufficientBalanceException(
                    "Withdrawal exceeds overdraft limit");
        }

        setBalance(getBalance() - amount);
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}