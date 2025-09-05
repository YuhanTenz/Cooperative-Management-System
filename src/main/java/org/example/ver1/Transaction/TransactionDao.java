package org.example.ver1.Transaction;

import java.util.List;

/**
 * Data Access Object interface for Transaction operations
 * Defines the contract for transaction-related database operations
 */
public interface TransactionDao {

    /**
     * Inserts a new transaction into the database
     * @param transaction The transaction object to insert
     * @return true if insertion was successful, false otherwise
     */
    boolean insertTransaction(Transaction transaction);

    /**
     * Updates an existing transaction in the database
     * @param transaction The transaction object with updated information
     * @return true if update was successful, false otherwise
     */
    boolean updateTransaction(Transaction transaction);

    /**
     * Deletes a transaction from the database
     * @param transaction The transaction object to delete (requires ID)
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteTransaction(Transaction transaction);

    /**
     * Retrieves all transactions from the database
     * @return List of all transactions
     */
    List<Transaction> fetchTransactions();

    /**
     * Retrieves transactions for a specific member
     * @param memberId The ID of the member whose transactions to fetch
     * @return List of transactions for the specified member
     */
    List<Transaction> fetchTransactionsByMemberId(int memberId);

    /**
     * Retrieves transactions by type (DEPOSIT, WITHDRAWAL, LOAN, PAYMENT)
     * @param type The transaction type to filter by
     * @return List of transactions of the specified type
     */
    List<Transaction> fetchTransactionsByType(String type);

    /**
     * Retrieves a specific transaction by its ID
     * @param transactionId The ID of the transaction to retrieve
     * @return The transaction object if found, null otherwise
     */
    Transaction fetchTransactionById(int transactionId);

    /**
     * Calculates the total balance for a specific member
     * @param memberId The ID of the member
     * @return The total balance (deposits - withdrawals)
     */
    double calculateMemberBalance(int memberId);

    /**
     * Calculates the total balance for a member (deposits + payments - withdrawals - loans)
     * This gives the net financial position of the member
     * @param memberId The member ID
     * @return Total balance of the member
     */
    double calculateMemberTotalBalance(int memberId);

    /**
     * Gets the outstanding loan balance for a member
     * @param memberId The member ID
     * @return Outstanding loan balance
     */
    double getOutstandingLoanBalance(int memberId);

    /**
     * Retrieves transactions within a date range
     * @param startDate Start date in YYYY-MM-DD format
     * @param endDate End date in YYYY-MM-DD format
     * @return List of transactions within the specified date range
     */
    List<Transaction> fetchTransactionsByDateRange(String startDate, String endDate);
}