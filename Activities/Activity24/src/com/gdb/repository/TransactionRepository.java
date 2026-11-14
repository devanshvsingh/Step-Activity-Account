
package com.gdb.repository;

import com.gdb.domain.Transaction;
import java.util.List;

public interface TransactionRepository {

    void save(Transaction transaction);

    List<Transaction> findByAccount(int accountNumber);

    List<Transaction> findAll();

    void clear();

    default void transfer(
        int fromAccountNumber,
        int toAccountNumber,
        double amount
    ) {
        throw new UnsupportedOperationException(
            "Transfer is not supported by this repository"
        );
    }
}
