package org.example.ver1.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of TransactionDao interface
 * Handles all database operations for transactions with balance validation
 */
public class TransactionDaoImplementation implements TransactionDao {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/cooperative_management_system";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = ""; // Update with your database password

    // SQL Queries
    private static final String INSERT_TRANSACTION =
            "INSERT INTO transactions (member_id, amount, type, description, date) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_TRANSACTION =
            "UPDATE transactions SET member_id = ?, amount = ?, type = ?, description = ?, date = ? WHERE id = ?";

    private static final String DELETE_TRANSACTION =
            "DELETE FROM transactions WHERE id = ?";

    private static final String SELECT_ALL_TRANSACTIONS =
            "SELECT * FROM transactions ORDER BY date DESC, id DESC";

    private static final String SELECT_TRANSACTIONS_BY_MEMBER =
            "SELECT * FROM transactions WHERE member_id = ? ORDER BY date DESC, id DESC";

    private static final String SELECT_TRANSACTIONS_BY_TYPE =
            "SELECT * FROM transactions WHERE type = ? ORDER BY date DESC, id DESC";

    private static final String SELECT_TRANSACTION_BY_ID =
            "SELECT * FROM transactions WHERE id = ?";

    private static final String SELECT_TRANSACTIONS_BY_DATE_RANGE =
            "SELECT * FROM transactions WHERE date BETWEEN ? AND ? ORDER BY date DESC, id DESC";

    private static final String CALCULATE_MEMBER_BALANCE =
            "SELECT " +
                    "COALESCE(SUM(CASE WHEN type IN ('DEPOSIT', 'PAYMENT') THEN amount ELSE 0 END), 0) - " +
                    "COALESCE(SUM(CASE WHEN type IN ('WITHDRAWAL', 'LOAN') THEN amount ELSE 0 END), 0) AS balance " +
                    "FROM transactions WHERE member_id = ?";

    private static final String GET_MEMBER_OUTSTANDING_LOAN_BALANCE =
            "SELECT " +
                    "COALESCE(SUM(CASE WHEN type = 'LOAN' THEN amount ELSE 0 END), 0) - " +
                    "COALESCE(SUM(CASE WHEN type = 'PAYMENT' THEN amount ELSE 0 END), 0) AS loan_balance " +
                    "FROM transactions WHERE member_id = ?";

    private static final String CALCULATE_MEMBER_TOTAL_BALANCE =
            "SELECT " +
                    "COALESCE(SUM(CASE WHEN type IN ('DEPOSIT') THEN amount ELSE 0 END), 0) + " +
                    "COALESCE(SUM(CASE WHEN type IN ('PAYMENT') THEN amount ELSE 0 END), 0) - " +
                    "COALESCE(SUM(CASE WHEN type IN ('WITHDRAWAL') THEN amount ELSE 0 END), 0) - " +
                    "COALESCE(SUM(CASE WHEN type IN ('LOAN') THEN amount ELSE 0 END), 0) AS total_balance " +
                    "FROM transactions WHERE member_id = ?";

    /**
     * Establishes database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }

    /**
     * Validates if a transaction can be performed based on member's balance
     * @param transaction The transaction to validate
     * @return ValidationResult containing validation status and message
     */
    public ValidationResult validateTransaction(Transaction transaction) {
        String type = transaction.getType().toUpperCase();
        double amount = transaction.getAmount();
        int memberId = transaction.getMemberId();

        // Only validate WITHDRAWAL and PAYMENT transactions
        if (!type.equals("WITHDRAWAL") && !type.equals("PAYMENT")) {
            return new ValidationResult(true, "Transaction type does not require balance validation");
        }

        double currentBalance = calculateMemberBalance(memberId);

        if (type.equals("WITHDRAWAL")) {
            if (currentBalance < amount) {
                return new ValidationResult(false,
                        String.format("Insufficient balance. Current balance: $%.2f, Withdrawal amount: $%.2f",
                                currentBalance, amount));
            }
        } else if (type.equals("PAYMENT")) {
            // For loan payments, check if member has sufficient balance
            if (currentBalance < amount) {
                return new ValidationResult(false,
                        String.format("Insufficient balance for loan payment. Current balance: $%.2f, Payment amount: $%.2f",
                                currentBalance, amount));
            }

            // Additional check: ensure payment doesn't exceed outstanding loan balance
            double outstandingLoanBalance = getOutstandingLoanBalance(memberId);
            if (amount > outstandingLoanBalance) {
                return new ValidationResult(false,
                        String.format("Payment amount exceeds outstanding loan balance. Outstanding loan: $%.2f, Payment amount: $%.2f",
                                outstandingLoanBalance, amount));
            }
        }

        return new ValidationResult(true, "Transaction validated successfully");
    }

