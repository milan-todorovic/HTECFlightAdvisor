package com.htec.service;

import com.htec.dao.ICommentDao;
import com.htec.model.Comment;

import javax.inject.Inject;
import java.util.List;

public class CommentService implements ICommentService {

    @Inject
    ICommentDao commentDao;

    @Override
    public List<Comment> findAll() {
        return commentDao.findAll();
    }

    @Override
    public boolean save(Comment comment) {
        return commentDao.save(comment);
    }

    @Override
    public Comment find(Long id) {
        return commentDao.find(id);
    }

    @Override
    public boolean update(Comment comment, Long id) {
        return commentDao.update(comment, id);
    }

    @Override
    public boolean delete(Long id) {
        return commentDao.delete(id);
    }
}
