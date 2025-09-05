package org.example.ver2.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDaoImplementation implements TransactionDao {
    private static final String URL = "jdbc:mysql://localhost:3306/cooperative_management_system";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "";

    @Override
    public boolean recordTransaction(Transaction transaction) {
        String query = "INSERT INTO transaction(account_number, amount, type, timestamp) VALUES(?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {


             preparedStatement.setString(1, transaction.getAccountNumber());
             preparedStatement.setDouble(2, transaction.getAmount());
             preparedStatement.setString(3, transaction.getType());
             preparedStatement.setTimestamp(4, Timestamp.valueOf(transaction.getTimestamp()));

             int numberOfRowsInserted = preparedStatement.executeUpdate();

             return numberOfRowsInserted > 0;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    @Override
    public boolean updateTransaction(Transaction transaction) {

        String query = "UPDATE transaction SET account_number = ? WHERE id = ?";

        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, transaction.getAccountNumber());
            preparedStatement.setInt(2, transaction.getId());

            int numberOfRowsUpdated = preparedStatement.executeUpdate();

            if(numberOfRowsUpdated > 0) {
                return true;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public List<Transaction> getTransactionsForMember(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();

        String query = "SELECT * FROM transaction WHERE account_number = ?";

        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, accountNumber);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setId(resultSet.getInt("id"));
                    transaction.setAccountNumber(resultSet.getString("account_number"));
                    transaction.setAmount(resultSet.getDouble("amount"));
                    transaction.setType(resultSet.getString("type"));

                    Timestamp sqlTimestamp = resultSet.getTimestamp("timestamp");
                    if(sqlTimestamp != null) {
                        transaction.setTimestamp(sqlTimestamp.toLocalDateTime());
                    }
                    transactions.add(transaction);

                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public double getTotalBalance(String accountNumber) {
        String query = """
                SELECT SUM(CASE WHEN type = 'Deposit' THEN amount
                                WHEN type = 'Withdrawal' THEN -amount
                                ELSE 0 END) AS balance
                FROM transaction
                WHERE account_number = ?
                """;

        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, accountNumber);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getDouble("balance");
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
