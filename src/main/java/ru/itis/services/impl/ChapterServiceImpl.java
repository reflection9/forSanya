package ru.itis.services.impl;

import ru.itis.models.Chapter;
import ru.itis.repositories.ChapterRepository;
import ru.itis.repositories.FileRepository;
import ru.itis.services.ChapterService;

import java.util.List;
import java.util.Optional;

public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;

    public ChapterServiceImpl(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    @Override
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }

    @Override
    public void addChapter(Chapter chapter) {
        chapterRepository.save(chapter);
    }

    @Override
    public void deleteChapter(Long id) {
        chapterRepository.removeById(id);
    }

    @Override
    public List<Chapter> findByTitleId(Long titleId) {
        return chapterRepository.findByTitleId(titleId);
    }

    @Override
    public Optional<Chapter> findById(Long id) {
        return Optional.empty();
    }
}
