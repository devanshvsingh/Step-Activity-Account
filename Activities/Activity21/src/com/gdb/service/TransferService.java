
package com.gdb.service;

import com.gdb.domain.AbstractAccount;
import com.gdb.domain.IAccount;
import com.gdb.exceptions.*;
import com.gdb.domain.Transaction;
import com.gdb.domain.TransactionType;

public class TransferService
{
    public void transfer(
        IAccount from,
        IAccount to,
        double amount,
        String pin) throws AccountException
    {
        // STEP 1: Check that both accounts exist
        if(from == null || to == null)
        {
            throw new AccountException(
                "Source and destination accounts are required"
            );
        }

        // STEP 2: Check that both accounts are active
        if(!"ACTIVE".equalsIgnoreCase(from.get_status()) ||
           !"ACTIVE".equalsIgnoreCase(to.get_status()))
        {
            throw new InactiveAccountException(
                "Both accounts must be active to transfer funds"
            );
        }

        // Use the existing AbstractAccount implementation.
        if(!(from instanceof AbstractAccount))
        {
            throw new AccountException(
                "Unsupported source account implementation"
            );
        }

        AbstractAccount source = (AbstractAccount) from;

        // STEP 3: Verify the sender's PIN
        if(!source.verifyPin(pin))
        {
            throw new InvalidPinException("Incorrect PIN");
        }

        // STEP 4: Check the available balance
        if(!source.canWithdraw(amount))
        {
            throw new InsufficientBalanceException(
                "Insufficient balance for transfer of Rs. " + amount
            );
        }

        // STEP 5: Check the daily transfer limit
        source.resetDailyTransferIfNeeded();

        if(!source.canTransfer(amount))
        {
            double remaining =
                source.getRemainingDailyTransferLimit();

            throw new AccountException(
                "Daily transfer limit exceeded. Remaining today: Rs. "
                + remaining
            );
        }

        // STEP 6: Debit the sender
        from.withdraw(amount, pin);

        // STEP 7: Credit the receiver
        to.deposit(amount);

        // STEP 8: Record the successful transfer
        source.updateDailyTransferTotal(amount);
    }

// Activity 16 - STEP 9: Transfer and return a transaction record
public Transaction transferWithTransaction(
    IAccount from,
    IAccount to,
    double amount,
    int pin) throws AccountException
{
    // Reuse the existing transfer workflow
    transfer(from, to, amount, String.valueOf(pin));

    AbstractAccount source = (AbstractAccount) from;
    AbstractAccount destination = (AbstractAccount) to;

    return new Transaction(
        Transaction.generateId(),
        java.time.LocalDateTime.now(),
        source.get_account_number(),
        TransactionType.TRANSFER,
        amount,
        source.get_balance(),
        "SUCCESS",
        "Transfer of Rs. " + amount
            + " to Account #" + destination.get_account_number(),
        source.get_account_number(),
        destination.get_account_number()
    );
}
}
