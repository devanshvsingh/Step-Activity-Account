
package com.gdb.repository;

import com.gdb.db.ConnectionProvider;
import com.gdb.domain.*;
import com.gdb.exceptions.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcAccountRepository implements AccountRepository {

    private final ConnectionProvider provider;

    public JdbcAccountRepository(ConnectionProvider provider) {
        this.provider = provider;
    }

    // Save account
    @Override
    public void save(IAccount account) {

        if (account == null) {
            throw new IllegalArgumentException(
                "Account cannot be null"
            );
        }

        String sql =
            "INSERT INTO accounts " +
            "(account_number, customer_name, age, balance, " +
            "account_type, status, pin) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection conn = provider.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, account.get_account_number());
            ps.setString(2, account.get_customer_name());

            // Activity 23 IAccount has no age getter
            ps.setInt(3, 18);

            ps.setDouble(4, account.get_balance());
            ps.setString(5, account.get_account_type());
            ps.setString(6, account.get_status());

            // Activity 23 IAccount has no PIN getter
            ps.setString(7, null);

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new DataAccessException(
                "Failed to save account", e
            );
        }
    }

    // Find account by ID
    @Override
    public IAccount findById(int accountNumber) {

        String sql =
            "SELECT * FROM accounts WHERE account_number = ?";

        try (
            Connection conn = provider.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, accountNumber);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                "Failed to find account", e
            );
        }

        return null;
    }

    // Find all accounts
    @Override
    public List<IAccount> findAll() {

        List<IAccount> accounts = new ArrayList<>();

        String sql = "SELECT * FROM accounts";

        try (
            Connection conn = provider.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {

            while (rs.next()) {
                accounts.add(mapRow(rs));
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                "Failed to retrieve accounts", e
            );
        }

        return accounts;
    }

    // Update account
    @Override
    public void update(IAccount account) {

        if (account == null) {
            throw new IllegalArgumentException(
                "Account cannot be null"
            );
        }

        String sql =
            "UPDATE accounts SET customer_name = ?, " +
            "balance = ?, status = ? WHERE account_number = ?";

        try (
            Connection conn = provider.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, account.get_customer_name());
            ps.setDouble(2, account.get_balance());
            ps.setString(3, account.get_status());
            ps.setInt(4, account.get_account_number());

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new DataAccessException(
                "Failed to update account", e
            );
        }
    }

    // Delete account
    @Override
    public void delete(int accountNumber) {

        String sql =
            "DELETE FROM accounts WHERE account_number = ?";

        try (
            Connection conn = provider.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, accountNumber);
            ps.executeUpdate();

        } catch (SQLException e) {

            throw new DataAccessException(
                "Failed to delete account", e
            );
        }
    }

    // Check account existence
    @Override
    public boolean exists(int accountNumber) {

        return findById(accountNumber) != null;
    }

    // Generate next account number
    @Override
    public int nextAccountNumber() {

        String sql =
            "SELECT COALESCE(MAX(account_number), 1000) + 1 " +
            "FROM accounts";

        try (
            Connection conn = provider.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                "Failed to generate account number", e
            );
        }

        return 1001;
    }

    // Convert database row to account object
    private IAccount mapRow(ResultSet rs)
            throws SQLException {

        int accountNumber = rs.getInt("account_number");
        String name = rs.getString("customer_name");
        int age = Math.max(18, rs.getInt("age"));
        double balance = rs.getDouble("balance");

        String type = rs.getString("account_type");

        // Convert class names into AccountFactory types
        if (type.endsWith("Account")) {
            type = type.substring(
                0, type.length() - "Account".length()
            );
        }

        if ("FixedDeposit".equalsIgnoreCase(type)) {
            type = "FIXED_DEPOSIT";
        }

        AbstractAccount account =
            AccountFactory.createAccount(
                type,
                accountNumber,
                name,
                age,
                balance
            );

        account.setStatus(rs.getString("status"));

        return account;
    }
}
