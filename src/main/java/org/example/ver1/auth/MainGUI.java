package org.example.ver1.auth;

import org.example.ver1.Member.MemberManagementGUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainGUI {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            UserDao userDao = new UserDaoImplementation();

            JFrame frame = new JFrame("Login & Signup");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);
            frame.setLocationRelativeTo(null);

            JTabbedPane tabbedPane = new JTabbedPane();

            Font labelFont = new Font("Segoe UI", Font.PLAIN, 18);
            Font titleFont = new Font("Segoe UI", Font.BOLD, 28);
            Color bgColor = Color.WHITE;

            JPanel signUpPanel = new JPanel();
            signUpPanel.setBackground(bgColor);
            signUpPanel.setLayout(new BoxLayout(signUpPanel, BoxLayout.Y_AXIS));
            signUpPanel.setBorder(new EmptyBorder(80, 400, 80, 400));

            JLabel signUpTitle = new JLabel("Create Account", SwingConstants.CENTER);
            signUpTitle.setFont(titleFont);
            signUpTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            JTextField signUpUsername = new JTextField();
            signUpUsername.setMaximumSize(new Dimension(400, 70));
            signUpUsername.setPreferredSize(new Dimension(400, 70));
            signUpUsername.setFont(labelFont);
            signUpUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
            signUpUsername.setBorder(BorderFactory.createTitledBorder("Username"));

            JPasswordField signUpPassword = new JPasswordField();
            signUpPassword.setMaximumSize(new Dimension(400, 70));
            signUpPassword.setPreferredSize(new Dimension(400, 70));
            signUpPassword.setFont(labelFont);
            signUpPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
            signUpPassword.setBorder(BorderFactory.createTitledBorder("Password"));

            JButton signUpButton = new JButton("Sign Up");
            signUpButton.setFont(labelFont);
            signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel signUpMessage = new JLabel("", SwingConstants.CENTER);
            signUpMessage.setFont(labelFont);
            signUpMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

            signUpPanel.add(signUpTitle);
            signUpPanel.add(Box.createVerticalStrut(30));
            signUpPanel.add(signUpUsername);
            signUpPanel.add(Box.createVerticalStrut(15));
            signUpPanel.add(signUpPassword);
            signUpPanel.add(Box.createVerticalStrut(25));
            signUpPanel.add(signUpButton);
            signUpPanel.add(Box.createVerticalStrut(15));
            signUpPanel.add(signUpMessage);

            signUpButton.addActionListener(e -> {
                String username = signUpUsername.getText().trim();
                String password = new String(signUpPassword.getPassword()).trim();
                if (username.isEmpty() || password.isEmpty()) {
                    signUpMessage.setForeground(Color.RED);
                    signUpMessage.setText("Please fill in all fields.");
                    return;
                }
                User user = User.builder().username(username).password(password).build();
                try {
                    userDao.signUp(user);
                    signUpMessage.setForeground(new Color(0, 128, 0));
                    signUpMessage.setText("Sign up successful! Please login.");
                    // Clear the fields after successful signup
                    signUpUsername.setText("");
                    signUpPassword.setText("");
                    // Switch to login tab
                    tabbedPane.setSelectedIndex(1);
                } catch (IllegalArgumentException ex) {
                    signUpMessage.setForeground(Color.RED);
                    signUpMessage.setText(ex.getMessage());
                }
            });

            JPanel loginPanel = new JPanel();
            loginPanel.setBackground(bgColor);
            loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
            loginPanel.setBorder(new EmptyBorder(80, 400, 80, 400));

            JLabel loginTitle = new JLabel("Login", SwingConstants.CENTER);
            loginTitle.setFont(titleFont);
            loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            JTextField loginUsername = new JTextField();
            loginUsername.setMaximumSize(new Dimension(400, 70));
            loginUsername.setPreferredSize(new Dimension(400, 70));
            loginUsername.setFont(labelFont);
            loginUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
            loginUsername.setBorder(BorderFactory.createTitledBorder("Username"));

            JPasswordField loginPassword = new JPasswordField();
            loginPassword.setMaximumSize(new Dimension(400, 70));
            loginPassword.setPreferredSize(new Dimension(400, 70));
            loginPassword.setFont(labelFont);
            loginPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
            loginPassword.setBorder(BorderFactory.createTitledBorder("Password"));

            JButton loginButton = new JButton("Login");
            loginButton.setFont(labelFont);
            loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel loginMessage = new JLabel("", SwingConstants.CENTER);
            loginMessage.setFont(labelFont);
            loginMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

            loginPanel.add(loginTitle);
            loginPanel.add(Box.createVerticalStrut(30));
            loginPanel.add(loginUsername);
            loginPanel.add(Box.createVerticalStrut(15));
            loginPanel.add(loginPassword);
            loginPanel.add(Box.createVerticalStrut(25));
            loginPanel.add(loginButton);
            loginPanel.add(Box.createVerticalStrut(15));
            loginPanel.add(loginMessage);

            loginButton.addActionListener(e -> {
                String username = loginUsername.getText().trim();
                String password = new String(loginPassword.getPassword()).trim();
                if (username.isEmpty() || password.isEmpty()) {
                    loginMessage.setForeground(Color.RED);
                    loginMessage.setText("Please fill in all fields.");
                    return;
                }
                User loggedUser = userDao.logIn(username, password);
                if (loggedUser != null) {
                    loginMessage.setForeground(new Color(0, 128, 0));
                    loginMessage.setText("Login successful! Redirecting...");

                    // Use SwingUtilities.invokeLater to ensure UI updates are completed
                    SwingUtilities.invokeLater(() -> {
                        try {
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        } catch (Exception error) {
                            error.printStackTrace();
                        }

                        // Close the login window
                        frame.dispose();

                        // Open the member management GUI
                        new MemberManagementGUI().setVisible(true);
                    });
                } else {
                    loginMessage.setForeground(Color.RED);
                    loginMessage.setText("Invalid username or password!");
                }
            });

            tabbedPane.addTab("Sign Up", signUpPanel);
            tabbedPane.addTab("Login", loginPanel);

            frame.add(tabbedPane);
            frame.setVisible(true);
        });
    }
}