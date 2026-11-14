
package com.gdb.repository;

import com.gdb.db.ConnectionProvider;
import com.gdb.domain.Transaction;
import com.gdb.domain.TransactionType;
import com.gdb.exceptions.DataAccessException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JdbcTransactionRepository
        implements TransactionRepository {

    private final ConnectionProvider provider;

    public JdbcTransactionRepository(ConnectionProvider provider) {
        this.provider = Objects.requireNonNull(provider);
    }

    // STEP 1: Save transaction
    @Override
    public void save(Transaction transaction) {

        if (transaction == null) {
            throw new IllegalArgumentException(
                "Transaction cannot be null"
            );
        }

        String sql =
            "INSERT INTO transactions " +
            "(id, timestamp, account_number, type, amount, " +
            "balance_after, status, remarks, from_account, to_account) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection conn = provider.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            bindTransaction(ps, transaction);
            ps.executeUpdate();

        } catch (SQLException e) {

            throw new DataAccessException(
                "Failed to save transaction", e
            );
        }
    }

    // Bind transaction values
    private void bindTransaction(
        PreparedStatement ps,
        Transaction t
    ) throws SQLException {

        ps.setString(1, t.getTransactionId());
        ps.setString(2, t.getTimestamp().toString());
        ps.setInt(3, t.getAccountNumber());
        ps.setString(4, t.getType().name());
        ps.setDouble(5, t.getAmount());
        ps.setDouble(6, t.getBalanceAfter());
        ps.setString(7, t.getStatus());
        ps.setString(8, t.getDescription());
        ps.setInt(9, t.getFromAccount());
        ps.setInt(10, t.getToAccount());
    }

    // STEP 2: Find transactions by account
    @Override
    public List<Transaction> findByAccount(int accountNumber) {

        String sql =
            "SELECT * FROM transactions " +
            "WHERE account_number = ? " +
            "OR from_account = ? " +
            "OR to_account = ? " +
            "ORDER BY timestamp ASC";

        List<Transaction> transactions = new ArrayList<>();

        try (
            Connection conn = provider.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, accountNumber);
            ps.setInt(2, accountNumber);
            ps.setInt(3, accountNumber);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    transactions.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                "Failed to find transactions", e
            );
        }

        return transactions;
    }

    // STEP 3: Find all transactions
    @Override
    public List<Transaction> findAll() {

        List<Transaction> transactions = new ArrayList<>();

        String sql =
            "SELECT * FROM transactions ORDER BY timestamp ASC";

        try (
            Connection conn = provider.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                transactions.add(mapRow(rs));
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                "Failed to retrieve transactions", e
            );
        }

        return transactions;
    }

    // STEP 4: Clear transaction table
    @Override
    public void clear() {

        String sql = "DELETE FROM transactions";

        try (
            Connection conn = provider.getConnection();
            Statement stmt = conn.createStatement()
        ) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {

            throw new DataAccessException(
                "Failed to clear transactions", e
            );
        }
    }

    // STEP 5: Atomic money transfer
    @Override
    public void transfer(
        int fromAccount,
        int toAccount,
        double amount
    ) {

        if (fromAccount == toAccount) {
            throw new IllegalArgumentException(
                "Source and destination must be different"
            );
        }

        if (amount <= 0 || !Double.isFinite(amount)) {
            throw new IllegalArgumentException(
                "Transfer amount must be positive"
            );
        }

        String readBalance =
            "SELECT balance FROM accounts WHERE account_number = ?";

        String debit =
            "UPDATE accounts SET balance = balance - ? " +
            "WHERE account_number = ? AND balance >= ?";

        String credit =
            "UPDATE accounts SET balance = balance + ? " +
            "WHERE account_number = ?";

        String audit =
            "INSERT INTO transactions " +
            "(id, timestamp, account_number, type, amount, " +
            "balance_after, status, remarks, from_account, to_account) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = provider.getConnection()) {

            // Disable automatic commit
            conn.setAutoCommit(false);

            try {

                double senderBalance;

                // Read sender balance
                try (
                    PreparedStatement ps =
                        conn.prepareStatement(readBalance)
                ) {

                    ps.setInt(1, fromAccount);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new DataAccessException(
                                "Sender account not found"
                            );
                        }

                        senderBalance = rs.getDouble("balance");
                    }
                }

                // Check receiver exists
                try (
                    PreparedStatement ps =
                        conn.prepareStatement(readBalance)
                ) {

                    ps.setInt(1, toAccount);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new DataAccessException(
                                "Receiver account not found"
                            );
                        }
                    }
                }

                // Debit sender
                try (
                    PreparedStatement ps =
                        conn.prepareStatement(debit)
                ) {

                    ps.setDouble(1, amount);
                    ps.setInt(2, fromAccount);
                    ps.setDouble(3, amount);

                    if (ps.executeUpdate() != 1) {
                        throw new DataAccessException(
                            "Insufficient balance"
                        );
                    }
                }

                // Credit receiver
                try (
                    PreparedStatement ps =
                        conn.prepareStatement(credit)
                ) {

                    ps.setDouble(1, amount);
                    ps.setInt(2, toAccount);

                    if (ps.executeUpdate() != 1) {
                        throw new DataAccessException(
                            "Unable to credit receiver"
                        );
                    }
                }

                // Create transfer audit record
                Transaction transaction = new Transaction(
                    Transaction.generateId(),
                    LocalDateTime.now(),
                    fromAccount,
                    TransactionType.TRANSFER,
                    amount,
                    senderBalance - amount,
                    "SUCCESS",
                    "Transfer from " + fromAccount +
                        " to " + toAccount,
                    fromAccount,
                    toAccount
                );

                // Insert audit record using SAME connection
                try (
                    PreparedStatement ps =
                        conn.prepareStatement(audit)
                ) {

                    bindTransaction(ps, transaction);
                    ps.executeUpdate();
                }

                // Commit all database changes
                conn.commit();

            } catch (Exception e) {

                // Rollback all changes if anything fails
                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }

                throw new DataAccessException(
                    "Transfer failed and was rolled back", e
                );
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                "Database transfer failed", e
            );
        }
    }

    // STEP 6: Convert database row into Transaction object
    private Transaction mapRow(ResultSet rs)
            throws SQLException {

        return new Transaction(
            rs.getString("id"),
            LocalDateTime.parse(rs.getString("timestamp")),
            rs.getInt("account_number"),
            TransactionType.valueOf(rs.getString("type")),
            rs.getDouble("amount"),
            rs.getDouble("balance_after"),
            rs.getString("status"),
            rs.getString("remarks"),
            rs.getInt("from_account"),
            rs.getInt("to_account")
        );
    }
}
