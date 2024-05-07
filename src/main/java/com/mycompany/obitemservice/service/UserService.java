package com.mycompany.obitemservice.service;

import com.mycompany.obitemservice.model.User;
import com.mycompany.obitemservice.repository.UserRepository;
import com.mycompany.obitemservice.model.UserRegistrationDTO;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private HttpSession httpSession;
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) throws NotFoundException {
        try {
            return userRepository.findById(String.valueOf(id))
                    .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()); // Empty list of authorities
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(String.valueOf(id));
    }

    public void registerUser(UserRegistrationDTO userDTO) {
        // Create a new User object from the registration DTO
        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setEmail(userDTO.getEmail());
        // Encode the password before saving
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Save the new user to the database
        userRepository.save(newUser);
    }

    public boolean signIn(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            httpSession.setAttribute("user", user);
            return true;
        }
        return false;
    }
}
