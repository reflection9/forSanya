package ru.itis.services.impl;

import ru.itis.repositories.RatingRepository;
import ru.itis.services.RatingService;

public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratioRepository;
    public RatingServiceImpl(RatingRepository ratioRepository) {
        this.ratioRepository = ratioRepository;
    }
    @Override
    public void addOrUpdateRating(Long userId, Long titleId, int rating) {
        ratioRepository.addOrUpdateRating(userId, titleId, rating);
    }

    @Override
    public double getAverageRating(Long titleId) {
        return ratioRepository.getAverageRating(titleId);
    }
}
