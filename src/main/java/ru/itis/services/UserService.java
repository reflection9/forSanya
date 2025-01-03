package ru.itis.services;

import ru.itis.models.User;

import java.util.List;

public interface UserService {
    void register(User user);

    User authenticate(String email, String password);

    boolean isEmailOrUsernameTaken(String email, String username);
    boolean isValidEmail(String email);
    List<User> getAllUsers();
    void deleteUser(Long id);
    void updateRole(Long userId, String role);
    void linkTitle(Long userId, Long titleId, String status);
    String getStatusByTitleIdAndUserId(Long titleId, Long userId);
}