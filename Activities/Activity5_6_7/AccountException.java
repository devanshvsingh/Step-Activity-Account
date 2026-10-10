package Activity5_6_7;

public class AccountException extends Exception {

    public AccountException(String message) {
        super(message);
    }
}

class InvalidAmountException extends AccountException {

    public InvalidAmountException(String message) {
        super(message);
    }
}

class InsufficientBalanceException extends AccountException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}

class InactiveAccountException extends AccountException {

    public InactiveAccountException(String message) {
        super(message);
    }
}

class InvalidPinException extends AccountException {

    public InvalidPinException(String message) {
        super(message);
    }
}

class MinimumBalanceViolationException extends AccountException {

    public MinimumBalanceViolationException(String message) {
        super(message);
    }
}

class Account {

    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    private Integer pin;

    private static final double MIN_BALANCE_SAVINGS = 500.0;
    private static final double MIN_BALANCE_CURRENT = 1000.0;

    private static final int MIN_AGE = 18;
    private static final int MIN_PIN = 1000;
    private static final int MAX_PIN = 9999;

    public Account(int accountNumber, String name, int age,
            double initialBalance, String accountType) {

        if (age < MIN_AGE) {
            throw new IllegalArgumentException(
                    "Customer must be at least 18 years old. Provided: " + age);
        }

        if (!accountType.equalsIgnoreCase("Savings")
        && !accountType.equalsIgnoreCase("Current")
        && !accountType.equalsIgnoreCase("Fixed_Deposit")
        && !accountType.equalsIgnoreCase("Salary")) {

            throw new IllegalArgumentException(
                    "Account type must be Savings or Current. Provided: "
                            + accountType);
        }

        if (accountType.equalsIgnoreCase("Savings")
                && initialBalance < MIN_BALANCE_SAVINGS) {

            throw new IllegalArgumentException(
                    "Savings account requires minimum balance of "
                            + MIN_BALANCE_SAVINGS
                            + ". Provided: " + initialBalance);
        }

        if (accountType.equalsIgnoreCase("Current")
                && initialBalance < MIN_BALANCE_CURRENT) {

            throw new IllegalArgumentException(
                    "Current account requires minimum balance of "
                            + MIN_BALANCE_CURRENT
                            + ". Provided: " + initialBalance);
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
                    "PIN must be a 4-digit number. Provided: " + pin);
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
                    "Deposit amount must be positive. Provided: " + amount);
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

        if (this.pin == null) {
            throw new InvalidPinException(
                    "PIN not set for this account");
        }

        if (!verifyPin(pin)) {
            throw new InvalidPinException(
                    "Incorrect PIN");
        }

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Withdrawal amount must be positive. Provided: " + amount);
        }

        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: "
                            + balance
                            + ", Requested: "
                            + amount);
        }

        if (accountType.equalsIgnoreCase("Savings")
                && balance - amount < MIN_BALANCE_SAVINGS) {

            throw new MinimumBalanceViolationException(
                    "Minimum balance of "
                            + MIN_BALANCE_SAVINGS
                            + " must be maintained");
        }

        if (accountType.equalsIgnoreCase("Current")
                && balance - amount < MIN_BALANCE_CURRENT) {

            throw new MinimumBalanceViolationException(
                    "Minimum balance of "
                            + MIN_BALANCE_CURRENT
                            + " must be maintained");
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

    public String getAccountType() {
        return accountType;
    }

    public String getStatus() {
        return status;
    }
}

class SavingsAccount extends Account {
    final private double minBalance = 1000.0;
    final private double interestRate = 4.0;

    SavingsAccount(int accountNumber, String name, int age,
            double initialBalance) {

        super(accountNumber, name, age, initialBalance, "Savings");

    }
    void applyInterest() {
        double interest = getBalance() * interestRate / 100;

        try {
            deposit(interest);
        } catch (InvalidAmountException e) {
            System.out.println(e.getMessage());
        } catch (InactiveAccountException e) {
            System.out.println(e.getMessage());
        }
    }
    public double getMinBalance() {
    return minBalance;
}
    

}
class CurrentAccount extends Account {

    private double overdraftLimit = 25000.0;

    public CurrentAccount(int accountNumber, String name, int age,
                          double initialBalance) {

        super(accountNumber, name, age, initialBalance, "CURRENT");
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}


class FixedDepositAccount extends Account {

    private int tenureMonths = 12;
    private double interestRate = 6.5;

    public FixedDepositAccount(int accountNumber, String name, int age,
                               double initialBalance) {

        super(accountNumber, name, age, initialBalance, "FIXED_DEPOSIT");
    }

    public double calculateMaturityAmount() {

        double years = tenureMonths / 12.0;

        double maturityAmount =
                getBalance() * Math.pow(1 + interestRate / 100, years);

        return maturityAmount;
    }
    public int getTenureMonths() {
    return tenureMonths;
}

public double getInterestRate() {
    return interestRate;
}
}


class SalaryAccount extends Account {

    private String employerName;
    private int inactiveMonths;

    public SalaryAccount(int accountNumber, String name, int age,
                         double initialBalance, String employerName) {

        super(accountNumber, name, age, initialBalance, "SALARY");

        this.employerName = employerName;
        this.inactiveMonths = 0;

    }
    public String getEmployerName() {
    return employerName;
}
}