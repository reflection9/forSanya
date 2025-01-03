package ru.itis.services;

import ru.itis.models.Genre;
import java.util.List;

public interface GenreService {
    List<Genre> getAllGenres();
    void addGenre(Genre genre);
    void deleteGenre(Long id);
    void updateGenre(Genre genre);
}
