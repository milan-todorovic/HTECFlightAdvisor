package com.htec.service;

import com.htec.model.FlightResult;

import java.util.List;

public interface IFindFlightsService {
    public List<FlightResult> findBySourceAndDestination(String source, String destination);
}
