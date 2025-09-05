package org.example.ver2.Transaction;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    private int id;
    private String accountNumber;
    private double amount;
    private String type;
    private LocalDateTime timestamp;

}
