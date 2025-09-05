package org.example.archive;

import org.example.ver1.Member.Member;
import org.example.ver1.Member.MemberDao;
import org.example.ver1.Member.MemberDaoImplementation;

public class MemberApplication {
    public static void main(String[] args) {
//        MemberDao memberDao = new MemberDaoImplementation();
//
//        Member member = Member.builder()
//                .firstName("Lloyd")
//                .middleName("Nostradamus")
//                .lastName("Dwight")
//                .address("Roman, Italy")
//                .birthdate("1950-10-12")
//                .build();
//
//        boolean result = memberDao.insertMember(member);
//
//        if(result) {
//            System.out.print("Successfully inserted!");
//        } else {
//            System.out.print("insert data failed!");
//        }

///       FETCH ALL MEMBERS
//        MemberDao memberDao = new MemberDaoImplementation();
//
//        List<Member> memberList = memberDao.fetchMembers();
//
//        for (Member member : memberList) {
//            System.out.println("ID: " + member.getId());
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
//                .id(1)
//                .build();
//
//        boolean result = memberDao.deleteMember(member);
//
//        if(result) {
//            System.out.print("Member Deleted Successfully!");
//        } else {
//            System.out.print("Member Not Found!");
//        }

        // UPDATE MEMBER
        MemberDao memberDao = new MemberDaoImplementation();

        Member member = Member.builder()
                .id(2)
                .firstName("Stephen Vaughn")
                .middleName("Miranda")
                .lastName("Bartolome")
                .address("Ontario, Canada")
                .birthdate("2002-05-10")
                .build();

        boolean result = memberDao.updateMember(member);

        if(result) {
            System.out.print("Successfully updated!");
        } else {
            System.out.print("updating failed");
        }
    }


}

