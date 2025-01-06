package ru.itis.repositories;

import ru.itis.helper.ReadingStatus;
import ru.itis.models.Author;
import ru.itis.models.File;
import ru.itis.models.Genre;
import ru.itis.models.Title;

import java.util.List;
import java.util.Optional;

public interface TitleRepository extends CrudRepository<Title> {
    Optional<Title> findByName(String name);

    List<Title> findByAuthor(Long authorId);

    List<Title> findByGenre(Long genreId);

    void addGenresToTitle(Long titleId, List<Genre> genres);

    Author findAuthorForTitle(Long authorId);

    List<Genre> findGenresForTitle(Long titleId);

    void removeGenresFromTitle(Long titleId);

    File findCoverFileForTitle(Long titleId);
    List<Title> findTitlesByUserAndStatus(Long userId, ReadingStatus status);
}
