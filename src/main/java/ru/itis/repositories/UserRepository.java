package ru.itis.repositories;

import ru.itis.models.Title;
import ru.itis.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    void linkTitle(Long userId, Long titleId, String status);
    void unlinkTitle(Long userId, Long titleId);
    List<Title> findTitlesByUser(Long userId);
    void updateRole(Long userId, String role);
    String getStatusByTitleIdAndUserId(Long titleId, Long userId);
}