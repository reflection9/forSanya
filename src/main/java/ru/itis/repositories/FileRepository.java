package ru.itis.repositories;

import ru.itis.models.File;

import java.util.List;
import java.util.Optional;

public interface FileRepository {
    void save(File file);

    Optional<File> findById(Long id);

    List<File> findByTitleId(Long titleId);

    List<File> findByChapterId(Long chapterId);

    void removeByTitleId(Long titleId);
}