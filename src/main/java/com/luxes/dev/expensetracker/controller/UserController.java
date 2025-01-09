package com.luxes.dev.expensetracker.controller;

import com.luxes.dev.expensetracker.model.User;
import com.luxes.dev.expensetracker.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    List<User> findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    User findById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@Valid @RequestBody User user, @PathVariable int id) {
        userRepository.update(user, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable int id) {
        userRepository.delete(id);
    }


}
