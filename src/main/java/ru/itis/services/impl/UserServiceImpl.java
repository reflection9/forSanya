package ru.itis.services.impl;

import org.mindrot.jbcrypt.BCrypt;
import ru.itis.helper.ReadingStatus;
import ru.itis.models.User;
import ru.itis.models.UserTitle;
import ru.itis.repositories.UserRepository;
import ru.itis.repositories.UserTitleRepository;
import ru.itis.services.UserService;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserTitleRepository userTitleRepository;

    public UserServiceImpl(UserRepository userRepository, UserTitleRepository userTitleRepository) {
        this.userRepository = userRepository;
        this.userTitleRepository = userTitleRepository;
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
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Пользователь с ID " + id + " не найден"));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.removeById(id);
    }

    @Override
    public void updateRole(Long userId, String role) {
        userRepository.updateRole(userId, role);
    }

    public void linkTitle(Long userId, Long titleId, ReadingStatus status) {
        userTitleRepository.save(userId,titleId,status);
    }


    @Override
    public ReadingStatus getStatusByTitleIdAndUserId(Long titleId, Long userId) {
        return userTitleRepository.getStatus(userId, titleId);
    }
}