    @Override
    public boolean insertTransaction(Transaction transaction) {
        // Validate transaction before inserting
        ValidationResult validation = validateTransaction(transaction);
        if (!validation.isValid()) {
            System.err.println("Transaction validation failed: " + validation.getMessage());
            return false;
        }

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_TRANSACTION)) {

            statement.setInt(1, transaction.getMemberId());
            statement.setDouble(2, transaction.getAmount());
            statement.setString(3, transaction.getType());
            statement.setString(4, transaction.getDescription());
            statement.setString(5, transaction.getDate());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateTransaction(Transaction transaction) {
        // Validate transaction before updating
        ValidationResult validation = validateTransaction(transaction);
        if (!validation.isValid()) {
            System.err.println("Transaction validation failed: " + validation.getMessage());
            return false;
        }

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TRANSACTION)) {

            statement.setInt(1, transaction.getMemberId());
            statement.setDouble(2, transaction.getAmount());
            statement.setString(3, transaction.getType());
            statement.setString(4, transaction.getDescription());
            statement.setString(5, transaction.getDate());
            statement.setInt(6, transaction.getId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteTransaction(Transaction transaction) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_TRANSACTION)) {

            statement.setInt(1, transaction.getId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Transaction> fetchTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TRANSACTIONS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Transaction transaction = mapResultSetToTransaction(resultSet);
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching transactions: " + e.getMessage());
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public List<Transaction> fetchTransactionsByMemberId(int memberId) {
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_TRANSACTIONS_BY_MEMBER)) {

            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction transaction = mapResultSetToTransaction(resultSet);
                    transactions.add(transaction);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching transactions by member ID: " + e.getMessage());
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public List<Transaction> fetchTransactionsByType(String type) {
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_TRANSACTIONS_BY_TYPE)) {

            statement.setString(1, type);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction transaction = mapResultSetToTransaction(resultSet);
                    transactions.add(transaction);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching transactions by type: " + e.getMessage());
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public Transaction fetchTransactionById(int transactionId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_TRANSACTION_BY_ID)) {

            statement.setInt(1, transactionId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToTransaction(resultSet);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching transaction by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public double calculateMemberBalance(int memberId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(CALCULATE_MEMBER_BALANCE)) {

            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("balance");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error calculating member balance: " + e.getMessage());
            e.printStackTrace();
        }

        return 0.0;
    }

    @Override
    public double calculateMemberTotalBalance(int memberId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(CALCULATE_MEMBER_TOTAL_BALANCE)) {

            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("total_balance");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error calculating member total balance: " + e.getMessage());
            e.printStackTrace();
        }

        return 0.0;
    }

    /**
     * Gets the outstanding loan balance for a member
     * This calculates the difference between total loans taken and payments made
     * @param memberId The member ID
     * @return Outstanding loan balance (always non-negative)
     */
    @Override
    public double getOutstandingLoanBalance(int memberId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_MEMBER_OUTSTANDING_LOAN_BALANCE)) {

            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double loanBalance = resultSet.getDouble("loan_balance");
                    // Ensure non-negative balance - if payments exceed loans, return 0
                    return Math.max(loanBalance, 0.0);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error calculating outstanding loan balance for member ID " + memberId + ": " + e.getMessage());
            e.printStackTrace();
        }

        // Return 0.0 if there's an error or no data found
        return 0.0;
    }

    @Override
    public List<Transaction> fetchTransactionsByDateRange(String startDate, String endDate) {
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_TRANSACTIONS_BY_DATE_RANGE)) {

            statement.setString(1, startDate);
            statement.setString(2, endDate);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction transaction = mapResultSetToTransaction(resultSet);
                    transactions.add(transaction);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching transactions by date range: " + e.getMessage());
            e.printStackTrace();
        }

        return transactions;
    }

    /**
     * Maps a ResultSet row to a Transaction object
     * @param resultSet The ResultSet containing transaction data
     * @return Transaction object
     * @throws SQLException if there's an error accessing the ResultSet
     */
    private Transaction mapResultSetToTransaction(ResultSet resultSet) throws SQLException {
        return Transaction.builder()
                .id(resultSet.getInt("id"))
                .memberId(resultSet.getInt("member_id"))
                .amount(resultSet.getDouble("amount"))
                .type(resultSet.getString("type"))
                .description(resultSet.getString("description"))
                .date(resultSet.getString("date"))
                .build();
    }

    /**
     * Test method to verify database connection
     * @return true if connection is successful, false otherwise
     */
    public boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Creates the transactions table if it doesn't exist
     * This method can be called during application startup
     */
    public void createTransactionsTableIfNotExists() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS transactions (
                id INT AUTO_INCREMENT PRIMARY KEY,
                member_id INT NOT NULL,
                amount DECIMAL(15,2) NOT NULL,
                type ENUM('DEPOSIT', 'WITHDRAWAL', 'LOAN', 'PAYMENT') NOT NULL,
                description TEXT,
                date DATE NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                INDEX idx_member_id (member_id),
                INDEX idx_type (type),
                INDEX idx_date (date),
                FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
            )
            """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(createTableSQL);
            System.out.println("Transactions table created successfully or already exists.");

        } catch (SQLException e) {
            System.err.println("Error creating transactions table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inner class to represent validation results
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}