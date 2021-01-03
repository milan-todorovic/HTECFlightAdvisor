package com.htec.dao;

import com.htec.model.Comment;

import java.util.List;

public interface ICommentDao {

    public List<Comment> findAll();

    public boolean save(Comment comment);

    public Comment find(Long id);

    public boolean update(Comment comment, Long id);

    public boolean delete(Long id);
}
