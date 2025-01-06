package ru.itis.repositories.impl;

import ru.itis.repositories.RatingRepository;
import ru.itis.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingRepositoryJbdcImpl implements RatingRepository {
    @Override
    public void addOrUpdateRating(Long userId, Long titleId, int rating) {
        String query = "INSERT INTO ratings (user_id, title_id, rating) " +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT (user_id, title_id) DO UPDATE SET rating = EXCLUDED.rating";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, userId);
            statement.setLong(2, titleId);
            statement.setInt(3, rating);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении или обновлении рейтинга", e);
        }
    }

    @Override
    public double getAverageRating(Long titleId) {
        String query = "SELECT AVG(rating) AS average_rating FROM ratings WHERE title_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, titleId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("average_rating");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получение среднего рейтинга", e);
        }
        return 0.0;
    }
}
