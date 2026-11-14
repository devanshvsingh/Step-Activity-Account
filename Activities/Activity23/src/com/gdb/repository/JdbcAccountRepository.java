package com.gdb.repository;

import com.gdb.db.ConnectionProvider;
import com.gdb.domain.AbstractAccount;
import com.gdb.domain.AccountFactory;
import com.gdb.domain.IAccount;
import com.gdb.exceptions.DataAccessException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcAccountRepository implements AccountRepository {

    private final ConnectionProvider provider;

    public JdbcAccountRepository(ConnectionProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException(
                "ConnectionProvider cannot be null"
            );
        }

        this.provider = provider;
    }

    // STEP 1: Save Account

    @Override
    public void save(IAccount account) {

        if (account == null) {
            throw new IllegalArgumentException(
                "Account cannot be null"
            );
        }

        AbstractAccount data = requireAbstractAccount(account);

        String sql = "INSERT INTO accounts "
                + "(account_number, account_holder_name, age, balance, "
                + "account_type, status, pin, opening_date, "
                + "overdraft_used, interest_earned) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection con = provider.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, account.get_account_number());
            ps.setString(2, account.get_customer_name());
            ps.setInt(3, data.getAge());
            ps.setDouble(4, account.get_balance());

            ps.setString(5,
                toDatabaseAccountType(account.get_account_type()));

            ps.setString(6, account.get_status());

            if (data.getPinValue() == null) {
                ps.setNull(7, Types.VARCHAR);
            } else {
                ps.setString(7, data.getPinValue());
            }

            ps.setString(8, LocalDate.now().toString());
            ps.setDouble(9, 0.0);
            ps.setDouble(10, 0.0);

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new DataAccessException(
                "Unable to save account", e
            );
        }
    }


    // STEP 2: Find Account By ID

    @Override
    public IAccount findById(int accountNumber) {

        String sql =
            "SELECT * FROM accounts WHERE account_number = ?";

        try (
            Connection con = provider.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, accountNumber);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapRow(rs);
                }

                return null;
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                "Unable to find account", e
            );
        }
    }


    // STEP 3: Find All Accounts

    @Override
    public List<IAccount> findAll() {

        List<IAccount> accounts = new ArrayList<>();

        String sql =
            "SELECT * FROM accounts ORDER BY account_number";

        try (
            Connection con = provider.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                accounts.add(mapRow(rs));
            }

            return accounts;

        } catch (SQLException e) {

            throw new DataAccessException(
                "Unable to retrieve accounts", e
            );
        }
    }


    // STEP 4: Update Account

    @Override
    public void update(IAccount account) {

        if (account == null) {
            throw new IllegalArgumentException(
                "Account cannot be null"
            );
        }

        AbstractAccount data = requireAbstractAccount(account);

        String sql =
            "UPDATE accounts SET balance = ?, status = ?, pin = ? "
            + "WHERE account_number = ?";

        try (
            Connection con = provider.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setDouble(1, account.get_balance());
            ps.setString(2, account.get_status());

            if (data.getPinValue() == null) {
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(3, data.getPinValue());
            }

            ps.setInt(4, account.get_account_number());

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new DataAccessException(
                "Unable to update account", e
            );
        }
    }


    // STEP 5: Delete Account

    @Override
    public void delete(int accountNumber) {

        String sql =
            "DELETE FROM accounts WHERE account_number = ?";

        try (
            Connection con = provider.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, accountNumber);

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new DataAccessException(
                "Unable to delete account", e
            );
        }
    }


    // STEP 6: Check Account Exists

    @Override
    public boolean exists(int accountNumber) {

        String sql =
            "SELECT 1 FROM accounts WHERE account_number = ?";

        try (
            Connection con = provider.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, accountNumber);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next();
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                "Unable to check account existence", e
            );
        }
    }


    // STEP 7: Generate Next Account Number

    @Override
    public int nextAccountNumber() {

        String sql =
            "SELECT COALESCE(MAX(account_number), 1000) + 1 "
            + "AS next_id FROM accounts";

        try (
            Connection con = provider.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getInt("next_id");
            }

            return 1001;

        } catch (SQLException e) {

            throw new DataAccessException(
                "Unable to generate account number", e
            );
        }
    }


    // STEP 8: Convert ResultSet Row Into Account Object

    public IAccount mapRow(ResultSet rs) throws SQLException {

        int accountNumber =
            rs.getInt("account_number");

        String name =
            rs.getString("account_holder_name");

        int age =
            rs.getInt("age");

        double balance =
            rs.getDouble("balance");

        String type =
            rs.getString("account_type");

        String status =
            rs.getString("status");

        String pin =
            rs.getString("pin");

        try {

            AbstractAccount account =
                AccountFactory.createAccount(
                    fromDatabaseAccountType(type),
                    accountNumber,
                    name,
                    Math.max(18, age),
                    balance
                );

            if (pin != null && !pin.trim().isEmpty()) {
                account.setPin(pin);
            }

            if (status != null) {
                account.setStatus(status);
            }

            return account;

        } catch (RuntimeException e) {

            throw new SQLException(
                "Unable to map database row", e
            );
        }
    }


    // Helper Methods

    private AbstractAccount requireAbstractAccount(IAccount account) {

        if (!(account instanceof AbstractAccount)) {

            throw new IllegalArgumentException(
                "Unsupported account implementation"
            );
        }

        return (AbstractAccount) account;
    }


    private String toDatabaseAccountType(String type) {

        String value = type.trim().toUpperCase();

        if (value.equals("SAVINGSACCOUNT")
                || value.equals("SAVINGS")) {
            return "SAVINGS";
        }

        if (value.equals("CURRENTACCOUNT")
                || value.equals("CURRENT")) {
            return "CURRENT";
        }

        if (value.equals("FIXEDDEPOSITACCOUNT")
                || value.equals("FIXED_DEPOSIT")
                || value.equals("FD")) {
            return "FIXED_DEPOSIT";
        }

        if (value.equals("SALARYACCOUNT")
                || value.equals("SALARY")) {
            return "SALARY";
        }

        throw new IllegalArgumentException(
            "Unsupported account type: " + type
        );
    }


    private String fromDatabaseAccountType(String type) {

        return toDatabaseAccountType(type);
    }
}