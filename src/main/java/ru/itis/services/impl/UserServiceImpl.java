package ru.itis.services.impl;

import org.mindrot.jbcrypt.BCrypt;
import ru.itis.models.User;
import ru.itis.repositories.UserRepository;
import ru.itis.services.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void register(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("user");
        }

        userRepository.save(user);
    }

    @Override
    public User authenticate(String email, String password) {
        try {
            return userRepository.findByEmail(email)
                    .filter(user -> BCrypt.checkpw(password, user.getPassword()))
                    .orElse(null);
        } catch (Exception e) {
            System.err.println("Ошибка при аутентификации: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isEmailOrUsernameTaken(String email, String username) {
        return userRepository.findByEmail(email).isPresent() || userRepository.findByUsername(username).isPresent();
    }
    @Override
    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.removeById(id);
    }

    @Override
    public void updateRole(Long userId, String role) {
        userRepository.updateRole(userId, role);
    }

    @Override
    public void linkTitle(Long userId, Long titleId, String status) {
        userRepository.linkTitle(userId, titleId, status);
    }

    @Override
    public String getStatusByTitleIdAndUserId(Long titleId, Long userId) {
        return userRepository.getStatusByTitleIdAndUserId(titleId, userId);
    }
}

