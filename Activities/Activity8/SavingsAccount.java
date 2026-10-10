package Activity8;

public class SavingsAccount extends Account {

    private double minBalance = 1000.0;

    public SavingsAccount(int accountNumber, String name,
                          int age, double initialBalance) {

        super(accountNumber, name, age,
              initialBalance, "Savings");
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

        if (amount > getBalance()) {
            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }

        if (getBalance() - amount < minBalance) {
            throw new MinimumBalanceViolationException(
                    "Minimum balance of Rs " + minBalance
                    + " must be maintained");
        }

        // Actual withdrawal
        super.withdraw(amount, pin);
    }

    public double getMinBalance() {
        return minBalance;
    }
}