package com.htec.model;

import lombok.*;

/**
 * Provides data model for user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long idUser;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String salt;
    private String role;
    private int registered;
}
