package Activity15;

import java.time.LocalDateTime;

public abstract class Account implements IAccount {

    protected int accountNumber;
    protected String name;
    protected int age;
    protected double balance;
    protected String accountType;
    protected String status;
    protected int pin;

    private double dailyTransferTotal = 0.0;
    private LocalDateTime lastTransferDate = LocalDateTime.now();

    public Account(int accountNumber, String name, int age,
                   double balance, String accountType) {

        if (age < 18) {
            throw new IllegalArgumentException(
                "Customer must be at least 18 years old"
            );
        }

        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = balance;
        this.accountType = accountType;
        this.status = "ACTIVE";
    }

    @Override
    public boolean verifyPin(int pin) {
        return this.pin == pin;
    }

    @Override
    public void setPin(int pin) {

        if (pin < 1000 || pin > 9999) {
            throw new IllegalArgumentException(
                "PIN must be a 4-digit number"
            );
        }

        this.pin = pin;
    }

    @Override
    public void deposit(double amount) throws AccountException {

        if (!status.equalsIgnoreCase("ACTIVE")) {
            throw new InactiveAccountException(
                "Account is inactive"
            );
        }

        if (amount <= 0) {
            throw new InvalidAmountException(
                "Deposit amount must be positive"
            );
        }

        balance += amount;
    }

    @Override
    public void withdraw(double amount, int pin)
            throws AccountException {

        if (!status.equalsIgnoreCase("ACTIVE")) {
            throw new InactiveAccountException(
                "Account is inactive"
            );
        }

        if (!verifyPin(pin)) {
            throw new InvalidPinException(
                "Incorrect PIN"
            );
        }

        if (amount <= 0) {
            throw new InvalidAmountException(
                "Withdrawal amount must be positive"
            );
        }

        if (!canWithdraw(amount)) {
            throw new InsufficientBalanceException(
                "Insufficient balance"
            );
        }

        balance -= amount;
    }

    public boolean canWithdraw(double amount) {
        return amount <= balance;
    }

    // ---------------- Daily Transfer Methods ----------------

    public double getDailyTransferLimit() {

        return AccountRulesEngine.getInstance()
                .getDailyTransferLimit(
                    getAccountType(),
                    getTenureYears()
                );
    }

    public double getRemainingDailyTransferLimit() {

        resetDailyTransferIfNeeded();

        return Math.max(
            0.0,
            getDailyTransferLimit() - dailyTransferTotal
        );
    }

    public boolean canTransfer(double amount) {

        resetDailyTransferIfNeeded();

        return dailyTransferTotal + amount
                <= getDailyTransferLimit();
    }

    public void updateDailyTransferTotal(double amount) {

        resetDailyTransferIfNeeded();

        dailyTransferTotal += amount;
        lastTransferDate = LocalDateTime.now();
    }

    public void resetDailyTransferIfNeeded() {

        LocalDateTime now = LocalDateTime.now();

        if (!lastTransferDate.toLocalDate()
                .equals(now.toLocalDate())) {

            dailyTransferTotal = 0.0;
            lastTransferDate = now;
        }
    }

    public double getDailyTransferTotal() {
        return dailyTransferTotal;
    }

    public LocalDateTime getLastTransferDate() {
        return lastTransferDate;
    }

    // For Activity 15, default tenure is 0 years.
    // SavingsAccount overrides this.
    public int getTenureYears() {
        return 0;
    }

    // ---------------- Getters ----------------

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

    public String getAccountType() {
        return accountType;
    }

    public String getStatus() {
        return status;
    }
}