package com.luxes.dev.expensetracker.controller;

import com.luxes.dev.expensetracker.config.JwtUtil;
import com.luxes.dev.expensetracker.model.TokenResponse;
import com.luxes.dev.expensetracker.model.User;
import com.luxes.dev.expensetracker.model.UserLogin;
import com.luxes.dev.expensetracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/register")
    ResponseEntity<TokenResponse> register(@Valid @RequestBody User user) {
        String username = userService.registerUser(user).name();
        String token = jwtUtil.createToken(username);
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserLogin userLogin) {
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(userLogin.username(), userLogin.password());
        authenticationManager.authenticate(login);
        String jwt = this.jwtUtil.createToken(userLogin.username());
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).build();
    }

}
