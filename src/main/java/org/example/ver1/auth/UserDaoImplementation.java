package org.example.ver1.auth;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserDaoImplementation implements UserDao {

    private static final String URL = "jdbc:mysql://localhost:3306/cooperative_management_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    /**
     * Establishes a JDBC connection.
     */
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    @Override
    public void signUp(User user) {
        if (isUsernameExists(user.getUsername())) {
            System.out.println("Username already exists. Please choose another one.");
            return;
        }

        final String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        final String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));

        try (Connection connection = connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, user.getUsername().trim().toLowerCase());
            ps.setString(2, hashedPassword);

            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("Registration successful!");
            } else {
                System.out.println("Registration failed. Please try again.");
            }

        } catch (SQLException e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }

    @Override
    public User logIn(String username, String password) {
        final String query = "SELECT * FROM users WHERE username = ?";
        User user = null;

        try (Connection connection = connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, username.trim().toLowerCase());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");

                if (BCrypt.checkpw(password, storedHashedPassword)) {
                    System.out.println("Login successful. Welcome, " + username + "!");
                    user = User.builder()
                            .id(rs.getInt("id"))
                            .username(rs.getString("username"))
                            .build();
                } else {
                    System.out.println("Incorrect password. Please try again.");
                }
            } else {
                System.out.println("Username not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
        }

        return user;
    }

    /**
     * Checks if a username already exists in the database.
     */
    private boolean isUsernameExists(String username) {
        final String query = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection connection = connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, username.trim().toLowerCase());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error checking username existence: " + e.getMessage());
        }

        return false;
    }
}
