package org.example.ver1.Member;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Member {
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String birthdate;
}
