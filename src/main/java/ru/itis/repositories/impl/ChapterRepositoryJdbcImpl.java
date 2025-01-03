package ru.itis.repositories.impl;

import ru.itis.models.Chapter;
import ru.itis.repositories.ChapterRepository;
import ru.itis.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChapterRepositoryJdbcImpl implements ChapterRepository {

    @Override
    public List<Chapter> findAll() {
        List<Chapter> chapters = new ArrayList<>();
        String query = "SELECT * FROM chapters";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                chapters.add(mapToChapter(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chapters;
    }

    @Override
    public Optional<Chapter> findById(Long id) {
        String query = "SELECT * FROM chapters WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapToChapter(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении главы по ID", e);
        }
    }

    @Override
    public void save(Chapter entity) {
        String query = "INSERT INTO chapters (title_id, name, chapter_number) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, entity.getTitleId());
            statement.setString(2, entity.getName());
            statement.setInt(4, entity.getChapterNumber());
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
    public void update(Chapter entity) {
        String query = "UPDATE chapters SET title_id = ?, name = ?, chapter_number = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, entity.getTitleId());
            statement.setString(2, entity.getName());
            statement.setInt(4, entity.getChapterNumber());
            statement.setLong(5, entity.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Chapter entity) {
        removeById(entity.getId());
    }

    @Override
    public void removeById(Long id) {
        String query = "DELETE FROM chapters WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Chapter> findByTitleId(Long titleId) {
        String query = "SELECT * FROM chapters WHERE title_id = ?";
        List<Chapter> chapters = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, titleId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                chapters.add(mapToChapter(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении глав по ID тайтла", e);
        }
        return chapters;
    }

    private Chapter mapToChapter(ResultSet resultSet) throws SQLException {
        return Chapter.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .titleId(resultSet.getLong("title_id"))
                .chapterNumber(resultSet.getInt("chapter_number"))
                .build();
    }

}
