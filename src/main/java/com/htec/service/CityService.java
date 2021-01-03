package com.htec.service;

import com.htec.dao.ICityDao;
import com.htec.model.City;

import javax.inject.Inject;
import java.util.List;

public class CityService implements ICityService{

    @Inject
    private ICityDao cityDao;

    @Override
    public List<City> findAll() {
        return cityDao.findAll();
    }

    @Override
    public boolean save(City city) {
        return cityDao.save(city);
    }

    @Override
    public City find(Long id) {
        return cityDao.find(id);
    }

    @Override
    public List<City> findByName(String name) {
        return cityDao.findByName(name);
    }

    @Override
    public boolean update(City city, Long id) {
        return cityDao.update(city,id);
    }

    @Override
    public boolean delete(Long id) {
        return cityDao.delete(id);
    }
}
