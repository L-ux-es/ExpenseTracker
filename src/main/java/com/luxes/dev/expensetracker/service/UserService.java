package com.luxes.dev.expensetracker.service;

import com.luxes.dev.expensetracker.exception.UserException;
import com.luxes.dev.expensetracker.model.User;
import com.luxes.dev.expensetracker.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public User registerUser(User user) {
        if (userRepository.findByName(user.name()).isPresent()) {
            throw new UserException("Username already exists:" + user.name());
        }
        String encodedPassword = passwordEncoder.encode(user.password());
        User newUser = new User(user.id(), user.name(), encodedPassword, "Client");
        userRepository.create(newUser);
        return newUser;
    }

    public String updatePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("User not authenticated.");
        }
        String username = authentication.getName();
        return userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found." + username));
    }

}
