package com.htec.dao;

import com.htec.model.Airport;

import java.util.HashMap;

public interface IAirportDao {
    public HashMap<String, Airport> findAll();
}
