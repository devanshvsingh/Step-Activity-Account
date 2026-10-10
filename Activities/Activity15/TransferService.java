package Activity15;

public class TransferService {

    public void transfer(
            IAccount from,
            IAccount to,
            double amount,
            int pin) throws AccountException {

        // 1. Null check
        if (from == null || to == null) {
            throw new AccountException(
                "Source and destination accounts are required"
            );
        }

        // 2. Both accounts active
        if (!from.getStatus().equalsIgnoreCase("ACTIVE")
                || !to.getStatus().equalsIgnoreCase("ACTIVE")) {

            throw new InactiveAccountException(
                "Both accounts must be active to transfer funds"
            );
        }

        // 3. PIN check
        if (!from.verifyPin(pin)) {
            throw new InvalidPinException(
                "Incorrect PIN"
            );
        }

        // 4. Check balance
        if (!((Account) from).canWithdraw(amount)) {
            throw new InsufficientBalanceException(
                "Insufficient balance for transfer of Rs. "
                + amount
            );
        }

        Account source = (Account) from;

        // 5. Daily limit
        source.resetDailyTransferIfNeeded();

        if (!source.canTransfer(amount)) {

            throw new AccountException(
                "Daily transfer limit exceeded. Remaining today: Rs. "
                + source.getRemainingDailyTransferLimit()
            );
        }

        // 6. Debit source
        from.withdraw(amount, pin);

        // 7. Credit destination
        to.deposit(amount);

        // 8. Record transfer
        source.updateDailyTransferTotal(amount);
    }
}