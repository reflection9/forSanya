package ru.itis.repositories;

import ru.itis.models.Genre;

import java.util.Optional;

public interface GenreRepository extends CrudRepository<Genre> {
    Optional<Genre> findByName(String name);
}
