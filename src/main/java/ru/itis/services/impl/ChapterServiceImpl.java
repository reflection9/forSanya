package ru.itis.services.impl;

import ru.itis.models.Chapter;
import ru.itis.repositories.ChapterRepository;
import ru.itis.repositories.FileRepository;
import ru.itis.services.ChapterService;

import java.util.List;

public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;
    private final FileRepository fileRepository;

    public ChapterServiceImpl(ChapterRepository chapterRepository, FileRepository fileRepository) {
        this.chapterRepository = chapterRepository;
        this.fileRepository = fileRepository;
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
}
