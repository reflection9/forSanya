package ru.itis.services.impl;

import ru.itis.models.Comment;
import ru.itis.repositories.CommentRepository;
import ru.itis.services.CommentService;

import java.util.List;

public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public void deleteCommentById(Long id) {
        commentRepository.removeById(id);
    }

    @Override
    public void addCommentForTitle(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByTitleId(Long titleId) {
        return commentRepository.findByTitle(titleId);
    }
}
