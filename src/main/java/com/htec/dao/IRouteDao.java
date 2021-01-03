package com.htec.dao;

import com.htec.model.Airport;
import com.htec.model.Route;

import java.util.HashMap;
import java.util.List;

public interface IRouteDao {
    public List<Route> findAll(HashMap<String, Airport> airportsMap);
}
