package ru.itis.services;

import ru.itis.models.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getAllComments();

    void deleteCommentById(Long id);
    void addCommentForTitle(Comment comment);

    List<Comment> getCommentsByTitleId(Long titleId);
}
