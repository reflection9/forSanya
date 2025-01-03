package ru.itis.repositories.impl;

import ru.itis.models.Title;
import ru.itis.models.User;
import ru.itis.repositories.UserRepository;
import ru.itis.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryJdbcImpl implements UserRepository {

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapToUser(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapToUser(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void save(User entity) {
        String query = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getEmail());
            statement.setString(3, entity.getPassword());
            statement.setString(4, entity.getRole());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                entity.setId(keys.getLong(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(User entity) {
        String query = "UPDATE users SET username = ?, email = ?, password = ?, role = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getEmail());
            statement.setString(3, entity.getPassword());
            statement.setString(4, entity.getRole());
            statement.setLong(5, entity.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(User entity) {
        removeById(entity.getId());
    }

    @Override
    public void removeById(Long id) {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapToUser(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapToUser(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public void linkTitle(Long userId, Long titleId, String status) {
        String query = "INSERT INTO user_titles (user_id, title_id, status) VALUES (?, ?, ?) " +
                "ON CONFLICT (user_id, title_id) DO UPDATE SET status = EXCLUDED.status";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, userId);
            statement.setLong(2, titleId);
            statement.setString(3, status);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlinkTitle(Long userId, Long titleId) {
        String query = "DELETE FROM user_titles WHERE user_id = ? AND title_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, userId);
            statement.setLong(2, titleId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Title> findTitlesByUser(Long userId) {
        List<Title> titles = new ArrayList<>();
        String query = " SELECT t.id, t.name, t.description, t.type, t.cover_image, t.author_id " +
                "FROM titles t JOIN user_titles ut ON t.id = ut.title_id " +
                "WHERE ut.user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                titles.add(mapToTitle(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return titles;
    }

    @Override
    public void updateRole(Long userId, String role) {
        String query = "UPDATE users SET role = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, role);
            statement.setLong(2, userId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении роли пользователя", e);
        }
    }

    @Override
    public String getStatusByTitleIdAndUserId(Long titleId, Long userId) {
        String query = "SELECT status FROM user_titles WHERE user_id = ? AND title_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, userId);
            statement.setLong(2, titleId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("status");
            } else {
                return "notReading";
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении статуса", e);
        }
    }


    private Title mapToTitle(ResultSet resultSet) throws SQLException {
        return Title.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .type(resultSet.getString("type"))
                .authorId(resultSet.getLong("author_id"))
                .build();
    }

    private User mapToUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .username(resultSet.getString("username"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .role(resultSet.getString("role"))
                .build();
    }
}
