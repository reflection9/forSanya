package ru.itis.services.impl;

import ru.itis.models.Genre;
import ru.itis.models.Title;
import ru.itis.repositories.FileRepository;
import ru.itis.repositories.GenreRepository;
import ru.itis.repositories.TitleRepository;
import ru.itis.services.TitleService;

import java.util.List;
import java.util.Optional;

public class TitleServiceImpl implements TitleService {
    private final TitleRepository titleRepository;
    private final GenreRepository genreRepository;
    private final FileRepository fileRepository;

    public TitleServiceImpl(TitleRepository titleRepository, GenreRepository genreRepository, FileRepository fileRepository) {
        this.titleRepository = titleRepository;
        this.genreRepository = genreRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public List<Title> getAllTitles() {
        return titleRepository.findAll();
    }

    @Override
    public void addTitle(Title title) {
        titleRepository.save(title);
    }

    @Override
    public void deleteTitle(Long id) {
        titleRepository.removeById(id);
    }

    @Override
    public void addGenresToTitle(Long titleId, List<Genre> genres) {
        titleRepository.addGenresToTitle(titleId, genres);
    }

    @Override
    public void updateTitle(Title title) {
        titleRepository.update(title);
    }

    @Override
    public void updateGenresForTitle(Long titleId, List<Genre> genres) {
        titleRepository.removeGenresFromTitle(titleId);
        titleRepository.addGenresToTitle(titleId, genres);
    }

    @Override
    public Optional<Title> getTitleById(Long id) {
        return titleRepository.findById(id);
    }

}
