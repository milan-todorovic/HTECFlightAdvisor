package com.htec.dao;

import com.htec.model.User;

import java.util.List;

public interface IUserDao {

    public List<User> findAll();

    public boolean save(User user);

    public User find(Long id);

    public User findRoleByUsername(String username);

    public boolean update(User user, Long id);

    public boolean delete(Long id);

    public boolean updateRole(String role, Long id);

    public boolean register(Long id);
}