package ru.itis.services;

import ru.itis.models.Chapter;
import java.util.List;

public interface ChapterService {
    List<Chapter> getAllChapters();
    void addChapter(Chapter chapter);
    void deleteChapter(Long id);
}
