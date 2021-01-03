package com.htec.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DistanceCalculatorTest {
    private DistanceCalculator distanceCalculator;
    private static final double DELTA = 0.00001d;

    @Before
    public void initMocks() {
        distanceCalculator = new DistanceCalculator();
    }

    @Test
    public void distance_sameDimensions() {
        double distance = distanceCalculator.distance(1, 2, 1, 2);
        assertEquals(0, distance, DELTA);
    }
    @Test
    public void distance_regularDimensions() {
        double distance = distanceCalculator.distance(43.449901580811,39.956600189209, 55.606201171875,49.278701782227);
        assertEquals(1506.7531467139274, distance, DELTA);
    }
}