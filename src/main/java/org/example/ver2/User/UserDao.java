package org.example.ver2.User;

public interface UserDao {
    void signUp(User user);

    User logIn(String username, String password);
}
