package org.example.ver2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class MemberDaoImplementation implements MemberDao {
    private static final String URL = "jdbc:mysql://localhost:3306/cooperative_management_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    @Override
    public boolean insertMember(Member member) {

            String query = "INSERT INTO member(account_number, first_name, middle_name, last_name, address, birthdate, age) VALUES(?, ?, ?, ?, ?, ?, ?)";


            try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(query);) {


                preparedStatement.setString(1, member.getAccountNumber());
                preparedStatement.setString(2, member.getFirstName());
                preparedStatement.setString(3, member.getMiddleName());
                preparedStatement.setString(4, member.getLastName());
                preparedStatement.setString(5, member.getAddress());
                preparedStatement.setDate(6, java.sql.Date.valueOf(member.getBirthdate()));
                preparedStatement.setInt(7, member.getAge());

                int numberOfRowsInserted = preparedStatement.executeUpdate();

                if(numberOfRowsInserted > 0) {
                    return true;
                }

            } catch(Exception e) {
                e.printStackTrace();
            }

        return false;
    }

    @Override
    public boolean deleteMember(Member member) {

        String query = "DELETE FROM member WHERE id = ?";


        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query);) {


            preparedStatement.setInt(1, member.getId());

            int numberOfRowsDeleted = preparedStatement.executeUpdate();

            if(numberOfRowsDeleted > 0) {
                return true;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateMember(Member member) {

        String query = "UPDATE member SET account_number = ?, first_name = ?, middle_name = ?, last_name = ?, address = ?, birthdate = ?, age = ? WHERE id = ?";


        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query);) {

            preparedStatement.setString(1, member.getAccountNumber());
            preparedStatement.setString(2, member.getFirstName());
            preparedStatement.setString(3, member.getMiddleName());
            preparedStatement.setString(4, member.getLastName());
            preparedStatement.setString(5, member.getAddress());
            preparedStatement.setDate(6, java.sql.Date.valueOf(member.getBirthdate()));
            preparedStatement.setInt(7, member.getAge());
            preparedStatement.setInt(8, member.getId());

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
    public List<Member> fetchMembers() {
        String query = "SELECT * FROM member";
        List<Member> memberList = new ArrayList<>();


        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();) {



            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                Member member = new Member();

                member.setId(resultSet.getInt("id"));
                member.setAccountNumber(resultSet.getString("account_number"));
                member.setFirstName(resultSet.getString("first_name"));
                member.setMiddleName(resultSet.getString("middle_name"));
                member.setLastName(resultSet.getString("last_name"));
                member.setAddress(resultSet.getString("address"));
                member.setBirthdate(resultSet.getDate("birthdate").toLocalDate());
                member.setAge(resultSet.getInt("age"));

                memberList.add(member);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return memberList;
    }


    @Override
    public List<Member> getAllMembers() {
        String query = "SELECT * FROM member";
        List<Member> memberList = new ArrayList<>();


        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();) {



            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                Member member = new Member();

                member.setId(resultSet.getInt("id"));
                member.setAccountNumber(resultSet.getString("account_number"));
                member.setFirstName(resultSet.getString("first_name"));
                member.setMiddleName(resultSet.getString("middle_name"));
                member.setLastName(resultSet.getString("last_name"));
                member.setAddress(resultSet.getString("address"));
                member.setBirthdate(resultSet.getDate("birthdate").toLocalDate());
                member.setAge(resultSet.getInt("age"));

                memberList.add(member);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return memberList;
    }

    @Override
    public Member fetchSpecificMember(int id) {


            String query = "SELECT * FROM member WHERE id = ?";

            Member member = null;


            try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(query);) {

                preparedStatement.setInt(1, id);

                ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()) {
                    member = Member.builder()
                            .id(resultSet.getInt("id"))
                            .accountNumber(resultSet.getString("account_number"))
                            .firstName(resultSet.getString("first_name"))
                            .middleName(resultSet.getString("middle_name"))
                            .lastName(resultSet.getString("last_name"))
                            .address(resultSet.getString("address"))
                            .birthdate(resultSet.getDate("birthdate").toLocalDate())
                            .age(resultSet.getInt("age"))
                            .build();
                }

            } catch(Exception e) {
                e.printStackTrace();
            }
        return member;
    }
}