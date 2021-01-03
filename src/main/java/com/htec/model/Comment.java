package com.htec.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * Provides data model for comment.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Long idComment;
    private String comment;
    private Long fkCityId;
    LocalDate createdDate;
    LocalDate modifiedDate;
}
