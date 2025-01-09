package com.luxes.dev.expensetracker.service;

import com.luxes.dev.expensetracker.exception.UserException;
import com.luxes.dev.expensetracker.model.User;
import com.luxes.dev.expensetracker.repository.UserRepository;
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

    public User findUserById(int idUser) {
        return userRepository.findById(idUser).orElse(null);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByName(username).orElse(null);
    }


    @Transactional
    public User registerUser(User user) {
        if (userRepository.findByName(user.name()).isPresent()) {
            throw new UserException("Username already exists:" + user.name());
        }
        String encodedPassword = passwordEncoder.encode(user.password());
        User newUser = new User(user.id(), user.name(), encodedPassword, "Admin");
        userRepository.create(newUser);
        return newUser;
    }

}
