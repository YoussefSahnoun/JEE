package com.mycompany.obitemservice.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

// UserLoginDTO.java
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    private String username;
    private String password;
}
