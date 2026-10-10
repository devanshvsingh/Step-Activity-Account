package Activity8;

public class Account {

    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    private Integer pin;

    protected static final double MIN_BALANCE_SAVINGS = 500.0;
    protected static final double MIN_BALANCE_CURRENT = 1000.0;

    private static final int MIN_AGE = 18;
    private static final int MIN_PIN = 1000;
    private static final int MAX_PIN = 9999;

    public Account(int accountNumber, String name, int age,
                   double initialBalance, String accountType) {

        if (age < MIN_AGE) {
            throw new IllegalArgumentException(
                    "Customer must be at least 18 years old");
        }

        if (!accountType.equalsIgnoreCase("Savings")
                && !accountType.equalsIgnoreCase("Current")
                && !accountType.equalsIgnoreCase("Fixed_Deposit")
                && !accountType.equalsIgnoreCase("Salary")) {

            throw new IllegalArgumentException(
                    "Invalid account type: " + accountType);
        }

        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.status = "Active";
    }

    public void setPin(int pin) {

        if (pin < MIN_PIN || pin > MAX_PIN) {
            throw new IllegalArgumentException(
                    "PIN must be a 4-digit number");
        }

        this.pin = pin;
    }

    public boolean verifyPin(int pin) {

        if (this.pin == null) {
            return false;
        }

        return this.pin == pin;
    }

    public boolean hasPin() {
        return this.pin != null;
    }

    public void deposit(double amount)
            throws InvalidAmountException, InactiveAccountException {

        if (status.equalsIgnoreCase("Inactive")) {
            throw new InactiveAccountException(
                    "Account is inactive");
        }

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Deposit amount must be positive");
        }

        balance += amount;
    }

    public void withdraw(double amount, int pin)
            throws InvalidAmountException,
            InsufficientBalanceException,
            InactiveAccountException,
            InvalidPinException,
            MinimumBalanceViolationException {

        if (status.equalsIgnoreCase("Inactive")) {
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

        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }

        balance -= amount;
    }

    public void closeAccount() {

        if (status.equalsIgnoreCase("Inactive")) {
            throw new IllegalStateException(
                    "Account is already closed");
        }

        status = "Inactive";
    }

    public void reopenAccount() {

        if (status.equalsIgnoreCase("Active")) {
            throw new IllegalStateException(
                    "Account is already active");
        }

        status = "Active";
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getBalance() {
        return balance;
    }
    protected void setBalance(double balance) {
    this.balance = balance;
}
    public String getAccountType() {
        return accountType;
    }

    public String getStatus() {
        return status;
    }
}