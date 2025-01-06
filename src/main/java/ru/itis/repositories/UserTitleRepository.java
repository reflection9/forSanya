package ru.itis.repositories;

import ru.itis.models.UserTitle;
import ru.itis.helper.ReadingStatus;

public interface UserTitleRepository {
    void save(Long userId, Long titleId, ReadingStatus status);
    ReadingStatus getStatus(Long userId, Long titleId);

}
