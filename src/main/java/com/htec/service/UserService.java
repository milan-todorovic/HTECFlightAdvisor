package com.htec.service;

import com.htec.dao.IUserDao;
import com.htec.model.User;

import javax.inject.Inject;
import java.util.List;

public class UserService implements IUserService{

    @Inject
    private IUserDao userDao;

    @Override
    public List<User> findAll() {

        return userDao.findAll();
    }

    @Override
    public boolean save(User user) {

        return userDao.save(user);
    }

    @Override
    public User find(Long id) {

        return userDao.find(id);
    }

    @Override
    public boolean update(User user, Long id) {

        return userDao.update(user, id);
    }

    @Override
    public boolean delete(Long id) {

        return userDao.delete(id);
    }

    @Override
    public boolean updateRole(String role, Long id) {

        return userDao.updateRole(role,id);
    }

    @Override
    public boolean register(Long id) {

        return userDao.register(id);
    }
}
