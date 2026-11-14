
package com.gdb.command;

import com.gdb.domain.AbstractAccount;
import com.gdb.domain.Transaction;
import com.gdb.service.TransferService;

public class TransferCommand implements TransactionCommand
{
    private static final long serialVersionUID = 1L;

    private final AbstractAccount from;
    private final AbstractAccount to;
    private final double amount;
    private final int pin;

    private Transaction transaction;

    public TransferCommand(
        AbstractAccount from,
        AbstractAccount to,
        double amount,
        int pin)
    {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.pin = pin;
    }

    @Override
    public void execute() throws Exception
    {
        TransferService service = new TransferService();

        transaction = service.transferWithTransaction(
            from, to, amount, pin
        );
    }

    @Override
    public Transaction getTransaction()
    {
        return transaction;
    }
}
