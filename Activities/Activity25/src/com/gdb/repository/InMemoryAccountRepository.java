package com.gdb.repository;

import com.gdb.domain.IAccount;
import java.util.*;

public class InMemoryAccountRepository implements AccountRepository {

    private final Map<Integer, IAccount> accounts = new HashMap<>();
    private int nextAccountNumber = 1001;

    @Override
    public synchronized void save(IAccount account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        accounts.put(account.get_account_number(), account);
    }

    @Override
    public synchronized IAccount findById(int accountNumber) {
        return accounts.get(accountNumber);
    }

    @Override
    public synchronized List<IAccount> findAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public synchronized void update(IAccount account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        int number = account.get_account_number();

        if (accounts.containsKey(number)) {
            accounts.put(number, account);
        }
    }

    @Override
    public synchronized void delete(int accountNumber) {
        accounts.remove(accountNumber);
    }

    @Override
    public synchronized boolean exists(int accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    @Override
    public synchronized int nextAccountNumber() {
        return nextAccountNumber++;
    }
}