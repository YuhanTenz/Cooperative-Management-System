package org.example.ver2;
import java.time.LocalDate;

public class MemberApplication {
    public static void main(String[] args) {
//        MemberDao memberDao = new MemberDaoImplementation();
//
//        Member member = Member.builder()
//                .accountNumber("A04B4")
//                .firstName("Lloyd")
//                .middleName("Nostradamus")
//                .lastName("Dwight")
//                .address("Roman, Italy")
//                .birthdate("1950-10-12");
//                .build();
//
//        boolean result = memberDao.insertMember(member);
//
//        if(result) {
//            System.out.print("Successfully inserted!");
//        } else {
//            System.out.print("insert data failed!");
//        }

      //FETCH ALL MEMBERS
//        MemberDao memberDao = new MemberDaoImplementation();
//
//        List<Member> memberList = memberDao.fetchMembers();
//
//        for (Member member : memberList) {
//            System.out.println("ID: " + member.getId());
//            System.out.println("Account Number " + member.getAccountNumber());
//            System.out.println("First name: " + member.getFirstName());
//            System.out.println("Middle name: " + member.getMiddleName());
//            System.out.println("Last name: " + member.getLastName());
//            System.out.println("Address: " + member.getAddress());
//            System.out.println("Birthdate: " + member.getBirthdate());
//            System.out.println("*******************");
//        }

        // FETCH SPECIFIC MEMBER
//        MemberDao memberDao = new MemberDaoImplementation();
//        Member member = memberDao.fetchSpecificMember(2);
//
//            System.out.println("ID: " + member.getId());
//            System.out.println("Account Number: " + member.getAccountNumber());
//            System.out.println("First name: " + member.getFirstName());
//            System.out.println("Middle name: " + member.getMiddleName());
//            System.out.println("Last name: " + member.getLastName());
//            System.out.println("Address: " + member.getAddress());
//            System.out.println("Birthdate: " + member.getBirthdate());
//            System.out.println("*******************");

        //DELETE MEMBER
//        MemberDao memberDao = new MemberDaoImplementation();
//
//        Member member = Member.builder()
//                .id(6)
//                .accountNumber("AAA-08")
//                .firstName("Lloyd")
//                .middleName("Nostradamus")
//                .lastName("Dwight")
//                .address("Roman, Italy")
//                .birthdate("1950-10-12")
//                .build();
//
//        boolean result = memberDao.deleteMember(member);
//
//        if(result) {
//            System.out.print("Successfully deleted!");
//        } else {
//            System.out.print("delete data failed!");
//        }

        // UPDATE MEMBER
        MemberDao memberDao = new MemberDaoImplementation();

        Member member = Member.builder()
                .id(4)
                .accountNumber("A05B5")
                .firstName("Stephen Vaughn")
                .middleName("Miranda")
                .lastName("Bartolome")
                .address("Ontario, Canada")
                .birthdate(LocalDate.of(2002, 5, 10))
                .age(25)
                .build();

        boolean result = memberDao.updateMember(member);

        if(result) {
            System.out.print("Successfully updated!");
        } else {
            System.out.print("updating failed");
        }
    }


}

