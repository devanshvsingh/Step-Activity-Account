package Activity11;

public interface IAccount {

    String getAccountNumber();

    String getName();

    double getBalance();

    String getAccountType();

    String getStatus();

    int getAge();

    boolean validatePin(String enteredPin);

    boolean changePin(String oldPin, String newPin);

    void deposit(double amount) throws InvalidAmountException;

    void withdraw(double amount, String pin) throws AccountException;

    void displayAccountInfo();
}