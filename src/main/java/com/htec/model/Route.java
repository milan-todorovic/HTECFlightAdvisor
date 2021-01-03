package com.htec.model;

import lombok.*;

/**
 * Provides data model for route.
 */
@Data
@Builder
public class Route {

    private String airline;
    private String airlineId;
    private String sourceAirport;
    private String sourceAirportId;
    private String destinationAirport;
    private String destinationAirportId;
    private String codeshare;
    private int stopsNumber;
    private String equipment;
    private double price;
    private double length;
}
