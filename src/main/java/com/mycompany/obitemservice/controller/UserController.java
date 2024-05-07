package com.mycompany.obitemservice.controller;
import com.mycompany.obitemservice.model.UserLoginDTO;
import com.mycompany.obitemservice.model.UserRegistrationDTO;
import com.mycompany.obitemservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private  UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody UserLoginDTO user) {
        try {
            boolean result = userService.signIn(user.getUsername(), user.getPassword());
            System.out.println("Received login request for username: " + user.getUsername()+"with password :" + user.getPassword());
            System.out.println(result);
            if (result) {
                return new ResponseEntity<>("Successfully signed in.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid username or password.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to sign in. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody  UserRegistrationDTO userDTO) {
        // Check if a user with the same username or email already exists
        if (userService.getUserByUsername(userDTO.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        if (userService.getUserByUsername(userDTO.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        // If username and email are unique, proceed with user registration
        userService.registerUser(userDTO);
        String message = "User '" + userDTO.getUsername() + "' has been created successfully.";

        // Redirect the user to /api/users/success
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/users/success");
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(message);
    }

}
