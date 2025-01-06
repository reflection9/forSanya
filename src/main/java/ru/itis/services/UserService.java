package ru.itis.services;

import ru.itis.helper.ReadingStatus;
import ru.itis.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void register(User user);

    User authenticate(String email, String password);

    boolean isEmailOrUsernameTaken(String email, String username);
    boolean isValidEmail(String email);
    List<User> getAllUsers();
    User getUserById(Long id);
    void deleteUser(Long id);
    void updateRole(Long userId, String role);
    void linkTitle(Long userId, Long titleId, ReadingStatus status);
    ReadingStatus getStatusByTitleIdAndUserId(Long titleId, Long userId);
}