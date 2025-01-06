package ru.itis.services;

public interface RatingService {
    void addOrUpdateRating(Long userId, Long titleId, int rating);
    double getAverageRating(Long titleId);
}
