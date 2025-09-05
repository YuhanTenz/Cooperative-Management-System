package org.example.ver2.Transaction;

import java.util.List;

public interface TransactionDao {
    boolean recordTransaction(Transaction transaction);

    boolean updateTransaction(Transaction transaction);

    List<Transaction> getTransactionsForMember(String accountNumber);

    double getTotalBalance(String accountNumber);
}
