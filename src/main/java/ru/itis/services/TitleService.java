package ru.itis.services;

import ru.itis.models.Genre;
import ru.itis.models.Title;
import java.util.List;
import java.util.Optional;

public interface TitleService {
    List<Title> getAllTitles();
    void addTitle(Title title);
    void deleteTitle(Long id);
    void addGenresToTitle(Long titleId, List<Genre> genres);
    void updateTitle(Title title);
    void updateGenresForTitle(Long titleId, List<Genre> genres);

    Optional<Title> getTitleById(Long id);
}
