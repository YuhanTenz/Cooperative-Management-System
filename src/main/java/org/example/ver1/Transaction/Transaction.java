package org.example.ver1.Transaction;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Transaction {
    private int id;
    private int memberId;
    private Double amount;
    private String type;
    private String description;
    private String date;
    private String birthdate;
}