package org.example.ver2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberService {
    private static final String URL = "jdbc:mysql://localhost:3306/cooperative_management_system";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "";


    public boolean deleteMember(int id) {
        String query = "DELETE FROM member WHERE id = ?";


        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query);) {


            preparedStatement.setInt(1, id);

            int numberOfRowsDeleted = preparedStatement.executeUpdate();

            return numberOfRowsDeleted > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateMember(Member member) {
        String query = "UPDATE member SET account_number = ?, first_name = ?, middle_name = ?, last_name = ?, address = ?, birthdate = ?, age =? WHERE id = ?";


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

            return numberOfRowsUpdated > 0;

        } catch(Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    public void fetchSpecificMember(int id) {
        String query = "SELECT * FROM member WHERE id = ?";


        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query);) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Account number: " + resultSet.getString("account_number"));
                System.out.println("First name: " + resultSet.getString("first_name"));
                System.out.println("Middle name: " + resultSet.getString("middle_name"));
                System.out.println("Last name: " + resultSet.getString("last_name"));
                System.out.println("Address: " + resultSet.getString("address"));
                System.out.println("Birthdate: " + resultSet.getDate("birthdate").toString());
                System.out.println("Age: " + resultSet.getInt("age"));
                System.out.println("*******************");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchMembers() {
        String query = "SELECT * FROM member";


        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();) {



            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Account number:" + resultSet.getString("account_number"));
                System.out.println("First name: " + resultSet.getString("first_name"));
                System.out.println("Middle name: " + resultSet.getString("middle_name"));
                System.out.println("Last name: " + resultSet.getString("last_name"));
                System.out.println("Address: " + resultSet.getString("address"));
                System.out.println("Birthdate: " + resultSet.getDate("birthdate").toString());
                System.out.println("Age: " + resultSet.getInt("age"));
                System.out.println("*******************");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void insertMember(Member member) {
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
                System.out.println("Successfully inserted!");
            } else {
                System.out.println("Inserting failed!");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isDuplicateMember(Member member) {
        String query = "SELECT COUNT(*) FROM member WHERE account_number = ? AND first_name = ? AND middle_name = ? AND last_name = ? AND address = ? AND birthdate = ? AND age = ?";

        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, member.getAccountNumber());
            preparedStatement.setString(2, member.getFirstName());
            preparedStatement.setString(3, member.getMiddleName());
            preparedStatement.setString(4, member.getLastName());
            preparedStatement.setString(5, member.getAddress());
            preparedStatement.setDate(6, java.sql.Date.valueOf(member.getBirthdate()));
            preparedStatement.setInt(7, member.getAge());

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM member";

        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {

            while(resultSet.next()) {
                Member member = Member.builder()
                        .id(resultSet.getInt("id"))
                        .accountNumber(resultSet.getString("account_number"))
                        .firstName(resultSet.getString("first_name"))
                        .middleName(resultSet.getString("middle_name"))
                        .lastName(resultSet.getString("last_name"))
                        .address(resultSet.getString("address"))
                        .birthdate(resultSet.getDate("birthdate").toLocalDate())
                        .age(resultSet.getInt("age"))
                        .build();

                members.add(member);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return members;
    }
}
