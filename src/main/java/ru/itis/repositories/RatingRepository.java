package ru.itis.repositories;

public interface RatingRepository {
    void addOrUpdateRating(Long userId, Long titleId, int rating);
    double getAverageRating(Long titleId);
}
