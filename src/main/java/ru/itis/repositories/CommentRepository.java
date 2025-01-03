package ru.itis.repositories;

import ru.itis.models.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment> {
    List<Comment> findByTitle(Long titleId);
    List<Comment> findByUser(Long userId);
}
