
package com.gdb.service;

import com.gdb.domain.*;
import com.gdb.command.*;
import com.gdb.logging.TransactionLogger;
import com.gdb.exceptions.*;
import com.gdb.repository.*;

import java.util.*;

public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionLogger logger;
    private final TransferService transferService;

    public AccountService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            TransactionLogger logger) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.logger = logger;
        this.transferService = new TransferService();
    }

    public AccountService(TransactionLogger logger) {
        this(
            RepositoryFactory.getAccountRepository(),
            RepositoryFactory.getTransactionRepository(),
            logger
        );
    }

    public IAccount openAccount(
            String type,
            String name,
            int age,
            double initialBalance) throws AccountException {

        int accountNumber = accountRepository.nextAccountNumber();

        IAccount account = AccountFactory.createAccount(
                type, accountNumber, name, age, initialBalance
        );

        accountRepository.save(account);
        return account;
    }

    public IAccount getAccount(int accountNumber) {
        return accountRepository.findById(accountNumber);
    }

    private AbstractAccount getAbstractAccount(int accountNumber)
            throws AccountException {

        IAccount account = getAccount(accountNumber);

        if (account == null) {
            throw new AccountException(
                    "Account not found: " + accountNumber
            );
        }

        if (!(account instanceof AbstractAccount)) {
            throw new AccountException(
                    "Unsupported account implementation"
            );
        }

        return (AbstractAccount) account;
    }

    public Transaction deposit(int accountNumber, double amount)
            throws Exception {

        AbstractAccount account = getAbstractAccount(accountNumber);

        if (!account.isActive()) {
            throw new InactiveAccountException("Account is not active");
        }

        DepositCommand command = new DepositCommand(account, amount);
        command.execute();

        accountRepository.update(account);

        if (command.getTransaction() != null) {
            transactionRepository.save(command.getTransaction());
        }

        if (logger != null) {
            logger.log(command);
        }

        return command.getTransaction();
    }

    public Transaction withdraw(
            int accountNumber, double amount, int pin)
            throws Exception {

        AbstractAccount account = getAbstractAccount(accountNumber);

        if (!account.isActive()) {
            throw new InactiveAccountException("Account is not active");
        }

        WithdrawCommand command =
                new WithdrawCommand(account, amount, pin);

        command.execute();

        accountRepository.update(account);

        if (command.getTransaction() != null) {
            transactionRepository.save(command.getTransaction());
        }

        if (logger != null) {
            logger.log(command);
        }

        return command.getTransaction();
    }

    public Transaction transfer(
            int fromAccountNumber,
            int toAccountNumber,
            double amount,
            int pin) throws Exception {

        AbstractAccount fromAccount =
                getAbstractAccount(fromAccountNumber);

        AbstractAccount toAccount =
                getAbstractAccount(toAccountNumber);

        TransferCommand command = new TransferCommand(
                fromAccount, toAccount, amount, pin
        );

        command.execute();

        accountRepository.update(fromAccount);
        accountRepository.update(toAccount);

        if (command.getTransaction() != null) {
            transactionRepository.save(command.getTransaction());
        }

        if (logger != null) {
            logger.log(command);
        }

        return command.getTransaction();
    }

    public void closeAccount(int accountNumber, int pin)
            throws AccountException {

        AbstractAccount account = getAbstractAccount(accountNumber);

        if (!account.verifyPin(String.valueOf(pin))) {
            throw new InvalidPinException("Incorrect PIN");
        }

        account.setStatus("CLOSED");
        accountRepository.update(account);
    }

    // Preserve the return type expected by the existing TestAccountService
    public List<Transaction> getTransactionHistory() {

        List<Transaction> history = new ArrayList<>();

        if (logger == null) {
            return history;
        }

        List<TransactionCommand> commands = logger.readAll();

        for (TransactionCommand command : commands) {
            if (command.getTransaction() != null) {
                history.add(command.getTransaction());
            }
        }

        return history;
    }

    public List<Transaction> getTransactionRecords() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionRecords(int accountNumber) {
        return transactionRepository.findByAccount(accountNumber);
    }

    public List<IAccount> getAllAccounts() {
        return accountRepository.findAll();
    }

    public int getNextAccountNumber() {
        return accountRepository.nextAccountNumber();
    }
}
