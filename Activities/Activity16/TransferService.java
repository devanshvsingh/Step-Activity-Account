package Activity16;

public class TransferService {

    // Original transfer method
    public void transfer(
            IAccount from,
            IAccount to,
            double amount,
            int pin) throws AccountException {

        if (from == null || to == null) {
            throw new AccountException(
                "Accounts cannot be null"
            );
        }

        if (!from.getStatus().equalsIgnoreCase("ACTIVE")
                || !to.getStatus().equalsIgnoreCase("ACTIVE")) {

            throw new InactiveAccountException(
                "Both accounts must be active"
            );
        }

        if (!from.verifyPin(pin)) {
            throw new InvalidPinException(
                "Incorrect PIN"
            );
        }

        Account source = (Account) from;

        if (!source.canWithdraw(amount)) {
            throw new InsufficientBalanceException(
                "Insufficient balance"
            );
        }

        from.withdraw(amount, pin);
        to.deposit(amount);
    }

    // New Activity 16 method
    public Transaction transferWithTransaction(
            IAccount from,
            IAccount to,
            double amount,
            int pin) throws AccountException {

        transfer(
            from,
            to,
            amount,
            pin
        );

        return new Transaction(
            Transaction.generateId(),
            java.time.LocalDateTime.now(),
            from.getAccountNumber(),
            TransactionType.TRANSFER,
            amount,
            from.getBalance(),
            "SUCCESS",
            "Transfer of Rs. " + amount
                + " to Account #"
                + to.getAccountNumber(),
            from.getAccountNumber(),
            to.getAccountNumber()
        );
    }
}