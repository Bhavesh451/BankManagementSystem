
package com.bank;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDAO {

    public long createAccount(Account account) throws SQLException {
        String sql = """
                INSERT INTO accounts
                    (customer_name, account_type, balance)
                VALUES (?, ?, ?)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, account.getCustomerName());
            ps.setString(2, account.getAccountType());
            ps.setBigDecimal(3,
                    BigDecimal.valueOf(account.getBalance()));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }

        throw new SQLException("Account creation failed.");
    }

    public void deposit(long accountNumber, double amount)
            throws SQLException {
        validateAmount(amount);

        String updateSql = """
                UPDATE accounts SET balance = balance + ?
                WHERE account_number = ?
                """;

        String transactionSql = """
                INSERT INTO transactions
                    (account_number, transaction_type, amount)
                VALUES (?, 'DEPOSIT', ?)
                """;

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                    ps.setBigDecimal(1, BigDecimal.valueOf(amount));
                    ps.setLong(2, accountNumber);
                    if (ps.executeUpdate() != 1)
                        throw new SQLException("Account not found.");
                }

                try (PreparedStatement ps =
                             con.prepareStatement(transactionSql)) {
                    ps.setLong(1, accountNumber);
                    ps.setBigDecimal(2, BigDecimal.valueOf(amount));
                    ps.executeUpdate();
                }

                con.commit();
            } catch (SQLException | RuntimeException e) {
                try { con.rollback(); }
                catch (SQLException ex) { e.addSuppressed(ex); }
                throw e;
            }
        }
    }

    public void withdraw(long accountNumber, double amount)
            throws SQLException {
        validateAmount(amount);

        String updateSql = """
                UPDATE accounts
                SET balance = balance - ?
                WHERE account_number = ? AND balance >= ?
                """;

        String transactionSql = """
                INSERT INTO transactions
                    (account_number, transaction_type, amount)
                VALUES (?, 'WITHDRAWAL', ?)
                """;

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                    BigDecimal value = BigDecimal.valueOf(amount);
                    ps.setBigDecimal(1, value);
                    ps.setLong(2, accountNumber);
                    ps.setBigDecimal(3, value);

                    if (ps.executeUpdate() != 1) {
                        if (!accountExists(con, accountNumber))
                            throw new SQLException("Account not found.");
                        throw new SQLException("Insufficient balance.");
                    }
                }

                try (PreparedStatement ps =
                             con.prepareStatement(transactionSql)) {
                    ps.setLong(1, accountNumber);
                    ps.setBigDecimal(2, BigDecimal.valueOf(amount));
                    ps.executeUpdate();
                }

                con.commit();
            } catch (SQLException | RuntimeException e) {
                try { con.rollback(); }
                catch (SQLException ex) { e.addSuppressed(ex); }
                throw e;
            }
        }
    }

    public BigDecimal getBalance(long accountNumber) throws SQLException {
        String sql = """
                SELECT balance FROM accounts
                WHERE account_number = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, accountNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal("balance");
            }
        }

        throw new SQLException("Account not found.");
    }

    // Feature 5: Transaction history
    public void showTransactionHistory(long accountNumber)
            throws SQLException {

        String sql = """
                SELECT transaction_id, transaction_type,
                       amount, transaction_time
                FROM transactions
                WHERE account_number = ?
                ORDER BY transaction_time DESC, transaction_id DESC
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, accountNumber);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("\n===== TRANSACTION HISTORY =====");
                System.out.printf("%-8s %-12s %-12s %s%n",
                        "ID", "TYPE", "AMOUNT", "DATE");

                boolean found = false;

                while (rs.next()) {
                    found = true;
                    System.out.printf("%-8d %-12s ₹%-11s %s%n",
                            rs.getLong("transaction_id"),
                            rs.getString("transaction_type"),
                            rs.getBigDecimal("amount").toPlainString(),
                            rs.getTimestamp("transaction_time"));
                }

                if (!found) {
                    System.out.println("No transactions found.");
                }
            }
        }
    }

    private boolean accountExists(Connection con, long accountNumber)
            throws SQLException {
        String sql = """
                SELECT account_number FROM accounts
                WHERE account_number = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void validateAmount(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero.");
        }
    }
}
