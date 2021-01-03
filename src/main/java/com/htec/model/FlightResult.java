package com.htec.model;

import lombok.Builder;
import lombok.Data;

import java.util.Comparator;

/**
 * Provides data model for flight results with calculated route, price and length.
 */
@Data
@Builder
public class FlightResult {
    private String travelRoute;
    private double cost;
    private double length;

    public static class SortingByCost implements Comparator<FlightResult>
    {
        /**
         * Provides logic for sorting of routes based on cost.
         * @param a
         * @param b
         * @return
         */
        @Override
        public int compare(FlightResult a, FlightResult b)
        {
            double cost1 = a.getCost();
            double cost2 = b.getCost();

            if (cost1 == cost2)
                return 0;
            else if (cost1 > cost2)
                return 1;
            else
                return -1;
        }
    }
}