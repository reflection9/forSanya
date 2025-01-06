package ru.itis.services;

import ru.itis.models.Chapter;
import java.util.List;
import java.util.Optional;

public interface ChapterService {
    List<Chapter> getAllChapters();
    void addChapter(Chapter chapter);
    void deleteChapter(Long id);
    List<Chapter> findByTitleId(Long titleId);
    Optional<Chapter> findById(Long id);
}
