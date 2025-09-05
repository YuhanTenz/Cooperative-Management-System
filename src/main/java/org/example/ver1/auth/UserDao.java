package org.example.ver1.auth;

public interface UserDao {

    void signUp(User user);

    User logIn(String username, String password);

}
