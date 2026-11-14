
package com.gdb.command;

import com.gdb.domain.AbstractAccount;
import com.gdb.domain.Transaction;

public class WithdrawCommand implements TransactionCommand
{
    private static final long serialVersionUID = 1L;

    private final AbstractAccount account;
    private final double amount;
    private final int pin;
    private Transaction transaction;

    public WithdrawCommand(
        AbstractAccount account,
        double amount,
        int pin)
    {
        this.account = account;
        this.amount = amount;
        this.pin = pin;
    }

    @Override
    public void execute() throws Exception
    {
        transaction = account.withdrawWithTransaction(amount, pin);
    }

    @Override
    public Transaction getTransaction()
    {
        return transaction;
    }
}
