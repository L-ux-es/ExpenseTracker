package com.luxes.dev.expensetracker.controller;

import com.luxes.dev.expensetracker.model.User;
import com.luxes.dev.expensetracker.model.UserToUpdate;
import com.luxes.dev.expensetracker.repository.UserRepository;
import com.luxes.dev.expensetracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @PutMapping("/{id}")
    ResponseEntity<String> update(@Valid @RequestBody UserToUpdate user, @PathVariable int id) {
        User userAuth = userService.getAuthenticatedUser();
        if (userAuth.id() == id) {
            UserToUpdate newUser = new UserToUpdate(userService.updatePassword(user.password()));
            userRepository.update(newUser, id);
            return ResponseEntity.ok(userAuth.name() + "password updated");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to update other user");
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> delete(@PathVariable int id) {
        if (userService.getAuthenticatedUser().id() == id) {
             userRepository.delete(id);
                return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to delete other user");
        }
    }


}
