package com.htec.model;

import lombok.Builder;
import lombok.Data;

/**
 * Provides data model for route attributes like cost and length during route calculations.
 */
@Data
@Builder
public class RootAttributes {
    private double cost;
    private double length;
}