package org.example.ver2.Transaction;

public class TransactionService {
    public static void main(String[] args) {
//        TransactionDao transactionDao = new TransactionDaoImplementation();
//
//        Transaction transaction = Transaction.builder()
//                .accountNumber("A01B1")
//                .amount(9000.00)
//                .type("Savings")
//                .build();
//
//        boolean result = transactionDao.recordTransaction(transaction);
//
//        if(result) {
//            System.out.println("Recorded successfully!");
//        } else {
//            System.out.println("Record Failed!");
//        }

        TransactionDao transactionDao = new TransactionDaoImplementation();

        Transaction transaction = Transaction.builder()
                .accountNumber("A02B2")
                .id(2)
                .build();

        boolean result = transactionDao.updateTransaction(transaction);

        if(result) {
            System.out.println("Updated transaction successfully!");
        } else {
            System.out.println("Update failed!");
        }

    }
}
