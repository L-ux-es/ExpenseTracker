package com.luxes.dev.expensetracker.service;

import com.luxes.dev.expensetracker.model.User;
import com.luxes.dev.expensetracker.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userEntity = userRepository.findByName(username);
        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException("User" + username + " not found!");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.get().name())
                .password(userEntity.get().password())
                .roles(userEntity.get().rol())
                .build();
    }


}
