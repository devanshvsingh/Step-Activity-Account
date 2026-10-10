package Activity15;

public interface IAccount {

    int getAccountNumber();

    String getName();

    int getAge();

    double getBalance();

    String getAccountType();

    String getStatus();

    boolean verifyPin(int pin);

    void setPin(int pin);

    void deposit(double amount) throws AccountException;

    void withdraw(double amount, int pin) throws AccountException;
}