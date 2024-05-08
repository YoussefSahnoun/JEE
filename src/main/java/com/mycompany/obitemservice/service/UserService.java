package com.mycompany.obitemservice.service;

import com.mycompany.obitemservice.model.User;
import com.mycompany.obitemservice.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<User> signUp(User user) {
        return Mono.defer(() -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }).onErrorResume(error -> Mono.error(new RuntimeException("Error occurred during sign up.", error)));
    }

    public Mono<Boolean> signIn(String username, String password) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    boolean match = passwordEncoder.matches(password, user.getPassword());
                    return Mono.just(match);
                })
                .defaultIfEmpty(false)
                .onErrorResume(error -> Mono.error(new RuntimeException("Error occurred during sign in.", error)));
    }

    public Mono<Boolean> existsByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> true)
                .defaultIfEmpty(false)
                .onErrorResume(error -> Mono.error(new RuntimeException("Error occurred during checking username existence.", error)));
    }

    public Mono<Boolean> existsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> true)
                .defaultIfEmpty(false)
                .onErrorResume(error -> Mono.error(new RuntimeException("Error occurred during checking email existence.", error)));
    }


    public Mono<Void> logout(ServerWebExchange exchange) {
        return exchange.getSession().doOnNext(session -> session.invalidate()).then();
    }
}
