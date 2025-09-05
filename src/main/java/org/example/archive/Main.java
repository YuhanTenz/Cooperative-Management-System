package org.example.archive;

import org.example.ver1.auth.User;
import org.example.ver1.auth.UserDao;
import org.example.ver1.auth.UserDaoImplementation;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

////        User user = User.builder()
////                .username("Summer")
////                .password("bossmapagmahal")
////                .build();
//
//        UserDao userDao = new UserDaoImplementation();
////        userDao.signUp(user);
//
//        User userLogged = userDao.logIn("summer", "bossmapagmahal");
//
//        if (userLogged == null) {
//            System.out.println("Invalid Credentials");
//        } else {
//            System.out.println(userLogged.getUsername());
//        }

        Scanner scanner = new Scanner(System.in);
        UserDao userDao = new UserDaoImplementation();

        while (true) {
            System.out.println("Choose an option: ");
            System.out.println("[1] Sign Up");
            System.out.println("[2] Login");
            System.out.println("[3] Exit");

            System.out.print("Enter Choice: ");
            byte choice = scanner.nextByte();

            scanner.nextLine();

            switch (choice) {
                case 1: // Sign Up
                    System.out.print("Enter Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();

                    User user = User.builder()
                            .username(username)
                            .password(password)
                            .build();

                    try {
                        userDao.signUp(user);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2: // login
                    System.out.print("Enter Username: ");
                    String loginUsername = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String loginPassword = scanner.nextLine();

                    User loggedUser = userDao.logIn(loginUsername, loginPassword);

                    if (loggedUser != null) {
                        System.out.println("Login Successfully. Welcome " + loginUsername);
                    } else {
                        System.out.println("Invalid username or password!");
                    }
                    break;
                case 3:
                    System.exit(0);
            }
        }
    }
}