package ru.itis.repositories.impl;

import ru.itis.helper.ReadingStatus;
import ru.itis.repositories.UserTitleRepository;
import ru.itis.models.UserTitle;
import ru.itis.utils.DatabaseConnection;

import java.sql.*;


public class UserTitleRepositoryImpl implements UserTitleRepository {
    @Override
    public void save(Long userId, Long titleId, ReadingStatus status) {
        String sql = "INSERT INTO user_titles (user_id, title_id, status) " +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT (user_id, title_id) DO UPDATE SET status = EXCLUDED.status;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.setLong(2, titleId);
            pstmt.setString(3, status.name());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ReadingStatus getStatus(Long userId, Long titleId) {
        String sql = "SELECT status FROM user_titles WHERE user_id = ? AND title_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.setLong(2, titleId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String statusStr = rs.getString("status");
                return ReadingStatus.valueOf(statusStr);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }
}

