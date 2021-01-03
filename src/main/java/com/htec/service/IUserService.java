package com.htec.service;

import com.htec.model.User;

import java.util.List;

public interface IUserService {

    public List<User> findAll();

    public boolean save(User city);

    public User find(Long id);

    public boolean update(User city, Long id);

    public boolean delete(Long id);

    public boolean updateRole(String role, Long id);

    public boolean register(Long id);
}
