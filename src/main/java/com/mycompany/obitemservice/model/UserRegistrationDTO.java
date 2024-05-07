package com.mycompany.obitemservice.model;

import lombok.*;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
// UserRegistrationDTO.java
public class UserRegistrationDTO {
    private String username;
    private String password;
    private String email;

    // Getters and setters
}
