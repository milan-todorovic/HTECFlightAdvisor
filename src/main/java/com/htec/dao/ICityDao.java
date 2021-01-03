package com.htec.dao;

import com.htec.model.City;

import java.util.List;

public interface ICityDao {

    public List<City> findAll();

    public boolean save(City city);

    public City find(Long id);

    public List<City> findByName(String name);

    public boolean update(City city, Long id);

    public boolean delete(Long id);
}
