package ru.itis.repositories;

import ru.itis.models.Chapter;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends CrudRepository<Chapter>{
    void save(Chapter chapter);

    List<Chapter> findByTitleId(Long titleId);

    Optional<Chapter> findById(Long id);

    void removeById(Long id);
}