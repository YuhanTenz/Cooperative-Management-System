package org.example.ver2;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Member {
    private int id;
    private String accountNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private LocalDate birthdate;
    private int age;
}
