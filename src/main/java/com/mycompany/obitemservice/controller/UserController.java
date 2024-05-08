package com.mycompany.obitemservice.controller;

import com.mycompany.obitemservice.model.User;
import com.mycompany.obitemservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public Mono<ResponseEntity<String>> signUp(@RequestBody User user) {
        return userService.existsByUsername(user.getUsername())
                .flatMap(usernameExists -> {
                    if (usernameExists) {
                        return Mono.just(ResponseEntity.badRequest().body("Username already exists. Please choose a different one."));
                    } else {
                        return userService.existsByEmail(user.getEmail())
                                .flatMap(emailExists -> {
                                    if (emailExists) {
                                        return Mono.just(ResponseEntity.badRequest().body("Email already exists. Please use a different one."));
                                    } else {
                                        return userService.signUp(user)
                                                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("Registration successful."));
                                    }
                                });
                    }
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to sign up. Please try again.")));
    }

    @PostMapping("/signin")
    public Mono<ResponseEntity<?>> signIn(@RequestBody User user) {
        return userService.signIn(user.getUsername(), user.getPassword())
                .flatMap(result -> {
                    if (result) {
                        return Mono.just(ResponseEntity.status(HttpStatus.FOUND).header("Location", "/api/v1/items").build());
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password."));
                    }
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to sign in. Please try again.")));
    }

    @GetMapping("/logout")
    public Mono<ResponseEntity<Object>> logout(ServerWebExchange exchange) {
        return userService.logout(exchange)
                .thenReturn(ResponseEntity.status(HttpStatus.FOUND).header("Location", "/login.html").build())
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to logout. Please try again.")));
    }
}
