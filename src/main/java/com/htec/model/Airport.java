package com.htec.model;

import lombok.*;

/**
 * Provides data model for airports.
 */
@Data
@Builder
public class Airport {

    private int airportId;
    private String name;
    private String city;
    private String country;
    private String iata;
    private String icao;
    private double latitude;
    private double longitude;
    private double altitude;
    private String timezone;
    private String dst;
    private String tz;
    private String type;
    private String source;
}
