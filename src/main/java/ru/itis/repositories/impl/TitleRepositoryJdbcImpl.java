package ru.itis.repositories.impl;

import ru.itis.models.Author;
import ru.itis.models.File;
import ru.itis.models.Genre;
import ru.itis.models.Title;
import ru.itis.repositories.TitleRepository;
import ru.itis.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TitleRepositoryJdbcImpl implements TitleRepository {

    @Override
    public List<Title> findAll() {
        List<Title> titles = new ArrayList<>();
        String query = "SELECT * FROM titles";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Title title = mapToTitle(resultSet);

                title.setGenres(findGenresForTitle(title.getId()));

                title.setAuthor(findAuthorForTitle(title.getAuthorId()));
                titles.add(title);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех тайтлов", e);
        }
        return titles;
    }

    @Override
    public Optional<Title> findById(Long id) {
        String query = "SELECT * FROM titles WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Title title = mapToTitle(resultSet);
                title.setGenres(findGenresForTitle(title.getId()));
                title.setAuthor(findAuthorForTitle(title.getAuthorId()));
                title.setFiles(List.of(findCoverFileForTitle(title.getId())));
                return Optional.of(title);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении тайтла по ID", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Title entity) {
        String query = "INSERT INTO titles (name, description, type, author_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getDescription());
            statement.setString(3, entity.getType());
            statement.setLong(4, entity.getAuthorId());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                entity.setId(keys.getLong(1));
            }

            if (entity.getGenres() != null) {
                addGenresToTitle(entity.getId(), entity.getGenres());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении тайтла", e);
        }
    }

    @Override
    public void update(Title entity) {
        String query = "UPDATE titles SET name = ?, description = ?, type = ?, author_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getDescription());
            statement.setString(3, entity.getType());
            statement.setLong(4, entity.getAuthorId());
            statement.setLong(5, entity.getId());
            statement.executeUpdate();

            if (entity.getGenres() != null) {
                removeGenresFromTitle(entity.getId());
                addGenresToTitle(entity.getId(), entity.getGenres());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении тайтла", e);
        }
    }

    @Override
    public void remove(Title entity) {
        removeById(entity.getId());
    }

    @Override
    public void removeById(Long id) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String deleteFiles = "DELETE FROM files WHERE title_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteFiles)) {
                statement.setLong(1, id);
                statement.executeUpdate();
            }

            removeGenresFromTitle(id);

            String deleteTitle = "DELETE FROM titles WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteTitle)) {
                statement.setLong(1, id);
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении тайтла", e);
        }
    }

    @Override
    public Optional<Title> findByName(String name) {
        String query = "SELECT * FROM titles WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Title title = mapToTitle(resultSet);
                title.setGenres(findGenresForTitle(title.getId()));
                return Optional.of(title);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении тайтла по имени", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Title> findByAuthor(Long authorId) {
        List<Title> titles = new ArrayList<>();
        String query = "SELECT * FROM titles WHERE author_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, authorId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                titles.add(mapToTitle(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении тайтлов по автору", e);
        }
        return titles;
    }

    @Override
    public List<Title> findByGenre(Long genreId) {
        List<Title> titles = new ArrayList<>();
        String query = "SELECT t.* FROM titles t JOIN title_genres tg ON t.id = tg.title_id WHERE tg.genre_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, genreId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                titles.add(mapToTitle(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении тайтлов по жанру", e);
        }
        return titles;
    }

    @Override
    public Author findAuthorForTitle(Long authorId) {
        String query = "SELECT * FROM authors WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, authorId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Author.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении автора для тайтла", e);
        }
        return null;
    }

    @Override
    public List<Genre> findGenresForTitle(Long titleId) {
        List<Genre> genres = new ArrayList<>();
        String query = "SELECT g.id, g.name FROM genres g JOIN title_genres tg ON g.id = tg.genre_id WHERE tg.title_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, titleId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                genres.add(Genre.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении жанров для тайтла", e);
        }
        return genres;
    }

    @Override
    public void addGenresToTitle(Long titleId, List<Genre> genres) {
        String query = "INSERT INTO title_genres (title_id, genre_id) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (Genre genre : genres) {
                statement.setLong(1, titleId);
                statement.setLong(2, genre.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении жанров к тайтлу", e);
        }
    }

    @Override
    public void removeGenresFromTitle(Long titleId) {
        String query = "DELETE FROM title_genres WHERE title_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, titleId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении жанров у тайтла", e);
        }
    }

    @Override
    public File findCoverFileForTitle(Long titleId) {
        String query = "SELECT * FROM files WHERE title_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, titleId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return File.builder()
                        .id(resultSet.getLong("id"))
                        .titleId(resultSet.getLong("title_id"))
                        .filePath(resultSet.getString("file_path"))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении обложки для тайтла", e);
        }
        return null;
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
}
