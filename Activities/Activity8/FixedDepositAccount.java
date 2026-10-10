package Activity8;

import java.time.LocalDate;

public class FixedDepositAccount extends Account {

    private int tenureMonths;
    private double interestRate;
    private LocalDate maturityDate;

    public FixedDepositAccount(int accountNumber, String name,
                               int age, double initialBalance) {

        this(accountNumber, name, age, initialBalance, 12);
    }

    public FixedDepositAccount(int accountNumber, String name,
                               int age, double initialBalance,
                               int tenureMonths) {

        super(accountNumber, name, age,
              initialBalance, "Fixed_Deposit");

        this.tenureMonths = tenureMonths;
        this.interestRate = 6.5;

        this.maturityDate =
                LocalDate.now().plusMonths(tenureMonths);
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

        if (LocalDate.now().isBefore(maturityDate)) {
            throw new InactiveAccountException(
                    "Fixed Deposit has not matured yet. "
                    + "Maturity Date: " + maturityDate);
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

        setBalance(getBalance() - amount);
    }

    public double calculateMaturityAmount() {

        double years = tenureMonths / 12.0;

        return getBalance()
                * Math.pow(1 + interestRate / 100, years);
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }
}