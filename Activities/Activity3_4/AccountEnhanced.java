package Activity3_4;
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

    private int AgeValidation(int age){
        if(age<MIN_AGE){
            return MIN_AGE;
        }
        return age;
    }
    private String AccountTypeValidation(String AccType){
        if((!AccType.equalsIgnoreCase("Savings")) && (!AccType.equalsIgnoreCase("Current")) ){
            return "Savings";
        }
        return AccType;
    }
    private double MinimumBalance(double initialBalance, String AccType ){
            if(AccType.equalsIgnoreCase("Savings")){
                if(initialBalance<MIN_BALANCE_SAVINGS){
                    return MIN_BALANCE_SAVINGS;
                }
            }
            if(AccType.equalsIgnoreCase("Current")){
                if(initialBalance<MIN_BALANCE_CURRENT){
                    return MIN_BALANCE_CURRENT;
                }
            }
            return initialBalance;
    }
    public Account(int accountNumber, String name, int age,
                   double initialBalance, String accountType) {
        age = AgeValidation(age);
        accountType = AccountTypeValidation(accountType);
        initialBalance = MinimumBalance(initialBalance,accountType);
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.status = "Active";
    }
    boolean setPin(int pin){
        if(pin<MIN_PIN || pin>MAX_PIN){
            return false;
        }
        this.pin = pin;
        return true;
    }
    boolean verifyPin(int pin){
        if(this.pin == pin){
            return true;
        }
        return false;
    }
    boolean hasPin(){
        if(this.pin != null){
            return true;
        }
        return false;
    }
    public boolean closeAccount(){
        this.status = "Inactive";
        return false; 
    }

    public boolean reopenAccount(){
        this.status = "Active";
        return true; 
    }
    public boolean deposit(double amount) {

        if (this.status.equalsIgnoreCase("Inactive")||amount <= 0 ) {
            return false;
        }

        balance += amount;
        return true;
    }

    public boolean withdraw(double amount,int pin) {
        if(pin != this.pin){
            return false;
        }
        if (this.status.equalsIgnoreCase("Inactive") || amount <= 0 || amount > balance) {
            return false;
        }
        if(this.accountType.equalsIgnoreCase("Savings")){
            if((this.balance-amount)<MIN_BALANCE_SAVINGS){
                return false;
            }
        }
        if(this.accountType.equalsIgnoreCase("Current")){
            if((this.balance-amount)<MIN_BALANCE_CURRENT){
                return false;
            }
        }

        this.balance -= amount;
        return true;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}