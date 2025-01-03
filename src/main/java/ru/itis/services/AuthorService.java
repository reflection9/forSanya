package ru.itis.services;

import ru.itis.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> getAllAuthors();
    void addAuthor(Author author);
    void deleteAuthor(Long authorId);
}
