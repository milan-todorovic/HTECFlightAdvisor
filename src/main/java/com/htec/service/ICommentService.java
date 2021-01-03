package com.htec.service;

import com.htec.model.Comment;

import java.util.List;

public interface ICommentService {

    public List<Comment> findAll();

    public boolean save(Comment comment);

    public Comment find(Long id);

    public boolean update(Comment comment, Long id);

    public boolean delete(Long id);
}
