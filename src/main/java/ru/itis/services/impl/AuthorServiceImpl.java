package ru.itis.services.impl;

import ru.itis.models.Author;
import ru.itis.repositories.AuthorRepository;
import ru.itis.services.AuthorService;

import java.util.List;

public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public void addAuthor(Author author) {
        authorRepository.save(author);
    }

    @Override
    public void deleteAuthor(Long authorId) {
        authorRepository.removeById(authorId);
    }
}
