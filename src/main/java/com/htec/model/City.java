package com.htec.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides data model for city.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class City {
    private Long idCity;
    private String name;
    private String country;
    private String description;
    private List<Comment> comments = new ArrayList<>();
}
