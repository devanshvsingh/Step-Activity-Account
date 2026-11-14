
package com.gdb.service;

import com.gdb.domain.*;
import com.gdb.command.*;
import com.gdb.logging.TransactionLogger;
import com.gdb.exceptions.*;

import java.util.*;

public class AccountService
{
    // STEP 1: Fields
    private Map<Integer, IAccount> accounts;
    private TransactionLogger logger;
    private TransferService transferService;
    private int nextAccountNumber = 1001;

    // STEP 2: Constructor
    public AccountService(TransactionLogger logger)
    {
        this.accounts = new HashMap<>();
        this.logger = logger;
        this.transferService = new TransferService();
    }

    // STEP 3: Open a new account
    public IAccount openAccount(
        String type,
        String name,
        int age,
        double initialBalance)
    {
        int accountNumber = nextAccountNumber++;

        AbstractAccount account = AccountFactory.createAccount(
            type,
            accountNumber,
            name,
            age,
            initialBalance
        );

        accounts.put(accountNumber, account);

        return account;
    }

    // STEP 4: Close an account
    public void closeAccount(int accountNumber, int pin)
        throws AccountException
    {
        AbstractAccount account = findAccount(accountNumber);

        if(!account.verifyPin(String.valueOf(pin)))
        {
            throw new InvalidPinException("Incorrect PIN");
        }

        account.setStatus("CLOSED");
    }

    // STEP 5: Deposit money
    public Transaction deposit(int accountNumber, double amount)
        throws Exception
    {
        AbstractAccount account = findAccount(accountNumber);

        if(!account.isActive())
        {
            throw new InactiveAccountException(
                "Account is not active"
            );
        }

        DepositCommand command =
            new DepositCommand(account, amount);

        command.execute();

        logger.log(command);

        return command.getTransaction();
    }

    // STEP 6: Withdraw money
    public Transaction withdraw(
        int accountNumber,
        double amount,
        int pin) throws Exception
    {
        AbstractAccount account = findAccount(accountNumber);

        if(!account.isActive())
        {
            throw new InactiveAccountException(
                "Account is not active"
            );
        }

        if(!account.verifyPin(String.valueOf(pin)))
        {
            throw new InvalidPinException("Incorrect PIN");
        }

        WithdrawCommand command =
            new WithdrawCommand(account, amount, pin);

        command.execute();

        logger.log(command);

        return command.getTransaction();
    }

    // STEP 7: Transfer money
    public Transaction transfer(
        int fromAcc,
        int toAcc,
        double amount,
        int pin) throws Exception
    {
        AbstractAccount from = findAccount(fromAcc);
        AbstractAccount to = findAccount(toAcc);

        TransferCommand command =
            new TransferCommand(from, to, amount, pin);

        command.execute();

        logger.log(command);

        return command.getTransaction();
    }

    // Helper method: Find account or throw exception
    private AbstractAccount findAccount(int accountNumber)
        throws AccountException
    {
        IAccount account = accounts.get(accountNumber);

        if(account == null)
        {
            throw new AccountException(
                "Account not found: " + accountNumber
            );
        }

        if(!(account instanceof AbstractAccount))
        {
            throw new AccountException(
                "Unsupported account implementation"
            );
        }

        return (AbstractAccount) account;
    }

    // STEP 8: Get an account
    public IAccount getAccount(int accountNumber)
        throws AccountException
    {
        return findAccount(accountNumber);
    }

    // STEP 9: Get all accounts
    public List<IAccount> getAllAccounts()
    {
        return new ArrayList<>(accounts.values());
    }

    // STEP 10: Get transaction history
    public List<Transaction> getTransactionHistory()
    {
        List<Transaction> history = new ArrayList<>();

        List<TransactionCommand> commands = logger.readAll();

        for(TransactionCommand command : commands)
        {
            if(command.getTransaction() != null)
            {
                history.add(command.getTransaction());
            }
        }

        return history;
    }

    // STEP 11: Get next account number
    public int getNextAccountNumber()
    {
        return nextAccountNumber;
    }
}
