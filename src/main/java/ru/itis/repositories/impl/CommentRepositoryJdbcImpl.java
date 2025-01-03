package ru.itis.repositories.impl;

import ru.itis.models.Comment;
import ru.itis.repositories.CommentRepository;
import ru.itis.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentRepositoryJdbcImpl implements CommentRepository {

    @Override
    public List<Comment> findAll() {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT * FROM comments";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                comments.add(mapToComment(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        String query = "SELECT * FROM comments WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapToComment(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void save(Comment entity) {
        String query = "INSERT INTO comments (title_id, user_id, content) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, entity.getTitleId());
            statement.setLong(2, entity.getUserId());
            statement.setString(3, entity.getContent());
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
    public void update(Comment entity) {
        String query = "UPDATE comments SET title_id = ?, user_id = ?, content = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, entity.getTitleId());
            statement.setLong(2, entity.getUserId());
            statement.setString(3, entity.getContent());
            statement.setLong(4, entity.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Comment entity) {
        removeById(entity.getId());
    }

    @Override
    public void removeById(Long id) {
        String query = "DELETE FROM comments WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Comment> findByTitle(Long titleId) {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT * FROM comments WHERE title_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, titleId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                comments.add(mapToComment(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    @Override
    public List<Comment> findByUser(Long userId) {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT * FROM comments WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                comments.add(mapToComment(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    private Comment mapToComment(ResultSet resultSet) throws SQLException {
        return Comment.builder()
                .id(resultSet.getLong("id"))
                .titleId(resultSet.getLong("title_id"))
                .userId(resultSet.getLong("user_id"))
                .content(resultSet.getString("content"))
                .createdAt(resultSet.getTimestamp("created_at"))
                .build();
    }
}
