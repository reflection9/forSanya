package ru.itis.repositories.impl;

import ru.itis.models.File;
import ru.itis.repositories.FileRepository;
import ru.itis.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileRepositoryJdbcImpl implements FileRepository {

    @Override
    public void save(File file) {
        String query = "INSERT INTO files (title_id, chapter_id, file_path) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setObject(1, file.getTitleId());
            statement.setObject(2, file.getChapterId());
            statement.setString(3, file.getFilePath());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                file.setId(keys.getLong(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении файла", e);
        }
    }

    @Override
    public Optional<File> findById(Long id) {
        String query = "SELECT * FROM files WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapToFile(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении файла по ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<File> findByTitleId(Long titleId) {
        String query = "SELECT * FROM files WHERE title_id = ?";
        List<File> files = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, titleId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                files.add(mapToFile(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении файлов по ID тайтла", e);
        }
        return files;
    }

    @Override
    public List<File> findByChapterId(Long chapterId) {
        String query = "SELECT * FROM files WHERE chapter_id = ?";
        List<File> files = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, chapterId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                files.add(mapToFile(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении файлов по ID главы", e);
        }
        return files;
    }

    @Override
    public void removeByTitleId(Long titleId) {
        String query = "DELETE FROM files WHERE title_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, titleId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении файла", e);
        }
    }

    private File mapToFile(ResultSet resultSet) throws SQLException {
        return File.builder()
                .id(resultSet.getLong("id"))
                .titleId(resultSet.getLong("title_id"))
                .chapterId(resultSet.getLong("chapter_id"))
                .filePath(resultSet.getString("file_path"))
                .build();
    }
}
