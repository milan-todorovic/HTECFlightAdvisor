package com.htec.util;

/**
 * Provides distance measurement based on provided coordinates of two points on planet.
 */
public class DistanceCalculator {

    /**
     * Calculates distance in kilometers between airports/places based on longitude and latitude
     * @param lat1 latitude of first airport
     * @param lon1 longitude of first airport
     * @param lat2 latitude of second airport
     * @param lon2 longitude of second airport
     * @return distance in kilometers
     */
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            //for conversion to kilometers
            dist = dist * 1.609344;

            return (dist);
        }
    }
}
