package Activity16;

import java.time.LocalDateTime;

public abstract class Account implements IAccount {

    protected int accountNumber;
    protected String name;
    protected int age;
    protected double balance;
    protected String accountType;
    protected String status;
    protected int pin;

    public Account(int accountNumber,
                   String name,
                   int age,
                   double balance,
                   String accountType) {

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

    // Original deposit method
    @Override
    public void deposit(double amount)
            throws AccountException {

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

    // New Activity 16 method
    public Transaction depositWithTransaction(double amount)
            throws AccountException {

        deposit(amount);

        return new Transaction(
            Transaction.generateId(),
            LocalDateTime.now(),
            accountNumber,
            TransactionType.DEPOSIT,
            amount,
            balance,
            "SUCCESS",
            "Deposit of Rs. " + amount,
            0,
            accountNumber
        );
    }

    // Original withdraw method
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

    // New Activity 16 method
    public Transaction withdrawWithTransaction(
            double amount,
            int pin) throws AccountException {

        withdraw(amount, pin);

        return new Transaction(
            Transaction.generateId(),
            LocalDateTime.now(),
            accountNumber,
            TransactionType.WITHDRAW,
            amount,
            balance,
            "SUCCESS",
            "Withdrawal of Rs. " + amount,
            accountNumber,
            0
        );
    }

    public boolean canWithdraw(double amount) {
        return amount <= balance;
    }

    public int getTenureYears() {
        return 0;
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

    public String getAccountType() {
        return accountType;
    }

    public String getStatus() {
        return status;
    }
}