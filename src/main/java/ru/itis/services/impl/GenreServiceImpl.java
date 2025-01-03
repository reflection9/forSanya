package ru.itis.services.impl;

import ru.itis.models.Genre;
import ru.itis.repositories.GenreRepository;
import ru.itis.services.GenreService;

import java.util.List;

public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public void addGenre(Genre genre) {
        genreRepository.save(genre);
    }

    @Override
    public void deleteGenre(Long id) {
        genreRepository.removeById(id);
    }

    @Override
    public void updateGenre(Genre genre) {
        genreRepository.update(genre);
    }
}
